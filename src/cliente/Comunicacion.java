package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;

public class Comunicacion extends Observable implements Runnable {
	private Socket s;
	private boolean conectado;

	public Comunicacion() {

		try {
			s = new Socket(InetAddress.getByName("127.0.0.1"), 5000);
			conectado = true;
			// enviar();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		while (conectado) {
			recibirMensaje();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void recibirMensaje() {
		DataInputStream entrada = null;
		try {
			entrada = new DataInputStream(s.getInputStream());
			String mensaje = entrada.readUTF();

			setChanged();
			notifyObservers(mensaje);
			clearChanged();

		} catch (IOException e) {
			try {
				if (entrada != null) {
					entrada.close();
				}
				s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			s = null;
			conectado = false;
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void enviarMensaje(String mensaje) {
		DataOutputStream salida = null;

		try {
			salida = new DataOutputStream(s.getOutputStream());
			salida.writeUTF(mensaje);
			System.out.println("Se envió el mensaje: " + mensaje);
		} catch (IOException e) {
			try {
				if (salida != null) {
					salida.close();
				}
				s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			s = null;
			conectado = false;
			e.printStackTrace();
		}
	}

}