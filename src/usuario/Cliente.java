package usuario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import tiquete.Tiquete;

public class Cliente extends Usuario implements IDuenoTiquetes,  Serializable   {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double saldo;
	private ArrayList<Tiquete> tiquetes;
	private List<HashMap<Tiquete, String>> listaOfertas;
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

	

	

	@Override
	public List<HashMap<Tiquete, String>> getListaOfertas() {
		return  listaOfertas;
	}
	
	
	

}
