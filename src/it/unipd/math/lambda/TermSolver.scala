/**
 * @author Alberto Franco
 * -- TermSolver.scala --
 * Contains definition of the trait term solver that defines a prototype of 
 * implementation for lambda-terms normalizer.     
 */
package it.unipd.math.lambda


trait TermSolver {
  /**
   *  Normalize a term. This process ends only for those that are strongly 
   *  normalizable, other may not end at all or may end depending on the way 
   *  the term is solved. The only operation that is allowed is beta-reduction.
   *  @param lambda The term to normalize.
   *  @return Normalized term. 
   */
  def normalize(lambda:Term): Term
}

// -- Best solution method implemented.
object BestSolver {
  /**
   * The instance of the best solution method for lambda terms. Currently 
   * is lambda rewriter, in the future maybe I will add a categorical 
   * interpretation if got enough time.
   */
  var solver:TermSolver = new LambdaRewriter;
}
