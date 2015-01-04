package com.ak.untrue;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
/**
 * Basic infinitely nested list implementation. Used to store parsed source.
 * 
 * @author akoso_000
 * @param <T> Type of tree data, besides list
 */
public class AbstractSyntaxTree<T> implements Iterable<AbstractSyntaxTree<T>> {
	
	private List<AbstractSyntaxTree<T>> nodes_;
	private T val_;
	
	public AbstractSyntaxTree(T value) {	
		nodes_ = new LinkedList<AbstractSyntaxTree<T>>();
		val_ = value;
	}
	
	@SafeVarargs
	public AbstractSyntaxTree(T... values) {
		nodes_ = new LinkedList<AbstractSyntaxTree<T>>();
		for (T value : values) {
			nodes_.add(new AbstractSyntaxTree<T>(value));
		}
	}
	
	public AbstractSyntaxTree(AbstractSyntaxTree<T> node) {
		nodes_ = new LinkedList<AbstractSyntaxTree<T>>();
		nodes_.add(node);
	}
	
	public List<AbstractSyntaxTree<T>> getNodes() {
		return nodes_;
	}
	
	public T getVal() {
		return val_;
	}
	
	public void add(AbstractSyntaxTree<T> node) {
		nodes_.add(node);
	}
	
	public String toString() {
		if (val_ != null) {
			return val_.toString();	
		}
		return "";
	}
	
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder();
		if (val_ == null) {
			sb.append("(");
			for (AbstractSyntaxTree<T> node : nodes_) {
				sb.append(node.prettyPrint());
			}	
			sb.append(")");				
		} else {
			sb.append(" ");
			sb.append(this.toString());
			sb.append(" ");
		}
		return sb.toString();
	}
	
	@Override
	public Iterator<AbstractSyntaxTree<T>> iterator() {	
		return new ASTIterator<T>(this);
	}
	
	private class ASTIterator<E> implements Iterator<AbstractSyntaxTree<T>> {
		private Deque<AbstractSyntaxTree<T>> toVisit;
		public ASTIterator(AbstractSyntaxTree<T> master) {
			toVisit = new LinkedList<>();
			toVisit.addAll(master.getNodes());
		}
		@Override
		public boolean hasNext() {
			return toVisit.size() != 0;
		}

		@Override
		public AbstractSyntaxTree<T> next() {
			AbstractSyntaxTree<T> visited = toVisit.removeFirst();
			Deque<AbstractSyntaxTree<T>> newVisit = new LinkedList<>();
			newVisit.addAll(visited.getNodes());
			newVisit.addAll(toVisit);
			toVisit = newVisit;
			return visited;
		}
	}
}
