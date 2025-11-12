package excepciones;


public class TransferenciaNoPermitidaException extends Exception {

	private static final long serialVersionUID = 1L;

	public TransferenciaNoPermitidaException(String motivo) {
        super("Transferencia no permitida: " + motivo);
    }
}
