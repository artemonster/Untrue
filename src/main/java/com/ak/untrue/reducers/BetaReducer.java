package com.ak.untrue.reducers;

import java.util.Deque;

import com.ak.untrue.Expression;
import com.ak.untrue.Expression.Type;

public class BetaReducer {
	public static Expression getNew(Expression toRewrite) {
		//sanity check
		assert toRewrite.getType() == Type.SYMBOL : "WUT?";
		String operation = toRewrite.getValue();
		@SuppressWarnings("unchecked")
		Deque<Expression> args = (Deque<Expression>) toRewrite.getChildren();
		switch(operation) {
			case "list": {
				Expression toRet = new Expression("",0,null);
			}
			case "def": {
				Expression symbolDef = args.removeFirst(); //this is a list (funName arg1...argN)
				Expression body = args.removeFirst();
				toRewrite.addToEnv(symbolDef.getValue(), body);
				return new Expression("#t",0,Type.LITERAL_BOOL);
			}
			default: {
				Expression newBody = toRewrite.find(toRewrite.getValue());
				if (newBody != null) {
					//substitute args
				}
				return newBody;
			}
		}
	}
}
