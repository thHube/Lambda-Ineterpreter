package it.unipd.math.lambda.cam

// -- To use environments in a
abstract class Environment {
  def copy():Environment;
}

// -- Identity function as a place holder in env -------------------------------
case class EnvIdentity extends Environment {
  override def copy():Environment = new EnvIdentity;
} 

// -- Variable in the environment. ---------------------------------------------
case class EnvVar(variable:String) extends Environment {
  override def copy(): Environment =new EnvVar(variable);
}

// -- Asm instruction ----------------------------------------------------------
case class EnvInst(asm:CamAssembly) extends Environment {
  override def copy(): Environment = new EnvInst(asm);
}

// -- A couple in the environment ----------------------------------------------
case class EnvCouple(first:Environment, second:Environment) extends Environment {
  override def copy():Environment =new EnvCouple(first, second); 
  
  // -- Return first element of the couple -------------------------------------
  def getFirst():Environment  = first;
  
  // -- Return second element of the couple ------------------------------------
  def getSecond():Environment = second;
} 

case class EnvList(list:List[Environment]) extends Environment {
  override def copy = new EnvList(list);
}

/**
 * Class for printing environments.
 */
object EnvPrinter {
  def write(env:Environment):String = env match {
    case EnvIdentity() => "id";
    case EnvVar(name) => name;
    case EnvInst(asm) => asm.toString;
    case EnvCouple(fst, snd) => "<" + write(fst) + ", " + write(snd) + ">"; 
  }
}
