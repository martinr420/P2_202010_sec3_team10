package model.logic;

import java.util.Comparator;

public class Edge<K extends Comparable<K>> implements Comparable<Edge>{
	
	private double peso;
	private K from;
	private K to;
	
	
	public Edge(double peso, K from, K to) {
		super();
		this.peso = peso;
		this.from = from;
		this.to = to;
	}
	
	public double getPeso() {
		return peso;
	}
	
	public void setPeso(double peso) {
		this.peso = peso;
	}
	
	public K getFrom() {
		return from;
	}
	
	public void setFrom(K from) {
		this.from = from;
	}
	
	public K getTo() {
		return to;
	}
	
	public void setTo(K to) {
		this.to = to;
	}

	@Override
	public int compareTo(Edge o) {
		
		return from.compareTo((K) o.from);
	}
	
}
