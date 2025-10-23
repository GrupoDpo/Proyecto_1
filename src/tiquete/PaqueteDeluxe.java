package tiquete;

import java.util.ArrayList;
import java.util.Collection;



public class PaqueteDeluxe {
	
	private String mercanciaYBeneficios;
	private double precioPaquete;
	private ArrayList<Tiquete> tiquetes;
	private ArrayList<Tiquete> tiquetesAdicionales;
	private boolean anulado;

	
	public PaqueteDeluxe(String mercanciaYBeneficios, boolean anulado) {
		this.setMercanciaYBeneficios(mercanciaYBeneficios);
		this.setPrecioPaquete(precioPaquete);
		tiquetes = new ArrayList<Tiquete>();
		tiquetesAdicionales =  new ArrayList<Tiquete>();
		this.anulado = false;
		
	}
	
	public void agregarTiquete(Tiquete tiquete) {
        tiquetesAdicionales.add(tiquete);
    }


	public String getMercanciaYBeneficios() {
		return this.mercanciaYBeneficios;
	}


	public void setMercanciaYBeneficios(String mercanciaYBeneficios) {
		this.mercanciaYBeneficios = mercanciaYBeneficios;
	}
	
	
	public Collection<Tiquete> getTiquetes( )
    {
        return tiquetes;
    }
	public Collection<Tiquete> getTiquetesAdicionales( )
    {
        return tiquetesAdicionales;
    }

	public double getPrecioPaquete() {
		return this.precioPaquete;
	}

	public void setPrecioPaquete(double precioPaquete) {
		this.precioPaquete = precioPaquete;
	}
	
	
	
}
