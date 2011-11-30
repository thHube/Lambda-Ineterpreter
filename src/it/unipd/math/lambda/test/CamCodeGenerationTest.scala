package it.unipd.math.lambda.test

import it.unipd.math.lambda.cam.CamCodeGenerator
import it.unipd.math.lambda.cam.EnvPrinter
import it.unipd.math.lambda.cam.CamAsmPrinter
import it.unipd.math.lambda.cam.CamAssembly
import it.unipd.math.lambda.cam.Environment

object CamCodeGenerationTest {
  
  private var generator:CamCodeGenerator = new CamCodeGenerator;
  
  def generateCode(filename:String): (List[CamAssembly], Environment) = {
    val term = ParsingTest.parserTest(filename);
    val (code, env) = generator.generate(term);
    
    println("Code is: " + CamAsmPrinter.write(code));
    println("Env is: " + EnvPrinter.write(env));
    
    (code, env);
  }
  
  def main(args:Array[String]) {
    generateCode("examples/application.lbd")
  }
  
}