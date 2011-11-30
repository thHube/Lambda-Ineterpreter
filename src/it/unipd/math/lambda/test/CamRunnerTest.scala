package it.unipd.math.lambda.test

import it.unipd.math.lambda.cam.CamRunner
import it.unipd.math.lambda.cam.EnvPrinter

object CamRunnerTest {

  def run(filename:String) = {
    val (code, env) = CamCodeGenerationTest.generateCode(filename);
    val runner:CamRunner = new CamRunner(env);
    val finalEnv = runner.run(code);
   
    println("Normalized category: " + EnvPrinter.write(finalEnv));
  } 
  
  def main(args:Array[String]) {
	run("examples/application.lbd");  
  }
  
}