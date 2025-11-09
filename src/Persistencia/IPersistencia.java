package Persistencia;

import java.util.List;

public interface IPersistencia<T> {
	
    List<T> cargarTodos();

    /**
     * Guarda la lista completa de objetos T en el archivo correspondiente.
     */
    void guardarTodos(List<T> elementos);

    /**
     * Agrega un nuevo elemento: por defecto implementa
     * leer → agregar → guardar.
     */
    default void agregar(T nuevo) {
        List<T> elementos = cargarTodos();
        elementos.add(nuevo);
        guardarTodos(elementos);
    }
}
