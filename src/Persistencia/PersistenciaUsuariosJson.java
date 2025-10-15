package Persistencia;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import usuario.Usuario;

public class PersistenciaUsuariosJson implements IPersistenciaUsuarios {
	
	private static final String RUTA = "data/usuarios.json";
    
	@Override
	public void crearArchivo() {
		 try {
	            Files.createDirectories(Path.of("data"));
	        } catch (IOException e) {
	            System.out.println("Error al crear carpeta data: " + e.getMessage());
	        }
		
	}
	
	public void cargarUsuario(Usuario newUsuario) {
		String format = newUsuario.formatear(newUsuario);
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
			salvarUsuario(newJson);
		
		}
		
	@Override
	public void salvarUsuario(String jsonFormatted) {
		try (BufferedWriter writer = Files.newBufferedWriter(Path.of(RUTA))) {
            writer.write(jsonFormatted);
            System.out.println("\n Usuario registrado correctamente en " + RUTA);
        } catch (IOException e) {
            System.out.println(" Error al guardar el archivo: " + e.getMessage());
        }
    }


		

	

	
	

}
