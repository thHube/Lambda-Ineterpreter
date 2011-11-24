/**
 * @author Alberto Franco
 * @file   Term.scala
 * @brief  contains the definition of hierarchy for parsing and beta reducing.
 */
package it.unipd.math.lambda

// -- Hierarchy of lambda calculus, base is Term. We have variables, lambda  --
// -- abstraction and application of functions. We use this classes to parse  --
// -- input files and to beta reduct                                          --
abstract class Term 

// -- VARIABLES ----------------------------------------------------------------
case class Var(name:String) extends Term

// -- LAMBDA -------------------------------------------------------------------
case class Lambda(abtraction:Var, body:Term) extends Term

// -- APPLICATION OF FUNCTIONS -------------------------------------------------
case class App(func:Lambda, param:Term) extends Term

// -- For printing ------------------------------------------------------------- 
object TermWriter {
  
  /**
   * Print the given term to a string. 
   * @param term The term to print
   * @return String holding representation of the lambda term
   */
  def write(term:Term):String = (term) match {
    case Var(name) => name;
    case Lambda(Var(name), term) => "[lambda " + name + "]." + write(term);
    case App(lam, term) => write(lam) + "(" + write(term) + ")";
  } 
}
