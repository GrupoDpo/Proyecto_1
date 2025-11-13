package tiquete;

import Evento.Evento;
import Persistencia.PersistenciaUsuarios;
import usuario.Administrador;

public class TiqueteSimple extends Tiquete {
	
	
	
	public TiqueteSimple(String tipoTiquete, double recargo, double d, String identificador, String fechaExpiracion, 
			double precio, String nombre, boolean transferido, boolean anulado, Evento evento) {
		super(tipoTiquete,recargo,identificador,fechaExpiracion,precio,nombre, transferido, anulado,evento);
		
	}

	

	

	public double calcularPrecio() {
	    double precioBase = this.precio;

	    PersistenciaUsuarios persistencia = new PersistenciaUsuarios();
	    Administrador admin = persistencia.recuperarAdministrador();

	    double precioConRecargo = precioBase + (precioBase * (recargo / 100));

	    if (admin != null) {
	        double cobroEmision = admin.getCobroEmision();
	        precioConRecargo += (precioBase * (cobroEmision / 100));
	    }

	    return precioConRecargo;
	}





	@Override
	public String getTipoTiquete() {
		return "SIMPLE";
	}
	
	
	
	
	

}
