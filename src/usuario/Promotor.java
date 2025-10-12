package usuario;

import java.util.ArrayList;
import java.util.Collection;

import tiquete.Tiquete;

public class Promotor extends Usuario {
	
	private double saldo;
	private ArrayList<Tiquete> tiquetes;

	public Promotor(String login, String password, double saldo) {
		super(login, password);
		this.saldo = saldo;
	}
	
	public double getSaldo() {
		return this.saldo;
	}
	
	public double getGanancias() {
		
	}
	
	public double getPrecio() {
		
	}
	
	public double getPorcentajeVenta() {
		
	}
	
	public double getPorcentajeVentaEvento() {
		
	}
	
	public double getPorcentajeVentaLocalidad(String identificadorEvento, String nombreLocalidad) {
		
	}
	
	public Collection<Tiquete> getTiquetes() {
		return this.tiquetes;
	}

	@Override
	public String getTipoUsuario() {
		return "PROMOTOR";
	}

}
