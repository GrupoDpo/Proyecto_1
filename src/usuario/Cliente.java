package usuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import tiquete.Tiquete;

public class Cliente extends Usuario implements IDuenoTiquetes  {
	
	private double saldo;
	private ArrayList<Tiquete> tiquetes;
	private List<HashMap<Tiquete, String>> listaOfertas;
	private List<String> idsTiquetes = new ArrayList<>();
	public Cliente(String login, String password, double saldo, String tipoUsuario) {
		super(login, password, tipoUsuario);
		this.saldo = saldo;
		this.tiquetes = new ArrayList<Tiquete>();
		this.idsTiquetes = new ArrayList<String>();
		
	}
	
	public double getSaldo() {
		return this.saldo;
	}
	
	public Collection<Tiquete> getTiquetes() {
		return this.tiquetes;
	}
	public void agregarTiquete(Tiquete t) {
        tiquetes.add(t);
        agregarIdTiquete(t.getId());
    }

    public void eliminarTiquete(Tiquete t) {
        tiquetes.remove(t);
        eliminarIdTiquete(t.getId());
    }

    @Override
    public List<String> getIdsTiquetes() {
        return idsTiquetes;
    }

    @Override
    public void setIdsTiquetes(List<String> ids) {
        this.idsTiquetes = ids;
    }

    @Override
    public void agregarIdTiquete(String id) {
        if (!idsTiquetes.contains(id)) idsTiquetes.add(id);
    }

    @Override
    public void eliminarIdTiquete(String id) {
        idsTiquetes.remove(id);
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
