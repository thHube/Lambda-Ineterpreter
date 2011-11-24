/**
 * @author Alberto Franco
 * @file   Parser.scala
 * @brief  Contains the definition of the Parser class. 
 */
package it.unipd.math.lambda

class ParserException extends Exception;

// -- Production rules for lambda grammar --------------------------------------
// -- 
// --     PROG   ::= VAR | LAMBDA | APP;
// --     VAR    ::= "varname";
// --     LAMBDA ::= '[' VAR ',' PROG ']';
// --     APP    ::= '(' LAMBDA ',' PROG ')';
// -- 
// -----------------------------------------------------------------------------
class Parser {

  def error(msg:String) {
    println("[Lambda Parser] >> " + msg);
  }
  
  // -- Production rule PROG --------------------------------------------------- 
  def parseProg(token: List[Token]): (Term, List[Token]) = (token) match {
    
    // -- Production rule VAR --------------------------------------------------
    case Variable(name)::list => {
        (Var(name), list);
    }
    
    // -- Choose the right production ------------------------------------------
    case Operator(symbol)::list => (symbol) match {
      case '[' => parseLambda(list);
      case '(' => parseApp(list);
      case _ => {
       error("Expected [ or ( and found " + symbol);
       throw new ParserException;
      }
    }
    
    case _ => {
      error("Something went wrong.");
      throw new ParserException;
    }
  }
  
  // -- Production rule VAR ----------------------------------------------------
  def parseVar(token: List[Token]): (Var, List[Token]) = (token) match {
    case Variable(name)::list => (Var(name), list);
    case _ => { 
      error("Expected variable.");
      throw new ParserException;
    }
  }
  
  // -- Production rule LAMBDA -------------------------------------------------
  def parseLambda(token: List[Token]): (Lambda, List[Token]) = (token) match {
    
    case Variable(name)::(Operator(comma)::list) => {
      if (comma == ',') {
        val (prog, restOfList) = parseProg(list);
        (restOfList) match {
          case Operator(']')::listToReturn => 
            (Lambda(Var(name), prog), listToReturn);
          case _ => {
            error("Expected ]");
            throw new ParserException;
          }
        }
      } else {
        error("Expected comma (,)");
        throw new ParserException;
      }
    } 
    
    // -- Any other case is an error ------------------------------------------- 
    case _ => {
      error("Expected lamba declaration");
      throw new ParserException;
    } 
  }
  
  // --  Production rule APP ---------------------------------------------------
  def parseApp(token: List[Token]): (App, List[Token]) = (token) match {
    case Operator('[')::list => {
      val (lambda, restOfList) = parseLambda(list);
      (restOfList) match {
        case Operator(',')::anothList => {
          val (prog, list2) = parseProg(anothList);
	      (list2) match {
	        case Operator(')')::toRetList => (App(lambda, prog), toRetList);
	        case _ => {
	          error("Expected )");
	          throw new ParserException;
	       } 
	      }
        }
        case _ => {
	      error("Expected comma (,)")
	      throw new ParserException;
        }
      }
      
    } 
    case _ => {
      error("Expected lambda abstraction");
      throw new ParserException;
    }
  }
  
  /**
   * Parse the given list of tokens.
   * @param tokens The list of token to parse
   * @return Root of the parse tree
   */
  def parse(tokens:List[Token]):Term = {
    val (parseTree, list) = parseProg(tokens);
    if (list != Nil) {
      error("Some tokens have been ignored.")
    }
    return parseTree;
  }
  
}