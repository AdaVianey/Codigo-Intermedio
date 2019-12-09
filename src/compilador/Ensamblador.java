package compilador;

import java.util.ArrayList;

public class Ensamblador extends Analizador {

	private ArrayList<ArrayList<RegistroCuadruplo>> listaExpresionesConvertidas;
	private String[] operaciones= {"ADD","SUB","MUL","DIV","MOV"};
	private ArrayList<RegistroEnsamblador> listaRegistrosEnsamblador;

	public Ensamblador()
	{
		this.listaExpresionesConvertidas=new ArrayList<ArrayList<RegistroCuadruplo>>();
		listaRegistrosEnsamblador=new ArrayList<RegistroEnsamblador>();
	}

	public Ensamblador(ArrayList<ArrayList<RegistroCuadruplo>> listaExpresionesConvertidas)
	{
		this.listaExpresionesConvertidas=listaExpresionesConvertidas;
		listaRegistrosEnsamblador=Convierte_A_Ensamblador();
	}

	private ArrayList<RegistroEnsamblador> Convierte_A_Ensamblador()
	{
		for(ArrayList<RegistroCuadruplo> cuadruplosExpresionX: listaExpresionesConvertidas)//se recorre la lista de listas de registros de cuadruplosxD
		{
			for(RegistroCuadruplo cuadruplo: cuadruplosExpresionX)//se recorre la lista de registros de cuadruplos
			{
				AgregaRegistro(4,"AX",cuadruplo.getArgumento1());
				switch(cuadruplo.getOperador())
				{
					case "+":
						AgregaRegistro(0,"AX",cuadruplo.getArgumento2());
					break;
					case "-":
						AgregaRegistro(1,"AX",cuadruplo.getArgumento2());
					break;
					case "*":
						AgregaRegistro(2,cuadruplo.getArgumento2(),"");
					break;
					case "/":
						AgregaRegistro(3,cuadruplo.getArgumento2(),"");
					break;				
				}
				AgregaRegistro(4,cuadruplo.getResultado(),"AX");
			}
		}
		return listaRegistrosEnsamblador;
	}
	
	private void AgregaRegistro(int operacion,String argumento1,String argumento2)
	{
		RegistroEnsamblador registroEnsamblador=new RegistroEnsamblador("","","");
		registroEnsamblador.setOperacion(operaciones[operacion]);
		registroEnsamblador.setOperando1(argumento1);
		registroEnsamblador.setOperando2(argumento2);
		listaRegistrosEnsamblador.add(registroEnsamblador);
	}
	
	public String MuestraCodigoEnsamblador()
	{
		String tablaEnsamblador="\t\t.MODEL\tsmall\n\t\t.DATA\n";
		ArrayList<String> variables_A_Crear=BuscaVariables();
		for(String variable:variables_A_Crear)
		{
			tablaEnsamblador+=variable+"\t\tDW\t0\n";
		}
		tablaEnsamblador+="\t\t.CODE\nComienza:\n";
		for(RegistroEnsamblador registro:listaRegistrosEnsamblador)
		{
			if(registro.getOperando2().isBlank())
				tablaEnsamblador+="\t\t"+registro.getOperacion()+"\t"+registro.getOperando1()+"\n";
			else
				tablaEnsamblador+="\t\t"+registro.getOperacion()+"\t"+registro.getOperando1()+", "+registro.getOperando2()+"\n";
		}	
		tablaEnsamblador+="\t\t.EXIT\n\t\tend\tComienza\n";
		return tablaEnsamblador;
	}

	private ArrayList<String> BuscaVariables()
	{
		ArrayList<String> respuesta=new ArrayList<String>();
		for(ArrayList<RegistroCuadruplo> cuadruplosExpresionX: listaExpresionesConvertidas)//se recorre la lista de listas de registros de cuadruplosxD
		{
			for(RegistroCuadruplo cuadruplo: cuadruplosExpresionX)//se recorre la lista de registros de cuadruplos
			{
				if(esIdentificador(cuadruplo.getArgumento1()))
					if(!ExisteVariable(respuesta,cuadruplo.getArgumento1()))
						respuesta.add(cuadruplo.getArgumento1());
				if(esIdentificador(cuadruplo.getArgumento2()))
					if(!ExisteVariable(respuesta,cuadruplo.getArgumento2()))
						respuesta.add(cuadruplo.getArgumento2());
				if(esIdentificador(cuadruplo.getResultado()))
					if(!ExisteVariable(respuesta,cuadruplo.getResultado()))
						respuesta.add(cuadruplo.getResultado());
			}
		}	
		return respuesta;
	}
	
	private boolean ExisteVariable(ArrayList<String> lista,String nombre)
	{
		boolean z=false;
		for(String variable:lista)
		{
			if(nombre.equals(variable))
				z=true;
		}
		return z;
	}
	
	public ArrayList<ArrayList<RegistroCuadruplo>> getListaExpresionesConvertidas() {
		return listaExpresionesConvertidas;
	}

	public void setListaExpresionesConvertidas(ArrayList<ArrayList<RegistroCuadruplo>> listaExpresionesConvertidas) {
		this.listaExpresionesConvertidas = listaExpresionesConvertidas;
		listaRegistrosEnsamblador=Convierte_A_Ensamblador();
	}
}
