package usuario;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import tiquete.Tiquete;

public interface IDuenoTiquetes {
	Collection<Tiquete> getTiquetes();
	void agregarTiquete(Tiquete tiquete);
	void eliminarTiquete(Tiquete tiquete);
	public double getSaldo();
	public void actualizarSaldo(double precioNuevo);
	public List<HashMap<Tiquete, String>> getOfertas();
	void eliminarOferta(Tiquete tiqueteOferta);

}
