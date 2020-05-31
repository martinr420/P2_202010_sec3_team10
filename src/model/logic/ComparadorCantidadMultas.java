package model.logic;

import java.util.Comparator;

public class ComparadorCantidadMultas implements Comparator<Interseccion>
{


	@Override
	public int compare(Interseccion arg0, Interseccion arg1) 
	{
		return arg0.darComparendos().size() - arg1.darComparendos().size();
	}

}
