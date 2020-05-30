package model.logic;

import com.teamdev.jxmaps.LatLng;

import edu.princeton.cs.algs4.Queue;

public class Vertice implements Comparable<Vertice>
{

	private int key;
	private LatLng val;
	private Queue<Multa> comparendos;
	private Queue<Estacion> estaciones;
	
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getVal() {
		return val.toString();
	}
	public void setVal(LatLng val) {
		this.val = val;
	}
	public Vertice(int key, LatLng val) {
		super();
		this.key = key;
		this.val = val;
		comparendos = new Queue<>();
		estaciones = new Queue<>();
	}
	public double getLat()
	{
		return val.getLat();
	}
	
	public double getLng()
	{
		return val.getLng();
	}
	public void agregarMulta(Multa pMulta)
	{
		comparendos.enqueue(pMulta);
	}
	public Queue<Multa> darComparendos()
	{
		return comparendos;
	}
	
	public void agregarEstacion(Estacion item)
	{
		estaciones.enqueue(item);
	}
	public Queue<Estacion> darEstaciones()
	{
		return estaciones;
	}

	
	@Override
	public int compareTo(Vertice arg0) {
		// TODO Auto-generated method stub
		return key - arg0.getKey();
	}
	
	public String toString()
	{
		return key + ":" + getLat() + ":" + getLng() ;
	}

}
