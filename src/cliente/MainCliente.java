package cliente;

import processing.core.PApplet;

public class MainCliente extends PApplet {

	Logica logica;

	public static void main(String[] args) {
		PApplet.main("cliente.MainCliente");
	}
	
	@Override
	public void settings() {
		// TODO Auto-generated method stub
		size(500, 500);
	}

	@Override
	public void setup() {
		colorMode(HSB, 360, 100, 100);
		noStroke();
		smooth();
		textAlign(CENTER);
		logica = new Logica(this);
	}

	@Override
	public void draw() {
		background(360);
		logica.pintar();
	}

}
