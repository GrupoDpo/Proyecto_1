package tiquete;

import java.util.ArrayList;
import java.util.Collection;

import Evento.Evento;

public class TiqueteMultiple extends Tiquete{
	private ArrayList<TiqueteSimple> tiquetesInternos;
	private ArrayList<Evento> eventosAsociados;

	public TiqueteMultiple(String tipoTiquete, double cargoPorcentual, double cuotaAdicional, String identificador,
			String fechaExpiracion, int precio, String nombre, boolean transferido, boolean anulado) {
		super(tipoTiquete, cargoPorcentual, cuotaAdicional, identificador, fechaExpiracion, precio, nombre, transferido, anulado);
		this.tiquetesInternos = new ArrayList<>();
	}

	

	@Override
	//Se permite que tenga un descuento o un precio especial sobre la suma
	public double calcularPrecio() {
		int cantidadTiquetes = 0;
		for(Tiquete t:tiquetesInternos) {
			cantidadTiquetes += t.calcularPrecio();
		}
		double precioAgrupado = cantidadTiquetes * 0.9;  
		return  (precioAgrupado + (precioAgrupado * (cargoPorcentual)/100) + cuotaAdicional);
	}
	
	public Collection<TiqueteSimple> getTiquetes( )
    {
        return tiquetesInternos;
	
	
    }
	
	public void agregarTiquete(TiqueteSimple tiquete) {
		tiquetesInternos.add(tiquete);
    }
	
	
	 public boolean puedeTransferirseCompleto(String fechaActual) {
	        for (Tiquete t : tiquetesInternos) {
	            if (!t.esVigente(fechaActual)|| t.isTransferido()) {
	                return false;
	            }
	        }
	        return true;
	    }



	 public ArrayList<Evento> getEventosAsociados() {
		return eventosAsociados;
	 }
	 
	 public int getCantidadEventos() {
	        return eventosAsociados.size();
	    }
	 
	 public boolean esParaVariosEventos() {
	        return getCantidadEventos() > 1;
	    }

	 public void setEventosAsociados(ArrayList<Evento> eventosAsociados) {
		this.eventosAsociados = eventosAsociados;
	 }



	 @Override
	 public String getTipoTiquete() {
		return "MULTIPLE";
	 }
	

}
