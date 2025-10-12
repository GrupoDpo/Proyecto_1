package usuario;

import java.util.ArrayList;
import java.util.Collection;

import tiquete.Tiquete;

public class Cliente extends Usuario {
	
	private double saldo;
	private ArrayList<Tiquete> tiquetes;

	public Cliente(String login, String password, double saldo) {
		super(login, password);
		this.saldo = saldo;
		this.tiquetes = new ArrayList<Tiquete>();
		
	}
	
	public double getSaldo() {
		return this.saldo;
	}
	
	public Collection<Tiquete> getTiquetes() {
		return this.tiquetes;
	}

	@Override
	public String getTipoUsuario() {
		return "CLIENTE";
	}
	
	
	
	

}
