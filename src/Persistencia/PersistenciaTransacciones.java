package Persistencia;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import Finanzas.Transaccion;

public class PersistenciaTransacciones implements IPersistencia<Transaccion>  {
	
	private static final String RUTA = "data/transferencias.json";

	@Override
	public void crearArchivo() {
		// TODO Auto-generated method stub
		try {
            Files.createDirectories(Path.of("data"));
        } catch (IOException e) {
            System.out.println("Error al crear carpeta data: " + e.getMessage());
        }
		
		
		
	}

	@Override
	public void cargar(Transaccion newTransaccion) {
		// TODO Auto-generated method stub
		String format = newTransaccion.formatear();
		String info = "";
		try {
			if (Files.exists(Path.of(RUTA))) {
				info = Files.readString(Path.of(RUTA));
			}
		} catch (IOException e) {
			System.out.println("ERROR: no se puede leer el archivo");
		}
		
		String newJson;
		if (info.isBlank()) {
			newJson = "[\n" + format + "\n]";
		} else {
			info = info.trim();
			
			if (info.endsWith("]")) {
				info = info.substring(0, info.length() - 1);
				if (info.contains("{")) {
					newJson = info +",\n" + format + "\n]";
				} else {
					newJson = "[\n" + format + "\n]";
				}
				
			} else { 
					newJson = "[\n" + format + "\n]";
				}
			
			}
			salvar(newJson);
		
		}

	@Override
	
	public void salvar(String jsonFormatted) {
		try (BufferedWriter writer = Files.newBufferedWriter(Path.of(RUTA))) {
            writer.write(jsonFormatted);
            System.out.println("\n Usuario registrado correctamente en " + RUTA);
        } catch (IOException e) {
            System.out.println(" Error al guardar el archivo: " + e.getMessage());
        }
    }
	

}
