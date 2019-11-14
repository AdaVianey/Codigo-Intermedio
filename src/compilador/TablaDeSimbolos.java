package compilador;

import java.util.ArrayList;

public class TablaDeSimbolos 
{
	ArrayList<Identificador> Lista=new ArrayList<Identificador>();

	public TablaDeSimbolos(ArrayList<Identificador> lista) 
	{
		Lista=lista;
	}
	
	public ArrayList<Identificador> getLista() 
	{
		return Lista;
	}

	public void setLista(ArrayList<Identificador> lista) 
	{
		Lista = lista;
	}
	
	public void AgregaIdentificador(Identificador identificador)
	{
		Lista.add(identificador);		
	}
	
	public boolean ExisteIdentificador(String nombreIdentificador)
	{
		for(Identificador identificador:Lista)
		{
			if(nombreIdentificador.equals(identificador.getNombre()))
				return true;
		}		
		return false;
	}
	
	public Identificador BuscaPrimerIdentificadorDeclarado(String nombreIdentificador)
	{
		for(Identificador identificador:Lista)
		{
			if(nombreIdentificador.equals(identificador.getNombre()))
				return identificador;
		}		
		return null;
	}
	
	public String MuestraTablaSimbolos(ArrayList<Identificador> listaDeIdentificadores)
	{
		String tabla="    Nombre\t\tTipo de dato\t\tValor\t\tAlcance\t\tRenglon\t\tColumna\n", nombre="", tipo="",alcance="",valor="";
		int renglon=0, columna=0;
		for(Identificador identificador:listaDeIdentificadores)
		{
			nombre=identificador.getNombre();
			tipo=identificador.getTipo();
			renglon=identificador.getRenglon();
			columna=identificador.getColumna();
			alcance=identificador.getAlcance();
			valor=identificador.getValor();
			tabla=tabla+"    "+nombre+"\t\t"+tipo+"\t\t"+valor+"\t\t"+alcance+"\t\t"+renglon+"\t\t"+columna+"\n";
		}
		return tabla;
	}
}
