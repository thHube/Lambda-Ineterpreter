/**
 * @author Alberto Franco
 * 
 * Contains definition of Lambda place holders. This represent a lambda 
 * abstraction with all the place where the variable appear with a marker.
 */
package it.unipd.math.lambda

class LambdaPlaceholder(abs:Var, body:Term) extends Lambda(abs, body) {
  
  // -- Print an error on error stream. ----------------------------------------
  private def error(msg:String) {
    System.err.println("[Lambda Rewriter] >> " + msg);
  }
  
  /**
   * Support method for lambda substitution
   * @param body Body to exploit and substitute.
   * @param term The term to substitute 
   * @return Substituted lambda term
   * @throws LambdaRewriteException If something go wrong.
   */
  @throws(classOf[LambdaRewriteException])
  protected def lambdaSubstitute(body:Term, term:Term):Lambda = (body) match {
    case Lambda(a, b) => {
      if (a.name != abs.name) {
        Lambda(a, subsistute(b, term.copy()));
      } else {
        Lambda(a, b);
      }
    }
    case _ => {
     error("Required lambda, other found");
     throw new LambdaRewriteException;
    }
     
  }
  
  /**
   * Substitute variable into the lambda term.
   * @param body Body to exploit and substitute.
   * @param term The term to substitute 
   * @return Substituted lambda term
   * @throws LambdaRewriteException If something go wrong.
   */
  @throws(classOf[LambdaRewriteException])
  protected def subsistute(body:Term, term:Term):Term = (body) match {
    case Var(name) => if (abs.name.equals(name)) { term; } else { Var(name); }
    case Lambda(a, b) => {
      lambdaSubstitute(Lambda(a, b), term);
    }
    case App(lam, b) => (lam) match {
      case Lambda(a, b) => App(lambdaSubstitute(lam, term), subsistute(b, term));
      case Var(name) => App(lam, subsistute(b, term));
    }
    case _ => {
      error("Misplaced term");
      throw new LambdaRewriteException
    }
  }
  
  /**
   * Public method for substitution
   * @param term the term to substitute into current lambda abstraction
   * @return substituted term.
   */
  def replace(term:Term):Term = subsistute(body, term);
  
} 
