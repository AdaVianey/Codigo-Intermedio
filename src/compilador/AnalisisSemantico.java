package compilador;

import java.util.ArrayList;

public class AnalisisSemantico extends Analizador {

	private TablaDeSimbolos tablaSimbolos;
	private String MensajeSemantico;
	private ArrayList<String> listaExpresiones;
	private ArrayList<ArrayList<RegistroCuadruplo>> listaExpresionesConvertidas;
	
	public AnalisisSemantico()
	{
		MensajeSemantico="Error(es) Semanticos: \n";
		tablaSimbolos=new TablaDeSimbolos(new ArrayList<Identificador>());
		listaExpresiones=new ArrayList<String>();
		listaExpresionesConvertidas=new ArrayList<ArrayList<RegistroCuadruplo>>();
	}

	public boolean Semantico(String texto)
	{
		boolean z=false;
		LlenarTablaSimbolos(texto);
		if(AsignacionesValidas() &&
			VariablesDeclaradas() &&
				ValidaVariablesRepetidas() &&
					ValidaOperandosValidos(texto))
				z=true;
		return z;
	}
	
	private boolean AsignacionesValidas()
	{
		boolean z=true;
		String tipoAsignado="";
		if(!tablaSimbolos.getLista().isEmpty())
		{
			for(Identificador identificador:tablaSimbolos.getLista())
			{
				if(!identificador.getValor().isBlank() && !identificador.getTipo().isBlank())
				{
					tipoAsignado="";
					if(esBooleano(identificador.getValor()))
						tipoAsignado="boolean";
					if(esNumero(identificador.getValor()))
						tipoAsignado="int";
					if(esDecimal(identificador.getValor()))
						tipoAsignado="float";
					switch(identificador.getTipo())
					{
					case "int":
						if(!esNumero(identificador.getValor()))
						{
							z=false;
							MensajeSemantico+="La variable '"+identificador.getNombre()+"' es de tipo "+identificador.getTipo()+" y se le "
							+ "esta queriendo asignar un tipo "+tipoAsignado+" en la linea "+identificador.getRenglon()+"\n";
						}
					break;
					case "boolean":
						if(!esBooleano(identificador.getValor()))
						{
							z=false;					
							MensajeSemantico+="La variable '"+identificador.getNombre()+"' es de tipo "+identificador.getTipo()+" y se le "
									+ "esta queriendo asignar un tipo "+tipoAsignado+" en la linea "+identificador.getRenglon()+"\n";		
						}
					break;
					case "double":
						if(!identificador.getValor().matches("[0-9]*\\.[0-9]*"))
						{
							z=false;										
							MensajeSemantico+="La variable '"+identificador.getNombre()+"' es de tipo "+identificador.getTipo()+" y se le "
									+ "esta queriendo asignar un tipo "+tipoAsignado+" en la linea "+identificador.getRenglon()+"\n";						
						}
					break;
					case "float":
						if(!identificador.getValor().matches("[0-9]*\\.[0-9]*"))
						{
							z=false;															
							MensajeSemantico+="La variable '"+identificador.getNombre()+"' es de tipo "+identificador.getTipo()+" y se le "
									+ "esta queriendo asignar un tipo "+tipoAsignado+" en la linea "+identificador.getRenglon()+"\n";
						}
					break;
					}
				}
			}
		}
		return z;
	}
	
	private boolean VariablesDeclaradas()
	{
		boolean z=true;	
		for(Identificador identificador:tablaSimbolos.getLista())
		{
			if(identificador.getTipo().equals("No_declarado")) 
			{
				z=false;
				MensajeSemantico+="La variable '"+identificador.getNombre()+"' de la linea "+identificador.getRenglon()+" no esta declarada"+"\n";
			}
		}
		return z;
	}
	
	private boolean ValidaVariablesRepetidas()
	{
		boolean z=true;
		Identificador IdentificadorDeclarado;
		if(!tablaSimbolos.getLista().isEmpty())
		{
			for(Identificador identificador:tablaSimbolos.getLista())
			{
				if(identificador.getTipo().isBlank())
				{
					IdentificadorDeclarado=tablaSimbolos.BuscaPrimerIdentificadorDeclarado(identificador.getNombre());
					z=false;
					MensajeSemantico+="La variable '"+identificador.getNombre()+"' del renglon "+identificador.getRenglon()+
							" ya se encuentra declarada en el renglon "+IdentificadorDeclarado.getRenglon()+"\n";
				}
			}
		}
		return z;
	}
	
