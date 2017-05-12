package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

public class Comunicacion extends Observable implements Runnable {

	private int id;

	private Socket s;

	private boolean conectado;

	public Comunicacion(Socket s, int id) {
		this.s = s;
		this.id = id;
		conectado = true;

	}

	@Override
	public void run() {

		// aqui le saluda al cliente
		enviarMensaje("Hola cliente");

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
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(s.getInputStream());
			String mensaje = dis.readUTF();
			System.out.println("[ControladorCliente " + id + "] Se recibio: " + mensaje + " del cliente " + id);

			setChanged();
			notifyObservers(mensaje);

		} catch (IOException e) {
			System.err.println("[ControladorCliente " + id + "] Se perdió la conexión con el cliente");
			try {
				if (dis != null) {
					dis.close();
				}
				s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			s = null;
			conectado = false;
			setChanged();
			notifyObservers("cliente desconectado");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void enviarMensaje(String mensaje) {
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF(mensaje);
			System.out.println("[ControladorCliente " + id + "] Se envió el mensaje al cliente: " + mensaje);
		} catch (IOException e) {
			System.err.println("[ControladorCliente " + id + "] Se perdió la conexión con el cliente");
			try {
				if (dos != null) {
					dos.close();
				}

				s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			s = null;
			conectado = false;
			setChanged();
			notifyObservers("cliente desconectado");
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isConectado() {
		return conectado;
	}

	public void setConectado(boolean conectado) {
		this.conectado = conectado;
	}

}