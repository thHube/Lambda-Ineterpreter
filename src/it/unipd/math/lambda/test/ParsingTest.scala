/**
 * @author Alberto Franco
 * -- ParsingTest.scala --
 * Contains basic test for parsing  
 */
package it.unipd.math.lambda.test

import scala.io.Source
import it.unipd.math.lambda.Parser
import it.unipd.math.lambda.LexicalAnalyzer
import it.unipd.math.lambda.Term
import it.unipd.math.lambda.ParserException

// -- Test parsing utility -----------------------------------------------------
object ParsingTest {
  
  def parserTest(filename:String):Term = {
    val source = Source.fromFile(filename);
    val lines = source.mkString;
    source.close();
    
    val parser:Parser = new Parser;
    var lexer:LexicalAnalyzer = new LexicalAnalyzer;
    
    // -- Lexical Analysis -----------------------------------------------------
    val tokenList = lexer.lex(lines.toList);
    println(tokenList);
    try {
	    // -- Parsing ----------------------------------------------------------
	    val term:Term = parser.parse(tokenList);
	    println(term);
	    term;
    } catch {
      case exc:ParserException => {
        println("Something went wrong in parsing");
        exc.printStackTrace();
        null;
      }
      case e:Exception => {
        println("An exception occurred");
        e.printStackTrace;
        null;
      }
    }
  }
  
  def main(args:Array[String]) {
    parserTest("examples/application.lbd");
  }
  
}