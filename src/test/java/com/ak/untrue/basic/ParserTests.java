package com.ak.untrue.basic;

import org.junit.Assert;
import org.junit.Test;

import com.ak.untrue.AbstractSyntaxTree;
import com.ak.untrue.util.Parser;

public class ParserTests {
	
	@Test
    public void testBasicAST_PrettyPrint() {
		AbstractSyntaxTree<String> main = new AbstractSyntaxTree<String>();
		AbstractSyntaxTree<String> c1 = new AbstractSyntaxTree<String>();
		AbstractSyntaxTree<String> c3 = new AbstractSyntaxTree<String>();
		main.add(c1);
		main.add(new AbstractSyntaxTree<String>("c2"));
		main.add(c3);
		c1.add(new AbstractSyntaxTree<String>("c11"));
		c1.add(new AbstractSyntaxTree<String>("c12"));
		c3.add(new AbstractSyntaxTree<String>("c31"));
		
		Assert.assertEquals("(( c11  c12 ) c2 ( c31 ))", main.prettyPrint());
	}
	
	@Test
    public void testAST_Iterator() {
		AbstractSyntaxTree<String> main = new AbstractSyntaxTree<String>();
		AbstractSyntaxTree<String> c1 = new AbstractSyntaxTree<String>();
		AbstractSyntaxTree<String> c3 = new AbstractSyntaxTree<String>();
		main.add(c1);
		main.add(new AbstractSyntaxTree<String>("c2"));
		main.add(c3);
		c1.add(new AbstractSyntaxTree<String>("c11"));
		c1.add(new AbstractSyntaxTree<String>("c12"));
		c1.add(new AbstractSyntaxTree<String>("c13"));
		c3.add(new AbstractSyntaxTree<String>("c31"));
		StringBuilder sb = new StringBuilder();
		for (AbstractSyntaxTree<String> node: main) {
			sb.append(node.toString()).append(" ");
		}		
		Assert.assertEquals("() c11 c12 c13 c2 () c31 ", sb.toString()); //note spaces!
	}	
		
	@Test
    public void testBasicParseAndPrint1() {
		String toParse = "(a b c 1 2)";
		AbstractSyntaxTree<String> ast = Parser.parseString(toParse);
		Assert.assertEquals("( a  b  c  1  2 )", ast.prettyPrint());
	}
	
	@Test
    public void testBasicParseAndPrint2() {
		String toParse = "((a b) (e f))";
		AbstractSyntaxTree<String> ast = Parser.parseString(toParse);
		Assert.assertEquals("(( a  b )( e  f ))", ast.prettyPrint());
	}
		
	@Test
    public void testBasicParseAndPrint3() {
		String toParse = "(a b (c 1) 2)";
		AbstractSyntaxTree<String> ast = Parser.parseString(toParse);
		Assert.assertEquals("( a  b ( c  1 ) 2 )", ast.prettyPrint());
	}

	@Test
    public void testAdvancedParsePrint1() {
		String toParse = "(main (blargh (a b c d e)) blob ())";
		AbstractSyntaxTree<String> ast = Parser.parseString(toParse);
		Assert.assertEquals("( main ( blargh ( a  b  c  d  e )) blob ())", ast.prettyPrint());
	}
	
	@Test
    public void testAdvancedParsePrint2() {
		String toParse = "(a b      \n"
				+ "         (c 1) 2)";
		AbstractSyntaxTree<String> ast = Parser.parseString(toParse);
		Assert.assertEquals("( a  b ( c  1 ) 2 )", ast.prettyPrint());
	}	
	
	@Test
    public void testAdvancedParsePrint3() {
		String toParse = "(a b ;;;;this is a comment    \n"
				+ "         (c 1) 2)";
		AbstractSyntaxTree<String> ast = Parser.parseString(toParse);
		Assert.assertEquals("( a  b ( c  1 ) 2 )", ast.prettyPrint());
	}
}
