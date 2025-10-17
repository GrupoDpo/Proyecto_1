package Persistencia;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import Finanzas.EstadosFinancieros;

public class PersistenciaEstadosFinan implements IPersistencia<EstadosFinancieros>  {

	private static final String RUTA = "data/estadosFinancieros.json";

	@Override
	public void crearArchivo() {
		 try {
	            Files.createDirectories(Path.of("data"));
	        } catch (IOException e) {
	            System.out.println("Error al crear carpeta data: " + e.getMessage());
	        }
		
	}
	
	@Override
	public void cargar(EstadosFinancieros newObjeto) {
		String format = newObjeto.formatear();
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
            System.out.println("\n Tiquete registrado correctamente en " + RUTA);
        } catch (IOException e) {
            System.out.println(" Error al guardar el archivo: " + e.getMessage());
        }
    }

	


}
