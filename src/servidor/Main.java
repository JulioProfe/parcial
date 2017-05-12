package servidor;

public class Main {

	private static Logica logica;

	public static void main(String[] args) {
		logica = new Logica();
		new Thread(logica).start();
	}

	
}
