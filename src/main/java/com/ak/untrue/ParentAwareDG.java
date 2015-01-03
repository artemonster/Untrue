package com.ak.untrue;

import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 * This implementation is a parent aware directed graph, which is used to store expressions
 * and rewrite upon them.
 * 
 * @author akoso_000
 * @param <T> Type of tree data, besides list
 */
public class ParentAwareDG<T> implements Iterable<ParentAwareDG<T>> {
	private static Map<Integer, ParentAwareDG<?>> lookup;
	private static Integer G_ID = 0;
	
	static {
		lookup = new HashMap<>();
	}

	private Integer id_;
	private List<ParentAwareDG<T>> children_;
	private List<ParentAwareDG<T>> parents_;
	public T val_;
	
	public ParentAwareDG(T value) {	
		id_ = G_ID;
		G_ID++;
		children_ = new LinkedList<ParentAwareDG<T>>();
		val_ = value;
		lookup.put(id_, this);
	}
	
	public List<ParentAwareDG<T>> getChildren() {
		return children_;
	}
	
	public void addChild(ParentAwareDG<T> child) {
		children_.add(child);
		child.addParent(this);
	}
	
	public void addParent(ParentAwareDG<T> parent) {
		parents_.add(parent);
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
			for (ParentAwareDG<T> node : children_) {
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
	public Iterator<ParentAwareDG<T>> iterator() {	
		return new ASTIterator<T>(this);
	}
	
	private class ASTIterator<E> implements Iterator<ParentAwareDG<T>> {
		private Deque<ParentAwareDG<T>> toVisit;
		public ASTIterator(ParentAwareDG<T> master) {
			toVisit = new LinkedList<>();
			toVisit.addAll(master.getChildren());
		}
		@Override
		public boolean hasNext() {
			return toVisit.size() != 0;
		}

		@Override
		public ParentAwareDG<T> next() {
			ParentAwareDG<T> visited = toVisit.removeFirst();
			Deque<ParentAwareDG<T>> newVisit = new LinkedList<>();
			newVisit.addAll(visited.getChildren());
			newVisit.addAll(toVisit);
			toVisit = newVisit;
			return visited;
		}
	}
}
