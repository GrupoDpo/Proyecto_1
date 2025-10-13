package usuario;

import java.util.ArrayList;
import java.util.Collection;

import tiquete.Tiquete;

public class Promotor extends Usuario implements IDuenoTiquetes {
	
	private double saldo;
	private ArrayList<Tiquete> tiquetes;

	public Promotor(String login, String password, double saldo, String tipoUsuario) {
		super(login, password, tipoUsuario);
		this.saldo = saldo;
	}
	
	public double getSaldo() {
		return this.saldo;
	}
	
	public double getGanancias() {
		return saldo;
		
	}
	
	public double getPrecio() {
		return saldo;
		
	}
	
	public double getPorcentajeVenta() {
		return saldo;
		
	}
	
	public double getPorcentajeVentaEvento() {
		return saldo;
		
	}
	
	public double getPorcentajeVentaLocalidad(String identificadorEvento, String nombreLocalidad) {
		return saldo;
		
	}
	
	public Collection<Tiquete> getTiquetes() {
		return this.tiquetes;
	}
	
	public void agregarTiquete(Tiquete tiquete) {
        tiquetes.add(tiquete);
    }
	public void eliminarTiquete(Tiquete tiquete) {
        tiquetes.remove(tiquete);
    }

	@Override
	public String getTipoUsuario() {
		return "PROMOTOR";
	}
	
	
	@Override
	public void actualizarSaldo(double precioNuevo) {
		this.saldo = precioNuevo;
		
	}

}
