package compilador;

import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

public class Ventana extends JFrame implements ActionListener,WindowListener{

	private static final long serialVersionUID = 1L;
	
	private File ArchivoL,ArchivoE;//para manejar dos archivos, uno de escritura y otro de lectura
	private BufferedReader br;//para leer el archivo
	private PrintWriter pw;//Para escribir en el archivo
	
	private AnalisisLexico lexico;
	private AnalisisSintactico sintactico;
	private AnalisisSemantico semantico;
	private JMenu menuCompila, menuUtiles,menuArchivo;
	private JMenuItem jmiCompilar, jmiTablaSimbolos,jmiNuevo,jmiAbrir,jmiGuardar,jmiSalir,jmiCuadruplos,jmiEnsamblador;
	private JTextArea texto;//Es donde vamos a escribir y mostrar texto
	private JMenuBar barra;
	private boolean ejecutadoCorrectamente;
	private Cuadruplos cuadruplos;
	private Ensamblador ensamblador;
	
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
		cuadruplos=new Cuadruplos();
		ensamblador=new Ensamblador();
		
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
		
		//se crea y se agrega el menu item Nuevo
		jmiNuevo=new JMenuItem("Nuevo");
		jmiNuevo.addActionListener(this);
		menuArchivo.add(jmiNuevo);
		
		//se crea y se agrega el menu item Abrir
		jmiAbrir=new JMenuItem("Abrir...");
		jmiAbrir.addActionListener(this);
		menuArchivo.add(jmiAbrir);
		
		//se crea y se agrega el menu item guardar
		jmiGuardar=new JMenuItem("Guardar...");
		jmiGuardar.addActionListener(this);
		menuArchivo.add(jmiGuardar);

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

