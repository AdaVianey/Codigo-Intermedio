package compilador;

public class AnalisisSintactico extends Analizador{

	private String MensajeSintactico;
	
	public AnalisisSintactico()
	{
		MensajeSintactico="Error(es) Sintacticos: \n";

	}

	public boolean Sintactico(String Texto) 
	{
		boolean z=true,modificador=false;
		String declaraclase="";
		int contador=0,numeroDeTokens=CuentaTokens(Texto);
		String cuerpoClase="";
		if(!Texto.isBlank()) 
		{
			if(numeroDeTokens<=5)
			{
				String[] tokensRen=Texto.split("\n");
				for(String renglon:tokensRen)
				{
					String[] tokensCol=renglon.split("( |\t)");
					for(String token:tokensCol)
					{
						if(!token.isBlank())
							declaraclase+=token+" ";
					}
					declaraclase+="\n";
				}
			}
			else 
			{
				String[] tokensRen=Texto.split("\n");
				for(String renglon:tokensRen)
				{
					String[] tokensCol=renglon.split("( |\t)");
					for(String token:tokensCol)
					{
						if(!token.isBlank())
						{
							contador++;
							if(esModificador(token) && contador==1)
								modificador=true;
							if(modificador)
							{
								if(contador<5 || contador==numeroDeTokens)
									declaraclase+=" "+token;
								else
									cuerpoClase+=" "+token;
							}
							else
							{
								if(contador<4 || contador==numeroDeTokens)//contador == numero de tokens es para que concatene el ultimo token del string a la declaracion de la clase
									declaraclase+=" "+token;
								else
									cuerpoClase+=" "+token;
							}
						}
					}
					cuerpoClase+="\n";
					declaraclase+="\n";
				}
			}
			if(!DeclaraClase(declaraclase))
				z=false;
			if(!cuerpoClase.isBlank())
				if(!ChequeoCuerpo(cuerpoClase))
					z=false;
		}
		return z;
	}
	
