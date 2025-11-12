package excepciones;

public class SaldoInsuficienteExeption extends Exception {
	
	private static final long serialVersionUID = 1L;

	public SaldoInsuficienteExeption (String mensaje) {
		super(mensaje);
	}

}
