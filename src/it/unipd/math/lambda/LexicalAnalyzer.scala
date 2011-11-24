/**
 * @author Alberto Franco
 * @file   LexicalAnalyzer.scala
 * @brief  Contains the definition the class used to lexical analyze input files
 */
package it.unipd.math.lambda

class LexicalAnalyzer {
  
  /**
   * Parse a variable of the list. 
   * @param symbols List of char to parse
   * @param auxVar  Variable name
   * @return A variable token.
   */
  def lexVariable(symbols: List[Char], auxVar:String): (List[Token], List[Char]) = 
    (symbols) match {
    
    // -- The list is not ended ------------------------------------------------
    case symbol::list => {
      
      // -- If it is a whitespace return variable token ------------------------
      if (LexicalUtil.isWhitespace(symbol)) {
        (Variable(auxVar)::Nil, list);
      } else if (LexicalUtil.isOperator(symbol)) {
        (Variable(auxVar)::Operator(symbol)::Nil, list);
      } else {
        lexVariable(list, auxVar + symbol);
      }
    }
    
    //-- Last element of the list ----------------------------------------------
    case Nil => (Variable(auxVar)::Nil, Nil); 
  }
  
  /**
   * Lexical analyze a list of char.
   * @param symbols The list of symbols to analyze.
   * @return The calculated list.
   */
  def lex(symbols:List[Char]): List[Token] = (symbols) match {
    
    // -- Convert the list of chars into a list of tokens
  	case symbol::restOfList => {
      if (LexicalUtil.isOperator(symbol)) {
        Operator(symbol) :: lex(restOfList);
      } else if (LexicalUtil.isWhitespace(symbol)) {
        lex(restOfList);
      } else {
        val (variable, list) = lexVariable(symbols, "");
        variable ++ lex(list);
      }
    }
    
    // -- We are at the end of the list. ---------------------------------------
    case Nil => Nil;
  } 
}
