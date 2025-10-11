package tiquete;

public abstract class Tiquete {
	
	private String tipoTiquete;
	protected double cargoPorcentual;
	protected double cuotaAdicional;
	private String identificador;
	private String fechaExpiracion;
	protected double precio;
	private String nombre;
	
	public Tiquete(String tipoTiquete, double cargoPorcentual, double cuotaAdicional, String identificador, String fechaExpiracion, int precio, String nombre){
		this.tipoTiquete = tipoTiquete;
		this.cargoPorcentual = cargoPorcentual;
		this.cuotaAdicional = cuotaAdicional;
		this.identificador = identificador;
		this.fechaExpiracion = fechaExpiracion;
		this.precio = precio;
		this.nombre = nombre;
	}
	
	public String getTipoTiquete() {
        return this.tipoTiquete;
    }
	
	public String getId() {
        return this.identificador;
    }
	
	public String getNombre() {
        return this.nombre;
    }	
	
	public String getFechaExpiracion() {
        return this.fechaExpiracion;
    }
	
	public abstract double calcularPrecio();
}
