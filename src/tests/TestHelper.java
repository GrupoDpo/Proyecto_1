package tests;



import java.util.*;
import usuario.*;
import Evento.*;
import tiquete.*;
import Persistencia.SistemaPersistencia;

public class TestHelper {
    
    // Crear un sistema limpio para cada prueba
    public static SistemaPersistencia crearSistemaLimpio() {
        SistemaPersistencia sistema = new SistemaPersistencia();
        // NO cargar datos existentes
        return sistema;
    }
    
    // Crear un cliente de prueba
    public static Cliente crearCliente(String login, String password, double saldo) {
        return new Cliente(login, password, saldo, "CLIENTE");
    }
    
    // Crear un organizador de prueba
    public static Organizador crearOrganizador(String login, String password, double saldo) {
        return new Organizador(login, password, saldo, "ORGANIZADOR");
    }
    
    // Crear un promotor de prueba
    public static Promotor crearPromotor(String login, String password, double saldo) {
        return new Promotor(login, password, saldo, "PROMOTOR");
    }
    
    // Crear un venue de prueba
    public static Venue crearVenue(String ubicacion, int capacidad) {
        return new Venue(ubicacion, capacidad, true);
    }
    
    // Crear un evento de prueba
    public static Evento crearEvento(String nombre, String fecha, Venue venue, String loginOrganizador) {
        return new Evento(nombre, fecha, "20:00", new HashMap<>(), venue, loginOrganizador);
    }
    
    // Crear un tiquete simple de prueba
    public static TiqueteSimple crearTiqueteSimple(
            String id, 
            double precio, 
            Evento evento,
            Localidad localidad) {
        return new TiqueteSimple(
            "SIMPLE",
            id,
            "2026-12-31",
            precio,
            "Tiquete Test",
            false,
            false,
            evento,
            0,
            localidad
        );
    }
    
    // Crear una localidad de prueba
    public static Localidad crearLocalidad(String nombre, double precio, int capacidad) {
        return new Localidad(nombre, precio, capacidad, 0);
    }
}