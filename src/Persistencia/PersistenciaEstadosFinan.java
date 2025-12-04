package Persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.List;

import Finanzas.EstadosFinancieros;

public class PersistenciaEstadosFinan  {

	private static final String ARCHIVO_ESTADOS_FINANCIEROS = "data/EstadosFinancieros.dat";

    @SuppressWarnings("unchecked")
    public List<EstadosFinancieros> cargarTodos() {
        File archivo = new File(ARCHIVO_ESTADOS_FINANCIEROS);

        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<EstadosFinancieros>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void guardarTodos(List<EstadosFinancieros> eventos) {
        File carpeta = new File("data");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_ESTADOS_FINANCIEROS))) {
            oos.writeObject(eventos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    
	
	

	
    }