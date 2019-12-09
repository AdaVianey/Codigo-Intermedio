package compilador;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

class CasoPrueba {

	Scanner leer=new Scanner(System.in);
	
	@Test
	public void IngresaMes() {
		String[] meses= {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
		String texto=leer.next();
		boolean z=false;
		for(String mes:meses)
		{
			if(mes.equalsIgnoreCase(texto))
				z=true;
		}
		System.out.println(z);
		assertEquals(true, z);		
	}

	@Test
	public void IngresaNumero() {
		String texto=leer.next();
		boolean z=false;
		if(texto.matches("[0-9]+"))
			z=true;
		System.out.println(z);
		assertEquals(true, z);		
	}
	
	@Test
	public void IngresaLetras() {
		String texto=leer.next();
		boolean z=false;
		if(texto.matches("[a-zA-Z]+"))
			z=true;
		System.out.println(z);
		assertEquals(true, z);		
	}
}
