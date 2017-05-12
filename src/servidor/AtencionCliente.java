package servidor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class AtencionCliente implements Observer, Runnable {

	private final int PORT = 5000;

	private ServerSocket socketServidor;
	private ControlXMLMensajes cxmlMensajes;

	private boolean conectado;

	private ArrayList<Comunicacion> clientes = new ArrayList<>();

	public AtencionCliente() {

		try {
			socketServidor = new ServerSocket(PORT);
			conectado = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public AtencionCliente(PApplet app){
		cxmlMensajes = new ControlXMLMensajes(app);
	}

	@Override
	public void run() {
		while (conectado) {
			try {
				Socket s = socketServidor.accept();

				Comunicacion com = new Comunicacion(s, clientes.size());
				com.addObserver(this);
				new Thread(com).start();

				clientes.add(com);
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void update(Observable o, Object msn) {
		Comunicacion controladorCliente = (Comunicacion) o;

		if (msn instanceof String) {
			String mensaje = (String) msn;

			System.out.println(mensaje);

			if (mensaje.equalsIgnoreCase("cliente desconectado")) {
				clientes.remove(controladorCliente);
				System.out.println("[Servidor] Tenemos: " + clientes.size() + " clientes");
			}

			if (mensaje.contains("values")) {
				controladorCliente.enviarMensaje("id:" + clientes.size());
				reenviarMensaje("mas", controladorCliente);
			}

			if (mensaje.contains("envio")) {
				if (controladorCliente.getId() == clientes.size()-1) {
					clientes.get(0).enviarMensaje("llego");
				} else {
					clientes.get(controladorCliente.getId() + 1).enviarMensaje("llego");
				}
				
				for (Iterator<Comunicacion> iterator = clientes.iterator(); iterator.hasNext();) {
					cxmlMensajes.agregarMensaje(controladorCliente.getId(), controladorCliente.getId());
				}
			}
			
		}

	}

	private void reenviarMensaje(String mensaje, Comunicacion remitente) {
		int reenvios = 0;
		for (Iterator<Comunicacion> iterator = clientes.iterator(); iterator.hasNext();) {
			Comunicacion com = (Comunicacion) iterator.next();
			if (!com.equals(remitente)) {
				com.enviarMensaje(mensaje);
				reenvios++;
			}

		}
		System.out.println("[Servidor] Se reenvió la nota a : " + reenvios + " clientes");
	}

}
