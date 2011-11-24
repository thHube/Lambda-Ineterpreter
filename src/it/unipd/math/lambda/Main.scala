package it.unipd.math.lambda

import scala.io.Source;

object Main extends scala.App{
 
  // -- Compiler informations --------------------------------------------------
  private val INTERPRETER_VERSION:String = "0.1 alpha"; 
  private val EXEC_NAME = "lambda";
  
  // -- Compiler core components -----------------------------------------------
  private var lexer:LexicalAnalyzer = new LexicalAnalyzer;
  private var parser:Parser = new Parser;
  private var solver:TermSolver = BestSolver.solver;
  
  // -- 
  def interpret(filename:String) = {
    // -- Open file and convert to a list of char
    val source = Source.fromFile(filename);
    val lines  = source.mkString;
    source.close();
    println("-- Read lambda term from " + filename)
    
    // -- Lex and parse input file --------------------------------------------- 
    val tokens = lexer.lex(lines.toList);
    try {
      val term   = parser.parse(tokens);
    
      println(TermWriter.write(term));
    
      // -- Normalize the term -------------------------------------------------
      val normalTerm = solver.normalize(term);
    
      println("\n-- Normalized term:");
      println(TermWriter.write(normalTerm));
    } catch {
      case e:Exception => println("Compilation incomplete");
    }
  }
  
  // -- Help message printing --------------------------------------------------
  def printHelpMessage() {
    println("Usage: " + EXEC_NAME + " <input file> to interepret a lamda term");
    println("Other options: ");
    println("   -v, --version: print compiler version");
    println("   -h, --help:    print this message");
  }
  
  // -- Argument parse ---------------------------------------------------------
  def parseArg(arg:String):Boolean = (arg) match {
    case "-v" | "--version" => {
      println("Lambda Interpreter v" + INTERPRETER_VERSION);
      true;
    }
    case "-h" | "--help" => { printHelpMessage(); true; }
    case _ => false;
  }
  
  // -- If no argument given print help message --------------------------------
  if (args.length < 1) {
    printHelpMessage();
  }
  
  // -- Loop through all arguments --------------------------------------------- 
  for (i <- 0 until args.length) {
    if (!parseArg(args(i))) {
      interpret(args(i));
    }
  }
}