package compilador;

import java.util.ArrayList;

public class AnalisisSemantico extends Analizador {

	private TablaDeSimbolos tablaSimbolos;
	private String MensajeSemantico;
	private ArrayList<String> listaExpresiones;
	
	public AnalisisSemantico()
	{
		MensajeSemantico="Error(es) Semanticos: \n";
		tablaSimbolos=new TablaDeSimbolos(new ArrayList<Identificador>());
		listaExpresiones=new ArrayList<String>();
	}

	public boolean Semantico(String texto)
	{
		boolean z=false;
		LlenarTablaSimbolos(texto);
		if(VariablesDeclaradas() &&
				VariablesInicializadas() &&
				ValidaOperandosValidos(texto) &&
				AsignacionesValidas() &&
				ValidaVariablesRepetidas()
					)
				z=true;
		return z;
	}
	
	private boolean VariablesInicializadas()
	{
		boolean z=true;
		for(Identificador identificador:tablaSimbolos.getLista())
		{
			if(identificador.getTipo().equals("no_se_inicializo"))
			{
				z=false;
				MensajeSemantico+="La variable '"+identificador.getNombre()+"' no esta inicializada en la linea "+identificador.getRenglon()+"\n";
			}
		}
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
					if(esIdentificador(identificador.getValor()))
						tipoAsignado=tablaSimbolos.BuscaPrimerIdentificadorDeclarado(identificador.getValor()).getTipo();
					switch(identificador.getTipo())
					{
					case "int":
						if(!tipoAsignado.equals("int"))
						{
							z=false;
							MensajeSemantico+="La variable '"+identificador.getNombre()+"' es de tipo "+identificador.getTipo()+" y se le "
							+ "esta queriendo asignar un tipo "+tipoAsignado+" en la linea "+identificador.getRenglon()+"\n";
						}
					break;
					case "boolean":
						if(!tipoAsignado.equals("boolean"))
						{
							z=false;					
							MensajeSemantico+="La variable '"+identificador.getNombre()+"' es de tipo "+identificador.getTipo()+" y se le "
									+ "esta queriendo asignar un tipo "+tipoAsignado+" en la linea "+identificador.getRenglon()+"\n";		
						}
					break;
					case "float":
						if(!tipoAsignado.equals("float"))
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
									operando=ConsultaTokensxPosicion(texto, contador+2);									
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
												listaExpresiones.add(expresion);
												expresionAgregada=true;
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
												listaExpresiones.add(expresion);
												expresionAgregada=true;
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
		return z;
	}
	
	private void LlenarTablaSimbolos(String texto)
	{
		int ren=1,columna=1,contador=1,aux=0;
		String tipoDeDatoToken="",valorAsignado="",verificaIgual="";
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
						if(esIdentificador(token) && !esPalabraReservada(tipoDeDatoToken))
						{
							valorAsignado=ConsultaTokensxPosicion(texto, contador+2);//el valor asignado al token sera el que este dos posiciones a despues de el
							verificaIgual=ConsultaTokensxPosicion(texto, contador+1);//se hace esto para ver si el siguiente token es un ; y asi hacer el valor asignado nulo
							if( !esBooleano(valorAsignado) && !esDecimal(valorAsignado) && !esNumero(valorAsignado) && !esIdentificador(valorAsignado) ) 
								valorAsignado="";
							if(verificaIgual.equals(";"))
								valorAsignado="";
							if(tablaSimbolos.ExisteIdentificador(token) && tablaSimbolos.BuscaPrimerIdentificadorDeclarado(token).getValor().equals("") && !verificaIgual.equals("="))
								tablaSimbolos.AgregaIdentificador(new Identificador(token,"no_se_inicializo",ren,0,"",""));
							else
							{
								if(esTipoDeDato(tipoDeDatoToken))
								{
									if(!tablaSimbolos.ExisteIdentificador(token))
									{
										if(verificaIgual.equals("="))
										{
											if(esIdentificador(valorAsignado) && tablaSimbolos.ExisteIdentificador(valorAsignado))
												valorAsignado=tablaSimbolos.BuscaPrimerIdentificadorDeclarado(valorAsignado).getValor();
											tablaSimbolos.AgregaIdentificador(new Identificador(token,tipoDeDatoToken,ren,columna,"global",valorAsignado));
										}
										else
											tablaSimbolos.AgregaIdentificador(new Identificador(token,tipoDeDatoToken,ren,columna,"global",valorAsignado));									
									}
									else
										tablaSimbolos.AgregaIdentificador(new Identificador(token,"",ren,columna,"global",valorAsignado));
								}
								else 
									if(!tablaSimbolos.ExisteIdentificador(token))
										tablaSimbolos.AgregaIdentificador(new Identificador(token,"No_declarado",ren,columna,"",""));
									else
										if(verificaIgual.equals("="))
										{
											aux=contador+2;
											while(!ConsultaTokensxPosicion(texto, aux+1).equals(";"))
											{
												valorAsignado+=" "+ConsultaTokensxPosicion(texto, aux+1);
												aux++;
											}
											switch (tablaSimbolos.BuscaPrimerIdentificadorDeclarado(token).getTipo()) {
											case "int":
												try 
												{
													valorAsignado=Integer.toString(TrabajaExpresionInt(valorAsignado));
												}catch(NumberFormatException e) {}
												catch(NullPointerException e) {} 
											break;
											case "float":
												try 
												{
													valorAsignado=Float.toString(TrabajaExpresionFloat(valorAsignado));											
												}catch(NumberFormatException e) {} 
												catch (NullPointerException e) {}
											break;
											}
											tablaSimbolos.BuscaPrimerIdentificadorDeclarado(token).setValor(valorAsignado);										
										}
							}
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

	private int TrabajaExpresionInt(String expresion) throws NullPointerException, NumberFormatException
	{
		int jerarquia=0,resultado=0;
		String[] expresionSeparada;
		String subParte="";
		int contador=0;
		boolean siguienteOperadorTieneMasJerarquia=false;
		while(CuentaTokens(expresion)>=2)
		{
			expresionSeparada=expresion.split(" ");//se hace un arreglo con los tokens de la expresion
			for(String token:expresionSeparada)//se recorre dicho arreglo
			{
				if(esOperador(token))//si es operador
				{
					jerarquia=JerarquiaOperador(token);//obtenemos la jerarquia del operador
					if((contador+2)<expresionSeparada.length)
						if(JerarquiaOperador(expresionSeparada[contador+2])>jerarquia)//si la jerarquia del siguiente operador es menor o igual que la del operador actual
							siguienteOperadorTieneMasJerarquia=true;
					if(!siguienteOperadorTieneMasJerarquia)
					{
						if(token.matches("(\\+|\\*)"))//si el token es un + o un * se le agregan los \\ porque si no la expresion regular los toma como los caracteres especiales que son
							subParte=expresionSeparada[contador-1]+" \\"+token+" "+expresionSeparada[contador+1];								
						else
							subParte=expresionSeparada[contador-1]+" "+token+" "+expresionSeparada[contador+1];
						//se agrega un registro de cuadruplo a la lista de registros
						switch (token) {
						case "+":
							if(esIdentificador(expresionSeparada[contador-1]) && !esIdentificador(expresionSeparada[contador+1]))
								resultado=Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())+Integer.parseInt(expresionSeparada[contador+1]);
							else if(!esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Integer.parseInt(expresionSeparada[contador-1])+Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else if(esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())+Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else
								resultado=Integer.parseInt(expresionSeparada[contador-1])+Integer.parseInt(expresionSeparada[contador+1]);
						break;
						case "-":
							if(esIdentificador(expresionSeparada[contador-1]) && !esIdentificador(expresionSeparada[contador+1]))
								resultado=Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())-Integer.parseInt(expresionSeparada[contador+1]);
							else if(!esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Integer.parseInt(expresionSeparada[contador-1])-Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else if(esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())-Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else
								resultado=Integer.parseInt(expresionSeparada[contador-1])-Integer.parseInt(expresionSeparada[contador+1]);
						break;
						case "/":
							if(esIdentificador(expresionSeparada[contador-1]) && !esIdentificador(expresionSeparada[contador+1]))
								resultado=Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())/Integer.parseInt(expresionSeparada[contador+1]);
							else if(!esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Integer.parseInt(expresionSeparada[contador-1])/Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else if(esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())/Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else
								resultado=Integer.parseInt(expresionSeparada[contador-1])/Integer.parseInt(expresionSeparada[contador+1]);
						break;
						case "*":
							if(esIdentificador(expresionSeparada[contador-1]) && !esIdentificador(expresionSeparada[contador+1]))
								resultado=Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())*Integer.parseInt(expresionSeparada[contador+1]);
							else if(!esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Integer.parseInt(expresionSeparada[contador-1])*Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else if(esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())*Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else
								resultado=Integer.parseInt(expresionSeparada[contador-1])*Integer.parseInt(expresionSeparada[contador+1]);
						break;
						}
						expresion=expresion.replaceFirst(subParte,""+resultado);//se reemplaza la parte modificada en la expresion
						contador=0;
						siguienteOperadorTieneMasJerarquia=false;
						break;
					}
					siguienteOperadorTieneMasJerarquia=false;
				}
				contador++;
			}
		}
		if(esIdentificador(expresion) && tablaSimbolos.ExisteIdentificador(expresion))
			resultado=Integer.parseInt(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresion).getValor());
		else
			resultado=Integer.parseInt(expresion);
		return resultado;
	}
	
	private float TrabajaExpresionFloat(String expresion) throws NullPointerException, NumberFormatException
	{
		int jerarquia=0;
		float resultado=0;
		String[] expresionSeparada;
		String subParte="";
		int contador=0;
		boolean siguienteOperadorTieneMasJerarquia=false;
		while(CuentaTokens(expresion)>=2)
		{
			expresionSeparada=expresion.split(" ");//se hace un arreglo con los tokens de la expresion
			for(String token:expresionSeparada)//se recorre dicho arreglo
			{
				if(esOperador(token))//si es operador
				{
					jerarquia=JerarquiaOperador(token);//obtenemos la jerarquia del operador
					if((contador+2)<expresionSeparada.length)
						if(JerarquiaOperador(expresionSeparada[contador+2])>jerarquia)//si la jerarquia del siguiente operador es menor o igual que la del operador actual
							siguienteOperadorTieneMasJerarquia=true;
					if(!siguienteOperadorTieneMasJerarquia)
					{
						if(token.matches("(\\+|\\*)"))//si el token es un + o un * se le agregan los \\ porque si no la expresion regular los toma como los caracteres especiales que son
							subParte=expresionSeparada[contador-1]+" \\"+token+" "+expresionSeparada[contador+1];								
						else
							subParte=expresionSeparada[contador-1]+" "+token+" "+expresionSeparada[contador+1];
						//se agrega un registro de cuadruplo a la lista de registros
						switch (token) {
						case "+":
							if(esIdentificador(expresionSeparada[contador-1]) && !esIdentificador(expresionSeparada[contador+1]))
								resultado=Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())+Float.parseFloat(expresionSeparada[contador+1]);
							else if(!esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Float.parseFloat(expresionSeparada[contador-1])+Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else if(esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())+Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else
								resultado=Float.parseFloat(expresionSeparada[contador-1])+Float.parseFloat(expresionSeparada[contador+1]);
						break;
						case "-":
							if(esIdentificador(expresionSeparada[contador-1]) && !esIdentificador(expresionSeparada[contador+1]))
								resultado=Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())-Float.parseFloat(expresionSeparada[contador+1]);
							else if(!esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Float.parseFloat(expresionSeparada[contador-1])-Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else if(esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())-Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else
								resultado=Float.parseFloat(expresionSeparada[contador-1])-Float.parseFloat(expresionSeparada[contador+1]);
						break;
						case "/":
							if(esIdentificador(expresionSeparada[contador-1]) && !esIdentificador(expresionSeparada[contador+1]))
								resultado=Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())/Float.parseFloat(expresionSeparada[contador+1]);
							else if(!esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Float.parseFloat(expresionSeparada[contador-1])/Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else if(esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())/Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else
								resultado=Float.parseFloat(expresionSeparada[contador-1])/Float.parseFloat(expresionSeparada[contador+1]);
						break;
						case "*":
							if(esIdentificador(expresionSeparada[contador-1]) && !esIdentificador(expresionSeparada[contador+1]))
								resultado=Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())*Float.parseFloat(expresionSeparada[contador+1]);
							else if(!esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Float.parseFloat(expresionSeparada[contador-1])*Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else if(esIdentificador(expresionSeparada[contador-1]) && esIdentificador(expresionSeparada[contador+1]))
								resultado=Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador-1]).getValor())*Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresionSeparada[contador+1]).getValor());
							else
								resultado=Float.parseFloat(expresionSeparada[contador-1])*Float.parseFloat(expresionSeparada[contador+1]);
						break;
						}
						expresion=expresion.replaceFirst(subParte,""+resultado);//se reemplaza la parte modificada en la expresion
						contador=0;
						siguienteOperadorTieneMasJerarquia=false;
						break;
					}
					siguienteOperadorTieneMasJerarquia=false;
				}
				contador++;
			}
		}
		if(esIdentificador(expresion) && tablaSimbolos.ExisteIdentificador(expresion))
			resultado=Float.parseFloat(tablaSimbolos.BuscaPrimerIdentificadorDeclarado(expresion).getValor());
		else
			resultado=Float.parseFloat(expresion);
		return resultado;
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
	
	public String getMensajeSemantico() 
	{
		return MensajeSemantico;
	}

	public void setMensajeSemantico(String mensajeSemantico) 
	{
		MensajeSemantico = mensajeSemantico;
	}

	public ArrayList<String> getListaExpresiones() {
		return listaExpresiones;
	}

	public void setListaExpresiones(ArrayList<String> listaExpresiones) {
		this.listaExpresiones = listaExpresiones;
	}
	
	public String MuestraTablaSimbolos()
	{
		return tablaSimbolos.MuestraTablaSimbolos(tablaSimbolos.getLista());
	}
}
