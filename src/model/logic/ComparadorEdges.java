package model.logic;

import java.util.Comparator;

import edu.princeton.cs.algs4.Edge;

public class ComparadorEdges implements Comparator<Edge> {

	@Override
	public int compare(Edge arg0, Edge arg1) {
		// TODO Auto-generated method stub
		return (int) (arg0.getMultas() - arg1.getMultas());
	}

}
