package controller;

import java.io.IOException;
import java.util.Scanner;

import model.data_structures.NoHayVerticeException;
import model.data_structures.noExisteObjetoException;
import model.logic.Modelo;
import view.View;

public class Controller {

	/* Instancia del Modelo*/
	private Modelo modelo;

	/* Instancia de la Vista*/
	private View view;

	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 */
	public Controller ()
	{
		view = new View();
		modelo = new Modelo();
	}

	public void run() throws noExisteObjetoException, IOException 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;
		String dato = "";
		String respuesta = "";

		while( !fin ){
			view.printMenu();

			int option = lector.nextInt();
			switch(option){
			case 1:
				try {
					modelo.cargarDatos();
				} catch (NoHayVerticeException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}					
				break;
			case 2: 
				System.out.println("ingrese la latitud inicial");
				double lat1 = Double.parseDouble(lector.next());
				System.out.println("Ingerese la lng inicial");
				double lng1 = Double.parseDouble(lector.next());
				System.out.println("ingrese la latitud final");
				double lat2 = Double.parseDouble(lector.next());
				System.out.println("Ingerese la lng final");
				double lng2 = Double.parseDouble(lector.next());
				modelo.ObtenerCostoMinimo(lat1, lng1, lat2, lng2);
				break;
			case 3:
				System.out.println("ingrese la cantidad de camaras");
				int numero = lector.nextInt();
				modelo.A2(numero);
				break;




			default: 
				view.printMessage("--------- \n Opcion Invalida !! \n---------");
				break;
			}
		}

	}	
}
