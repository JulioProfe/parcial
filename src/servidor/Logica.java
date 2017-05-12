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

public class Logica implements Observer, Runnable {

	private final int PORT = 5000;

	private ServerSocket socketServidor;

	private boolean conectado;
	private boolean moviendo;

	private ArrayList<Comunicacion> clientes = new ArrayList<>();

	public Logica() {

		try {
			socketServidor = new ServerSocket(PORT);
			conectado = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		moviendo = true;
	}

	@Override
	public void run() {
		while (conectado) {
			try {

				// Esperar a que un cliente se conecte
				Socket s = socketServidor.accept();

				Comunicacion com = new Comunicacion(s, clientes.size());

				// Agregar el gestor como observador
				com.addObserver(this);

				// Comenzar el hilo de ejecución del contrlador
				new Thread(com).start();

				// Agregar a la colección de clientes
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
				//controladorCliente.enviarMensaje("color:" + (int) (Math.random() * 360));
				reenviarMensaje("mas", controladorCliente);
			}

			if (moviendo) {
				controladorCliente.enviarMensaje("muevase");
				moviendo = false;
			}
			if (mensaje.contains("acabe")) {
				if (controladorCliente.getId() == clientes.size()-1) {
					clientes.get(0).enviarMensaje("muevase");
				} else {
					clientes.get(controladorCliente.getId() + 1).enviarMensaje("muevase");
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
