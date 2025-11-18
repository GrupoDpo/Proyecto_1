package tiquete;

import Evento.Evento;
import Evento.Localidad;

public class TiqueteSimple extends Tiquete {
	
	
	
	public TiqueteSimple(String tipoTiquete, String identificador, String fechaExpiracion, 
			double precio, String nombre, boolean transferido, boolean anulado, Evento eventoAsociado, double recargo,Localidad localidadAsociada) {
		super(tipoTiquete,identificador, fechaExpiracion, precio, nombre, transferido, anulado,eventoAsociado, recargo, localidadAsociada);
		
	}

	

	

	public double calcularPrecio(double cobroEmision) {
	    double precioBase = this.precio;

	    double precioConRecargo = precioBase + (precioBase * (recargo / 100));

	    precioConRecargo += (precioBase * (cobroEmision / 100));

	    return precioConRecargo;
	}





	@Override
	public String getTipoTiquete() {
		return "SIMPLE";
	}
	
	
	
	
	

}
