package Persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.List;

import Evento.Evento;

public class PersistenciaEventos implements IPersistencia<Evento>  {



	private static final String ARCHIVO_EVENTOS = "data/eventos.dat";

    @SuppressWarnings("unchecked")
    public List<Evento> cargarTodos() {
        File archivo = new File(ARCHIVO_EVENTOS);

        // Si el archivo no existe, devolvemos lista vacía
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Evento>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Si algo sale mal, devolvemos lista vacía para no romper la app
            return new ArrayList<>();
        }
    }

    public void guardarTodos(List<Evento> eventos) {
        // Nos aseguramos de que exista la carpeta "data"
        File carpeta = new File("data");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_EVENTOS))) {
            oos.writeObject(eventos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    // Ejemplo de búsqueda simple (puedes ajustarla a tu modelo)
    public Evento buscarEventoPorEntrada(String entrada) {
        List<Evento> listaEventos = cargarTodos();

        for (Evento ev : listaEventos) {
            if (ev.getEntrada().equals(entrada)) { 
                return ev;
            }
        }
        return null;
    }

}
