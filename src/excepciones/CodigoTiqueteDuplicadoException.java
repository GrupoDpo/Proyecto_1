package excepciones;



public class CodigoTiqueteDuplicadoException extends Exception {
    private static final long serialVersionUID = 1L;

	public CodigoTiqueteDuplicadoException(String codigo) {
        super("Ya existe un tiquete con el código '" + codigo + "'.");
    }
}
