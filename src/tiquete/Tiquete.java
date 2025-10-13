package tiquete;

import java.time.LocalDate;

public abstract class Tiquete {
	
	private String tipoTiquete;
	protected double cargoPorcentual;
	protected double cuotaAdicional;
	private String identificador;
	private String fechaExpiracion;
	protected double precio;
	private String nombre;
	private boolean transferido;
	
	public Tiquete(String tipoTiquete, double cargoPorcentual, double cuotaAdicional, String identificador, String fechaExpiracion, 
			int precio, String nombre, boolean transferido){
		this.tipoTiquete = tipoTiquete;
		this.cargoPorcentual = cargoPorcentual;
		this.cuotaAdicional = cuotaAdicional;
		this.identificador = identificador;
		this.fechaExpiracion = fechaExpiracion;
		this.precio = precio;
		this.nombre = nombre;
		this.transferido = true;
	}
	
	public String getTipoTiquete() {
        return this.tipoTiquete;
    }
	
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
}