	private boolean DeclaraClase(String declaraClase)
	{
		boolean z=false,entroModificador=false,entroClass=false,entroIdentificador=false,entroCorcheteApertura=false,requiereModificador=false;
		int columna=1,elemento=1,numeroRenglon=1,numeroDeTokens=CuentaTokens(declaraClase);
		if(!declaraClase.isBlank())
		{
			String primerToken=ConsultaTokensxPosicion(declaraClase, 1);
			String[] tokensRen=declaraClase.split("\n");
			for(String renglon:tokensRen)
			{
				String[] tokensCol=renglon.split("( |\t)");
				for(String token:tokensCol)
				{
					if(!token.isBlank())
					{
						if(primerToken.equals("class") && elemento==1)
							elemento++;
						if(elemento==1)
						{
							if(!esModificador(token)) 
							{
								if(numeroDeTokens>=2)
								{
									if(ConsultaTokensxPosicion(declaraClase, 2).equals("class"))//se pregunta si el token que sigue es la palabra class
									{
										MensajeSintactico+="Se esperaba un modificador en el renglon "+(numeroRenglon)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
										requiereModificador=true;
									}
								}
								else
									MensajeSintactico+="Se esperaba la palabra reservada class en el renglon "+(numeroRenglon)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
							}
							else
							{
								entroModificador=true;								
								if(CuentaTokens(declaraClase)>=2)
									if(ConsultaTokensxPosicion(declaraClase, 2).equals("class"))//se pregunta si el token que sigue es la palabra class
										requiereModificador=true;
								if(elemento==numeroDeTokens)
									MensajeSintactico+="Falta la palabra reservada class despues del token: '"+token+"' en el renglon "+(numeroRenglon)+" y columna "+(columna)+"\n";
							}								
						}
						if(elemento==2)
						{
							if(!token.equals("class")) 
								MensajeSintactico+="Se esperaba la palabra reservada class en el renglon "+(numeroRenglon)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
							else
							{
								entroClass=true;
								if(entroModificador)
								{
									if(elemento == numeroDeTokens)
										MensajeSintactico+="Falta un identificador despues del token: '"+token+"' en el renglon "+(numeroRenglon)+" y columna "+(columna)+"\n";								
								}
								else
									if(elemento == numeroDeTokens+1)
										MensajeSintactico+="Falta un identificador despues del token: '"+token+"' en el renglon "+(numeroRenglon)+" y columna "+(columna)+"\n";									
							}								
						}
						if(elemento==3)
						{
							if(!esIdentificador(token))
								MensajeSintactico+="Se esperaba un identificador en el renglon "+(numeroRenglon)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
							else
							{
								entroIdentificador=true;
								if(entroModificador)
								{
									if((elemento) == numeroDeTokens)
										MensajeSintactico+="Falta un '{' despues del token: '"+token+"' en el renglon "+(numeroRenglon)+" y columna "+(columna)+"\n";																
								}
								else
									if((elemento) == numeroDeTokens+1)
										MensajeSintactico+="Falta un '{' despues del token: '"+token+"' en el renglon "+(numeroRenglon)+" y columna "+(columna)+"\n";
							}
						}
						if(elemento==4)
						{
							if(!token.equals("{"))  
								MensajeSintactico+="Se esperaba un '{' en el renglon "+(numeroRenglon)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";					
							else
							{
								entroCorcheteApertura=true;
								if(entroModificador)
								{
									if(elemento==numeroDeTokens)
										MensajeSintactico+="Falta un '}' despues del token: '"+token+"' en el renglon "+(numeroRenglon)+" y columna "+(columna)+"\n";	
								}
								else
									if(elemento==numeroDeTokens+1)
										MensajeSintactico+="Falta un '}' despues del token: '"+token+"' en el renglon "+(numeroRenglon)+" y columna "+(columna)+"\n";
							}
						}
						if(elemento==5)
						{
							if(token.equals("}") && entroClass && entroIdentificador && entroCorcheteApertura)
							{
								if(requiereModificador)
								{
									if(entroModificador)
										z=true;
								}
								else
									z=true;
							}
							else
								if(!token.equals("}"))
									MensajeSintactico+="Se esperaba un '}' en el renglon "+(numeroRenglon)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
						}
						columna++;
						elemento++;
					}
				}
				columna=1;
				numeroRenglon++;
			}	
		}
		return z;
	}

