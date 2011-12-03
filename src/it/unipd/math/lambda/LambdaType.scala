package it.unipd.math.lambda

// -- This class represent a general type --------------------------------------
class LambdaType 

// -- A leaf type represent type that cannot be unified ------------------------
case class LeafType(name:Int) extends LambdaType;

// -- Application Type ---------------------------------------------------------
case class AppType(from:LambdaType, to:LambdaType) extends LambdaType

// -- CoupleType ---------------------------------------------------------------
case class CoupleType(fst:LambdaType, snd:LambdaType) extends LambdaType 

// -- Support class for typed lambda terms. ------------------------------------
case class TypedTerm(term:Term, t:LambdaType) {
  def getType():LambdaType = t;
}

object TypeWriter {
  
  /**
   * Convert a type to a string 
   * @param t Type to write
   * @return String with the type
   */
  def write(t:LambdaType):String = t match {
    case LeafType(name) => "T[" + name + "]";
    case AppType(from, to) => "(" + write(from) + "->" + write(to) + ")";
    case CoupleType(f, s) => "<" + write(f) + ", " + write(s) + ">";
  }
}