		jmiEnsamblador=new JMenuItem("Mostrar Codigo Ensamblador");
		jmiEnsamblador.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));//es un atajo para ejecutar esta opcion
		jmiEnsamblador.addActionListener(this);
		menuUtiles.add(jmiEnsamblador);
		
		//se agregan los menus a la barra
		barra.add(menuArchivo);
		barra.add(menuCompila);
		barra.add(menuUtiles);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object evento=e.getSource();
		if(evento==jmiNuevo) {//si se eligio la opcion nuevo
			if(!texto.getText().equals("")) {//si el texto no esta en blanco va a hacer lo siguiente, si esta en blanco no hara nada
				int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro que quiere iniciar una hoja en blanco? (Se borrara todo el progreso)", "Proyecto: Confirme Nueva Hoja", JOptionPane.YES_NO_OPTION);//abrimos una ventana de confirmacion
				if(opcion==JOptionPane.YES_OPTION) {//si se eligio la opcion si
					texto.setText("");//se deja en blanco el texto
				}
			}
		}
		if(evento==jmiAbrir) {//si se eligio la opcion abrir
			JFileChooser elegir=new JFileChooser();//Creamos una variable de tipo JFileChooser
			FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt");//Creamos una variable de tipo FileNameExtensionFilter que va a contener los filtros
			elegir.setFileFilter(filter);//se agregan los filtros a la variable elegir de tipo JFileChooser
			int returnVal=elegir.showOpenDialog(getParent());//aqui se abre la ventana de abrir y guarda en una variable la opcion que se selecciono
			String nombre="";
			try
			{
				nombre=elegir.getSelectedFile().getName();//Guardamos el nombre de el archivo seleccionado o el nombre tecleado				
			}catch(NullPointerException error) {}
			String dir=elegir.getCurrentDirectory().getAbsolutePath();//guardamos el directorio del archivo que se quiere abrir
			if(returnVal==JFileChooser.APPROVE_OPTION) {//si se elige la opcion de abrir
				if(texto.getText().equals("")) {//se pregunta para saber si el texto esta vacio para abrir el archivo sin preguntar
					try {
						AbrirArchivo(nombre, false, dir);//abrimos el archivo en modo lectura que se indica con el false
						String linea=br.readLine();//leemos la primera linea del archivo y la asignamos a una variable
						texto.setText(linea);//Escribimos dicha linea en nuestro programa
						linea=br.readLine();//leemos la siguiente linea del archivo
						while(linea!=null) {//mientras la linea del archivo sea diferente de null se seguira leyendo
							texto.setText(texto.getText()+"\n"+linea);//se va agregando lo leido al texto del programa
							linea=br.readLine();
						}
						CerrarArchivo();
					} catch (IOException e1) {//se hace lo mismo que en el try anterior pero se entra aqui si se escribio el nombre del archivo que se quiere abrir o si no existe ningun archivo con el nombre ingresado
						try {
							AbrirArchivo(nombre+".txt", false, dir);
							String linea=br.readLine();
							texto.setText(linea);
							linea=br.readLine();
							while(linea!=null) {
								texto.setText(texto.getText()+"\n"+linea);
								linea=br.readLine();
							}
						}catch(IOException e2) {
							JOptionPane.showMessageDialog(this,"El archivo no existe.");
						}
					}
					catch(NullPointerException error) {}
				}else {
					//es lo mismo que lo anterior solo que entra aqui cuando hay texto en el programa
					int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro que quiere abrir este archivo? (Se borrara todo el progreso)", "Proyecto: Abrir Archivo", JOptionPane.YES_NO_OPTION);
					if(opcion == JOptionPane.YES_OPTION) {
						try {
							AbrirArchivo(nombre, false, dir);
							String linea=br.readLine();
							texto.setText(linea);
							linea=br.readLine();
							while(linea!=null) {
								texto.setText(texto.getText()+"\n"+linea);
								linea=br.readLine();
							}
							CerrarArchivo();
						} catch (IOException e1) {
							try {
								AbrirArchivo(nombre+".txt", false, dir);
								String linea=br.readLine();
								texto.setText(linea);
								linea=br.readLine();
								while(linea!=null) {
									texto.setText(texto.getText()+"\n"+linea);
									linea=br.readLine();
								}
								CerrarArchivo();
							}catch(IOException e2) {
								JOptionPane.showMessageDialog(this, "El archivo no existe");
							}
						}
						catch(NullPointerException error) {}

					}
				}
			}
		}
		if(evento==jmiGuardar) {//Si se eligio la opcion guardar como
			JFileChooser elegir=new JFileChooser();//creamos un objeto de tipo jfilechooser
			int returnVal=elegir.showSaveDialog(getParent());//guardamos en una variable la opcion elegida
			boolean bandera=true;//Para saber si se eligio manualmente el archivo donde se va a guardar
			String dir=elegir.getCurrentDirectory().getAbsolutePath();//obtenemos la direccion de la carpeta donde se eligio guardar el archivo
			String nombre="";
			try
			{
				nombre=elegir.getSelectedFile().getName();//obtenemos el nombre que se le puso al archivo				
			}catch(NullPointerException error) {}
			if(returnVal==JFileChooser.APPROVE_OPTION) {//para saber si se eligio la opcion save
				try {//este try sirve por si hay una excepcion al manejar el archivo
					AbrirArchivo(nombre+".txt", false, dir);//abrimos el archivo en modo lectura señalado por el false y aqui marca una excepcion si el archivo no existe y dicho caso simplemente se va al catch y lo crea (tambien entra al catch si se eligio manualmente) pero si se escribio el nombre y existe se hace lo siguiente
					CerrarArchivo();//cerramos el archivo
					bandera=false;//hacemos la bandera igual a false para posteriormente no entrar en un if
					int opcion = JOptionPane.showConfirmDialog(this, "Ya existe un archivo con ese nombre ¿desea sobreescribir el archivo?", "Proyecto: Archivo existente", JOptionPane.YES_NO_OPTION);//preguntamos si quiere guardar en un archivo existente
					if(opcion==JOptionPane.YES_OPTION) {//si se eligio la opcion si
						String linea=null;//creamos un string donde guardaremos cada linea del programa para posteriormente escribirla en el archivo
						try {//este try es por si marca excepcion al momento de manejar el JTextArea
							AbrirArchivo(nombre+".txt", true, dir);//abrimos el archivo en modo de escritura para que se guarde en la variable de la clase ArchivoE
							CerrarArchivo();//cerramos el archivo para poder hacer el siguiente paso
							ArchivoE.delete();//eliminamos el archivo para que se borre lo que ya tenia y que el archivo contenga unicamente lo que se quiere guardar
							AbrirArchivo(nombre+".txt", true, dir);//Se crea el archivo con el mismo nombre que el que se borro pero este esta en blanco
							for(int i=0;i<texto.getLineCount();i++) {//hacemos un ciclo que va a dar iteraciones igual al numero de lineas de texto
								if(i+1==texto.getLineCount()) {//esto es para ver si es la ultima linea de texto
									linea=texto.getText(texto.getLineStartOffset(i),texto.getLineEndOffset(i)-texto.getLineStartOffset(i));//asignamos en una variable lo que hay en la linea i
									pw.println(linea);//escribimos dicha linea en el archivo	
								}else {//lo mismo que lo anterior en caso de que no sea la ultima linea
									linea = texto.getText(texto.getLineStartOffset(i),texto.getLineEndOffset(i)-texto.getLineStartOffset(i)-1);
									pw.println(linea);
								}
							}								
						} catch (BadLocationException e2) {	}
						catch(NullPointerException error) {}

						CerrarArchivo();
					}	
				} catch (IOException e1) {//entra aqui cuando el archivo no existe y se va a crear por primera vez por lo cual no preguntamos nada y se sigue el mismo proceso de creacion que anteriormente
					try {
						try {
							String linea=null;
							AbrirArchivo(nombre+".txt", true, dir);
							for(int i=0;i<texto.getLineCount();i++) {
								if(i+1==texto.getLineCount()) {
									linea=texto.getText(texto.getLineStartOffset(i),texto.getLineEndOffset(i)-texto.getLineStartOffset(i));
									pw.println(linea);	
								}else {
									linea = texto.getText(texto.getLineStartOffset(i),texto.getLineEndOffset(i)-texto.getLineStartOffset(i)-1);
									pw.println(linea);
								}
							}
						} catch (BadLocationException e2) {}
						catch(NullPointerException error) {}
						CerrarArchivo();
					} catch (IOException e2) {
					}
				}
			}
			if(bandera){//esto sirve para cuando no se escribio el nombre del archivo donde se quiere guardar sino que se eligio un archivo donde guardar ya que cuando se selecciona se viene un .txt junto al nombre y por eso no lo encuentra en el paso anterior y nos crea un archivo que tiene el nombre mas .txt al final
				try {
					AbrirArchivo(nombre, false, dir);//si se puede abrir el archivo quiere decir que se eligio manualmente
					CerrarArchivo();
					ArchivoE.delete();//como se eligio manualmente debemos borrar el nuevo archivo que se creo anteriormente ya que se quiere sobreescribir sobre un archivo, no crear uno nuevo
					int opcion = JOptionPane.showConfirmDialog(this, "¿desea sobreescribir el archivo?", "Proyecto: Archivo existente", JOptionPane.YES_NO_OPTION);//esperamos una confirmacion del usuario
					if(opcion==JOptionPane.YES_OPTION) {//de aqui en adelante se sigue el mismo procedimiento de guardado que anteriormente
						String linea=null;
						try {
							AbrirArchivo(nombre, true, dir);
							CerrarArchivo();
							ArchivoE.delete();
							AbrirArchivo(nombre, true, dir);
							for(int i=0;i<texto.getLineCount();i++) {
								if(i+1==texto.getLineCount()) {
									linea=texto.getText(texto.getLineStartOffset(i),texto.getLineEndOffset(i)-texto.getLineStartOffset(i));
									pw.println(linea);	
								}else {
									linea = texto.getText(texto.getLineStartOffset(i),texto.getLineEndOffset(i)-texto.getLineStartOffset(i)-1);
									pw.println(linea);
								}
							}
						} catch (BadLocationException e1) {}
						catch(NullPointerException error) {}
						CerrarArchivo();
					}	
				}catch(IOException e1){
				}
			}
		}
		if(evento==jmiSalir)
		{
			if(!texto.getText().isBlank())
			{
				int respuesta=JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea salir?","Salir",2);
				if(respuesta==JOptionPane.YES_OPTION)
					System.exit(0);					
			}
			else
				System.exit(0);
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
				tablaDeSimbolos.setSize(1200,600);
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
				cuadruplos.setListaExpresiones(semantico.getListaExpresiones());
				JDialog tablaCuadruplos=new JDialog(this,"Cuadruplos");
				JTextArea textArea=new JTextArea(cuadruplos.TablaCuadruplos());
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
		if(evento==jmiEnsamblador)
		{
			if(ejecutadoCorrectamente)
			{
				cuadruplos.setListaExpresiones(semantico.getListaExpresiones());
				ensamblador.setListaExpresionesConvertidas(cuadruplos.getListaExpresionesConvertidas());
				JDialog tablaEnsamblador=new JDialog(this,"Codigo Ensamblador");
				JTextArea textArea=new JTextArea(ensamblador.MuestraCodigoEnsamblador());
				textArea.setFont(new Font("Arial",Font.PLAIN,20));
				textArea.setEditable(false);
				JScrollPane sc=new JScrollPane(textArea);
				tablaEnsamblador.add(sc);
				tablaEnsamblador.setSize(800,400);
				tablaEnsamblador.setVisible(true);
				tablaEnsamblador.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			}
			else
				JOptionPane.showMessageDialog(this, "Debera compilar el programa y que no haya errores para ver el codigo ensamblador");
		}
	}
	
	private void AbrirArchivo(String NomA, boolean tipo, String dir) throws IOException {//metodo para abrir archivo donde recibimos el nombre, la direccion del sistema donde se va a trabajar con el archivo y true para saber si el archivo sera de escritura y false si es de lectura
		if(tipo){
			ArchivoE=new File(dir, NomA);
			pw=new PrintWriter(new FileWriter(ArchivoE,true));
		}
		else{
			ArchivoL=new File(dir, NomA);
			br=new BufferedReader(new FileReader(ArchivoL));
		}
	}

	private void CerrarArchivo() throws IOException {//programa para cerrar el archivo
		if(pw!=null)
			pw.close();
		if(br!=null)
			br.close();
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
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) 
	{
		if(!texto.getText().isBlank())
		{
			int respuesta=JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea salir?","Salir",2);
			if(respuesta==JOptionPane.YES_OPTION)
				System.exit(0);					
		}
		else
			System.exit(0);
	}
}