package model.logic;

public class Vertice<K extends Comparable<K>, V> implements Comparable<Vertice<K,V>>
{
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((val == null) ? 0 : val.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertice other = (Vertice) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (val == null) {
			if (other.val != null)
				return false;
		} else if (!val.equals(other.val))
			return false;
		return true;
	}
	private K key;
	private V val;
	private double lat;
	private double longi;
	public K getKey() {
		return key;
	}
	public void setKey(K key) {
		this.key = key;
	}
	public V getVal() {
		return val;
	}
	public void setVal(V val) {
		this.val = val;
	}
	public Vertice(K key, V val) {
		super();
		this.key = key;
		this.val = val;
		
		String[] partes = val.toString().split("/");
		longi = Double.parseDouble(partes[0]);
		lat = Double.parseDouble(partes[1]);
	}
	public double getLat()
	{
		return lat;
	}
	public void setLat(double pLat)
	{
		lat = pLat;
	}
	public double getLong()
	{
		return longi;
	}
	public void setLong(double pLong)
	{
		longi = pLong;
	}
	public int compareTo(Vertice<K,V> o) 
	{
		// TODO Auto-generated method stub
		return key.compareTo((K) o.getKey());
	}

}
