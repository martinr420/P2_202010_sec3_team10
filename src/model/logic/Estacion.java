package model.logic;

public class Estacion implements Comparable<Estacion>
{

	private double lat;
	private double lon;
	private int id;
	private String nombre;
	private String telefono;
	private String dir;
	private String descripcion;
	private String servicio;
	private String horario;
	private String local;
	public Estacion(double lat, double lon, int id, String nombre, String telefono, String dir, String descripcion, String servicio, String horario, String local) {
		super();
		this.lat = lat;
		this.lon = lon;
		this.id = id;
		this.nombre = nombre;
		this.telefono = telefono;
		this.dir = dir;
		this.setDescripcion(descripcion);
		this.setServicio(servicio);
		this.setHorario(horario);
		this.setLocal(local);
	}
	
	public double getLat() {
		return lat;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public double getLon() {
		return lon;
	}
	
	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getTelefono() {
		return telefono;
	}
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getDir()
	{
		return dir;
	}
	public void setDir(String pDir)
	{
		dir = pDir;
	}

	@Override
	public int compareTo(Estacion o) 
	{
		
		// TODO Auto-generated method stub
		return id - o.id;
	}
	
	public String toString()
	{
		return "El id es "+id +" La latitud es " + lat + " la longitud es " + lon + " el nombre es " + nombre + " el telefono es " + telefono
				+ " la direccion es " + dir + " la descripcion es " + descripcion + " el servicio es " + servicio + " el horario es "+ horario + "el local es " + local ;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}
}
