package com.ak.untrue.basic;

import org.junit.Assert;
import org.junit.Test;
import com.ak.untrue.EvalEngine;

public class BetaRedexTests {

	@Test
	public void testBasicListing() {
		String in = "(hd (1 2 3))";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("1", result);
		Assert.assertEquals(1, EvalEngine.getTotalTicks());				
	}	
	
	@Test
	public void testBasicListing2() {
		String in = "(tl (1 2 3))";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("2:[3]", result);
		Assert.assertEquals(1, EvalEngine.getTotalTicks());				
	}
	
	@Test
	public void testBasicListing3() {
		String in = "((hd (1 5)) 2 3)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("1:[2,3]", result);
		Assert.assertEquals(1, EvalEngine.getTotalTicks());				
	}	
	
	@Test
	public void testBasicListing4() {
		String in = "(tl (tl (1 2)))";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("", result);
		Assert.assertEquals(2, EvalEngine.getTotalTicks());				
	}	
	
	@Test
	public void testBasicListing5() {
		String in = "(tl (tl (1 2 3)))";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("3", result);
		Assert.assertEquals(2, EvalEngine.getTotalTicks());				
	}		
	@Test
	public void testBasicConditional0() {
		String in = "(if #t 1 0)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("1", result);
		Assert.assertEquals(1, EvalEngine.getTotalTicks());				
	}
	
	@Test
	public void testBasicConditional00() {
		String in = "(if #f 1 0)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("0", result);
		Assert.assertEquals(1, EvalEngine.getTotalTicks());				
	}	
	
	@Test
	public void testBasicConditional() {
		String in = "(if (> 1 0) 1 0)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("1", result);
		Assert.assertEquals(2, EvalEngine.getTotalTicks());				
	}
	
	@Test
	public void testBasicConditional2() {
		String in = "(if (> 1 10) 1 0)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("0", result);
		Assert.assertEquals(2, EvalEngine.getTotalTicks());				
	}
	/**
	 * In this test (although not checked programmatically) a speculative reduction of 
	 * both branches occur, even if predicate is not yet resolved.
	 */
	@Test
	public void testSpeculativeConditional() {
		String in = "(if (tl (tl (#f #f #f))) (+ 1 2) (* 3 4))";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("12", result);
		Assert.assertEquals(3, EvalEngine.getTotalTicks());				
	}	
	/**
	 * This test also shows speculative execution. The both branches of if and a predicate take 1 
	 * tick to be rewritten, and also 1 tick to rewrite if expression -> total of 2.
	 * If it would be serial execution, then it would be: 
	 * 1 tick to resolve predicate, 
	 * 1 tick to resolve if
	 * 1 tick to resolve expression
	 */
	@Test
	public void testSpeculativeConditional2() {
		String in = "(if (tl (#f #f)) (+ 1 2) (* 3 4))";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("12", result);
		Assert.assertEquals(2, EvalEngine.getTotalTicks());				
	}	
	@Test
	public void testBasicDefine() {
		EvalEngine.evaluate("(define five 5)");
		String in = "(+ 5 five)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("10", result);
		Assert.assertEquals(4, EvalEngine.getTotalTicks());				
	}
	
	@Test
	public void testBasicDefine2() {
		EvalEngine.evaluate("(define (square x) (* x x ))");
		String in = "(square 5)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("25", result);
		Assert.assertEquals(4, EvalEngine.getTotalTicks());		
	}
	
	@Test
	public void testBasicNested() {
		EvalEngine.evaluate("(define (square x) (* x x ))");
		String in = "(square (square 5))";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("625", result);
		Assert.assertEquals(4, EvalEngine.getTotalTicks());			
	}
	
	@Test
	public void testBasicRecursiveDefine() {
		EvalEngine.evaluate("(define (fact x) (if (= x 1) 1 (* x fact(- x 1))))");
		String in = "(fact 10)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("625", result);
		Assert.assertEquals(4, EvalEngine.getTotalTicks());				
	}
	
	@Test
	public void testConcurrent() {	
		EvalEngine.evaluate("(define (square x) (* x x ))");
		EvalEngine.evaluate("(define (fact x) (if (= x 1) 1 (* x fact(- x 1))))");
		EvalEngine.evaluate("(define (dist x y) (+ (square x) (square y)))");
		String in = "(dist_sq square(5) fact(11))";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("625", result);
		Assert.assertEquals(4, EvalEngine.getTotalTicks());				
	}
	
	@Test
	public void testBasicCurryCompose() {
		EvalEngine.evaluate("(define (repeat f x) (f (f x)))");
		EvalEngine.evaluate("(define (by2 x) (* 2 x))");
		String in = "((repeat by2) 10)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("40", result);
		Assert.assertEquals(4, EvalEngine.getTotalTicks());				
	}		
}