	//Aqui se utiliza registrocuadruplo
	private boolean ValidaOperandosValidos(String texto)
	{
		boolean z=true,expresionAgregada=false;
		if(!texto.isBlank())
		{
			int ren=1,contador=0,i=0;
			String siguienteToken="",operando="",operador="",tokenAnterior="",tipoOperando="",expresion="",tokenAux="";
			Identificador identificador,identificadorAux;
			String[] tokensRen=texto.split("\n");
			for(String renglon:tokensRen)
			{
				String[] tokensCol=renglon.split("( |\t)");
				for(String token:tokensCol)
				{
					if(!token.isBlank())
					{
						contador++;
						tokenAnterior=ConsultaTokensxPosicion(texto, contador-1);
						//Para encontrar las variables excepto el identificador de la clase
						if(esIdentificador(token) && !esPalabraReservada(tokenAnterior))
						{
							identificador=tablaSimbolos.BuscaPrimerIdentificadorDeclarado(token);//variable a la que se le se está asignando algun valor							
							siguienteToken=ConsultaTokensxPosicion(texto, contador+1);
							tokenAux="";
							expresion="";
							operador="";
							if(siguienteToken.equals("="))
							{
								i=contador;
								while(!tokenAux.equals(";"))
								{
									tokenAux=ConsultaTokensxPosicion(texto, i);
									expresion+=tokenAux+" ";
									i++;	
								}
								if(identificador.getTipo().equals("boolean"))
								{
									//esta es la palabra asignada al identificador
									operando=ConsultaTokensxPosicion(texto, contador+2);
									//Este es el ; esperado
									operador=ConsultaTokensxPosicion(texto, contador+3);
									if(esBooleano(operando))
										tipoOperando="boolean";
									if(esDecimal(operando))
										tipoOperando="float";
									if(esNumero(operando))
										tipoOperando="int";
									
									if(!operador.equals(";"))
									{
										MensajeSemantico+="Error en la expresion '"+expresion+"' en la linea "+ren+". A las variables booleanas solo se les puede asignar un 'true' o un 'false'.\n";
										z=false;
									}
									else
									{
										if(esIdentificador(operando))
										{
											identificadorAux=tablaSimbolos.BuscaPrimerIdentificadorDeclarado(operando);//variable que se está asignando
											if(!identificadorAux.getTipo().equals(identificador.getTipo()))//si el tipo de la variable que se está asignando no es igual al tipo de la variable a la que se le está asignando dicha variable
											{
												z=false;
												MensajeSemantico+="Error en la expresion '"+expresion+"' en la linea "+ren+". Se le esta queriendo asignar una variable tipo '"+identificadorAux.getTipo()+"' a la variable '"+identificador.getNombre()+"' de tipo '"+identificador.getTipo()+"'\n";
											}
										}
										else
										{
											if(!identificador.getTipo().equals(tipoOperando))
											{
												z=false;
												MensajeSemantico+="Error en la expresion '"+expresion+"' en la linea "+ren+". Se le esta queriendo asignar un '"+tipoOperando+"' a la variable '"+identificador.getNombre()+"' de tipo '"+identificador.getTipo()+"'\n";
											}
										}
									}
								}
								else
								{
									i=contador;
									while(!operador.equals(";"))
									{
										operando=ConsultaTokensxPosicion(texto, i+2);									
										operador=ConsultaTokensxPosicion(texto, i+3);
										if(esBooleano(operando))
											tipoOperando="boolean";
										if(esDecimal(operando))
											tipoOperando="float";
										if(esNumero(operando))
											tipoOperando="int";
										if(esIdentificador(operando))
										{
											identificadorAux=tablaSimbolos.BuscaPrimerIdentificadorDeclarado(operando);//variable que se está asignando
											if(!identificadorAux.getTipo().equals(identificador.getTipo()))//si el tipo de la variable que se está asignando no es igual al tipo de la variable a la que se le está asignando dicha variable
											{
												z=false;
												MensajeSemantico+="Error en la expresion '"+expresion+"' en la linea "+ren+". Se le esta queriendo asignar una variable tipo '"+identificadorAux.getTipo()+"' a la variable '"+identificador.getNombre()+"' de tipo '"+identificador.getTipo()+"'\n";
											}
											else if(!expresionAgregada)
											{
												//Es para identificar que las expresiones no sean unarias o simples
												if(CuentaTokens(expresion)>4)
												{
													//Lista de expresiones a convertir a cuadruplos
													listaExpresiones.add(expresion);
													expresionAgregada=true;
												}
											}
										}
										else
										{
											if(!identificador.getTipo().equals(tipoOperando))
											{
												z=false;
												MensajeSemantico+="Error en la expresion '"+expresion+"' en la linea "+ren+". Se le esta queriendo asignar un '"+tipoOperando+"' a la variable '"+identificador.getNombre()+"' de tipo '"+identificador.getTipo()+"'\n";
											}
											else if(!expresionAgregada)
											{
												if(CuentaTokens(expresion)>4)
												{
													listaExpresiones.add(expresion);
													expresionAgregada=true;
												}
											}
												
										}		
										i+=2;
									}
								}
								expresionAgregada=false;
							}
						}
					}
				}
				ren++;
			}
		}
		listaExpresionesConvertidas=GeneraCodigoIntermedioConCuadruplos();
		return z;
	}
	
