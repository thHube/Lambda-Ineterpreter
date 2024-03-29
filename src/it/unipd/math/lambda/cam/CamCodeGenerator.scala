package it.unipd.math.lambda.cam

import it.unipd.math.lambda.Term
import it.unipd.math.lambda.Var
import it.unipd.math.lambda.Lambda

class CamCodeGenerator() {

  // -- Environment and variable list and variable to skip ---------------------
  private var env:Environment = null;
  private var varList:List[String] = Nil;
  private var skipVars:List[String] = Nil;
  
  // -- Print error to error stream --------------------------------------------
  private def error(msg:String) {
    System.err.println("[CAM code generator] >> " + msg);
  }
  
  private def generateEnvironment(term:Term):Unit = term match {
    case Var(name) => {
  	  // -- Add to list if it has been never found and is not abstract --------- 
  	  if (!findVar(name, skipVars)) {
  	    if (!findVar(name, varList)) {
  	      varList ::= name;
  	      env = EnvCouple(env, EnvVar(name));
  	    }
  	  }
    }
    
    // -- Lambda abstraction --------------------------------------------------- 
    case Lambda(Var(abs), body) => {
      skipVars = abs::skipVars;
      generateEnvironment(body);
      skipVars = skipVars.tail;
    }
    
    // -- Use fully qualified name to avoid name clash with scala.App ----------
    case it.unipd.math.lambda.App(func, params) => {
      generateEnvironment(func);
      generateEnvironment(params); 
    }
  }
  
  /**
   * Generate CAM code with the given environment. 
   * @param term The term to analyze and compile
   * @param env Compilation environment
   * @return Couple asm where asm is the code and env the environment. 
   */
  private def codeGeneration(term:Term, env:Environment): List[CamAssembly] = 
    term match {
    // -- A variable is found --------------------------------------------------
  	case Var(varname) => {
  	  // -- Depending on environment compile var -------------------------------
  	  env match {
  	    case EnvCouple(e, EnvVar(name)) => {
  	      if (name equals varname) {
    	    Snd::Nil;
  	      } else {
  	        Fst::codeGeneration(term, e);
  	      }
  	    }
  	    case _ => {
  	      error("Expected a couple in environment");
  	      throw new Exception;
  	    }
  	  }
  	}
    
  	// -- A lambda abstraction -------------------------------------------------
    case Lambda(Var(varName), body) => {
      // -- In this context we do not want to care about abstract var ----------
      val compilation = codeGeneration(body, EnvCouple(env, EnvVar(varName)));
      Cur(compilation)::Nil;
    }
    
    // -- Function application -------------------------------------------------
    case it.unipd.math.lambda.App(func, params) => {
      val funcCompile = codeGeneration(func, env);
      val paramCompile = codeGeneration(params, env);
      Push::funcCompile ++ (Swap::paramCompile) ++ (Cons::App::Nil);
    }
  }
  
  /**
   * Support method to find a variable into a list. Method find in scala 
   * return an Option[A] we need to match that option to see if anything 
   * has been found.
   * @param name Name of variable to find.
   * @param list List to search in
   * @return If the name is in the list.
   */
  private def findVar(name:String, list:List[String]): Boolean = {
    val found = list find {e => e == name};
    found match {
      case None => false;
      case _ => true;
    }
  }
  
  /**
   * Generate CAM asm instructions from parse tree generatgeneraed by the parser.
   * @param term Parse tree generated 
   * @return Couple (asm, env) where asm is the CAM assembly code to run the 
   * program on env Environment. 
   */
  def generate(term:Term): (List[CamAssembly], Environment) = {
    // -- Reset variables ------------------------------------------------------
    env = EnvIdentity();
    varList = Nil;
    skipVars = Nil;
    
    // -- Generate code and environment ----------------------------------------
    generateEnvironment(term);
    (codeGeneration(term, env), env);
  }
  
}