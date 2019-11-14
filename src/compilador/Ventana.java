package compilador;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class Ventana extends JFrame implements ActionListener,WindowListener{

	private static final long serialVersionUID = 1L;
	
	private AnalisisLexico lexico;
	private AnalisisSintactico sintactico;
	private AnalisisSemantico semantico;
	private JMenu menuCompila, menuUtiles,menuAyuda,menuArchivo;
	private JMenuItem jmiCompilar, jmiTablaSimbolos,jmiAcercaDe,jmiAutor,jmiAbrir,jmiGuardar,jmiSalir,jmiCuadruplos;
	private JTextArea texto;//Es donde vamos a escribir y mostrar texto
	private JMenuBar barra;
	private boolean ejecutadoCorrectamente;
	
	public Ventana() 
	{
		InicializaComponentes();
		setTitle("Compilador");
		setSize(800,600);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
		addWindowListener(this);
	}

	private void InicializaComponentes() {
		lexico=new AnalisisLexico();
		sintactico=new AnalisisSintactico();
		semantico=new AnalisisSemantico();

		texto=new JTextArea();//se crea el componente donde vamos a escribir
		texto.setFont(new Font("Arial",Font.PLAIN,20));//se le asigna un tipo de letra
		JScrollPane scrollpane=new JScrollPane(texto); //se crea una barra a la que se le envia el JTextArea
		add(scrollpane);//se agrega el scroll (contiene el textArea)
		
		ejecutadoCorrectamente=false;
		//se agrega la barra
		barra=new JMenuBar();
		setJMenuBar(barra);
		
		//se crea el menu Archivo
		menuArchivo=new JMenu("Archivo");
		
		//se crea y se agrega el menu item Abrir
		jmiAbrir=new JMenuItem("Abrir...");
		jmiAbrir.addActionListener(this);
		//menuArchivo.add(jmiAbrir);
		
		//se crea y se agrega el menu item guardar
		jmiGuardar=new JMenuItem("Guardar...");
		jmiGuardar.addActionListener(this);
		//menuArchivo.add(jmiGuardar);

		menuArchivo.addSeparator();
		
		//se crea y se agrega el menu item salir
		jmiSalir=new JMenuItem("Salir");
		jmiSalir.addActionListener(this);
		menuArchivo.add(jmiSalir);

		//se crea el menu compilacion
		menuCompila=new JMenu("Compilacion");
		
		//se crea y se agrega el menu item ejecutar
		jmiCompilar=new JMenuItem("Ejecutar");
		jmiCompilar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));//es un atajo para ejecutar esta opcion
		jmiCompilar.addActionListener(this);
		menuCompila.add(jmiCompilar);

		//se crea el menu utiles
		menuUtiles=new JMenu("Utiles");
		//se crea y se agrega el menu item mostrar tabla de simbolos
		jmiTablaSimbolos=new JMenuItem("Mostrar Tabla de Simbolos");
		jmiTablaSimbolos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));//es un atajo para ejecutar esta opcion
		jmiTablaSimbolos.addActionListener(this);
		menuUtiles.add(jmiTablaSimbolos);
		
		jmiCuadruplos=new JMenuItem("Mostrar Cuadruplos");
		jmiCuadruplos.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));//es un atajo para ejecutar esta opcion
		jmiCuadruplos.addActionListener(this);
		menuUtiles.add(jmiCuadruplos);
		
		//se crea el menu Ayuda
		menuAyuda=new JMenu("Ayuda");
		
		//se crea y se agrega el menu item acerca de
		jmiAcercaDe=new JMenuItem("Acerca de...");
		jmiAcercaDe.addActionListener(this);
		menuAyuda.add(jmiAcercaDe);
		
		//se crea y se agrega el menu item acerca de
		jmiAutor=new JMenuItem("Autor...");
		jmiAutor.addActionListener(this);
		menuAyuda.add(jmiAutor);
				
		//se agregan los menus a la barra
		barra.add(menuArchivo);
		barra.add(menuCompila);
		barra.add(menuUtiles);
		barra.add(menuAyuda);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object evento=e.getSource();
		if(evento==jmiAbrir)
		{
			
		}
		if(evento==jmiGuardar)
		{
			
		}
		if(evento==jmiSalir)
		{
			if(!texto.getText().isBlank())
			{
				int respuesta=JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea salir?","Salir",2);
				if(respuesta==JOptionPane.YES_OPTION)
				{
					JOptionPane.showMessageDialog(this, "Vuelve por favor:'(","Mensaje de tu amigo",1);
					System.exit(0);					
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Vuelve por favor:'(","Mensaje de tu amigo",1);
				System.exit(0);
			}
		}
		if(evento==jmiAcercaDe)
		{
			JOptionPane.showMessageDialog(this, 
					  "Este compilador esta basado en el lenguaje JAVA y maneja solo tipos de datos int, float y boolean.\n"
					+ "Los tokens validos son cualquiera que no empiece por numeros ni contenga simbolos especiales.\n"
					+ "La sintaxis que maneja valida la estructura de la declaracion de la clase, de los if´s, while, declaracion de variables y asignaciones a variables.\n"
					+ "Los if´s y while pueden contener expresiones simples como condiciones, es decir, identificadores u operandos.\n"
					+ "Tambien hace las validaciones semanticas, se puede ver la tabla de simbolos y la generacion de cuadruplos en base a las expresiones codificadas.","Acerca de",1);
		}
		if(evento==jmiAutor)
		{
			JOptionPane.showMessageDialog(this,
				      "Autor(es):\n"
					+ "Victor Hugo Ontiveros Villalejos\n"
					+ "Ada Vianey Araujo Alvarez\n"
					+ "Escuela: Instituto Tecnologico de Culiacan\n"
					+ "Materia: Lenguajes y Automatas 2","Autor",1);
		}
		if(evento==jmiCompilar)
		{
			lexico=new AnalisisLexico();
			sintactico=new AnalisisSintactico();
			semantico=new AnalisisSemantico();
			String msg="",texto=this.texto.getText();
			texto=SeparaTokens(texto);
			if(lexico.Lexico(texto))
			{
				msg+="No hay errores lexicos\n";
				if(sintactico.Sintactico(texto))
				{
					msg+="No hay errores sintacticos\n";
					if(semantico.Semantico(texto))
					{
						msg+="No hay errores semanticos\n";
						ejecutadoCorrectamente=true;
						JOptionPane.showMessageDialog(this, msg);
					}
					else
					{
						JOptionPane.showMessageDialog(this, semantico.getMensajeSemantico());
						ejecutadoCorrectamente=false;						
					}
				}
				else
				{
					JOptionPane.showMessageDialog(this, sintactico.getMensajeSintactico());
					ejecutadoCorrectamente=false;
				}
			}
			else
			{
				JOptionPane.showMessageDialog(this, lexico.getMensajeLexico());				
				ejecutadoCorrectamente=false;
			}
		}
		if(evento==jmiTablaSimbolos)
		{
			if(ejecutadoCorrectamente)
			{
				JDialog tablaDeSimbolos=new JDialog(this,"Tabla de Simbolos");
				JTextArea textArea=new JTextArea(semantico.MuestraTablaSimbolos());
				textArea.setFont(new Font("Arial",Font.PLAIN,20));
				textArea.setEditable(false);
				JScrollPane sc=new JScrollPane(textArea);
				tablaDeSimbolos.add(sc);
				tablaDeSimbolos.setSize(1500,600);
				tablaDeSimbolos.setVisible(true);
				tablaDeSimbolos.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			}
			else
				JOptionPane.showMessageDialog(this, "Debera compilar el programa y que no haya errores para ver la tabla de simbolos");
		}
		if(evento==jmiCuadruplos)
		{
			if(ejecutadoCorrectamente)
			{
				JDialog tablaCuadruplos=new JDialog(this,"Cuadruplos");
				JTextArea textArea=new JTextArea(semantico.TablaCuadruplos());
				textArea.setFont(new Font("Arial",Font.PLAIN,20));
				textArea.setEditable(false);
				JScrollPane sc=new JScrollPane(textArea);
				tablaCuadruplos.add(sc);
				tablaCuadruplos.setSize(1250,600);
				tablaCuadruplos.setVisible(true);
				tablaCuadruplos.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			}
			else
				JOptionPane.showMessageDialog(this, "Debera compilar el programa y que no haya errores para ver la lista de cuadruplos");
		}
	}
	
	public String SeparaTokens(String texto) 
	{
		texto=texto.replace("=="," == ");
		texto=texto.replace("<="," <= ");
		texto=texto.replace(">="," >= ");
		texto=texto.replace("!="," != ");
		texto=texto.replace("+"," + ");
		texto=texto.replace("-"," - ");
		texto=texto.replace("*"," * ");
		texto=texto.replace("/"," / ");
		texto=texto.replace(">"," > ");
		texto=texto.replace("<"," < ");
		texto=texto.replace(";"," ; ");
		texto=texto.replace("("," ( ");
		texto=texto.replace(")"," ) ");
		texto=texto.replace("{"," { ");
		texto=texto.replace("}"," } ");
		texto=texto.replace("["," [ ");
		texto=texto.replace("]"," ] ");
		texto=texto.replace("="," = ");
		texto=texto.replace("=  ="," == ");
		texto=texto.replace("<  ="," <= ");
		texto=texto.replace(">  ="," >= ");
		texto=texto.replace("! ="," != ");	
		return texto;
	}

	@Override
	public void windowOpened(WindowEvent e) 
	{
		//JOptionPane.showMessageDialog(this, "Bienvenido, espero pases un buen rato :D","Mensaje de tu amigo",1);	
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) 
	{
		
	}

	@Override
	public void windowClosing(WindowEvent e) 
	{
		if(!texto.getText().isBlank())
		{
			int respuesta=JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea salir?","Salir",2);
			if(respuesta==JOptionPane.YES_OPTION)
			{
				//JOptionPane.showMessageDialog(this, "Vuelve por favor:'(","Mensaje de tu amigo",1);
				System.exit(0);					
			}
		}
		else
		{
			//JOptionPane.showMessageDialog(this, "Vuelve por favor:'(","Mensaje de tu amigo",1);
			System.exit(0);
		}
	}
}

/*
PRUEBAS
class p
{
	int x=0;
	x=12+2+3+2;
}

class p
{
	int x=0;
	x=12+
}

class p
{
	int x=0;
	x=12+12-12/21*32;
}

class p
{
	int x=0;
	float y=12.2;
	boolean z=true;
	x=z+y-12/21.21*true;
}

class p
{
	int x=0;
	float y=12.2;
	boolean z=true;
	z=z+y-12/21.21*true;
}

class p
{
	int x;
	int y;
	int z;
	z=z+y-12/21*12;
	x=18-6+3*6-z;
	y=x-12;
	if(x<z)
	{
		x=y;
	}
}


class p
{
	int x=0;
	x=12+2*3+2;
}
class p
{
	int x=0;
	x=12+2*3+2;
	x=1+2*3*2*2+2*1;
	x=3/2+1*7;
}
  */