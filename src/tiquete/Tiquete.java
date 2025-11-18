package tiquete;

import java.time.LocalDate;

import Evento.Evento;
import Evento.Localidad;




public abstract class Tiquete   {
	
	private String tipoTiquete;
	protected double recargo;
	private String identificador;
	private String fechaExpiracion;
	protected double precio;
	private String nombre;
	private boolean transferido;
	private boolean anulado;
	private Evento eventoAsociado;
	private Localidad localidadAsociada;
	

	public Tiquete(String tipoTiquete, String identificador, String fechaExpiracion, 
			double precio, String nombre, boolean transferido, boolean anulado, Evento eventoAsociado, double recargo,Localidad localidadAsociada){
		this.tipoTiquete = tipoTiquete;
		this.recargo = recargo;
		this.identificador = identificador;
		this.fechaExpiracion = fechaExpiracion;
		this.precio = precio;
		this.nombre = nombre;
		this.transferido = transferido;
		this.anulado = anulado;
		this.eventoAsociado = eventoAsociado;
		this.setLocalidadAsociada(localidadAsociada);
	}
	
	public Evento getEventoAsociado() {
		return this.eventoAsociado;

	}
	
	public boolean isAnulado() {
	    return anulado;
	}

	public void setAnulado(boolean anulado) {
	    this.anulado = anulado;
	}
	
	public abstract String getTipoTiquete();
	
	public String getId() {
        return this.identificador;
    }
	
	public String getNombre() {
        return this.nombre;
    }	
	
	public String getFechaExpiracion() {
        return this.fechaExpiracion;
    }
	
	
	public boolean esVigente(String fechaActual) {
        LocalDate fechaExp = LocalDate.parse(getFechaExpiracion());
        LocalDate fechaAct = LocalDate.parse(fechaActual);

        return!fechaExp.isBefore(fechaAct);
           
        
	}
	
	public abstract double calcularPrecio(double cobroEmision);

	public boolean isTransferido() {
		
		return transferido;
	}

	public void setTransferido(boolean isTransferido) {
		this.transferido = isTransferido;
	}
	
	
	

	public Evento getEvento() {
		return this.eventoAsociado;
	}

	public double getRecargo() {
		return this.recargo;
	}

	public void setEventoAsociado(Evento e) {
		this.eventoAsociado = e;
		
	}

	public Localidad getLocalidadAsociada() {
		return localidadAsociada;
	}

	public void setLocalidadAsociada(Localidad localidadAsociada) {
		this.localidadAsociada = localidadAsociada;
	}

	public double getPrecioBaseSinCalcular() {
		return precio;
	}
}
