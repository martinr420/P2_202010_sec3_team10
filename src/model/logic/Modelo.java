package model.logic;

import java.io.*;

import model.data_structures.*;
import model.data_structures.LinkedQueue;
import model.data_structures.noExisteObjetoException;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
	
	private GrafoNoDirigido<Integer, String> grafo;
	private Haversine haversine;

	private int n;
	
	public Modelo()
	{
		grafo = new GrafoNoDirigido<Integer,String>(228046);
		haversine = new Haversine();
	}
	
	public void cargarDatos() throws noExisteObjetoException, IOException
	{
		String pathVertex = "./data/bogota_vertices.txt";
	    String pathEdge = "./data/bogota_arcos.txt";
	    
	    
	    
	    	BufferedReader brVertex = new BufferedReader(new FileReader(pathVertex));
	    	BufferedReader brEdge = new BufferedReader(new FileReader(pathEdge));
	    String cadenaVertex;
	    	while(( cadenaVertex = brVertex.readLine()) != null)
	    	{
	    		String[] partes = cadenaVertex.split(",");
	    		
	    			int idVertex = Integer.parseInt(partes[0]);
	    			String infoVertex = partes[1] +"/" + partes[2];
	    			grafo.addVertex(idVertex, infoVertex);
	    		
	    	}
	    	brVertex.close();
	    	
	    	String cadenaEdge;
	    	while((cadenaEdge = brEdge.readLine()) != null)
	    	{
	    		String[] partes = cadenaEdge.split(" ");
	    		if (!partes[0].contains("#"))
	    		{
	    			for (int i = 1; i < partes.length ; i++)
	    			{
	    				
	    				String inicio = (String) grafo.getInfoVertex(Integer.parseInt(partes[0]));
	    				String fin = (String) grafo.getInfoVertex(Integer.parseInt(partes[i]));
	    				
	    				String[] partesInicio = inicio.split("/");
	    				String[] partesFinal = fin.split("/");
                        double startLat = Double.parseDouble(partesInicio[1]);
                        double startLong = Double.parseDouble(partesInicio[0]);
                        double endLat = Double.parseDouble(partesFinal[1]);
                        double endLong = Double.parseDouble(partesFinal[0]);
						double peso = Haversine.distance(startLat, startLong, endLat, endLong);
	    				grafo.addEdge(Integer.parseInt(partes[0]), Integer.parseInt(partes[i]), peso);
	    			}
	    		}
	    	}
	    	
	    	brEdge.close();
	    	
	    
	    
	   
	    
	    System.out.println("La cantidad de vertices es: " + grafo.cantidadVertices());
		System.out.println("La cantidad arcos es: " + grafo.cantidadArcos());
	}
	
	private void R1A (int latitudIni, int longitudIni, int latitudFin, int longitudFin)
	{
		
	}
	
	private void R2A(int m)
	{
		
	}
	
	private void R1B(int latitudIni, int longitudIni, int latitudFin, int longitudFin)
	{
		
	}
	
	private void R2B (int m)
	{
		
	}
	
	private void R1C (int m)
	{
		
	}
	
	private void R2C()
	{
		
	}
}
