package Persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import Finanzas.marketPlaceReventas;

public class marketplacePersistencia implements IPersistencia<marketPlaceReventas> {

	private static final String ARCHIVO_OFERTAS = "ofertas.dat";

    @SuppressWarnings("unchecked")
    public List<marketPlaceReventas> cargarTodos() {
        File archivo = new File(ARCHIVO_OFERTAS);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<marketPlaceReventas>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
           
            return new ArrayList<>();
        }
    }
    
    
    @Override
    public void guardarTodos(List<marketPlaceReventas> ofertas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_OFERTAS))) {
            oos.writeObject(ofertas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
}
