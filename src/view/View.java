package view;

import model.logic.Modelo;

public class View 
{
	    /**
	     * Metodo constructor
	     */
	    public View()
	    {
	    	
	    }
	    
		public void printMenu()
		{
			System.out.println("1. Cargar Datos");
			System.out.println("2. Id del vértice de la malla vial más cercano por distancia haversiana");
			System.out.println("3. adicionar Informacion");
			System.out.println("4. adicionar multas al grafo");
			System.out.println("5. adicionar estaciones al Grafo");
			System.out.println("6. generar camino mas corto");
			System.out.println("7. generar red");
			System.out.println("6. Exit");
			System.out.println("Dar el numero de opcion a resolver, luego oprimir tecla Return: (e.g., 1):");
		}

		public void printMessage(String mensaje) {

			System.out.println(mensaje);
		}		
		
	
}
