/**
 * @author Alberto Franco
 * @file   Token.scala
 * @brief  Contains the definition of the Token hierarchy for lexical analysis.
 */
package it.unipd.math.lambda

// -- A token is a syntatical element that has relevancy in our lambda 	      -- 
// -- interepreter, input file are transformed in a sequence of tokens before --
// -- begin parsed.                                                           --
abstract class Token

// -- An operator delimits lambda astraction and applications ------------------
case class Operator(name:Char) extends Token

// -- Variables are the atoms of lambda calculus -------------------------------  
case class Variable(name:String) extends Token

/**
 * This object contains a series of simple utility methods to lex files. 
 */
object LexicalUtil {
  
  /**
   * Check if the given symbol is a whitespace or not.
   * @param symbol The symbol to check.
   * @return True if it is whitespace, false else. 
   */
  def isWhitespace(symbol:Char):Boolean = (symbol) match {
    case ' '  => true;
    case '\n' => true;
    case '\t' => true;
    case _    => false;
  }
  
  /**
   * Return if the given symbol is an operator
   * @param symbol The symbol to analyze
   * @return True if the symbol is an operator, False else.
   */
  def isOperator(symbol:Char):Boolean = (symbol) match {
    case '[' | ']' => true;
    case '(' | ')' => true;
    case '<' | '>' => true;
    case ',' => true;
    case _ => false;
  }
}
