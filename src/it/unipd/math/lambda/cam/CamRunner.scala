package it.unipd.math.lambda.cam

import scala.collection.mutable.Stack

// --  Exception for errror handling -------------------------------------------
class CamRunnerException extends Exception;

// -- Runs a program in Categorical Abstract Machine ---------------------------
class CamRunner(environment:Environment) {
  
  // -- Environment stack and current environment ------------------------------
  private var stack:Stack[Environment] = new Stack[Environment];
  private var env:Environment = environment;
  
  // -- Error printing utility method ------------------------------------------
  private def error(msg:String) {
    System.err.println("[CAM runner] >> " + msg);
  }
  
  /**
   * Run a list of CAM instructions. Modify current environment and stack
   * does not return any value. 
   * @param instruction The list of instruction that has to be run  
   */
  def run(instrList:List[CamAssembly]):Environment = (instrList) match {
    case instruction::list => (instruction) match {
      
      // -- PUSH ---------------------------------------------------------------
      case Push => {
        stack.push(env.copy());
        run(list);
      }
      
      // -- SWAP ---------------------------------------------------------------
      case Swap => {
       val stackTop:Environment = stack.pop;
       stack.push(env);
       env = stackTop;
       run(list);
      }
      
      // -- CONS ---------------------------------------------------------------
      case Cons => {
        val stackTop:Environment = stack.pop;
        val newEnv:Environment = new EnvCouple(stackTop, env);
        env = newEnv;
        run(list)
      }
      
      // -- FST ----------------------------------------------------------------
      case Fst => env match {
        case EnvCouple(fst, snd) => {
          env = fst;
          run(list)
        }
        case _ => {
          error("Expected a couple in the environment");
          throw new CamRunnerException;
        }
      }
      
      // -- SND ----------------------------------------------------------------
      case Snd => env match {
        case EnvCouple(fst, snd) => {
          env = snd;
          run(list)
        }
        case _ => {
          error("Expected a couple in the environment");
          throw new CamRunnerException;
        }
      }
      
      // -- CUR ----------------------------------------------------------------
      case Cur(inst) => {
        env = EnvCouple(env, EnvInst(Cur(inst)));
        run(list);
      }
      
      // -- APP ----------------------------------------------------------------
      case App => env match {
        
        case EnvCouple(EnvCouple(a, EnvInst(Cur(l))), b) => {
          env = EnvCouple(a, b);
          run(l ++ list);
        }
        // -- Error? -----------------------------------------------------------
        case _ => {
          error("Expected a couple <a;CUR(L), b> in environment with APP");
          throw new CamRunnerException;
        } 
      }
      
    }
    
    // -- Terminate and return env ---------------------------------------------
    case Nil => env;
  } 
}
