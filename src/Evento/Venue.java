package Evento;

public class Venue {
	private String ubicacion;
	private int capacidadMax;
	private boolean aprobado;
	
	public Venue(String ubicacion, int capacidadMax) {
		this.ubicacion = ubicacion;
	    this.capacidadMax = capacidadMax;
	    this.aprobado = false;
		
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public int getCapacidadMax() {
		return capacidadMax;
	}

	public void setCapacidadMax(int capacidadMax) {
		this.capacidadMax = capacidadMax;
	}


	public void aprobar() {
		this.setAprobado(true);
	}

	public boolean isAprobado() {
		return aprobado;
	}

	public void setAprobado(boolean aprobado) {
		this.aprobado = aprobado;
	}

}
