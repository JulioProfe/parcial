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



	public Logica(PApplet app) {
		this.app = app;

		com = new Comunicacion();
		new Thread(com).start();
		com.addObserver(this);

		app.textSize(20);
		h = (int) app.random(0,360);
	}

	public void pintar() {
		app.fill(h, 100, 100);
		app.ellipse(app.width/2, 50, 50, 50);
		app.fill(0);
		app.textSize(20);
		app.text("ID " + id, app.width / 2, 55);
		app.fill(h, 100, 100);
		app.noStroke();
		app.rect(0, app.height-50, app.width, 100);
		app.fill(0);
		app.text("conectados: " + conectados, app.width/2, app.height-20);
		
		app.text("Enviar mensaje a: ", 150, 100);
		
		
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
			}

			if (mensaje.contains("mas")) {
				conectados++;
			}
			
			if (mensaje.contains("llego")) {
				app.fill(h, 100, 100);
				app.noStroke();
				app.rect(100, 130, 100, 100);
				app.fill(0);
				app.textSize(20);
				app.text(id, 110, 140);
			}


		}

	}

	public void validar() {
		// TODO Auto-generated method stub
		if (app.mouseX >= 100 && app.mouseX <= 200 && app.mouseY >= 130 && app.mouseY <= 230) {
			com.enviarMensaje("envio");
		}
		
		
	}

}