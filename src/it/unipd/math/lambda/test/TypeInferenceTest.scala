package it.unipd.math.lambda.test

import it.unipd.math.lambda.TypeInferAgent
import it.unipd.math.lambda.TypeWriter

object TypeInferenceTest {

  private var infer:TypeInferAgent = new TypeInferAgent;  
  
  def inferType(filename:String) {
    val parseTree = ParsingTest.parserTest(filename);
    val typed = infer.infer(parseTree);
    
    println("Type: " + TypeWriter.write(typed));
  }
  
  def main(args:Array[String]) {
    inferType("examples/application.lbd");
  }
  
}