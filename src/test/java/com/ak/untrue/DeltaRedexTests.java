package com.ak.untrue;

import org.junit.Assert;
import org.junit.Test;

public class DeltaRedexTests {
	@Test
    public void testBasicAdd() {
		String in = "(+ 1 2)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("3", result);
		Assert.assertEquals(1, EvalEngine.getTotalTicks());
    }
	
	@Test
    public void testBasicCompare() {
		String in = "(= 1 2)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("#f", result);
		Assert.assertEquals(1, EvalEngine.getTotalTicks());
    }  
	
	@Test
    public void testBasicCompare2() {
		String in = "(< 1 2)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("#t", result);
		Assert.assertEquals(1, EvalEngine.getTotalTicks());
    } 
	
	@Test
    public void testBasicBooleanLogic() {
		String in = "(and #t #t)";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("#t", result);
		Assert.assertEquals(1, EvalEngine.getTotalTicks());
    } 

	@Test
	public void testBasicConcurrentAdd() {
		String in = "(+ (+ 6 2) (* 3 4))";
		String result = EvalEngine.evaluate(in);
		Assert.assertEquals("20", result);
		Assert.assertEquals(2, EvalEngine.getTotalTicks());
	}
}
