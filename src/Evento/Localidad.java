package Evento;

public class Localidad {
	public static final int GRADA = 2;
	public static final int GENERAL = 1;
	public static final int VIP = 3;
	
	private String nombre;
	private double precio;
	private boolean numerada;
	private int capacidad;
	private int tipo;
	
	
	public Localidad(String nombre,double precio2,int capacidad, int tipo) {
		this.setNombre(nombre);
		this.precio= precio2;
		this.capacidad = capacidad;
		this.tipo = tipo;
		setNumerada(true);
		
	}

	public String getNombre() {
		return this.nombre;
	}
	public int getTipo() {
		return this.tipo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isNumerada() {
		return numerada;
	}

	public void setNumerada(boolean numerada) {
		this.numerada = numerada;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	// cuando es VIP se le suma 50000 de cargo, cuando es tipo grada se le suma 20000 y en general no se le suma nada de cargo.
	public double calcularPrecio() {
		
		double precioCalculado = precio;
	
		if(getTipo()==VIP) {
			precioCalculado +=50000;
			
		}else if(getTipo()== GRADA) {
			precioCalculado+=20000;
			
		}
		
		return precioCalculado;
	}
	

}
