package tiquete;

import java.util.ArrayList;
import java.util.Collection;

import Evento.Evento;
import Persistencia.PersistenciaUsuarios;
import usuario.Administrador;

public class TiqueteMultiple extends Tiquete{
	private ArrayList<TiqueteSimple> tiquetesInternos;
	private ArrayList<Evento> eventosAsociados;

	public TiqueteMultiple(String tipoTiquete, double recargo, String identificador, String fechaExpiracion, 
			double precio, String nombre, boolean transferido, boolean anulado, Evento evento) {
		super(tipoTiquete, recargo, identificador, fechaExpiracion, precio, nombre, transferido, anulado,evento);
		this.tiquetesInternos = new ArrayList<>();
	}

	

	@Override
	//Se permite que tenga un descuento o un precio especial sobre la suma
	public double calcularPrecio() {
		double precioTotal = 0;

	    for (Tiquete t : tiquetesInternos) {
	        precioTotal += t.calcularPrecio();
	    }

	    double precioConDescuento = precioTotal * 0.9;

	    double precioConRecargo = precioConDescuento + (precioConDescuento * (recargo / 100));

	    PersistenciaUsuarios persistencia = new PersistenciaUsuarios();
	    Administrador admin = persistencia.recuperarAdministrador();

	    if (admin != null) {
	        double cobroEmision = admin.getCobroEmision();
	        precioConRecargo += (precioConDescuento * (cobroEmision / 100));
	       
	    }

	    return precioConRecargo;
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
