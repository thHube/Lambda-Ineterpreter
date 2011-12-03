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
// --     APP    ::= '(' LAMBDA ',' PROG ')' | '(' VAR ',' PROG ')';
// --     COUPLE ::= '<' PROG ',' PROG '>'
// -- 
// -----------------------------------------------------------------------------
class Parser {

  def error(msg:String) {
    System.err.println("[Lambda Parser] >> " + msg);
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
      case '<' => parseCouple(list);
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
    
    // -- This case we have a lambda to resolve --------------------------------
    case Operator('[')::list => {
      val (lambda, restOfList) = parseLambda(list);
      
      (restOfList) match {
        // -- Remove the comma and parse second hand term ----------------------
        case Operator(',')::anothList => {
          val (prog, list2) = parseProg(anothList);
	      (list2) match {
	        
	        // -- Finally remove last parenthesis ------------------------------ 
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
    
    // -- Here we have a variable ----------------------------------------------
    case Variable(name)::Operator(',')::list => {
      val (term, restOfList) = parseProg(list);
      (restOfList) match {
        case Operator(')')::endOfList => (App(Var(name),term), endOfList);
        case _ => {
          error("Expected (");
          throw new ParserException;
        }
      }
    }
    
    // -- All other cases ------------------------------------------------------
    case _ => {
      error("Expected lambda abstraction");
      throw new ParserException;
    }
  }
  
  // -- Parse production COUPLE ------------------------------------------------
  def parseCouple(token: List[Token]): (Couple, List[Token]) = {
    // -- Get first 
    val (fst, rest) = parseProg(token);
    rest match {
      case Operator(',')::list => {
        // -- Get second
        val (snd, rest) = parseProg(list);
        rest match {
          case Operator('>')::list => (Couple(fst, snd), list);
          
          // -- Close couple operator missing 
          case _ => {
            error("Operator > missing. Forget to close the couple?");
            throw new ParserException;
          }
        }
      }
      
      // -- Commma missing
      case _ => {
        error("Expected a comma at this point");
        throw new ParserException;
      }
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
