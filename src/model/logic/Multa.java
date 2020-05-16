package model.logic;

import java.util.Date;

public class Multa implements Comparable<Multa>{

	public long id;

	public Date fecha;

	public String medioDete;

	public String vehiculo;

	public String servicio;

	public String infraccion;

	public String descripcion;

	public String localidad;

	public Geo geo;



	public Multa(long id, Date fecha, String medioDete, String vehiculo, String servicio, String infraccion,
			String descripcion, String localidad, Geo geo) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.medioDete = medioDete;
		this.vehiculo = vehiculo;
		this.servicio = servicio;
		this.infraccion = infraccion;
		this.descripcion = descripcion;
		this.localidad = localidad;
		this.geo = geo;
	}

	public String toString()
	{

		String msj ="El id es "+ id +" la fecha del comparendo "+ fecha 
				+" el medio de detencion es "+ medioDete +
				" el vehiculo es "+ vehiculo +" el tipo de servicio es " + servicio + 
				" la infraccion es " + infraccion + 
				" la descripcion de lo sucedido es " + descripcion + " la localidad es "+
				localidad +" la geografira es "+ geo.toString();


		return msj;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getMedioDete() {
		return medioDete;
	}

	public void setMedioDete(String medioDete) {
		this.medioDete = medioDete;
	}

	public String getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(String vehiculo) {
		this.vehiculo = vehiculo;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getInfraccion() {
		return infraccion;
	}

	public void setInfraccion(String infraccion) {
		this.infraccion = infraccion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public Geo getGeo() {
		return geo;
	}

	public void setGeo(Geo geo) {
		this.geo = geo;
	}

	public int compararComperndos(Multa pMulta)
	{
		return 0;
	}

	public boolean estaDentroDeFecha(String f1, String f2)
	{
		return false;
	}

	public int compareTo(Multa pMulta) {

		int num;

		if(fecha.compareTo(pMulta.getFecha()) > 0)
		{
			num = 1;
		}
		else if(fecha.compareTo(pMulta.fecha) < 0)
		{
			num = -1;
		}
		else
		{
			if (id - pMulta.id < 0)
			{
				num = -1;
			}
			else 
			{
				num = 1;
			}
		}
		// TODO Auto-generated method stub
		return num;
	}

	public int compareToInfraccion(Multa pMulta)
	{
		int num;
		if(infraccion.compareTo(pMulta.getInfraccion()) < 0)
		{
			num = -1;
		}
		else if(infraccion.compareTo(pMulta.getInfraccion()) > 0 )
		{
			num = 1;
		}
		else
		{
			num = 0;
		}
		return num;
	}
	
	public int compareToLocalidad(Multa pMulta)
	{
		int num;
		
		if(localidad.compareTo(pMulta.getLocalidad()) < 0)
		{
			num = -1;
		}
		else if(localidad.compareTo(pMulta.getLocalidad()) > 0)
		{
			num = 1;
		}
		else
		{
			num = 0;
		}
		return num;
	}

}
