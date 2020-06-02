 package model.data_structures;

import java.util.ArrayList;
import java.util.Iterator;

import edu.princeton.cs.algs4.BoruvkaMST;
import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.DepthFirstSearch;
import edu.princeton.cs.algs4.DijkstraUndirectedSP;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.GlobalMincut;
import edu.princeton.cs.algs4.NonrecursiveDFS;
import edu.princeton.cs.algs4.PrimMST;
import edu.princeton.cs.algs4.SeparateChainingHashST;

public class GrafoNoDirigido <K>
{
	private EdgeWeightedGraph grafo;
	private EdgeWeightedGraph grafoMultas;
	private SeparateChainingHashST<K, Integer> llaveAEntero;
	private SeparateChainingHashST<Integer, K> enteroALlave;
	private boolean[] marked;
	private int[] id;
	private int count;
	private int V;
	
	public GrafoNoDirigido(int tamano) {
		grafo = new EdgeWeightedGraph(tamano);
		grafoMultas = new EdgeWeightedGraph(tamano);
		llaveAEntero = new SeparateChainingHashST<K, Integer>();
		enteroALlave = new SeparateChainingHashST<Integer, K>();
		id = new int[tamano];
		count = 0;
		V = 0;
	}
	
	public int V()
	{
		return V;
		
	}
	
	public int E()
	{
		return grafo.E();
	}
	
	public void addEdge(K from, K to, double peso) {
		
		if(!llaveAEntero.contains(from)) {
			llaveAEntero.put(from, V);
			enteroALlave.put(V, from);
			V++;
		}
		int fromEntero = llaveAEntero.get(from);
		
		if(!llaveAEntero.contains(to)) {
			llaveAEntero.put(to, V);
			enteroALlave.put(V, to);
			V++;
		}
		int toEntero = llaveAEntero.get(to);

		grafo.addEdge(new Edge(fromEntero, toEntero, peso));
	}
	
	
	
	public void addVertex(K key) 
	{
		if(!llaveAEntero.contains(key)) {
			llaveAEntero.put(key, V);
			enteroALlave.put(V, key);
			V++;
		}
		
	}
	public double getCostArc(K idVertexIni, K idVertexFin) throws Exception
	{
		double costo = 0;
		boolean si = false;
		int llaveIni = llaveAEntero.get(idVertexIni);
		int llaveFin = llaveAEntero.get(idVertexIni);
		for(Edge myEdge: grafo.edges())
		{
			if (myEdge.other(llaveIni) == llaveFin)
			{
				costo = myEdge.weight();
				si = true;
				break;
			}
		}
		
		if (!si)
		{
			costo = -1;
		}
		return costo;
	}
	
	public void setCostArc (K idVertexIni, K idVertexFin, double cost)
	{
		int llaveIni = llaveAEntero.get(idVertexIni);
		int llaveFin = llaveAEntero.get(idVertexIni);
		
		for(Edge myEdge: grafo.edges())
		{
			if (myEdge.other(llaveIni) == llaveFin)
			{
				myEdge.setWeight(cost);
			}
		}
	}
	public void uncheck()
	{
	    marked = new boolean[grafo.V()];
	    for(boolean f : marked)
	    {
	    	f = false;
	    }
	}
	public void dfs(K s)
	{
		
		int llave = llaveAEntero.get(s);
		marked[llave] = true;
		
		for (K w: adj(s))
		{
			int llave2 = llaveAEntero.get(w);
			if (!marked[llave2])
			{
				id[llave2] = count;
				dfs(w);
			}
		}
				
	}
	

	public int cc ()
	{
		
		for (int s  = 0; s < grafo.V(); s++)
		{
			if(!marked[s])
			{
			dfs(enteroALlave.get(s));
			count++;
			}
		}
		return count;
		
	}
	
	public Iterable<K> getCC(K key) {
		CC cc = new CC (grafo);
		ArrayList<K> lista = new ArrayList<K>();
		
		for(int v = 0; v < grafo.V(); v++) {
			if(cc.id(v) == cc.id(llaveAEntero.get(key))) {
				lista.add(enteroALlave.get(v));
				
			}
		}
		
		return lista;
	}
	
	
	public Iterable<K> adj(K key) {
		Iterable<Edge> a = grafo.adj(llaveAEntero.get(key));
		
		ArrayList<K> resultado = new ArrayList<K>();
		for(Edge e: a) {
			int v = e.other(llaveAEntero.get(key));
			resultado.add(enteroALlave.get(v));
		}
		return resultado;
	}
	
