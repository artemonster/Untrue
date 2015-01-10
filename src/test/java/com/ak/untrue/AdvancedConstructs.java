package com.ak.untrue;



import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
/**
 * In this testcase some advanced language stuff is tested.
 */
@Ignore
public class AdvancedConstructs {
	@Test
	public void testBasicConditional() {
		String in = "(define (map f list) "
						+ "(if (notEmpty list) "
							+ "((f head(list)) (map f (tail (list))))"
						+ ")"
					+ ")";
		Assert.assertEquals(in, in);
	}
}
