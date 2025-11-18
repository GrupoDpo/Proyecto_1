package usuario;


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
	
	public boolean IsPasswordTrue(String pass) {
	    return this.password.equals(pass); 
	}
	
	public void solicitarRembolso(Tiquete tiquete) {
		
	}
	
	public abstract String getTipoUsuario();
	
	public String getPasswordInternal() {
	    return this.password;
	}
	
	
	
	
	
	
	

}
