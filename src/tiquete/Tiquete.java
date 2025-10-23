package tiquete;

import java.time.LocalDate;

import Persistencia.IFormateo;
import Persistencia.TextoUtils;

public abstract class Tiquete implements IFormateo  {
	
	private String tipoTiquete;
	protected double cargoPorcentual;
	protected double cuotaAdicional;
	private String identificador;
	private String fechaExpiracion;
	protected double precio;
	private String nombre;
	private boolean transferido;
	private boolean anulado;
	
	public Tiquete(String tipoTiquete, double cargoPorcentual, double cuotaAdicional, String identificador, String fechaExpiracion, 
			int precio, String nombre, boolean transferido, boolean anulado){
		this.tipoTiquete = tipoTiquete;
		this.cargoPorcentual = cargoPorcentual;
		this.cuotaAdicional = cuotaAdicional;
		this.identificador = identificador;
		this.fechaExpiracion = fechaExpiracion;
		this.precio = precio;
		this.nombre = nombre;
		this.transferido = true;
		this.anulado = false;
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
	
	
	public String formatear() {
		String formatJson = String.format("  {\n    \"nombre\": \"%s\",\n    \"Id\": \"%s\",\n    \"FechaExpiracion\": \"%s\"\n   "
				+ "\\\"precio\\\": \\\"%s\\\",\\n    \\\"TipoTiquete\\\": \\\"%s\\\",\\n    \\\"Transferido\\\": \\\"%s\\\",\\n                       }"
				, TextoUtils.escape(this.nombre), TextoUtils.escape(this.identificador), TextoUtils.escape(this.fechaExpiracion),
				 TextoUtils.escape(this.precio), TextoUtils.escape(this.tipoTiquete),TextoUtils.escape(this.transferido));
		
		return formatJson;
		
	}
}
