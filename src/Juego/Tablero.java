package Juego;
import java.io.Serializable;
import java.util.Arrays;

public class Tablero implements Serializable{
	
	private char[][] casillas;
	private int fila;
	private int columna;
	private boolean win;
	
	public Tablero(char[][] casillas) {
		super();
		this.casillas = casillas;
		this.fila=0;
		this.columna=0;
		this.win=false;
	}
	
	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	public int getFila() {
		return fila;
	}

	public void setFila(int fila) {
		this.fila = fila;
	}

	public int getColumna() {
		return columna;
	}

	public void setColumna(int columna) {
		this.columna = columna;
	}

	public char[][] getCasillas() {
		return casillas;
	}

	public void setCasillas(char[][] casillas) {
		this.casillas = casillas;
	}

	@Override
	public String toString() {
		return Arrays.deepToString(this.casillas).replace("], ", "]\n").replace("[[", "[").replace("]]", "]");
	}
	
	
}
