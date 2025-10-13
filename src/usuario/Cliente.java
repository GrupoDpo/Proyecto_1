package usuario;

import java.util.ArrayList;
import java.util.Collection;

import tiquete.Tiquete;

public class Cliente extends Usuario implements IDuenoTiquetes  {
	
	private double saldo;
	private ArrayList<Tiquete> tiquetes;

	public Cliente(String login, String password, double saldo, String tipoUsuario) {
		super(login, password, tipoUsuario);
		this.saldo = saldo;
		this.tiquetes = new ArrayList<Tiquete>();
		
	}
	
	public double getSaldo() {
		return this.saldo;
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
		return "CLIENTE";
	}
	
	@Override
	public void actualizarSaldo(double precioNuevo) {
		this.saldo = precioNuevo;
		
	}
	
	
	

}
