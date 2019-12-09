package compilador;

import java.util.ArrayList;

public class Cuadruplos extends Analizador{
	
	private ArrayList<String> listaExpresiones;
	private ArrayList<ArrayList<RegistroCuadruplo>> listaExpresionesConvertidas;
	
	public Cuadruplos(ArrayList<String> listaExpresiones)
	{
		this.listaExpresiones=listaExpresiones;
		listaExpresionesConvertidas=GeneraCodigoIntermedioConCuadruplos();
	}

	public Cuadruplos()
	{
		this.listaExpresiones=new ArrayList<String>();
		this.listaExpresionesConvertidas=new ArrayList<ArrayList<RegistroCuadruplo>>();
	}
	
	private ArrayList<ArrayList<RegistroCuadruplo>> GeneraCodigoIntermedioConCuadruplos()
	{
		ArrayList<ArrayList<RegistroCuadruplo>> respuesta=new ArrayList<ArrayList<RegistroCuadruplo>>();
		for(String expresion:listaExpresiones)//se recorren todas las expresiones
		{
			respuesta.add(TrabajaExpresion(expresion));//se agrega a la lista de listas de registros una nueva lista de registros
		}
		return respuesta;
	}
	
	private ArrayList<RegistroCuadruplo> TrabajaExpresion(String expresion)
	{
		ArrayList<RegistroCuadruplo> respuesta=new ArrayList<RegistroCuadruplo>();
		String subParte="";
		int jerarquia=0;
		String[] expresionSeparada;
		int contador=0;
		while(CuentaTokens(expresion)>=4)
		{
			expresionSeparada=expresion.split(" ");//se hace un arreglo con los tokens de la expresion
			for(String token:expresionSeparada)//se recorre dicho arreglo
			{
				if(CuentaTokens(expresion)==4)
				{
					respuesta.add(new RegistroCuadruplo("=",expresionSeparada[2],"",expresionSeparada[0]));//operador,operando1,operando2,resultado
					return respuesta;
				}
				if(contador>1)//se salta a la tercera posicion ya que el primer token es el identificador y no interesa y el segundo token tampoco interesa ya que es un =
				{
					if(esOperador(token))//si es operador
					{
						jerarquia=JerarquiaOperador(token);//obtenemos la jerarquia del operador
						if(JerarquiaOperador(expresionSeparada[contador+2])<=jerarquia)//si la jerarquia del siguiente operador es menor o igual que la del operador actual
						{
							if(token.matches("(\\+|\\*)"))//si el token es un + o un * se le agregan los \\ porque si no la expresion regular los toma como los caracteres especiales que son
								subParte=expresionSeparada[contador-1]+" \\"+token+" "+expresionSeparada[contador+1];								
							else
								subParte=expresionSeparada[contador-1]+" "+token+" "+expresionSeparada[contador+1];
							//se agrega un registro de cuadruplo a la lista de registros
							respuesta.add(new RegistroCuadruplo(token,expresionSeparada[contador-1],expresionSeparada[contador+1],"T"+(respuesta.size()+1)));//operador,operando1,operando2,resultado 
							expresion=expresion.replaceFirst(subParte, "T"+respuesta.size());//se reemplaza la parte modificada en la expresion
							contador=0;
							break;
						}
					}
				}
				contador++;
			}
		}
		expresionSeparada=expresion.split(" ");
		respuesta.add(new RegistroCuadruplo(expresionSeparada[1],expresionSeparada[2],"",expresionSeparada[0]));
		return respuesta;
	}
	
	private int JerarquiaOperador(String operador)
	{
		int jerarquia=0;
		if(operador.matches("(\\+|-)"))//si el operador es un + o un - la jerarquia es 1
			jerarquia=1;
		if(operador.matches("(\\*|/)"))//si el operador es un / o un * la jerarquia es 2
			jerarquia=2;
		return jerarquia;
	}
	
	public String TablaCuadruplos()
	{
		String tabla="";
		int contador=0,contadorRen=0;
		for(ArrayList<RegistroCuadruplo> cuadruplosExpresionX: listaExpresionesConvertidas)//se recorre la lista de listas de registros de cuadruplosxD
		{
			tabla+="    Expresion: "+listaExpresiones.get(contador)+"\n    Renglon\t\tOperador\t\tArgumento 1\t\tArgunmento 2\t\tResultado\n";
			for(RegistroCuadruplo cuadruplo: cuadruplosExpresionX)//se recorre la lista de registros de cuadruplos
			{
				tabla+="    "+(contadorRen+1)+"\t\t"+cuadruplo.getOperador()+"\t\t"+cuadruplo.getArgumento1()+"\t\t"+cuadruplo.getArgumento2()+"\t\t"+cuadruplo.getResultado()+"\n";
				contadorRen++;
			}
			contadorRen=0;
			tabla+="\n\n";
			contador++;
		}
		return tabla;
	}
	
	public ArrayList<String> getListaExpresiones() {
		return listaExpresiones;
	}

	public void setListaExpresiones(ArrayList<String> listaExpresiones) {
		this.listaExpresiones = listaExpresiones;
		listaExpresionesConvertidas=GeneraCodigoIntermedioConCuadruplos();
	}
	
	public ArrayList<ArrayList<RegistroCuadruplo>> getListaExpresionesConvertidas() {
		return listaExpresionesConvertidas;
	}

	public void setListaExpresionesConvertidas(ArrayList<ArrayList<RegistroCuadruplo>> listaExpresionesConvertidas) {
		this.listaExpresionesConvertidas = listaExpresionesConvertidas;
	}

}
