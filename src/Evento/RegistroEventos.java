package Evento;

import java.util.ArrayList;
import java.util.List;

public class RegistroEventos {
    private static List<Evento> eventosGlobales = new ArrayList<>();

    public static void agregarEventoGlobal(Evento evento) {
        eventosGlobales.add(evento);
    }

    public static List<Evento> getEventosGlobales() {
        return eventosGlobales;
    }

    public static void mostrarEventosGlobales() {
        System.out.println("=== LISTA GLOBAL DE EVENTOS ===");
        for (Evento e : eventosGlobales) {
            System.out.println(e);
        }
    }
}