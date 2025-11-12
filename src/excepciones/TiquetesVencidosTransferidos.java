package excepciones;

public class TiquetesVencidosTransferidos extends Exception {

    private static final long serialVersionUID = 1L;

	public TiquetesVencidosTransferidos(String mensaje) {
        super(mensaje);
    }
}
