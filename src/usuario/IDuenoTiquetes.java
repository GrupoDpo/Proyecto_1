package usuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tiquete.Tiquete;

public interface IDuenoTiquetes {
	Collection<Tiquete> getTiquetes();
	void agregarTiquete(Tiquete tiquete);
	void eliminarTiquete(Tiquete tiquete);
	public double getSaldo();
	public void actualizarSaldo(double precioNuevo);
	
	
	List<HashMap<Tiquete, String>> getListaOfertas();

    default void eliminarOferta(Tiquete tiqueteOferta) {
        List<HashMap<Tiquete, String>> listaOfertas = getListaOfertas();
        List<HashMap<Tiquete, String>> mapasAEliminar = new ArrayList<>();

        for (HashMap<Tiquete, String> T : listaOfertas) {
            for (Map.Entry<Tiquete, String> entry : T.entrySet()) {
                if (entry.getKey().getId().equals(tiqueteOferta.getId())) {
                    mapasAEliminar.add(T);
                    break;
                }
            }
        }

        listaOfertas.removeAll(mapasAEliminar);
    }

}
