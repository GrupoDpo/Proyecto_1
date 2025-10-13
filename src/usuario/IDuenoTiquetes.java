package usuario;

import java.util.Collection;

import tiquete.Tiquete;

public interface IDuenoTiquetes {
	Collection<Tiquete> getTiquetes();
	void agregarTiquete(Tiquete tiquete);
	void eliminarTiquete(Tiquete tiquete);
	public double getSaldo();
	public void actualizarSaldo(double precioNuevo);

}
