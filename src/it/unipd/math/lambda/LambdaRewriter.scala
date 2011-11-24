/**
 * @author Alberto Franco
 * -- LambdaRewriter.scala --
 * Contains definition of the rewriter. Each application is resolved with 
 * substitution that is straight beta reduction. 
 */
package it.unipd.math.lambda

class LambdaRewriteException extends Exception

class LambdaRewriter extends TermSolver {

  /**
   * Normalize a lambda term with rewrite mechanism. This is pretty inefficient
   * but it is academically interesting.  
   * @param lambda Term to normalize 
   * @return Normalized term
   */
  def normalize(lambda: Term): Term = (lambda) match {
    // -- Resolve application -------------------------------------------------- 
  	case App(Lambda(abs, body), term) => {
      val newLambda = new LambdaPlaceholder(abs, body);
      newLambda.replace(term);
    }
  	
    // -- Lambda abstraction are substituted with new class. --------------------
    case Lambda(abs, body) => {
      new LambdaPlaceholder(abs, body);
    }
    
    // -- Any other case is returned as is -------------------------------------
    case term:Term => term;
  }
  
}