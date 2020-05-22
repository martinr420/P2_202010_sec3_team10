package model.logic;

import java.io.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import edu.princeton.cs.algs4.Queue;
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
	private LinkedQueue linkedQueue1A;
	private MaxPQ<Integer> maxPQ2A;
	private LinkedQueue linkedQueue2A;
	private MaxPQ<Integer> maxPQ3A;
	private AVLTreeST<Integer, String> arbol1C;
	private AVLTreeST<Integer, String> arbol2C;

	private Queue<Vertice> qVertice;
	private Queue<Edge> qEdge;
	private int n;

	public Modelo()
	{
		grafo = new GrafoNoDirigido<Integer,String>(228046);
		haversine = new Haversine();
	}
	
	
	private void cargarEstaciones()
	{
		String path = "./data/estacionpolicia.geojson";
		JsonReader lector;

		try 
		{
			lector = new JsonReader(new FileReader(path));
			JsonElement element = JsonParser.parseReader(lector);
			JsonObject o = element.getAsJsonObject();
			JsonArray arreglo = o.get("features").getAsJsonArray();

			for(JsonElement e : arreglo)
			{
				JsonObject objeto = (JsonObject) e.getAsJsonObject().get("properties");
				double lat = objeto.get("EPOLATITUD").getAsDouble();
				double lon = objeto.get("EPOLONGITU").getAsDouble();
				int id = objeto.get("OBJECTID").getAsInt();
				String telefono = objeto.get("EPOTELEFON").getAsString();
				String nombre = objeto.get("EPONOMBRE").getAsString();
				String dir = objeto.get("EPODIR_SITIO").getAsString();

				Estacion estacion = new Estacion(lat, lon, id, nombre, telefono, dir);
				qEstacion.enqueue(estacion);

			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}


	}



	public void cargarDatos() throws noExisteObjetoException, IOException
	{
		String pathVertex = "./data/bogota_vertices.txt";
		String pathEdge = "./data/bogota_arcos.txt";



		BufferedReader brVertex = new BufferedReader(new FileReader(pathVertex));
		BufferedReader brEdge = new BufferedReader(new FileReader(pathEdge));
		String cadenaVertex;
		while((cadenaVertex = brVertex.readLine()) != null )
		{

			String[] partesVertice = cadenaVertex.split(",");
			int idVertex = Integer.parseInt(partesVertice[0]);
			String infoVertex = partesVertice[1] +"/" + partesVertice[2];
			grafo.addVertex(idVertex, infoVertex);

			Vertice v = new Vertice<Integer, String>(idVertex, infoVertex);
			qVertice.enqueue(v);

		} 
		brVertex.close();

		String cadenaEdge;
		while((cadenaEdge = brEdge.readLine()) != null)
		{
			String[] partesArco = cadenaEdge.split(" ");
			if (!partesArco[0].contains("#") && partesArco.length >= 2)
			{
				if(grafo.getInfoVertex(Integer.parseInt(partesArco[0])) != null)
				{
					for (int i = 1; i < partesArco.length ; i++)
					{

						if( grafo.getInfoVertex(Integer.parseInt(partesArco[i])) != null)
						{
							String inicio = (String) grafo.getInfoVertex(Integer.parseInt(partesArco[0]));
							String fin = (String) grafo.getInfoVertex(Integer.parseInt(partesArco[i]));

							String[] partesInicio = inicio.split("/");
							String[] partesFinal = fin.split("/");
							double startLong = Double.parseDouble(partesInicio[0]);
							double startLat = Double.parseDouble(partesInicio[1]);
							double endLong = Double.parseDouble(partesFinal[0]);
							double endLat = Double.parseDouble(partesFinal[1]);
							double peso = Haversine.distance(startLat, startLong, endLat, endLong);
							grafo.addEdge(Integer.parseInt(partesArco[0]), Integer.parseInt(partesArco[i]), peso);

							Edge e = new Edge<Integer>(peso, Integer.parseInt(partesArco[0]), Integer.parseInt(partesArco[i]));
							qEdge.enqueue(e);
						}
					}
				}
			}
		}

		brEdge.close();





		System.out.println("La cantidad de vertices es: " + grafo.cantidadVertices());
		System.out.println("La cantidad arcos es: " + grafo.cantidadArcos());
		System.out.println("================================================================\n\n\n\n ");
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
