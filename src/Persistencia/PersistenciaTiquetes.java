package Persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.List;

import tiquete.Tiquete;

public class PersistenciaTiquetes implements IPersistencia<Tiquete> {

	private static final String ARCHIVO_TIQUETES = "data/Tiquetes.dat";

    @SuppressWarnings("unchecked")
    public List<Tiquete> cargarTodos() {
        File archivo = new File(ARCHIVO_TIQUETES);

        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Tiquete>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void guardarTodos(List<Tiquete> eventos) {
        File carpeta = new File("data");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_TIQUETES))) {
            oos.writeObject(eventos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    
	
	

	
    }