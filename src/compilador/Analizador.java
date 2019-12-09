package compilador;

import java.util.Stack;

public class Analizador {
	
	public int CuentaTokens(String texto) {//metodo para contar los tokens totales
		String[] tokensRen=texto.split("\n");
		int numeroTokens=0;
		for(String renglon:tokensRen)
		{
			String[] tokensCol=renglon.split("( |\t)");
			for(String token:tokensCol)
			{
				if(!token.isBlank()) 
					numeroTokens++;
			}
		}
		return numeroTokens;
	}
	
	public String ConsultaTokensxPosicion(String texto, int posicionElemento) {//metodo para contar los tokens totales
		int elemento=1;
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
						if(elemento==posicionElemento)
							return token;
						elemento++;
					}
				}
			}			
		}
		return "";
	}
	

	//nota: este metodo solo sirve si los token que se quieren extraer están juntos y se puede mandar la posicion de algun token adicional que se quiera concatenar al string
	public String extraerTokens(String cuerpo, int tokens_A_Extraer, int primerToken, int posicionTokenExtra) {//recibe de parametro el string del que se quieren sacar los token, el numero de tokens a extraer y desde que token se quiere empezar la extraccion
		String respuesta="";
		String[] tokensRen=cuerpo.split("\n");
		int posicionToken=1, contador=0;
		for(String renglon:tokensRen)
		{
			String[] tokensCol=renglon.split("( |\t)");
			for(String token:tokensCol)
			{
				if(!token.isBlank()) 
				{
					if(posicionToken>=primerToken && contador<tokens_A_Extraer)
					{
						respuesta+=token+" ";
						contador++;
					}
					if(posicionTokenExtra==posicionToken)
						respuesta+=token+" ";
					posicionToken++;
				}
			}
			respuesta+="\n";
		}
		
		return respuesta;
	}

	public int ObtenerPosicionCierreCorchete(String cuerpo)
	{
		int posicion=0;
		Stack<Character> pila=new Stack<Character>();
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
						posicion++;
						if(token.equals("{"))
							pila.add('{');
						if(token.equals("}"))
						{
							if(!pila.isEmpty())
								pila.pop();
							else
								return -1;
							if(pila.isEmpty())
								return posicion;
						}
					}
				}
			}			
		}
		return -1;
	}
	
	public int ObtenerPosicionToken(String cuerpo, String tokenBuscado)//regresa la posicion del token buscado
	{
		int contador=0;
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
						contador++;
						if(token.equals(tokenBuscado))
							return contador;
					}
				}
			}			
		}
		return -1;
	}
	
	public boolean esModificador(String token)
	{
		if(token.matches("public|private|protected"))
			return true;
		return false;
	}
	public boolean esIdentificador(String token)
	{
		if(token.matches("[a-zA-Z]+[a-zA-Z0-9_]*") && !esBooleano(token) && !esModificador(token) && !esPalabraReservada(token) && !esTipoDeDato(token))
			return true;
		return false;
	}
	public boolean esOperador(String token)
	{
		if(token.matches("(\\+|\\*|/|-)"))
			return true;
		return false;
	}
	public boolean esOperadorRelacional(String token)
	{
		if(token.matches("(<|>|>=|<=|==|!=)"))
			return true;
		return false;
	}
	public boolean esSimboloEspecial(String token)
	{
		if(token.matches("[\\;|(|)|{|}|\\[|\\]|=]"))  
			return true;
		return false;
	}
	public  boolean esPalabraReservada(String token)
	{
		if(token.matches("(class|if|while)"))  
			return true;
		return false;
	}
	public  boolean esTipoDeDato(String token)
	{
		if(token.matches("(int|boolean|float)"))  
			return true;
		return false;
	}
	
	public boolean esBooleano(String token)
	{
		if(token.matches("(true|false)"))  
			return true;
		return false;
	}

	public boolean esNumero(String token)
	{
		if(token.matches("[0-9]+"))  
			return true;
		return false;
	}
	
	public boolean esDecimal(String token)
	{
		if(token.matches("[0-9][0-9]*\\.[0-9][0-9]*"))  
			return true;
		return false;
	}

}