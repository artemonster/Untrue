package com.ak.untrue;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class Expression implements Iterable<Expression> {
	public enum Type {
		SYMBOL,
		ALU_OP,
		NIL,
		LITERAL_NUM,
		LITERAL_BOOL,
		LITERAL_STR,
		LITERAL_CHR;
	}
	
	//https://www.cs.cmu.edu/~fp/papers/pldi88.pdf
	private static Map<Integer, Expression> lookup;
	private static Integer G_ID = 0;
	
	static {
		lookup = new HashMap<>();
	}
	private Integer id_;
	
	
	private Map<String, Expression> environment;
	private Deque<Expression> args;
	private Deque<String> bindings;
	private Expression body;
	private Set<Expression> parents;
	
	private String value;
	private Type type;
	private int arity;
	private boolean reduced_;

	public Expression(String value, int arity, Type type) {
		id_ = G_ID;
		G_ID++;
		args = new LinkedList<>();
		environment = new HashMap<>();
		parents = new HashSet<>();
		this.type = type;
		this.value = value;
		this.arity = arity;
		if (!type.equals(Type.ALU_OP) && !type.equals(Type.SYMBOL)) {
			reduced_ = true;
		} else {
			reduced_ = false;
		}
		lookup.put(id_, this);
	}
	// ============================ Get&Set shit ============================
	public String getValue() {
		return value;
	}	
	
	public int getArity() {
		return arity;
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean isReduced() {
		return reduced_;
	}
	
	public void addToEnv(String label, Expression exp) {
		environment.put(label,exp);
	}
	// ============================ Rewriting utilities ============================
	public Expression findLocal(String label) {
		if(environment.containsKey(label)) {
			return environment.get(label);
		} else {
			return null;
		}
	}	
	
	public Expression find(String label) {
		if(environment.containsKey(label)) {
			return environment.get(label);
		} else {
			Expression toRet = null;
			for(Expression parent : parents) {
				toRet = parent.find(label);
			}
			if(toRet == null) {
				return null;
			} else {
				return toRet;
			}
		}
	}

	public boolean isReady() {
		if (reduced_) {
			return true;
		} else {
			boolean checkReady = true;
			for (Expression arg : args) {
				checkReady &= arg.isReduced();
			}
			return checkReady;
		}
	}
	
	public Expression returnUpdated() {
		if (lookup.containsKey(this.id_)) {
			return lookup.get(this.id_);
		} else {
			return null; //TODO: return root!
		}
	}
	/**
	 * When called on some expression, it uses newExpr to substitute existing one.
	 * If current expression is used as argument for other expressions, then the exact spot
	 * should be found and substitution must occur.
	 * @param newExpr
	 */
	public void rewrite(Expression newExpr) {
		assert this.isReady() : "how could this be reduced?";//sanity check
		for (Expression parent : parents) {
			LinkedList<Expression> parentChildren = (LinkedList<Expression>) parent.getArgs();
			int index = parentChildren.indexOf(this);
			if (newExpr.getType() != Type.NIL) {
				parentChildren.set(index, newExpr);
				newExpr.addParent(parent);	
			}
			parent.removeChild(this);
		}
		newExpr.id_ = this.id_;
		lookup.put(newExpr.id_, newExpr);				
	}
	// ============================ Graph manipulation ============================
	public Deque<Expression> getArgs() {
		return args;
	}
	
	private void removeChild(Expression child) {
		args.remove(child);
	}
	
	public void addChildLinkParent(Expression child) {
		args.add(child);
		child.addParent(this);	
	}

	private void addParent(Expression parent) {
		parents.add(parent);
	}
	// ============================ Graph iteration and output ============================
	public String prettyPrint() {
		//TODO: implement this properly!
		return value.toString();
	}
	
	public String toString() {
		if (value != null) {
			StringBuilder sb = new StringBuilder().append(value.toString());	
			if (args.size() != 0) {
				sb.append(":[");
				for (Expression e: args) {
					sb.append(e.toString());
					sb.append(",");
				}
				sb.deleteCharAt(sb.length()-1);
				sb.append("]");
			}
			return sb.toString();
		}
		return "()";
	}
	@Override
	public Iterator<Expression> iterator() {
		return new ExprIterator(this);
	}
	
	private class ExprIterator implements Iterator<Expression> {
		private Deque<Expression> toVisit;
		public ExprIterator(Expression master) {
			toVisit = new LinkedList<>();
			toVisit.add(master);
		}
		@Override
		public boolean hasNext() {
			return toVisit.size() != 0;
		}

		@Override
		public Expression next() {
			Expression visited = toVisit.removeFirst();
			Deque<Expression> newVisit = new LinkedList<>();
			newVisit.addAll(visited.getArgs());
			newVisit.addAll(toVisit);
			toVisit = newVisit;
			return visited;
		}
	}
}
