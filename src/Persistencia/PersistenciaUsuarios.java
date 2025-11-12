package Persistencia;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import usuario.Administrador;
import usuario.Usuario;

public class PersistenciaUsuarios implements IPersistencia<Usuario> {
	
	private static final String RUTA = "data/usuarios.json";
    
	
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
	
	public boolean existeUsuario(String login, String password) {
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
        
        int start = indicePassword + patronPassword.length();
        int end = json.indexOf("\"", start);
        
        String passwordAct = json.substring(start, end);
        
        if (passwordAct.equals(password)) {
        	
        	condicion = true;
        } else {
        	return condicion;
        }
        
        return condicion;
    }
		
	
	public void salvar(String jsonFormatted) {
		try (BufferedWriter writer = Files.newBufferedWriter(Path.of(RUTA))) {
            writer.write(jsonFormatted);
            System.out.println("\n Usuario registrado correctamente en " + RUTA);
        } catch (IOException e) {
            System.out.println(" Error al guardar el archivo: " + e.getMessage());
        }
    }
	
	
	private static final String ARCHIVO_USUARIOS = "usuarios.dat";

    @SuppressWarnings("unchecked")
    public List<Usuario> cargarTodos() {
        File archivo = new File(ARCHIVO_USUARIOS);

        // Si el archivo no existe, devolvemos lista vacía
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (List<Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Si algo sale mal, devolvemos lista vacía para no romper la app
            return new ArrayList<>();
        }
    }

    public void guardarTodos(List<Usuario> usuarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
    public Usuario buscarUsuario(String login) {
		List<Usuario> listaUsuarios = cargarTodos();
		
		for (Usuario user: listaUsuarios) {
			String x = user.getLogin();
			if (x.equals(login)) {
				return user;
			}
			
		}
		
		return null;
    	
    }
    
    public Administrador recuperarAdministrador() {
        List<Usuario> listaUsuarios = cargarTodos();

        for (Usuario user : listaUsuarios) {
            if (user instanceof Administrador) {
                return (Administrador) user; 
            }
        }

        return null; 
    }


		

	

	
	

}
