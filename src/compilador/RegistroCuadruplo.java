package compilador;

public class RegistroCuadruplo {

	private String operador,argumento1,argumento2,resultado;
	
	public RegistroCuadruplo()
	{
		operador="";
		argumento1="";
		argumento2="";
		resultado="";
	}

	public RegistroCuadruplo(String operador, String argumento1, String argumento2, String resultado) {
		this.operador = operador;
		this.argumento1 = argumento1;
		this.argumento2 = argumento2;
		this.resultado = resultado;
	}

	public String getOperador() {
		return operador;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public String getArgumento1() {
		return argumento1;
	}

	public void setArgumento1(String argumento1) {
		this.argumento1 = argumento1;
	}

	public String getArgumento2() {
		return argumento2;
	}

	public void setArgumento2(String argumento2) {
		this.argumento2 = argumento2;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	
	
}
