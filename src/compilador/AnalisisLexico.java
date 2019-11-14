package compilador;

public class AnalisisLexico extends Analizador{

	private String MensajeLexico;
	
	public AnalisisLexico()
	{
		MensajeLexico="Error(es) Lexicos: \n";
	}
	
	public String getMensajeLexico() {
		return MensajeLexico;
	}

	public void setMensajeLexico(String mensajeLexico) {
		MensajeLexico = mensajeLexico;
	}

	public boolean Lexico(String Texto) 
	{
		int columna=0;
		boolean z=true;
		if(!Texto.isBlank())
		{
			String[] tokens=Texto.split("\n");
			for(int i=0;i<tokens.length;i++)
			{
				String[] tokens2=tokens[i].split("( |\t)");
				for(int j=0;j<tokens2.length;j++)
				{
					if(!tokens2[j].isBlank()) 
					{
						columna++;
						if(!esModificador(tokens2[j])) 
							if(!esNumero(tokens2[j]))
								if(!esOperador(tokens2[j])) 
									if(!esOperadorRelacional(tokens2[j])) 
										if(!esSimboloEspecial(tokens2[j])) 
											if(!esPalabraReservada(tokens2[j])) 
												if(!esTipoDeDato(tokens2[j])) 
													if(!esBooleano(tokens2[j])) 
														if(!esDecimal(tokens2[j]))
															if(!esIdentificador(tokens2[j])) 
															{															
																z=false;
																MensajeLexico+="Token: '"+tokens2[j]+"'       Renglon:"+(i+1)+"       Columna:"+columna+"\n";//se puede hacer referencia al renglon con la variable i
															}
					}
				}
				columna=0;
			}
		}
		return z;
	}
}
