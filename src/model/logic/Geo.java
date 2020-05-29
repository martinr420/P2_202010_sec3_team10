package model.logic;

public class Geo {

	
	


	public String tipo;
	
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public double[] getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(double[] coordenadas) {
		this.coordenadas = coordenadas;
	}

	public double[] coordenadas;
	
	
	public Geo(String tipo, double[] coordenadas) {
		super();
		this.tipo = tipo;
		this.coordenadas = coordenadas;
	}
	public double getLat()
	{
		return coordenadas[0];
	}
	public double getLng()
	{
		return coordenadas[1];
	}
	
	public String toString()
	{
		String msj = " el tipo de geo es " + tipo + " las coordenas son " + coordenadas[0] 
				+ ", " + coordenadas[1] + ", " + coordenadas[2]+ ".";
		
		return msj;
	}
}
