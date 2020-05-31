package model.logic;

import java.awt.BorderLayout;
import java.io.*;
import java.lang.ProcessBuilder.Redirect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
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

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.Heap;
import edu.princeton.cs.algs4.LinearProbingHashST;

import model.data_structures.GrafoNoDirigido;
import model.data_structures.MaxPQ;
import model.data_structures.NoHayVerticeException;
import model.data_structures.Queue;
import model.data_structures.noExisteObjetoException;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {

	// minimo 4.767362,-73.986181
	// maximo  4.599696,-74.294133


	private final static double LATITUD_MIN = 4.597714;
	private final static double LATITUD_MAX = 4.767362;
	private final static double LONGITUD_MIN = -74.294133; 
	private final static double LONGITUD_MAX = -73.986181;
	private final static int N = 20;
	;
	private Haversine haversine;
	private Queue<Estacion> estaciones;
	private Queue<Multa> multas;
	private GrafoNoDirigido<Interseccion> grafo;
	private Interseccion[] enteroAInterseccion;
	private Multa mayorMulta;
	private Estacion mayorEstacion;
	private Interseccion mayorInterseccion;
	private Edge mayorEdge;
	private MaxPQ<Interseccion> maxPQIntersecciones;
	private MaxPQ<Edge> maxPQEdge;



	public Modelo()
	{
		grafo = new GrafoNoDirigido<Interseccion>(228050);
		haversine = new Haversine();



		multas = new Queue<>();
		estaciones = new Queue<>();





		maxPQEdge = new MaxPQ<>(grafo.E(), new ComparadorEdges());

		enteroAInterseccion = new Interseccion[228050];

	}



	private void cargarGrafo() throws NoHayVerticeException, FileNotFoundException
	{
		String pathArcos = "./data/Json_Arcos";
		JsonReader lectorArcos;

		String pathVertex = "./data/Json_Vertices"; 
		JsonReader lectorVertices;


		lectorVertices = new JsonReader(new FileReader(pathVertex));
		JsonElement elementoV =  JsonParser.parseReader(lectorVertices);
		JsonArray listaVertices = elementoV.getAsJsonArray();

		mayorInterseccion = new Interseccion(-1, new LatLng(0.0, 0.0));
		for(JsonElement e : listaVertices)
		{
			JsonObject o = e.getAsJsonObject();
			int key = o.get("key").getAsInt();
			JsonObject val = o.get("val").getAsJsonObject();
			double lat = val.get("b").getAsDouble();
			double lon = val .get("a").getAsDouble();
			LatLng llVal = new LatLng(lat, lon); 
			Interseccion v = new Interseccion(key, llVal);


			enteroAInterseccion[key] = v;
			grafo.addVertex(v);
			if(v.getKey() > mayorInterseccion.getKey()) mayorInterseccion = v;

		}

		System.out.println("se agregaron los vertices");
		lectorArcos = new JsonReader(new FileReader(pathArcos));
		JsonElement elementoE =  JsonParser.parseReader(lectorArcos);
		JsonArray listaEdges = elementoE.getAsJsonArray();
		int mayorOrigen = -1;
		int mayorDestino = -1;
		double mayorPeso = -1;
		System.out.println("Agregando Arcos");
		for(JsonElement e : listaEdges)
		{
			JsonObject o = e.getAsJsonObject();
			String pesoS = o.get("peso").getAsString();

			double peso = Double.parseDouble(pesoS);


			int from = o.get("from").getAsInt();
			Interseccion vFrom = enteroAInterseccion[from]; 


			int to = o.get("to").getAsInt();
			Interseccion vTo = enteroAInterseccion[to]; 

			grafo.addEdge(vFrom, vTo, peso);	

			if(from > mayorOrigen || to > mayorDestino)
			{
				mayorOrigen = from;
				mayorDestino = to;
				mayorPeso = peso;
			}


		}
		mayorEdge = new Edge(mayorOrigen, mayorDestino, mayorPeso);
		System.out.println("Arcos agregados");
	}

	private void cargarEstaciones() throws FileNotFoundException
	{
		String path = "./data/estacionpolicia.geojson";
		JsonReader lector;
		InputStream inputstream = new FileInputStream(path);
		mayorEstacion = null;
		int mayorId = -1;

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

				estaciones.enqueue(estacion);
				if(estacion.getId() > mayorId)
				{
					mayorId = estacion.getId();
					mayorEstacion = estacion;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}


	}
	private void cargarMultas() throws ParseException, FileNotFoundException
	{
		String path = "./data/small_comparendos.txt";
		JsonReader lector;
		InputStream inputstream = new FileInputStream(path);
		System.out.println("Cargando multas");
		

		try {

			lector = new JsonReader(new InputStreamReader(inputstream, "UTF-8"));
			JsonElement elem = JsonParser.parseReader(lector);
			JsonObject ja = elem.getAsJsonObject();

			JsonArray features = ja.getAsJsonArray("features");
			mayorMulta = null;
			long mayorid = -1;
			int j = 0;


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

				multas.enqueue(multa);
				if(multa.getId() > mayorid)
				{
					mayorid = multa.getId();
					mayorMulta = multa;
				}
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
		System.out.println("Total comparendos en el archivo: " + multas.size());


		System.out.println("La multa con el mayor id es: " + mayorMulta.toString());

		System.out.println("Total de estaciones de policia del archivo es: " + estaciones.size());

	
		System.out.println("La mayo estacion es: " + mayorEstacion.toString());

		System.out.println("El total de vertices es  " + grafo.V());


		System.out.println("La mayor Interseccion es: " + mayorInterseccion.toString());

		System.out.println("El total de arcos es: " + grafo.E());


		System.out.println("EL mayor arco es: " + mayorEdge.toString());
		System.out.println("\n ============================================================================== \n\n\n");
		adicionarInformacion();
		agregarMultasEdge();
		adicionarEstacionesAlGrafo();
		System.out.println("================================================================================================\n\n\n");


	}

	private Interseccion darInterseccionMasCercana(double lat, double lng)
	{
		Interseccion masCercano = null;
		double distanciaMasCercana = Integer.MAX_VALUE;
		for(Interseccion actual : enteroAInterseccion)
		{
			if(actual != null)
			{
				double latActual = actual.getLat();
				double lngActual = actual.getLng();
				double distanciaActual = Haversine.distance(lat, lng, latActual, lngActual);
				if( distanciaActual < distanciaMasCercana)
				{
					distanciaMasCercana = distanciaActual;
					masCercano = actual;
				}	
			}

		}
		return masCercano;
	}

	private void adicionarInformacion()
	{
		maxPQIntersecciones = new MaxPQ<>(300, new ComparadorCantidadMultas());
		int tamMultas = multas.size();
		System.out.println("Aicionando las multas al vertice mas cercano");
		for(int i = 0; i < tamMultas; i++)
		{
			Multa actual = multas.dequeue();
			multas.enqueue(actual);
			double latActual = actual.getGeo().getLat();
			double lngActual = actual.getGeo().getLng();
			Interseccion v = darInterseccionMasCercana(latActual, lngActual);
			v.agregarMulta(actual);
			actual.setVertice(v);
			maxPQIntersecciones.insert(v);
		}
		System.out.println("Multas adicionadas");
	}

	private void agregarMultasEdge()
	{
		System.out.println("Agregando las multas al edge");
		for(Edge e : grafo.darEdges())
		{
			int iFrom = e.either();
			Interseccion from = enteroAInterseccion[iFrom];

			int iTo = e.other(iFrom);
			Interseccion to = enteroAInterseccion[iTo];


			int totalMultas = from.darComparendos().size() +  to.darComparendos().size();


			e.setMultas(totalMultas);
			maxPQEdge.insert(e);
		}
		System.out.println("Multas agregadas al edge");
	}

	private void adicionarEstacionesAlGrafo()
	{
		System.out.println("adicionando estaciones al grafo");
		for(Estacion actual : estaciones)
		{
			double latActual = actual.getLat();
			double lngActual = actual.getLon();
			Interseccion v = darInterseccionMasCercana(latActual, lngActual);
			v.agregarEstacion(actual);
		}
		System.out.println("Estaciones adicionadas al grafo");
	}

	public void ObtenerCostoMinimo(double latIni, double lngIni, double latFin, double lngFin)
	{
		if(estaDentroBogota(latIni, lngIni) && estaDentroBogota(latFin, lngFin))
		{
			Interseccion origen = darInterseccionMasCercana(latIni, lngIni);

			Interseccion destino = darInterseccionMasCercana(latFin, lngFin);

			Iterable<Interseccion> cosa = grafo.caminoMasCorto(origen, destino);
			Iterator<Interseccion> iter = cosa.iterator();
			Queue<Interseccion> lista = new Queue<Interseccion>();
			while(iter.hasNext())
			{
				lista.enqueue(iter.next());
			}

			LatLng[] camino = new LatLng[lista.size()/2];

			System.out.println("La cantidad de intersecciones es" + lista.size());
			int i = 0;
			while(lista.size() >= 2)
			{

				Interseccion from = lista.dequeue();
				int fromId = from.getKey();
				double latfrom = from.getLat();
				double lngfrom = from.getLng();
				camino[i] = new LatLng(latfrom, lngfrom);
				i++;

				Interseccion to = lista.dequeue();
				int idTo = to.getKey();
				double latTo = to.getLat();
				double lngTo = to.getLng();


				System.out.println(" el id orige es: " + fromId + " la latitud es: " + latfrom + 
						" la longitud es: " + lngfrom );
				System.out.println("el id de destino es: " + idTo +" La latitud es: " + latTo + 
						" la longitud es " + lngTo);


			}
			System.out.println("la cantidad de arcos es: " + lista.size()/2);
			System.out.println("La distancia total es: " + grafo.distancia(origen, destino));

			Mapa mapa = new Mapa("Camino mas corto");


			mapa.GenerateLine(false, camino);

		}
		else
		{
			System.out.println("coordenadas fuera de alcanze");
			Mapa mapa = new Mapa("Fuera de alcanze");
			LatLng vert1 = new LatLng(LATITUD_MIN, LONGITUD_MIN);
			LatLng vert2 = new LatLng(LATITUD_MAX, LONGITUD_MIN);
			LatLng vert3 = new LatLng(LATITUD_MAX, LONGITUD_MAX);
			LatLng vert4 = new LatLng(LATITUD_MIN, LONGITUD_MAX);

			mapa.GenerateLine(false, vert1, vert2, vert3, vert4);

			LatLng punto1 = new LatLng(latIni,lngIni);
			mapa.generateMarker(punto1);

			LatLng punto2 = new LatLng(latFin, lngFin);
			mapa.generateMarker(punto2);

		}	
	}

	public void generarRed(int m)
	{
		System.out.println("generando red");
		long ini = System.currentTimeMillis();
		double costoMonetario = 0;
		Edge[] edges = new Edge[m];
	
		Queue<Interseccion> intersecciones = new Queue<Interseccion>();
		Mapa map = new Mapa("Red");
		for(int i = 0; i < m; i++)
		{
			Edge actual = maxPQEdge.delMax();
			edges[i] = actual;
			Queue<Interseccion> q = grafo.darLlavesEdge(actual);
			Interseccion from = q.dequeue();
			Interseccion to = q.dequeue();
			System.out.println("de " + from.getKey() + " a " + to.getKey() + "Con un total de ultas de: " + actual.getMultas());
			double latini = from.getLat();
			double lngIni = from.getLng();
			LatLng origen = new LatLng(latini,lngIni);
			
			double latFin = to.getLat();
			double lngFin = to.getLng();
			LatLng destino = new LatLng(latFin, lngFin);
			
			map.generateSimplePath(origen, destino, true);
			
			
		}
		for(Edge i : edges)
		{
			maxPQEdge.insert(i);
		}
	
		long fin = System.currentTimeMillis();
		System.out.println("Tiempo: " + (fin-ini));
		
		
		
		

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

	private boolean estaDentro(double latMin, double lonMin, double latMax, double lonMax, double latActual, double lonActual)
	{
		return (latActual <= latMax && latActual >= latMin) && (lonActual <= lonMax && lonActual >= lonMin);
	}

	private boolean estaDentroBogota(double lat, double lng)
	{
		return (lat < LATITUD_MAX && lat > LATITUD_MIN && lng < LONGITUD_MAX && lng > LONGITUD_MIN);
	}

	//	public void inicial1(double lat, double lon)
	//	{
	//		double dist = Double.MAX_VALUE;
	//		
	//		for(grafo.)
	//		
	//	}
	//	public void reque1A





}
