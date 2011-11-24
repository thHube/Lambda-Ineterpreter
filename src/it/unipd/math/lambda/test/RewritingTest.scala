package it.unipd.math.lambda.test

import it.unipd.math.lambda.LambdaRewriter
import it.unipd.math.lambda.Term

object RewritingTest {

  def main(args:Array[String]) {
    val parseTree:Term = ParsingTest.parserTest("examples/simple.lbd");
    val rewriter  = new LambdaRewriter();
    
    val normalTerm = rewriter.normalize(parseTree);	
    println(normalTerm);
  }
}