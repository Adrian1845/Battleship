package Juego;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JOptionPane;

public class ClienteJugador {
	public static final char barco = (char) 178;
	public static final char tocado = (char) 88;
	public static final char empty = (char) 45;
	public static final char agua = (char) 48;
	public static int i = 0;

	public static void main(String[] args) {
//		mando
//		recibo
//		
//		espero
//		envio
		final int PUERTO = 6001;
		try {
			String hostRemoto = "192.168.3.114";
			Socket c1 = new Socket(hostRemoto, PUERTO);

			boolean win = false;
			boolean ok = false;

			Tablero misBarcos = ClienteJugador.generar_tabla();
			Tablero barcosRival = ClienteJugador.tabla_rival();
			Tablero tableroRival;

			System.out.println(misBarcos.toString());
			System.out.println("----------------------------------------\n");
			System.out.println(barcosRival.toString());

			ObjectInputStream ois = new ObjectInputStream(c1.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(c1.getOutputStream());
			while (barcosRival.isWin() == false) {
				// recibo jugada del rival
				tableroRival = (Tablero) ois.readObject();
				ClienteJugador.comprobarTablero(misBarcos, tableroRival);
				if (i == 3) {
					tableroRival.setWin(true);
					oos.writeObject(tableroRival);
					System.out.println("Has perdido");
					return;
				} else {
					// mando respuesta al rival
					oos.writeObject(tableroRival);
					ClienteJugador.mostrarTablero(misBarcos);
					// actualizo tablero rival
					while (ok == false) {
						barcosRival.setFila(Integer.valueOf(JOptionPane.showInputDialog("Fila")) - 1);
						barcosRival.setColumna(Integer.valueOf(JOptionPane.showInputDialog("Columna")) - 1);
						ok = ClienteJugador.comprobarMiTablero(barcosRival);
					}
					System.out.println("Ataquen't!");
					// mando mi jugada
					oos.writeObject(barcosRival);

					// recibo la respuesta del rival
					barcosRival = (Tablero) ois.readObject();
					ClienteJugador.mostrarTablero(barcosRival);
					if (barcosRival.isWin()) {
						System.out.println("Has ganado");
						return;
					}
					ok = false;
				}

			}
			oos.close();
			ois.close();
			c1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static boolean comprobarMiTablero(Tablero barcosRival) {
		// TODO Auto-generated method stub
		// cojo las casillas de mi tablero
		char[][] arrayBarcosRival = barcosRival.getCasillas();
		// comprobamos si la posición ya ha sido seleccionada
		switch (arrayBarcosRival[barcosRival.getFila()][barcosRival.getColumna()]) {
		case (char) tocado:
			System.out.println("Ya has seleccionado esta posición, prueba otra");
			return false;

		case (char) agua:
			System.out.println("Ya has seleccionado esta posición, prueba otra");
			return false;

		}
		return true;
	}

	private static void comprobarTablero(Tablero misBarcos, Tablero tableroRival) {
		// recogemos los tableros
		char[][] arrayMisBarcos = misBarcos.getCasillas();
		char[][] arrayTableroRival = tableroRival.getCasillas();
		// comprobamos la posicion que nos manda el rival en nuestro tablero
		switch (arrayMisBarcos[tableroRival.getFila()][tableroRival.getColumna()]) {
		// si la posicion es barco
		case barco:
			arrayTableroRival[tableroRival.getFila()][tableroRival.getColumna()] = tocado;
			arrayMisBarcos[tableroRival.getFila()][tableroRival.getColumna()] = tocado;
			System.out.println("Tocado: " + tocado);
			i++;
			break;
		// si no hay nada rellenamos con agua
		case empty:
			arrayTableroRival[tableroRival.getFila()][tableroRival.getColumna()] = agua;
			arrayMisBarcos[tableroRival.getFila()][tableroRival.getColumna()] = agua;
			System.out.println("Agua: " + agua);
			break;

		}

	}

	public static Tablero generar_tabla() {
		// array 5x5
		char[][] tablero_e = new char[5][5];
		int fila;
		int columna;

		for (int i = 0; i < 3; i++) {
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
		System.out.println(Arrays.deepToString(tablero_e));
		System.out.println("----------------------------------------\n");
		return tablero_r;
	}

	public static void mostrarTablero(Tablero t) {
		System.out.println(t.toString());
	}
}