	private boolean ChequeoCuerpo(String cuerpo) 
	{
		boolean z=true,entro=false;
		if(!cuerpo.isBlank())
		{
			int numeroDeTokens=CuentaTokens(cuerpo);
			String primerToken=ConsultaTokensxPosicion(cuerpo, 1);
			if(primerToken.matches("if|while"))//todo este if es para validar si es un if o un while
			{
				int posicionCierreCorchete=ObtenerPosicionCierreCorchete(cuerpo);
				if(posicionCierreCorchete==-1)//si no encontro un corchete que cierra o si no hay un corchete que abre
					posicionCierreCorchete=8;//se asigna para que se mande a checar al metodo es conficion y marque el error
				if(numeroDeTokens<=8) 
				{
					if(!esCondicion(extraerTokens(cuerpo,numeroDeTokens,1,0)))//para formar una condicion valida se necesitan 8 tokens minimamente y se manda a llamar el metodo aunque sean menos de 8 para que el metodo marque el error
						z=false;
				}
				else
				{
					String condicion=extraerTokens(cuerpo, 7, 1, posicionCierreCorchete),
					cuerpoCondicion=extraerTokens(cuerpo, posicionCierreCorchete-8, 8, 0),
					tokensDespuesDeCondicion=extraerTokens(cuerpo, numeroDeTokens-posicionCierreCorchete, posicionCierreCorchete+1, 0);
					if(!esCondicion(condicion) ||
							!ChequeoCuerpo(cuerpoCondicion) ||
							!ChequeoCuerpo(tokensDespuesDeCondicion))
						z=false;

				}
				entro=true;
			}
			if(esTipoDeDato(primerToken))//pregunta si es un tipo de dato y así saber que se trata de una declaracion de variable
			{
				int posicionPuntoYComa=ObtenerPosicionToken(cuerpo, ";");
				if(posicionPuntoYComa==-1 || posicionPuntoYComa>5)
					posicionPuntoYComa=3;
				if(numeroDeTokens<=5)
				{
					if(!EsDeclaracionDeVariable(cuerpo))
						z=false;
				}
				else
					if(!EsDeclaracionDeVariable(extraerTokens(cuerpo, posicionPuntoYComa, 1, 0)) ||
						!ChequeoCuerpo(extraerTokens(cuerpo, numeroDeTokens-posicionPuntoYComa, posicionPuntoYComa+1, 0)))
						z=false;
				entro=true;
			}
			if(esModificador(primerToken))//pregunta si es un modificador y así saber que se trata de una declaracion de variable
			{
				int posicionPuntoYComa=ObtenerPosicionToken(cuerpo, ";");
				if(posicionPuntoYComa==-1 || posicionPuntoYComa>6)
					posicionPuntoYComa=4;
				if(numeroDeTokens<=6)
				{
					if(!EsDeclaracionDeVariable(cuerpo))
						z=false;
				}
				else
					if(!EsDeclaracionDeVariable(extraerTokens(cuerpo, posicionPuntoYComa, 1, 0)) || 
							!ChequeoCuerpo(extraerTokens(cuerpo, numeroDeTokens-posicionPuntoYComa, posicionPuntoYComa+1, 0)))
						z=false;
				entro=true;
			}
			if(esIdentificador(primerToken))
			{
				int posicionPuntoYComa=ObtenerPosicionToken(cuerpo, ";");
				if(posicionPuntoYComa!=-1)
				{
					if(!EsAsignacion(extraerTokens(cuerpo, posicionPuntoYComa, 1, 0)) || 
						!ChequeoCuerpo(extraerTokens(cuerpo, numeroDeTokens-posicionPuntoYComa, posicionPuntoYComa+1, 0)) )
					z=false;
				}
				else
				{
					if(!EsAsignacion(cuerpo))
						z=false;
				}
				
				entro=true;
			}
			if(!entro)
			{
				MensajeError(cuerpo);
				z=false;
			}
		}
		return z;
	}
	
