package it.unipd.math.lambda

import it.unipd.math.lambda.cam._

// -- Interactive shell for lambda interpreter ---------------------------------
object LambdaShell {
  
  // -- Some vars to use in the shell ------------------------------------------
  private val SHELL_CURSOR = ">> ";
  private var cam = false;
  
    // -- Compiler core components -----------------------------------------------
  private var lexer:LexicalAnalyzer = new LexicalAnalyzer;
  private var parser:Parser = new Parser;
  private var solver:TermSolver = BestSolver.solver;
  
  // -- CAM components ---------------------------------------------------------
  private var codeGenerator:CamCodeGenerator = new CamCodeGenerator;
  private var runner:CamRunner = null;
  private var camInterpret:Boolean = false;
  
  def parseLine(line:String) = line match{
    case "help" => printHelpMessage();
    case "quit" => System.exit(0);
    case "cam"  => {
      cam = true;
      println("CAM set as solution engine");
    }
    case "rewr" => {
      cam = false;
      println("lambda-rewrite set as solution engine");
    }
    case _ => interpret(line);
  }
  
  // -- Interpret a file if flag is true run CAM -------------------------------
  def interpret(line:String) {
    // -- Lex and parse input file --------------------------------------------- 
    val tokens = lexer.lex(line.toList);
    try {
      val term = parser.parse(tokens);
    
      println(TermWriter.write(term));
    
      if(cam) {
        val (code, env) = codeGenerator.generate(term);
        runner = new CamRunner(env);
        val finalEnv = runner.run(code);
        
        println("\n-- Used CAM to normalize! ");
        println("Normalized Environment " + EnvPrinter.write(finalEnv));
      } else {
	    // -- Normalize the term ---------------------------------------------
	    val normalTerm = solver.normalize(term);
        println("\n-- Normalized term:");
        println(TermWriter.write(normalTerm));
      }
      
    } catch {
      case e:Exception => println("Compilation incomplete");
    }
  }
  
  // -- Once started print this message ----------------------------------------
  def printHelloMessage() {
    println("Lambda interpreter v" + Main.INTERPRETER_VERSION);
    println("created by Alberto Franco (c) 2011")
    println("type 'help' for commands, 'quit' for closing this shell");
  }
  
  // -- Print an help message --------------------------------------------------
  def printHelpMessage() {
    println("Commands: ");
    println("  help: print this message");
    println("  quit: close the program");
    println("  cam : pass to CAM solution engine");
    println("  rewr: pass to rewriting solution engine");
    println("Syntax: ");
    println("  [x, y] - is lambda x. y");
    println("  (a, b) - is a(b)");
  }
  
  // -- Program entry point ----------------------------------------------------
  def main(args:Array[String]) {
    printHelloMessage();
    print(SHELL_CURSOR);
    var line = readLine();
    while (line != null) {
      parseLine(line);
      
      print(SHELL_CURSOR);
      line = readLine();
    } 
  }
}

