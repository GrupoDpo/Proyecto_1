package usuario;


import Persistencia.TextoUtils;
import tiquete.Tiquete;

public abstract class Usuario  {
	
	private String login;
	private String password;
	private String tipoUsuario;
	
	public Usuario (String login, String password, String tipoUsuario) {
		this.login = login;
		this.password = password;
		this.tipoUsuario = tipoUsuario;
	}
	
	public String getLogin() {
		return this.login;
	}
	
	public boolean IsPasswordTrue(String passwordTry) {
		if (passwordTry == this.password) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public void solicitarRembolso(Tiquete tiquete) {
		
	}
	
	public abstract String getTipoUsuario();
	
	public String formatear() {
		String formatJson = String.format("  {\n    \"login\": \"%s\",\n    \"password\": \"%s\",\n    \"tipo\": \"%s\"\n  }"
				, TextoUtils.escape(this.login), TextoUtils.escape(this.password), TextoUtils.escape(this.tipoUsuario));
		
		return formatJson;
		
	}
	
	
	
	
	
	
	

}