	private boolean esCondicion(String condicion)
	{
		boolean z=false,entroAperturaParentesis=false,entroCierreParentesis=false,entroOperando=false,entroOperando2=false,entroOperadorRelacional=false,entroCorcheteApertura=false;	
		int elemento=1,columna=1,ren=1;
		if(!condicion.isBlank())
		{
			int numeroDeTokens=CuentaTokens(condicion);
			String[] tokensRen=condicion.split("\n");
			for(String renglon:tokensRen)
			{
				String[] tokensCol=renglon.split("( |\t)");
				for(String token:tokensCol)
				{
					if(!token.isBlank()) 
					{
						switch(elemento)
						{
						//en el case 1 solo se valida que no falte el parentesis que abre ya que para entrar a este metodo es necesario que el primer token sea un if o un while y no es necesario preguntar por el 
						case 1:
							if(elemento==numeroDeTokens)
								MensajeSintactico+="Falta un '(' despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
						break;
						case 2:
							if(!token.equals("(")) 
								MensajeSintactico+="Se esperaba '(' en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
							else
							{
								entroAperturaParentesis=true;
								if(elemento==numeroDeTokens)
									MensajeSintactico+="Falta una variable o un numero despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
							}						
							break;
						case 3:
							if(	!(esIdentificador(token) || esNumero(token) || esDecimal(token)) ) 
								MensajeSintactico+="Se esperaba una variable o un numero en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
							else
							{
								entroOperando=true;
								if(elemento==numeroDeTokens)
									MensajeSintactico+="Falta un operador relacional despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
							}
						break;
						case 4:
							if(!esOperadorRelacional(token))
								MensajeSintactico+="Se esperaba operador relacional en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
							else
							{
								entroOperadorRelacional=true;
								if(elemento==numeroDeTokens)
									MensajeSintactico+="Falta una variable o un numero despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
							}
						break;
						case 5:
							if(	!(esIdentificador(token) || esNumero(token) || esDecimal(token))) 
								MensajeSintactico+="Se esperaba una variable o un numero en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
							else
							{
								entroOperando2=true;
								if(elemento==numeroDeTokens)
									MensajeSintactico+="Falta un ')' despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
							}
						break;
						case 6:
							if(!token.equals(")")) 
								MensajeSintactico+="Se esperaba un ')' en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
							else
							{
								entroCierreParentesis=true;
								if(elemento==numeroDeTokens)
									MensajeSintactico+="Falta un '{' despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
							}						
						break;
						case 7:
							if(!token.equals("{")) 
								MensajeSintactico+="Se esperaba un '{' en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
							else
							{
								entroCorcheteApertura=true;
								if(elemento==numeroDeTokens)
									MensajeSintactico+="Falta un '}' despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
							}						
						break;
						case 8:
							if(token.equals("}") && entroAperturaParentesis && entroOperando && entroOperadorRelacional && entroOperando2 && entroCierreParentesis && entroCorcheteApertura)
								z=true;
							else
								if(!token.equals("}"))
									MensajeSintactico+="Se esperaba un '}' en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
						break;
						}
						elemento++;
						columna++;
					}
				}
				columna=1;
				ren++;
			}
		}
		return z;
	}
	
	private boolean EsDeclaracionDeVariable(String cuerpo) 
	{
		boolean z=false;
		int numeroDeTokens=CuentaTokens(cuerpo);
		if(!cuerpo.isBlank())
		{
			if(numeroDeTokens<=4)//si son 3 o 4 tokens significa que es una declaracion de variable sin asignacion
				if(ValidaDeclaracionSinAsignacion(cuerpo))
					z=true;
			if(numeroDeTokens==5 || numeroDeTokens==6)//si son 5 o 6 tokens significa que es una declaracion de variable con asignacion
				if(ValidaDeclaracionConAsignacion(cuerpo))
					z=true;
		}
		return z;
	}
	
	private boolean EsAsignacion(String cuerpo)
	{
		boolean z=false,sigueOperando=true;
		int columna=1,ren=1,elemento=1,numeroDeTokens=CuentaTokens(cuerpo);
		if(!cuerpo.isBlank())
		{
			String[] tokensRen=cuerpo.split("\n");
			for(String renglon:tokensRen)
			{
				String[] tokensCol=renglon.split("( |\t)");
				for(String token:tokensCol)
				{
					if(!token.isBlank()) 
					{
						if(elemento==2)
							if(!token.equals("="))
							{
								z=false;
								MensajeSintactico+="Se esperaba '=' en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";								
							}
						if(elemento>2)
						{
							if(sigueOperando)
							{
								if(esIdentificador(token) || esNumero(token) || esDecimal(token) || esBooleano(token))
									sigueOperando=false;
								else
								{
									MensajeSintactico+="Se esperaba un valor en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
									z=false;
								}
							}
							else
							{
								if(esOperador(token))
									sigueOperando=true;
								else
									if(!token.equals(";"))
									{
										MensajeSintactico+="Se esperaba un ';' en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
										z=false;
									}
									else
										z=true;
							}
							if(!ConsultaTokensxPosicion(cuerpo, elemento).equals(";") && elemento==numeroDeTokens)
								MensajeSintactico+="Se esperaba un ';' en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+ConsultaTokensxPosicion(cuerpo, elemento)+"'\n";
						}
						columna++;
						elemento++;
					}
				}
				columna=1;
				ren++;
			}
		}	
		return z;	
	}

