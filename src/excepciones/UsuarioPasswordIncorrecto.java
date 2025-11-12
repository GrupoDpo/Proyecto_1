package excepciones;

public class UsuarioPasswordIncorrecto extends Exception {
    private static final long serialVersionUID = 1L;
	
	public UsuarioPasswordIncorrecto(String mensaje) {
		super(mensaje);
	}

}
