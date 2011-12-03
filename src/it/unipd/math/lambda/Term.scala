/**
 * @author Alberto Franco
 * @file   Term.scala
 * @brief  contains the definition of hierarchy for parsing and beta reducing.
 */
package it.unipd.math.lambda

// -- Hierarchy of lambda calculus, base is Term. We have variables, lambda  --
// -- abstraction and application of functions. We use this classes to parse  --
// -- input files and to beta reduct                                          --
abstract class Term {
  def copy():Term;
}

// -- VARIABLES ----------------------------------------------------------------
case class Var(name:String) extends Term {
  def copy():Term = Var(name);
}

// -- LAMBDA -------------------------------------------------------------------
case class Lambda(abstraction:Var, body:Term) extends Term {
  def copy():Term = Lambda(abstraction, body);
}

// -- APPLICATION OF FUNCTIONS -------------------------------------------------
case class App(func:Term, param:Term) extends Term {
  def copy():Term = App(func, param);
}

// -- COUPLE (Used just for typing) --------------------------------------------
case class Couple(fst:Term, snd:Term) extends Term {
  def copy():Term = Couple(fst, snd);
}

// -- For printing ------------------------------------------------------------- 
object TermWriter {
  
  /**
   * Print the given term to a string. 
   * @param term The term to print
   * @return String holding representation of the lambda term
   */
  def write(term:Term):String = (term) match {
    case Var(name) => name;
    case Lambda(Var(name), term) => "[lambda " + name + "." + write(term) + "]";
    case App(lam, term) => write(lam) + "(" + write(term) + ")";
  } 
}
