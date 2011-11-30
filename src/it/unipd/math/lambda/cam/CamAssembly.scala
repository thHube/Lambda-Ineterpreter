package it.unipd.math.lambda.cam

// -- Abstract class for categorical abstract machine instructions ------------- 
abstract class CamAssembly 

// -- Push current enviroment into the stack ----------------------------------- 
case object Push extends CamAssembly

// -- Swap the head of the stack with the environment --------------------------
case object Swap extends CamAssembly

// -- Concatenate the head of the stack with environemt ------------------------
case object Cons extends CamAssembly

// -- If on the stack there is a couple take the first -------------------------
case object Fst extends CamAssembly

// -- If on the stack there is a couple take the second -----------------------
case object Snd extends CamAssembly

// -- Lambda abstraction -------------------------------------------------------
case class Cur(lambda:List[CamAssembly]) extends CamAssembly

// -- Application of function --------------------------------------------------
case object App extends CamAssembly

object CamAsmPrinter {
  def write(list: List[CamAssembly]): String = list match {
    case Push::rest => "PUSH; " + write(rest);
    case Swap::rest => "SWAP; " + write(rest);
    case Cons::rest => "CONS; " + write(rest);
    case Fst::rest => "FST; " + write(rest);
    case Snd::rest => "SND; " + write(rest);
    case Cur(asmList)::rest => "CUR[" + write(asmList) +"]; " + write(rest);
    case App::rest => "APP; " + write(rest);
    case Nil => "";
  }
}