	private boolean ValidaDeclaracionConAsignacion(String declaracion)
	{
		boolean z=false, requiereModificador=false, entroModificador=false,entroTipoDato=false,entroIdentificador=false,entroIgual=false,entroValor=false;
		int elemento=1,columna=1,ren=1,numeroDeTokens=CuentaTokens(declaracion);
		if(!declaracion.isBlank())
		{
			String primerToken=ConsultaTokensxPosicion(declaracion, 1);
			String[] tokensRen=declaracion.split("\n");
			for(String renglon:tokensRen)
			{
				String[] tokensCol=renglon.split("( |\t)");
				for(String token:tokensCol)
				{
					if(!token.isBlank()) 
					{
						if(esTipoDeDato(primerToken) && elemento==1)
							elemento++;
						switch(elemento)
						{
						case 1:
							if(!esModificador(token)) 
							{
								if(numeroDeTokens>=2)
									if(esTipoDeDato(ConsultaTokensxPosicion(declaracion, 2)))//se pregunta si el token que sigue es tipo de dato
									{
										MensajeSintactico+="Se esperaba un modificador en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
										requiereModificador=true;
									}
							}
							else
							{
								entroModificador=true;
								if(numeroDeTokens>=2)
									if(esTipoDeDato(ConsultaTokensxPosicion(declaracion, 2)))//se pregunta si el token que sigue es tipo de dato
										requiereModificador=true;
								if(elemento==numeroDeTokens)
									MensajeSintactico+="Falta el tipo de dato despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
							}
						break;
						case 2:
							if(!esTipoDeDato(token))  
								MensajeSintactico+="Se esperaba un tipo de dato en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";					
							else
							{
								entroTipoDato=true;
								if(entroModificador)
								{
									if(elemento==numeroDeTokens)
										MensajeSintactico+="Falta un identificador despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";	
								}
								else
									if(elemento==numeroDeTokens+1)
										MensajeSintactico+="Falta un identificador despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
							}
							
						break;
						case 3:
							if(!esIdentificador(token))
								MensajeSintactico+="Se esperaba un identificador en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";					
							else
							{
								entroIdentificador=true;
								if(entroModificador)
								{
									if(elemento==numeroDeTokens)
										MensajeSintactico+="Falta un '=' despues del token: "+token+" en el renglon "+(ren)+" y columna "+(columna)+"\n";	
								}
								else
									if(elemento==numeroDeTokens+1)
										MensajeSintactico+="Falta un '=' despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
							}
							
						break;
						case 4:
							if(!token.equals("="))  
								MensajeSintactico+="Se esperaba un '=' en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";					
							else
							{
								entroIgual=true;
								if(entroModificador)
								{
									if(elemento==numeroDeTokens)
										MensajeSintactico+="Falta un valor despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";	
								}
								else
									if(elemento==numeroDeTokens+1)
										MensajeSintactico+="Falta un valor despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
							}
							
						break;
						case 5:
							if( !(esBooleano(token) || esNumero(token) || esDecimal(token) || esIdentificador(token)) )  
								MensajeSintactico+="Se esperaba un valor en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";					
							else
							{
								entroValor=true;
								if(entroModificador)
								{
									if(elemento==numeroDeTokens)
										MensajeSintactico+="Falta un ';' despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";	
								}
								else
									if(elemento==numeroDeTokens+1)
										MensajeSintactico+="Falta un ';' despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
							}							
						break;
						case 6:
							if(token.equals(";") && entroTipoDato && entroIdentificador && entroIgual && entroValor)
							{
								if(requiereModificador)
								{
									if(entroModificador)
										z=true;
								}
								else
									z=true;
							}
							else
								if(!token.equals(";"))
									MensajeSintactico+="Se esperaba un ';' en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
						break;
						}
						elemento++;
						columna++;
					}
				}
				columna=1;
				ren++;
			}
		}
		return z;
	}
	
