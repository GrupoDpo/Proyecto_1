package tiquete;

import java.util.ArrayList;

import Evento.Evento;
import Evento.Localidad;

public class TiqueteMultiple extends Tiquete{
	private ArrayList<TiqueteSimple> tiquetesInternos;
	private ArrayList<Evento> eventosAsociados;

	public TiqueteMultiple(String tipoTiquete, String identificador, String fechaExpiracion, 
			double precio, String nombre, boolean transferido, boolean anulado, Evento eventoAsociado, double recargo,Localidad localidadAsociada,ArrayList<TiqueteSimple> tiquetesInternos2) {
		super(tipoTiquete,identificador, fechaExpiracion, precio, nombre, transferido, anulado,eventoAsociado, recargo, localidadAsociada);
		this.tiquetesInternos = tiquetesInternos2;
	}

	

	@Override
	//Se permite que tenga un descuento o un precio especial sobre la suma
	public double calcularPrecio(double cobroEmision) {
	    double precioTotal = 0;

	    for (Tiquete t : tiquetesInternos) {
	        precioTotal += t.calcularPrecio(cobroEmision);
	    }

	    double precioConDescuento = precioTotal * 0.9;

	    precioConDescuento += precioConDescuento * (recargo / 100);

	    precioConDescuento += precioConDescuento * (cobroEmision / 100);

	    return precioConDescuento;
	    
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



	 public ArrayList<TiqueteSimple> getTiquetes() {
		return tiquetesInternos;
	 }
	

}
