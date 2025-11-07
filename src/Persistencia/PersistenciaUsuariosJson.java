package Persistencia;
import java.io.*;
import java.nio.file.*;


import usuario.Usuario;

public class PersistenciaUsuariosJson implements IPersistencia<Usuario> {
	
	private static final String RUTA = "data/usuarios.json";
    
	@Override
	public void crearArchivo() {
		 try {
	            Files.createDirectories(Path.of("data"));
	        } catch (IOException e) {
	            System.out.println("Error al crear carpeta data: " + e.getMessage());
	        }
		
	}
	
	public void cargar(Usuario newUsuario) {
		String format = newUsuario.formatear();
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
	
	public static boolean existeUsuario(String login, String password) {
    	boolean condicion = false;
    	String ruta = "datos/usuarios.json";
        StringBuilder contenido = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return condicion;
        }

        
        String json = contenido.toString();
        String patronPassword = "\"password\":\"";
        String patronLogin = "\"login\":\"" + login + "\"";
        int indiceLogin = json.indexOf(patronLogin);
        int indicePassword = json.indexOf(indiceLogin);
        if (indicePassword == -1) {
            return condicion;
        }
        
        int start = indicePassword + "\"password\":\"".length();
        int end = json.indexOf("\"", start);
        
        String passwordAct = json.substring(start, end);
        
        if (passwordAct.equals(password)) {
        	
        	condicion = true;
        } else {
        	return condicion;
        }
        
        return condicion;
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