	private void LlenarTablaSimbolos(String texto)
	{
		int ren=1,columna=1,contador=1;
		String tipoDeDatoToken="",valorAsignado="";
		if(!texto.isBlank())
		{
			String[] tokensRen=texto.split("\n");
			for(String renglon:tokensRen)
			{
				String[] tokensCol=renglon.split("( |\t)");
				for(String token:tokensCol)
				{
					if(!token.isBlank())
					{
						tipoDeDatoToken=ConsultaTokensxPosicion(texto, contador-1);//el tipo de dato del token sera el que este en la posicion anterior a el
						valorAsignado=ConsultaTokensxPosicion(texto, contador+2);//el valor asignado al token sera el que este dos posiciones a despues de el
						if( !esBooleano(valorAsignado) && !esDecimal(valorAsignado) && !esNumero(valorAsignado) ) 
							valorAsignado="";
						if(esIdentificador(token) && !esPalabraReservada(tipoDeDatoToken))
						{
							if(esTipoDeDato(tipoDeDatoToken))
							{
								if(!tablaSimbolos.ExisteIdentificador(token))
									tablaSimbolos.AgregaIdentificador(new Identificador(token,tipoDeDatoToken,ren,columna,"global",valorAsignado));
								else
									tablaSimbolos.AgregaIdentificador(new Identificador(token,"",ren,columna,"global",valorAsignado));																				
							}
							else
								if(!tablaSimbolos.ExisteIdentificador(token))
									tablaSimbolos.AgregaIdentificador(new Identificador(token,"No_declarado",ren,columna,"",""));
						}
						contador++;
						columna++;
					}
				}
				columna=1;
				ren++;
			}			
		}
	}
	
	public String getMensajeSemantico() 
	{
		return MensajeSemantico;
	}

	public void setMensajeSemantico(String mensajeSemantico) 
	{
		MensajeSemantico = mensajeSemantico;
	}

	public String MuestraTablaSimbolos()
	{
		return tablaSimbolos.MuestraTablaSimbolos(tablaSimbolos.getLista());
	}
	
	public ArrayList<ArrayList<RegistroCuadruplo>> GeneraCodigoIntermedioConCuadruplos()
	{
		//Lista que contiene la lista de los cuadruplos de cada expresion
		ArrayList<ArrayList<RegistroCuadruplo>> respuesta=new ArrayList<ArrayList<RegistroCuadruplo>>();
		for(String expresion:listaExpresiones)//se recorren todas las expresiones
		{
			respuesta.add(TrabajaExpresion(expresion));//se agrega a la lista de listas de registros una nueva lista de registros
		}
		return respuesta;
	}
	
	public ArrayList<RegistroCuadruplo> TrabajaExpresion(String expresion)
	{
		ArrayList<RegistroCuadruplo> respuesta=new ArrayList<RegistroCuadruplo>();
		String subParte="";
		int jerarquia=0;
		String[] expresionSeparada;
		int contador=0;
		
		//Este while sirve para que se detenga cuando quede una asignacion simples
		//Si es mayor de 6 tokens es una asignacion binaria o mas 
		while(CuentaTokens(expresion)>=6)
		{
			expresionSeparada=expresion.split(" ");//se hace un arreglo con los tokens de la expresion
			for(String token:expresionSeparada)//se recorre dicho arreglo
			{
				if(contador>1)//se salta a la tercera posicion ya que el primer token es el identificador y no interesa y el segundo token tampoco interesa ya que es un =
				{
					//Hace algo solo cuando es operador para obtener su jerarquia
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
		//Cuando solo quede la asignacion simple 
		respuesta.add(new RegistroCuadruplo(expresionSeparada[1],expresionSeparada[2],"",expresionSeparada[0]));
		return respuesta;
	}
	
	public int JerarquiaOperador(String operador)
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
}
