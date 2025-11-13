package tiquete;

import java.time.LocalDate;

import Evento.Evento;


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

	
	public Tiquete(String tipoTiquete, double recargo, String identificador, String fechaExpiracion, 
			double precio, String nombre, boolean transferido, boolean anulado, Evento evento){
		this.tipoTiquete = tipoTiquete;
		this.recargo = recargo;
		this.identificador = identificador;
		this.fechaExpiracion = fechaExpiracion;
		this.precio = precio;
		this.nombre = nombre;
		this.transferido = true;
		this.anulado = false;
		this.eventoAsociado = evento;
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
	
	public abstract double calcularPrecio();

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
}