	public Iterable<K> caminoMasCorto(K from, K to)
	{
		Queue<K> llaves = new Queue<>();
		int desde = llaveAEntero.get(from);
		int hasta = llaveAEntero.get(to);
		DijkstraUndirectedSP djk = new DijkstraUndirectedSP(grafo, desde, false);
		Iterable<Edge> q = djk.pathTo(hasta);
		Iterator<Edge> iter = q.iterator();
		while(iter.hasNext())
		{		
			Edge e = iter.next();
			String[] datos = e.toString().split(":");
			int iFrom = Integer.parseInt(datos[0]);
			K origen = enteroALlave.get(iFrom);
			
			int iTo = Integer.parseInt(datos[1]);
			K destino = enteroALlave.get(iTo);
			
			llaves.enqueue(origen);
			llaves.enqueue(destino);	
		}
		return llaves;
	}
	
	public double distancia(K from, K to)
	{
		double dist;
		int desde = llaveAEntero.get(from);
		int hasta = llaveAEntero.get(to);
		DijkstraUndirectedSP djk = new DijkstraUndirectedSP(grafo, desde, false);
		dist = djk.distTo(hasta);
		return dist;
	}
	
	public double distanciaMultas(K from, K to)
	{
		double dist;
		int desde = llaveAEntero.get(from);
		int hasta = llaveAEntero.get(to);
		DijkstraUndirectedSP djk = new DijkstraUndirectedSP(grafo, desde, true);
		dist = djk.distTo(hasta);
		return dist;
	}
	
	public Iterable<K> caminoMasCortoMultas(K from, K to)
	{
		Queue<K> llaves = new Queue<>();
		int desde = llaveAEntero.get(from);
		int hasta = llaveAEntero.get(to);
		DijkstraUndirectedSP djk = new DijkstraUndirectedSP(grafo, desde, true);
		Iterable<Edge> q = djk.pathTo(hasta);
		for(Edge e: q )
		{
			String[] datos = e.toString().split(":");
			int iFrom = Integer.parseInt(datos[0]);
			K origen = enteroALlave.get(iFrom);
			
			int iTo = Integer.parseInt(datos[1]);
			K destino = enteroALlave.get(iTo);
			
			llaves.enqueue(origen);
			llaves.enqueue(destino);
			
			
		}
		return llaves;
	}
	
	
	
	public GrafoNoDirigido<K> mst()
	{
	
		
		PrimMST mst = new PrimMST(grafo);
		Iterable<Edge> edges = mst.edges();
		Queue<K> llaves = new Queue<K>();
		GrafoNoDirigido<K> st = new GrafoNoDirigido<K>(V());
		for(Edge edge : edges)
		{
			String[] info = edge.toString().split(":");
			int iFrom = Integer.parseInt(info[0]);
			K from = enteroALlave.get(iFrom);
			
			int iTo = Integer.parseInt(info[1]);
			K to = enteroALlave.get(iTo);
			
			double peso = Double.parseDouble(info[2]);
			
			st.addVertex(from);
			st.addVertex(to);
			st.addEdge(from, to, peso);
		}
		return st;
	
	}
	public double pesoMST()
	{
		PrimMST mst = new PrimMST(grafo);
		return mst.weight();
	}
	public GlobalMincut corte()
	{
		return new GlobalMincut(grafo);
	}
	public int grado(K key)
	{
		int v = llaveAEntero.get(key);
		return grafo.degree(v);
	}
	
	public Iterable<Edge> darEdges()
	{
		return grafo.edges();
	}
	
	public Queue<K> darLlavesEdge(Edge pEdge)
	{
		String[] info = pEdge.toString().split(":");
		int iFrom = Integer.parseInt(info[0]);
		K from = enteroALlave.get(iFrom);
		
		int iTo = Integer.parseInt(info[1]);
		K to = enteroALlave.get(iTo);

		Queue<K> llaves = new Queue<>();
		llaves.enqueue(from);
		llaves.enqueue(to);
		return llaves;
	}

	
	
	

	
	
	
}
