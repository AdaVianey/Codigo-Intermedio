package compilador;

public class Identificador {

	private String Nombre, Tipo, Alcance, Valor;
	private int Renglon,Columna;
	
	public Identificador(String nombre, String tipo, int renglon, int columna, String alcance, String valor)
	{
		Nombre=nombre;
		Tipo=tipo;
		Renglon=renglon;
		Columna=columna;
		Alcance=alcance;
		Valor=valor;
	}

	public String getAlcance() {
		return Alcance;
	}

	public void setAlcance(String alcance) {
		Alcance = alcance;
	}

	public String getValor() {
		return Valor;
	}

	public void setValor(String valor) {
		Valor = valor;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	public String getTipo() {
		return Tipo;
	}

	public void setTipo(String tipo) {
		Tipo = tipo;
	}

	public int getRenglon() {
		return Renglon;
	}

	public void setRenglon(int renglon) {
		Renglon = renglon;
	}

	public int getColumna() {
		return Columna;
	}

	public void setColumna(int columna) {
		Columna = columna;
	}	
}
