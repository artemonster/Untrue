package com.ak.untrue;

import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Expression implements Iterable<Expression> {
	
	private static Map<Integer, Expression> lookup;
	private static Integer G_ID = 0;
	
	static {
		lookup = new HashMap<>();
	}
	
	private List<Expression> args_;
	private List<Expression> parents_; //exprs, which reference this expr
	
	public enum Type {
		SYMBOL,
		ALU_OP,
		LITERAL_NUM,
		LITERAL_BOOL,
		LITERAL_STR,
		LITERAL_CHR;
	}
	
	public Integer id_;
	
	private Type type_;
	private int arity_;
	
	public String value_;
	private boolean reduced_;

	public Expression(String value, int arity, Type type) {
		id_ = G_ID;
		G_ID++;
		args_ = new LinkedList<>();
		parents_ = new LinkedList<>();
		type_ = type;
		value_ = value;
		arity_ = arity;
		if (arity == 0 && (!type.equals(Type.ALU_OP) || !type.equals(Type.SYMBOL))) {
			reduced_ = true;
		} else {
			reduced_ = false;
		}
		lookup.put(id_, this);
	}
	// ============================ Get&Set shit ============================
	public String getValue() {
		return value_;
	}	
	
	public int getArity() {
		return arity_;
	}
	
	public Type getType() {
		return type_;
	}
	
	public boolean isReduced() {
		return reduced_;
	}
	// ============================ Rewriting utilities ============================
	public boolean isReady() {
		if (reduced_) {
			return true;
		} else {
			boolean checkReady = true;
			for (Expression arg : args_) {
				checkReady &= arg.isReduced();
			}
			return checkReady;
		}
	}
	
	public Expression returnUpdated() {
		return lookup.get(this.id_);
	}
	
	public void rewrite(Expression newExpr) {
		if(isReady()) { //sanity check
			for (Expression parent : parents_) {
				parent.addChildLinkParent(newExpr);
				parent.removeChild(this);
			}
			newExpr.id_ = this.id_;
			lookup.put(newExpr.id_, newExpr);
		}
	}
	// ============================ Graph manipulation ============================
	public List<Expression> getChildren() {
		return args_;
	}
	
	private void removeChild(Expression child) {
		args_.remove(child);
	}

	public void addChildLinkParent(Expression child) {
		args_.add(child);
		child.addParent(this);	
	}

	private void addParent(Expression parent) {
		parents_.add(parent);
	}
	// ============================ Graph iteration and output ============================
	public String prettyPrint() {
		//TODO: implement this properly!
		return value_.toString();
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
			newVisit.addAll(visited.getChildren());
			newVisit.addAll(toVisit);
			toVisit = newVisit;
			return visited;
		}
	}
}
