package com.ak.untrue.reducers;

import java.util.Deque;

import com.ak.untrue.Expression;
import com.ak.untrue.Expression.Type;

public class DeltaReducer {
	public static Expression getNew(Expression toRewrite) {
		String operation = toRewrite.getValue();
		Deque<Expression> args = toRewrite.getArgs();
		Expression arg_1 = args.pop();
		Expression arg_2 = args.size() != 0 ? args.pop() : null; 
		//sanity checks	
		//assert args.size() == toRewrite.getArity(); 
		assert toRewrite.getType() == Type.ALU_OP;
		assert args.size() == 0 : "ALU operations can't have more arguments!";
		for (Expression arg : args) {
			assert arg.getType() == Type.LITERAL_NUM || arg.getType() == Type.LITERAL_BOOL;
		}
		switch (operation) {
			case "-": {
				Integer arg1 = Integer.valueOf(arg_1.getValue());
				Integer arg2 = Integer.valueOf(arg_2.getValue());
				Integer result = arg1 - arg2;
				return new Expression(result.toString(), 0, Type.LITERAL_NUM);
			}
			case "+": {
				Integer arg1 = Integer.valueOf(arg_1.getValue());
				Integer arg2 = Integer.valueOf(arg_2.getValue());
				Integer result = arg1 + arg2;
				return new Expression(result.toString(), 0, Type.LITERAL_NUM);
			}
			case "*": {
				Integer arg1 = Integer.valueOf(arg_1.getValue());
				Integer arg2 = Integer.valueOf(arg_2.getValue());
				Integer result = arg1 * arg2;
				return new Expression(result.toString(), 0, Type.LITERAL_NUM);				
			}
			case "and": {
				Boolean arg1 = arg_1.getValue().equals("#t") ? true : false;
				Boolean arg2 = arg_2.getValue().equals("#t") ? true : false;
				Boolean result = arg1 & arg2;
				String resValue = result ? "#t" : "#f";
				return new Expression(resValue, 0, Type.LITERAL_BOOL);				
			}
			case "or": {
				Boolean arg1 = arg_1.getValue().equals("#t") ? true : false;
				Boolean arg2 = arg_2.getValue().equals("#t") ? true : false;
				Boolean result = arg1 | arg2;
				String resValue = result ? "#t" : "#f";
				return new Expression(resValue, 0, Type.LITERAL_BOOL);				
			}
			case "xor": {
				Boolean arg1 = arg_1.getValue().equals("#t") ? true : false;
				Boolean arg2 = arg_2.getValue().equals("#t") ? true : false;
				Boolean result = arg1 ^ arg2;
				String resValue = result ? "#t" : "#f";
				return new Expression(resValue, 0, Type.LITERAL_BOOL);				
			}			
			case "not": {
				Boolean arg1 = arg_1.getValue().equals("#t") ? true : false;
				Boolean result = !arg1;
				String resValue = result ? "#t" : "#f";
				return new Expression(resValue, 0, Type.LITERAL_BOOL);				
			}	
			case ">": {
				Integer arg1 = Integer.valueOf(arg_1.getValue());
				Integer arg2 = Integer.valueOf(arg_2.getValue());
				Boolean result = arg1 > arg2;
				String resValue = result ? "#t" : "#f";
				return new Expression(resValue, 0, Type.LITERAL_BOOL);				
			}
			case "<": {
				Integer arg1 = Integer.valueOf(arg_1.getValue());
				Integer arg2 = Integer.valueOf(arg_2.getValue());
				Boolean result = arg1 < arg2;
				String resValue = result ? "#t" : "#f";
				return new Expression(resValue, 0, Type.LITERAL_BOOL);				
			}	
			case "=": {
				Integer arg1 = Integer.valueOf(arg_1.getValue());
				Integer arg2 = Integer.valueOf(arg_2.getValue());
				Boolean result = arg1 == arg2;
				String resValue = result ? "#t" : "#f";
				return new Expression(resValue, 0, Type.LITERAL_BOOL);				
			}			
			default: {
				System.out.println("What is " + operation +" ?"); break; 			
			}
		}
		return null;
	}
}