	private boolean ValidaDeclaracionSinAsignacion(String declaracion)
	{
		boolean z=false, requiereModificador=false, entroModificador=false,entroTipoDato=false,entroIdentificador=false;
		int elemento=1,columna=1,ren=1;
		if(!declaracion.isBlank())
		{
			String[] tokensRen=declaracion.split("\n");
			for(String renglon:tokensRen)
			{
				String[] tokensCol=renglon.split("( |\t)");
				for(String token:tokensCol)
				{
					if(!token.isBlank()) 
					{
						String primerToken=ConsultaTokensxPosicion(declaracion, 1);
						int numeroDeTokens=CuentaTokens(declaracion);
						if(esTipoDeDato(primerToken) && elemento==1)
							elemento++;
						switch(elemento)
						{
						case 1:
							if(!esModificador(token)) 
							{
								if(numeroDeTokens>=2)
									if(esTipoDeDato(ConsultaTokensxPosicion(declaracion, 2)))//se pregunta si el segundo token es tipo de dato
									{
										MensajeSintactico+="Se esperaba un modificador en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
										requiereModificador=true;
									}
							}
							else
							{
								entroModificador=true;
								if(numeroDeTokens>=2)
									if(esTipoDeDato(ConsultaTokensxPosicion(declaracion, 2)))//se pregunta si el segundo token es tipo de dato
										requiereModificador=true;
								if(elemento==numeroDeTokens)
									MensajeSintactico+="Falta el tipo de dato despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
							}						
							
						break;
						case 2:
							if(!esTipoDeDato(token))  
								MensajeSintactico+="Se esperaba un tipo de dato en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";					
							else
							{
								entroTipoDato=true;
								if(entroModificador)
								{
									if(elemento==numeroDeTokens)
										MensajeSintactico+="Falta un identificador despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";	
								}
								else
									if(elemento==numeroDeTokens+1)
										MensajeSintactico+="Falta un identificador despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
							}
						break;
						case 3:
							if(!esIdentificador(token))  
								MensajeSintactico+="Se esperaba un identificador en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";					
							else
							{
								entroIdentificador=true;
								if(entroModificador)
								{
									if(elemento==numeroDeTokens)
										MensajeSintactico+="Falta un ';' despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";	
								}
								else
									if(elemento==numeroDeTokens+1)
										MensajeSintactico+="Falta un ';' despues del token: '"+token+"' en el renglon "+(ren)+" y columna "+(columna)+"\n";
							}
						break;
						case 4:
							if(token.equals(";") && entroTipoDato && entroIdentificador)
							{
								if(requiereModificador)
								{
									if(entroModificador)
										z=true;
								}
								else
									z=true;
							}
							else
								if(!token.equals(";"))
									MensajeSintactico+="Se esperaba un ';' en el renglon "+(ren)+" y columna "+(columna)+" en vez del token: '"+token+"'\n";
						break;
						}
						elemento++;
						columna++;
					}
				}
				columna=1;
				ren++;
			}
		}
		return z;
	}
	
	private void MensajeError(String cuerpo)
	{
		int ren=1;
		if(!cuerpo.isBlank())
		{
			String[] tokensRen=cuerpo.split("\n");
			for(String renglon:tokensRen)
			{
				String[] tokensCol=renglon.split("( |\t)");
				for(String token:tokensCol)
				{
					if(!token.isBlank())
					{
						MensajeSintactico+="Error de sintaxis en el token: '"+ConsultaTokensxPosicion(cuerpo, 1)+"' en el renglon "+ren;
						return;
					}
				}
				ren++;
			}			
		}
	}

	public String getMensajeSintactico() {
		return MensajeSintactico;
	}

	public void setMensajeSintactico(String mensajeSintactico) {
		MensajeSintactico = mensajeSintactico;
	}

}
