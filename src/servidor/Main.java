package servidor;

public class Main {

	private static AtencionCliente cliente;

	public static void main(String[] args) {
		cliente = new AtencionCliente();
		new Thread(cliente).start();
	}

	
}
