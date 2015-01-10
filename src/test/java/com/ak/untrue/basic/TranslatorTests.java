package com.ak.untrue.basic;

import org.junit.Assert;
import org.junit.Test;

import com.ak.untrue.Expression;
import com.ak.untrue.util.Parser;
import com.ak.untrue.util.Translator;

public class TranslatorTests {
	@Test
	public void testComplexString() {
		String in = "(if (notEmpty list) "
							+ "((f (head list)) (map f (tail (list))))"
					+ ")";	
		Expression root = Translator.compileStringAST(Parser.parseString(in));
		Assert.assertEquals("if:[notEmpty:[list],f:[head:[list],map:[f,tail:[list]]]]", root.toString());
	}
	@Test
	public void testBasicList() {
		String in = "(hd (1 2 3 4 5))";	
		Expression root = Translator.compileStringAST(Parser.parseString(in));
		Assert.assertEquals("hd:[1:[2,3,4,5]]", root.toString());
	}	
	/**
	 * Although the result is somewhat funky, this is correct. The list 1 2 3 gets a new list(!)
	 * appended at the end.
	 */
	@Test
	public void testBasicList2() {
		String in = "((1 2 3) (4 5 6))";	
		Expression root = Translator.compileStringAST(Parser.parseString(in));
		Assert.assertEquals("1:[2,3,4:[5,6]]", root.toString());
	}	
	
	@Test
	public void testBasicList3() {
		String in = "(1 2 (3 (4 5)))";	
		Expression root = Translator.compileStringAST(Parser.parseString(in));
		Assert.assertEquals("1:[2,3:[4:[5]]]", root.toString());
	}
	@Test
	public void testCurry1() {
		String in = "((+) 2 3)";	
		Expression root = Translator.compileStringAST(Parser.parseString(in));
		Assert.assertEquals("+:[2,3]", root.toString());
	}
	@Test
	public void testCurry2() {
		String in = "(((+) 1) 3)";	
		Expression root = Translator.compileStringAST(Parser.parseString(in));
		Assert.assertEquals("+:[1,3]", root.toString());
	}
	@Test
	public void testStrangeCurry() {
		String in = "(+ (1 3))";	
		Expression root = Translator.compileStringAST(Parser.parseString(in));
		Assert.assertEquals("+:[1:[3]]", root.toString());
	}
	@Test
	public void testCurry3() {
		String in = "((((lol) arg1) arg2) arg3 arg4)";	
		Expression root = Translator.compileStringAST(Parser.parseString(in));
		Assert.assertEquals("lol:[arg1,arg2,arg3,arg4]", root.toString());
	}
}
