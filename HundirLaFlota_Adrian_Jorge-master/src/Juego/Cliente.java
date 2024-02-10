package Juego;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JOptionPane;

public class Cliente {
	public static final char barco = (char) 66;
	public static final char tocado = (char) 88;
	public static final char empty = (char) 45;
	public static final char agua = (char) 48;
	public static int i = 0;
	public static Tablero barcosRival;
	public static boolean ok;

	public static void main(String[] args) {
//		mando
//		recibo
//		
//		espero
//		envio
		final int PUERTO = 6001;
		try {
			ServerSocket servidor = new ServerSocket(PUERTO);
			System.out.println("Escuchando en el puerto: " + PUERTO);
			Socket c1 = servidor.accept();
			VentanaCliente1 vc1 = new VentanaCliente1();
			vc1.setVisible(true);

			

			boolean win = false;
			ok = false;
			Tablero misBarcos = Cliente.generar_tabla();
			barcosRival = Cliente.tabla_rival();
			Tablero tableroRival;

			VentanaCliente1.misBarcos.setText("<html><pre>" + Cliente.mostrarTablero(misBarcos) + "</pre></html>");
			VentanaCliente1.barcosRival.setText("<html><pre>" + Cliente.mostrarTablero(barcosRival) + "</pre></html>");


			ObjectOutputStream oos = new ObjectOutputStream(c1.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(c1.getInputStream());
			
			
			


			while (barcosRival.isWin() == false) {
				VentanaCliente1.btn_enviar.setEnabled(true);
				while (ok == false) {
					VentanaCliente1.txt_turno.setText("Tu turno");
					VentanaCliente1.btn_enviar.setEnabled(true);
				}
				Cliente.limpiar();
				VentanaCliente1.btn_enviar.setEnabled(false);
				// mando mi jugada
				oos.writeObject(barcosRival);
				// recibo la respuesta del rival

				barcosRival = (Tablero) ois.readObject();
				Cliente.actualizarBarcosRival(barcosRival);
				if (barcosRival.isWin()) {
					//hay que hacer ventana emergente
					VentanaGanar vg = new VentanaGanar();
					vc1.setVisible(true);
					vg.setVisible(true);
					return;
				}
				VentanaCliente1.txt_turno.setText("Turno rival");
				// recibo jugada del rival
				tableroRival = (Tablero) ois.readObject();
				Cliente.comprobarTablero(misBarcos, tableroRival);
				Cliente.actualizarMisBarcos(misBarcos);
				// mando respuesta al rival
				if (i == 3) {
					tableroRival.setWin(true);
					oos.writeObject(tableroRival);
					VentanaPerder vp = new VentanaPerder();
					vc1.setVisible(true);
					vp.setVisible(true);
					return;
				} else {
					oos.writeObject(tableroRival);
					ok = false;
				}

			}
			oos.close();
			ois.close();
			c1.close();
			servidor.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	public static boolean comprobarMiTablero(Tablero barcosRival) {
		// TODO Auto-generated method stub
		// cojo las casillas de mi tablero
		char[][] arrayBarcosRival = barcosRival.getCasillas();
		// comprobamos si la posición ya ha sido seleccionada
		switch (arrayBarcosRival[barcosRival.getFila()][barcosRival.getColumna()]) {
		case (char) tocado:
			VentanaCliente1.txt_misAtaques.setText("Ya has seleccionado esta posición, prueba otra");
			return false;

		case (char) agua:
			VentanaCliente1.txt_misAtaques.setText("Ya has seleccionado esta posición, prueba otra");
			return false;

		}
		return true;
	}

	public static void comprobarTablero(Tablero misBarcos, Tablero tableroRival) {
		// recogemos los tableros
		char[][] arrayMisBarcos = misBarcos.getCasillas();
		char[][] arrayTableroRival = tableroRival.getCasillas();
		// comprobamos la posicion que nos manda el rival en nuestro tablero
		switch (arrayMisBarcos[tableroRival.getFila()][tableroRival.getColumna()]) {
		// si la posicion es barco
		case barco:
			arrayTableroRival[tableroRival.getFila()][tableroRival.getColumna()] = tocado;
			arrayMisBarcos[tableroRival.getFila()][tableroRival.getColumna()] = tocado;
			i++;
			misBarcos.setLog("El rival ha atacado fila " + (tableroRival.getFila()+1) + " columna "+ (tableroRival.getColumna()+1) + " con el resultado: " + tocado);
			tableroRival.setLog("Has atacado file " +(tableroRival.getFila()+1)  + " columna "+ (tableroRival.getColumna()+1) + " con el resultado: " + tocado);
			break;
		// si no hay nada rellenamos con agua
		case empty:
			arrayTableroRival[tableroRival.getFila()][tableroRival.getColumna()] = agua;
			arrayMisBarcos[tableroRival.getFila()][tableroRival.getColumna()] = agua;
			misBarcos.setLog("El rival ha atacado fila " + (tableroRival.getFila()+1)+ " columna "+ (tableroRival.getColumna()+1) + " con el resultado: " + agua);
			tableroRival.setLog("Has atacado fila " + (tableroRival.getFila()+1) + " columna "+ (tableroRival.getColumna()+1) + " con el resultado: " + agua);
			break;
		}
	}

	public static Tablero generar_tabla() {
		// array 5x5
		char[][] tablero_e = new char[5][5];
		int fila;
		int columna;

		for (int i = 0; i < 5; i++) {
			// generamos la posición dónde se colocarán los barcos
			fila = (int) (Math.random() * (5 - 1));
			columna = (int) (Math.random() * (5 - 1));
			// comprobamos si la casilla ya tiene un barco
			if (tablero_e[fila][columna] == barco) {
				i--;
			}
			// añadimos el barco
			tablero_e[fila][columna] = barco;
		}
		// rellenamos el resto del tablero
		for (int j = 0; j < tablero_e.length; j++) {
			for (int k = 0; k < tablero_e.length; k++) {
				// si la posición no tiene barco, rellena con agua
				if (tablero_e[j][k] != barco) {
					tablero_e[j][k] = empty;
				}
			}
		}

		Tablero mitablero = new Tablero(tablero_e);
		return mitablero;

	}

	public static Tablero tabla_rival() {
		char[][] tablero_e = new char[5][5];
		// rellenamos el tablero rival con todo agua
		for (int j = 0; j < tablero_e.length; j++) {
			for (int k = 0; k < tablero_e.length; k++) {
				tablero_e[j][k] = empty;
			}
		}
		Tablero tablero_r = new Tablero(tablero_e);
		return tablero_r;
	}

	public static String mostrarTablero(Tablero t) {
		// hacemos un toString 
		return t.toString();
	}
	
	public static void actualizarMisBarcos(Tablero misBarcos)
	{
		VentanaCliente1.misBarcos.setText("<html><pre>" + Cliente.mostrarTablero(misBarcos) + "</pre></html>");
		VentanaCliente1.txt_ataquesRival.setText(misBarcos.getLog());
	}
	
	public static void actualizarBarcosRival(Tablero barcosRival)
	{
		VentanaCliente1.barcosRival.setText("<html><pre>" + Cliente.mostrarTablero(barcosRival) + "</pre></html>");
		VentanaCliente1.txt_misAtaques.setText(barcosRival.getLog());

	}
	
	private static void limpiar() {
		VentanaCliente1.edt_columna.setText(null);
		VentanaCliente1.edt_fila.setText(null);
	}
}
