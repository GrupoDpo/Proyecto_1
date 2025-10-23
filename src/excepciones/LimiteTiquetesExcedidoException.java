package excepciones;

public class LimiteTiquetesExcedidoException extends BoletaMasterException {
	public LimiteTiquetesExcedidoException(int maxPermitidos, int solicitados) {
	      super("Límite de tiquetes por transacción excedido. Máximo: " + maxPermitidos + ", solicitados: " + solicitados);
	    }
	}


