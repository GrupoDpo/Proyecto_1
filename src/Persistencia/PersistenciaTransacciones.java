package Persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.List;

import Finanzas.Transaccion;

public class PersistenciaTransacciones implements IPersistencia<Transaccion>  {

	private static final String ARCHIVO_TRANSACCION = "data/transaccion.dat";

    @SuppressWarnings("unchecked")
    public List<Transaccion> cargarTodos() {
        File archivo = new File(ARCHIVO_TRANSACCION);

        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Transaccion>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void guardarTodos(List<Transaccion> eventos) {
        File carpeta = new File("data");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_TRANSACCION))) {
            oos.writeObject(eventos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    
	
	

	
    }
	

