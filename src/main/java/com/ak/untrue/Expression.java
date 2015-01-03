package com.ak.untrue;

public class Expression {
	public enum Type {
		SYMBOL,
		ALU_OP,
		LITERAL_NUM,
		LITERAL_BOOL,
		LITERAL_STR,
		LITERAL_CHR
	}
	private Type type_;
	private String name_;
	private int arity_;
	private int minRdy_;
	private boolean ready_;
	
	public boolean isReady() {
		return ready_;
	}	
	
	public Type getType() {
		return type_;
	}
	
	public Expression(String name, int arity, Type type) {
		name_ = name;
		arity_ = arity;
		if (arity == 0) {
			ready_ = true;
		} else {
			ready_ = false;
		}
	}

	public void rewrite() {
		//this expression is a part of a tree with some children in it.
		//we take the body template from this exp, which is AST<EXP>
		//update the key-object map with new AST expression
		//take all children and replace all argument occurences with them
		//ALSO update their parent lists!
		
	}
}
