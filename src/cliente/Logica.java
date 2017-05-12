package cliente;

import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class Logica implements Observer {

	private PApplet app;

	static Comunicacion com;

	private int h;
	private int id;
	private int conectados;

	//private float x;
	private float y;

	private boolean movible;

	public Logica(PApplet app) {
		this.app = app;

		com = new Comunicacion();
		new Thread(com).start();
		com.addObserver(this);

		movible = false;
		app.textSize(20);
		h = (int) app.random(0,360);
	}

	public void pintar() {
		app.fill(0);
		app.text("soy " + id + "\n y hay " + conectados, app.width / 2, app.height - 50);
		if (movible) {
			
			app.fill(h, 100, 100);
			app.ellipse(app.width/2, y, 50, 50);
			y += 2;
		}
		validar();
	}

	private void validar() {
		if (y >= app.width + 25) {
			com.enviarMensaje("acabe");
			movible = false;
			y = 0;
		}
	}

	@Override
	public void update(Observable o, Object msn) {

		if (msn instanceof String) {
			String mensaje = (String) msn;
			System.out.println("Se recibio: " + mensaje);

			if (mensaje.contains("Hola cliente")) {
				com.enviarMensaje("values");
			}

			if (mensaje.contains("id")) {
				String[] partes = mensaje.split(":");
				id = Integer.parseInt(partes[1]);
				conectados = id;
				//y = id * 100;
				System.out.println(id);
			}

			if (mensaje.contains("mas")) {
				conectados++;
			}

			if (mensaje.contains("muevase")) {
				movible = true;
			}
		}

	}

}