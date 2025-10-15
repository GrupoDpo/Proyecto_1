package Persistencia;

import usuario.Usuario;

public interface IPersistenciaUsuarios {
	
	public void cargarUsuario(Usuario newUsuario);
	public void crearArchivo();
	public void salvarUsuario(String jsonFormatted);
	

}
