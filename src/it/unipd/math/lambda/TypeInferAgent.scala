package it.unipd.math.lambda

class CouldNotUnifyException extends Exception

/**
 * 
 */
class TypeInferAgent {

  // -- To assign types we use type ids ----------------------------------------
  private var typeId:Int = -1;
  // -- Current context --------------------------------------------------------
  private var context:List[TypedTerm] = Nil;
  
  /**
   * Create the next type identifier
   * @return type identifier.
   */
  private def getNextTypeId():Int = {
    typeId += 1;
    typeId;
  }
  
  /**
   * Check if in current context there is a var that has been yet typed.
   * @param v Var to search
   * @param list List to search in
   * @return Type if there is, null else
   */
  private def getVarType(v:Var, list:List[TypedTerm]):LambdaType = list match{
    case Nil => null;
    case TypedTerm(Var(name), t)::rest => 
      if (name == v.name) t
      else getVarType(v, rest);
  }
  
  /**
   * Unify types on roll back from type inference.
   * @param a Fist type to unify
   * @param b Second type to unify
   * @return Unified type, if possible
   */
  def unify(a:LambdaType, b:LambdaType):LambdaType = (a, b) match {
    // -- Leaf type is easy to unify -------------------------------------------
    case (LeafType(nameFst), LeafType(nameSnd)) => LeafType(nameFst);
    
    // -- If one is leaf and the other is app, type is app.
    case (LeafType(name), AppType(f, t)) => AppType(f, t);
    case (AppType(f, t), LeafType(name)) => AppType(f, t);
    
    // -- This is interesting we have to unify both types 
    case (AppType(f1, t1), AppType(f2, t2)) => {
      val from = unify(f1, f2);
      val to   = unify(t1, t2);
      AppType(from, to);
    }
    
    // -- If it is something else we cannot unify ------------------------------
    case _ => throw new CouldNotUnifyException;
  }
  
  /**
   * Infer type in given lambda term.
   * @param term The term to infer the type on.
   * @return term decorated with his type.
   */
  private def inferType(term:Term):TypedTerm = term match {
    case Var(name) => {
      // -- Check if there is the var in current context
      var typeVar = getVarType(Var(name), context);
      if (typeVar == null) {
        typeVar = LeafType(getNextTypeId());
      }
      TypedTerm(term, typeVar);
    }
    
    case App(func, params) => {
      // -- Calculate both type and then unify with expected.
      val f1:TypedTerm = inferType(func);
      val p1:TypedTerm = inferType(func);
      
      val expectedType = AppType(LeafType(getNextTypeId()), LeafType(getNextTypeId()));
      val unified = unify(f1.getType(), expectedType);
      // -- Now unify type from domain and params ------------------------------
      (unified) match {
        case AppType(d, c) => {
          val domain = unify(d, p1.getType());
          // -- Term has the type from codomain --------------------------------
          TypedTerm(term, c);
        }
        case _ => throw new CouldNotUnifyException;
      }
    }
    
    case Lambda(abs, body) => {
      // -- Augment context and recursively call type inference on body --------
      val absType = LeafType(getNextTypeId());
      context = TypedTerm(abs, absType) :: context;
      val bt = inferType(body);
      
      // -- Restore old context ------------------------------------------------
      context = context.tail;
      TypedTerm(term, AppType(absType, bt.getType()));
    } 
  }
  
  /**
   * Infer the type on current term 
   * @param term Lambda term to type
   * @return Type of term.
   */
  def infer(term:Term):LambdaType = {
    context = Nil;
    inferType(term).getType();
  }
  
}