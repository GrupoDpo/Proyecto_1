package tiquete;

import java.util.ArrayList;
import java.util.Collection;

public class PaqueteDeluxe {
    
    private String mercanciaYBeneficios;
    private double precioPaquete;
    private ArrayList<Tiquete> tiquetes;
    private ArrayList<Tiquete> tiquetesAdicionales;
    private boolean anulado;

    public PaqueteDeluxe(String mercanciaYBeneficios, double precioPaquete) {
        this.mercanciaYBeneficios = mercanciaYBeneficios;
        this.precioPaquete = precioPaquete;
        this.tiquetes = new ArrayList<>();
        this.tiquetesAdicionales = new ArrayList<>();
        this.anulado = false;
    }

    public void agregarTiquete(Tiquete tiquete) {
        tiquetes.add(tiquete);
    }

    public void agregarTiqueteAdicional(Tiquete tiquete) {
        tiquetesAdicionales.add(tiquete);
    }

    public String getMercanciaYBeneficios() {
        return mercanciaYBeneficios;
    }

    public void setMercanciaYBeneficios(String mercanciaYBeneficios) {
        this.mercanciaYBeneficios = mercanciaYBeneficios;
    }

    public Collection<Tiquete> getTiquetes() {
        return tiquetes;
    }

    public Collection<Tiquete> getTiquetesAdicionales() {
        return tiquetesAdicionales;
    }

    public double getPrecioPaquete() {
        return precioPaquete;
    }

    public void setPrecioPaquete(double precioPaquete) {
        this.precioPaquete = precioPaquete;
    }

    public boolean isAnulado() {
        return anulado;
    }

    public void setAnulado(boolean anulado) {
        this.anulado = anulado;
    }
}

