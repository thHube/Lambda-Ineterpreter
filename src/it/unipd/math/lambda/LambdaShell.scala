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
  private var typer:TypeInferAgent = new TypeInferAgent;
  
  // -- CAM components ---------------------------------------------------------
  private var codeGenerator:CamCodeGenerator = new CamCodeGenerator;
  private var runner:CamRunner = null;
  private var camInterpret:Boolean = false;
 
  /**
   * Utility function to parse a single line from the shell 
   * @param line Line to parse
   */
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
    case _ => {
      val split = line.split(""" """);
      if (split(0) equals "type") {
        // -- Request to type the term -----------------------------------------
        val typeString:String = split.tail.reduceLeft(_ + " " + _);
        val term = parser.parse(lexer.lex(typeString.toList));
        val typed = typer.infer(term);
        
        println("Term: " + TermWriter.write(term));
        println("Type: " + TypeWriter.write(typed));
        
        // -- Else interpret the line ------------------------------------------ 
      } else interpret(line);
    }
  }
  
  // -- Interpret a file if flag is true run CAM -------------------------------
  def interpret(line:String) {
    // -- Lex and parse input file --------------------------------------------- 
    val tokens = lexer.lex(line.toList);
    try {
      val term = parser.parse(tokens);
    
      println(TermWriter.write(term));
    
      if(cam) {
        // -- Create object and run the code -----------------------------------
        val (code, env) = codeGenerator.generate(term);
        runner = new CamRunner(env);
        val finalEnv = runner.run(code);
        
        println("\n-- Used CAM to normalize! ");
        println("Normalized Environment " + EnvPrinter.write(finalEnv));
      } else {
	    // -- Normalize the term ---------------------------------------------
	    val normalTerm = solver.normalize(term);
        println("\n-- Normalized term:");
        println("  " + TermWriter.write(normalTerm));
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
    println("  type [LAMBDA]: type a lambda term. In this context is allowed")
    println("                 to use couples with syntax '<a, b>'")
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

