package model.logic;

import java.util.Comparator;

public class ComparadorCantidadMultas implements Comparator<Vertice>
{


	@Override
	public int compare(Vertice arg0, Vertice arg1) 
	{
		return arg0.darComparendos().size() - arg1.darComparendos().size();
	}

}
