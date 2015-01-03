package com.ak.untrue.reducers;

import java.util.List;

import com.ak.untrue.Expression;
import com.ak.untrue.Expression.Type;

public class DeltaReducer {
	public static Expression getNew(Expression toRewrite) {
		String operation = toRewrite.getValue();
		//sanity checks
		List<Expression> args = toRewrite.getChildren();
		assert args.size() == toRewrite.getArity(); 
		assert toRewrite.getType() == Type.ALU_OP;
		for (Expression arg : args) {
			assert arg.getType() == Type.LITERAL_NUM || arg.getType() == Type.LITERAL_BOOL;
		}

		switch (operation) {
			case "+": {
				Integer arg1 = Integer.valueOf(args.get(0).getValue());
				Integer arg2 = Integer.valueOf(args.get(1).getValue());
				Integer result = arg1 + arg2;
				return new Expression(result.toString(), 0, Type.LITERAL_NUM);
			}
			case "*": {
				Integer arg1 = Integer.valueOf(args.get(0).getValue());
				Integer arg2 = Integer.valueOf(args.get(1).getValue());
				Integer result = arg1 * arg2;
				return new Expression(result.toString(), 0, Type.LITERAL_NUM);				
			}
			default: System.out.println("WTF is that?"); break; 			
		}
		return null;
	}
}
