package model.logic;

import java.awt.BorderLayout;
import java.io.*;
import java.lang.ProcessBuilder.Redirect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.xml.ws.handler.HandlerResolver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.MapViewOptions;

import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Queue;
import model.data_structures.Grafo;
import model.data_structures.Grafo.Edge;
import model.data_structures.MaxPQ;
import model.data_structures.NoHayVerticeException;
import model.data_structures.noExisteObjetoException;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {

	private final static double LATITUD_MIN = 4.597714;
	private final static double LATITUD_MAX = 4.621360;
	private final static double LONGITUD_MIN = -74.094723; 
	private final static double LONGITUD_MAX = -74.062707;
	private final static int N = 20;
;
	private Haversine haversine;
	private MaxPQ<Estacion> pqEstacion;
	private Queue<Estacion> estaciones;
	private MaxPQ<Multa> pqMultas;
	private Queue<Multa> multas;
	private Grafo<Vertice> grafo;
	private Vertice[] enteroAVertice;

	

	public Modelo()
	{
		grafo = new Grafo<Vertice>(false);
		haversine = new Haversine();
		pqEstacion = new MaxPQ<Estacion>();
		pqMultas = new MaxPQ<Multa>();
		haversine = new Haversine();
		multas = new Queue<>();
		estaciones = new Queue<>();

		enteroAVertice = new Vertice[228050];

	}

	
	
	private void cargarGrafo() throws NoHayVerticeException, FileNotFoundException
	{
		long inicio = System.currentTimeMillis();
		String pathArcos = "./data/Json_Arcos";
		JsonReader lectorArcos;

		String pathVertex = "./data/Json_Vertices"; 
		JsonReader lectorVertices;
		
			lectorVertices = new JsonReader(new FileReader(pathVertex));
			JsonElement elementoV =  JsonParser.parseReader(lectorVertices);
			JsonArray listaVertices = elementoV.getAsJsonArray();
			for(JsonElement e : listaVertices)
			{
				JsonObject o = e.getAsJsonObject();
				int key = o.get("key").getAsInt();
				JsonObject val = o.get("val").getAsJsonObject();
				double lat = val.get("b").getAsDouble();
				double lon = val .get("a").getAsDouble();
				LatLng llVal = new LatLng(lat, lon); 
				Vertice v = new Vertice(key, llVal);
				

				enteroAVertice[key] = v;
				
				grafo.addVertex(v);
				
			}

			System.out.println("se agregaron los vertices");
			lectorArcos = new JsonReader(new FileReader(pathArcos));
			JsonElement elementoE =  JsonParser.parseReader(lectorArcos);
			JsonArray listaEdges = elementoE.getAsJsonArray();
			System.out.println("Cargando edges Va a tradar demasiado");
			
			System.out.println("Cargando arcos esto va a tardar varios minutos");
			for(JsonElement e : listaEdges)
			{
				JsonObject o = e.getAsJsonObject();
				String pesoS = o.get("peso").getAsString();

				double peso = Double.parseDouble(pesoS);
				

				int from = o.get("from").getAsInt();
				Vertice vFrom = enteroAVertice[from]; 
			

				int to = o.get("to").getAsInt();
				Vertice vTo = enteroAVertice[to]; 
				
		
				grafo.addEdge(vFrom, vTo, peso);	
				System.out.println(from);
				
			
			}
			

			System.out.println("Arcos: " + grafo.E());
			System.out.println("Vertices" + grafo.V());
		
		

		

	}

	private void cargarEstaciones() throws FileNotFoundException
	{
		String path = "./data/estacionpolicia.geojson";
		JsonReader lector;
		InputStream inputstream = new FileInputStream(path);

		try 
		{
			lector = new JsonReader(new InputStreamReader(inputstream, "UTF-8"));
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
				String descripcion = objeto.get("EPODESCRIP").getAsString();
				String servicio = objeto.get("EPOSERVICIO").getAsString();
				String horario = objeto.get("EPOHORARIO").getAsString();
				String local = objeto.get("EPOIULOCAL").getAsString();

				Estacion estacion = new Estacion(lat, lon, id, nombre, telefono, dir, descripcion, servicio, horario, local);
				pqEstacion.insert(estacion);
				estaciones.enqueue(estacion);

			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}


	}
	private void cargarMultas() throws ParseException, FileNotFoundException
	{
		String path = "./data/comparendos.geojson";
		JsonReader lector;
		InputStream inputstream = new FileInputStream(path);

		try {

			lector = new JsonReader(new InputStreamReader(inputstream, "UTF-8"));
			JsonElement elem = JsonParser.parseReader(lector);
			JsonObject ja = elem.getAsJsonObject();

			JsonArray features = ja.getAsJsonArray("features");


			for(JsonElement e : features)
			{			
				JsonObject propiedades = (JsonObject) e.getAsJsonObject().get("properties");

				long id = propiedades.get("OBJECTID").getAsLong();

				String cadenaFecha = propiedades.get("FECHA_HORA").getAsString();
				String anio = cadenaFecha.substring(0,4);
				String mes = cadenaFecha.substring(5,7);
				String dia = cadenaFecha.substring(8,10);
				String hora = cadenaFecha.substring(11,13);
				String min = cadenaFecha.substring(14,16);
				String seg = cadenaFecha.substring(17,19);
				String fechaConcatenadita = anio + "-" + mes +"-"+ dia + " " + hora + ":" + min + ":" + seg; 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date fecha = sdf.parse(fechaConcatenadita);
				
				
				String medioDete = propiedades.getAsJsonObject().get("MEDIO_DETECCION").getAsString();
				String claseVehiculo = propiedades.getAsJsonObject().get("CLASE_VEHICULO").getAsString();
				String tipoServicio = propiedades.getAsJsonObject().get("TIPO_SERVICIO").getAsString();
				String infraccion = propiedades.getAsJsonObject().get("INFRACCION").getAsString();
				String descripcion = propiedades.getAsJsonObject().get("DES_INFRACCION").getAsString();
				String localidad = propiedades.getAsJsonObject().get("LOCALIDAD").getAsString();
				String municipio = propiedades.getAsJsonObject().get("MUNICIPIO").getAsString();


				JsonObject geometry = (JsonObject) e.getAsJsonObject().get("geometry");

				String tipo = geometry.get("type").getAsString();

				double[] listaCoords = new double[3];
				JsonArray coordsJson = geometry.getAsJsonArray("coordinates");
				for(int i = 0; i < coordsJson.size(); i ++)
				{
					listaCoords[i] = coordsJson.get(i).getAsDouble();
				}

				Geo geometria = new Geo(tipo, listaCoords);

				Multa multa = new Multa(id, fecha, medioDete, claseVehiculo, tipoServicio, infraccion, descripcion, localidad, municipio, geometria);

				pqMultas.insert(multa);
				multas.enqueue(multa);
				

			} //llave for grande
			

		}//llave try
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	
	} //llave metodo

	
	public void cargarDatos() throws FileNotFoundException, NoHayVerticeException 
	{
		
		try {
			cargarEstaciones();
			System.out.println("Estaciones Cargadas");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println("Problemas con el archivo de estaciones");;
		}
		try {
			cargarMultas();
			System.out.println("Multas cargadas");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("La fecha no sirve");;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("problemas con el archivo");
		}
		cargarGrafo();
		
		
		System.out.println("\n\n\n ======================================================================\n");
		System.out.println("Total comparendos en el archivo: " + pqMultas.size());
		
		Multa multaMayor = pqMultas.delMax();
		System.out.println("La multa con el mayor id es: " + multaMayor.toString());
		
		System.out.println("Total de estaciones de policia del archivo es: " + pqEstacion.size());
		
		Estacion mayorEstacion = pqEstacion.delMax();
		System.out.println(mayorEstacion.toString());
		
		System.out.println("El total de vertices es  " + grafo.V());
		
		Vertice mayorVertice = grafo.getMaxVert();
		System.out.println(mayorVertice.getKey());
		
		System.out.println("El total de arcos es: " + grafo.E());
		
		Vertice mayorEdge = grafo.getMaxEdge();
		System.out.println("EL mayor arco es: " + mayorEdge.getKey());
		System.out.println("\n ============================================================================== \n\n\n");
		
	}
	
	public Vertice darVerticeMasCercano(double lat, double lng)
	{
		Vertice masCercano = null;
		double distanciaMasCercana = Integer.MAX_VALUE;
		for(Vertice actual : grafo.getVertices())
		{
			double latActual = actual.getLat();
			double lngActual = actual.getLng();
			double distanciaActual = haversine.distance(lat, lng, latActual, lngActual);
			if( distanciaActual < distanciaMasCercana)
			{
				distanciaMasCercana = distanciaActual;
				masCercano = actual;
			}
		}
		return masCercano;
	}
	
	public void adicionarInformacion()
	{
		int tamMultas = multas.size();
		for(int i = 0; i < tamMultas; i++)
		{
			Multa actual = multas.dequeue();
			multas.enqueue(actual);
			double latActual = actual.getGeo().getLat();
			double lngActual = actual.getGeo().getLng();
			Vertice v = darVerticeMasCercano(latActual, lngActual);
			v.agregarMulta(actual);
			System.out.println(v.getKey());
		}
	}
	
	public void agregarMultasEdge()
	{
		for(Edge e : grafo.getEdges())
		{
			Vertice from = (Vertice) e.getFromVale();
			Vertice to = (Vertice) e.getToValue();
			
			int totalMultas = from.darComparendos().size() +  to.darComparendos().size();
			
			grafo.setCantidadMultasEdge(e, totalMultas);
		}
	}
	
	public void adicionarEstacionesAlGrafo()
	{
		for(Estacion actual : estaciones)
		{
			
		}
	}
	
	
	

	

//	public void graficar()
//	{
//
//		final Mapa mapa = new Mapa("test");
//
//		LatLng vert1 = new LatLng(LATITUD_MIN, LONGITUD_MIN);
//		LatLng vert2 = new LatLng(LATITUD_MAX, LONGITUD_MIN);
//		LatLng vert3 = new LatLng(LATITUD_MAX, LONGITUD_MAX);
//		LatLng vert4 = new LatLng(LATITUD_MIN, LONGITUD_MAX);
//
//		mapa.GenerateLine(false, vert1, vert2, vert3, vert4);
//		for(Estacion estacion : qEstacion)
//		{
//			double lat = estacion.getLat();
//			double lon = estacion.getLon();
//			mapa.generateMarker(new LatLng(lat, lon));
//		}
//
//		for(Vertice v : qVertice)
//		{
//			double lat = v.getLat();
//			double lon = v.getLong();
//			if(estaDentro(LATITUD_MIN, LONGITUD_MIN, LATITUD_MAX, LONGITUD_MAX, lat, lon))
//			{
//				mapa.generateArea(new LatLng(lat, lon), 5.0);
//			}
//
//		}
//		for (Edge e : qEdge)
//		{
//			int from = (int) e.getFrom();
//			int to = (int ) e.getTo();
//
//			String fromS = grafo.getValueVertex(from);
//			String toS = grafo.getValueVertex(to);
//
//			String[] partesFrom = fromS.split("/");
//			String[] partesTo = toS.split("/");
//
//			double latIni = Double.parseDouble(partesFrom[1]);
//			double lonIni = Double.parseDouble(partesFrom[0]);
//			double latFin = Double.parseDouble(partesTo[1]);
//			double lonFin = Double.parseDouble(partesTo[0]);
//
//			if(estaDentro(LATITUD_MIN, LONGITUD_MIN, LATITUD_MAX, LONGITUD_MAX, latIni, lonIni) && estaDentro(LATITUD_MIN, LONGITUD_MIN, LATITUD_MAX, LONGITUD_MAX, latFin, lonFin) )
//			{
//				LatLng start = new LatLng(latIni, lonIni);
//				LatLng end = new LatLng(latFin, lonFin);
//				mapa.generateSimplePath(start, end, false);
//			}
//		}
//
//
//		System.out.println("Mapa completo");
//	}

//	private boolean estaDentro(double latMin, double lonMin, double latMax, double lonMax, double latActual, double lonActual)
//	{
//		return (latActual <= latMax && latActual >= latMin) && (lonActual <= lonMax && lonActual >= lonMin);
//	}
	
//	public void inicial1(double lat, double lon)
//	{
//		double dist = Double.MAX_VALUE;
//		
//		for(grafo.)
//		
//	}
//	public void reque1A





}
