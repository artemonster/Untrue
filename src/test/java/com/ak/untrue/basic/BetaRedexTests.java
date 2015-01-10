package com.ak.untrue.basic;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.ak.untrue.EvalEngine;
@Ignore
public class BetaRedexTests {
	@Test
	public void testBasicSubstution() {
		String in = "(+ 5 five)";
		String result = EvalEngine.evaluate(in);
		EvalEngine.updateEnv("TODO:(define five 5)");
		Assert.assertEquals("10", result);
		Assert.assertEquals(2, EvalEngine.getTotalTicks());		
	}
	@Test
	public void testBasicCall() {	
		String in = "(square 5)";
		String result = EvalEngine.evaluate(in);
		EvalEngine.updateEnv("TODO:(define square (x) (* x x))");
		Assert.assertEquals("25", result);
		Assert.assertEquals(2, EvalEngine.getTotalTicks());			
	}
	@Test
	public void testBasicNested() {
		String in = "(square (square 5))";
		String result = EvalEngine.evaluate(in);
		EvalEngine.updateEnv("TODO:(define square (x) (* x x))");
		Assert.assertEquals("625", result);
		Assert.assertEquals(4, EvalEngine.getTotalTicks());			
	}	
	@Test
	public void testBasicConditional() {
		String in = "(if (> 1 0) 1 0)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("1", result);
		Assert.assertEquals(4, EvalEngine.getTotalTicks());				
	}
	@Test
	public void testBasicConditional2() {
		String in = "(if (> 1 10) 1 0)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("0", result);
		Assert.assertEquals(4, EvalEngine.getTotalTicks());				
	}	
	@Test
	public void testBasicDefine() {
		EvalEngine.evaluate("(define five () 5)");
		String in = "(+ 5 five)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("10", result);
		Assert.assertEquals(4, EvalEngine.getTotalTicks());				
	}
	@Test
	public void testBasicDefine2() {
		EvalEngine.evaluate("(define square (x) (* x x ))");
		String in = "(square 5)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("25", result);
		Assert.assertEquals(4, EvalEngine.getTotalTicks());				
	}	
	@Test
	public void testBasicRecursiveDefine() {
		EvalEngine.evaluate("(define fact (x) (if (= x 1) 1 (* x fact(- x 1))))");
		String in = "(fact 10)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("625", result);
		Assert.assertEquals(4, EvalEngine.getTotalTicks());				
	}
	@Test
	public void testConcurrent() {	
		EvalEngine.evaluate("(define square (x) (* x x ))");
		EvalEngine.evaluate("(define fact (x) (if (= x 1) 1 (* x fact(- x 1))))");
		EvalEngine.evaluate("(define dist (x y) (+ (square x) (square y)))");
		String in = "(dist_sq square(5) fact(11))";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("625", result);
		Assert.assertEquals(4, EvalEngine.getTotalTicks());				
	}
}