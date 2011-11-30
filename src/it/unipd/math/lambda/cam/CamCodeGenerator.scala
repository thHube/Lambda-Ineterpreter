package it.unipd.math.lambda.cam

import it.unipd.math.lambda.Term
import it.unipd.math.lambda.Var
import it.unipd.math.lambda.Lambda

class CamCodeGenerator {
  
  private var env:Environment = new EnvIdentity; 
  
  // -- Print error to error stream --------------------------------------------
  private def error(msg:String) {
    System.err.println("[CAM code generator] >> " + msg);
  }
  
  private def codeGeneration(term:Term): List[CamAssembly] = term match {
    // -- A variable is found --------------------------------------------------
  	case Var(name) => {
  	  env = EnvCouple(env, EnvVar(name))
  	  Snd::Nil;
  	}
    
  	// -- A lambda abstraction -------------------------------------------------
    case Lambda(Var(varName), body) => {
      val compilation = codeGeneration(body); 
      Cur(compilation)::Nil;
    }
    
    // -- Function application -------------------------------------------------
    case it.unipd.math.lambda.App(func, params) => {
      val funcCompile = codeGeneration(func);
      val paramCompile = codeGeneration(params);
      Push::funcCompile ++ (Swap::paramCompile) ++ (Cons::App::Nil);
    }
  }
   
  
  /**
   * Generate CAM asm instructions from parse tree generated by the parser.
   * @param term Parse tree generated 
   * @return Couple (asm, env) where asm is the CAM assembly code to run the 
   * program on env Environment. 
   */
  def generate(term:Term): (List[CamAssembly], Environment) = {
    (codeGeneration(term), env);
  }
  
}