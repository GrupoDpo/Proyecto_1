package interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ventanaComprarTiquete extends JFrame {
    private static final long serialVersionUID = 1L;

    // Modelos y componentes
    private JList<String> listaEventos;
    private JList<TiqueteEjemplo> listaTiquetes;
    private DefaultListModel<String> modeloEventos;
    private DefaultListModel<TiqueteEjemplo> modeloTiquetes;

    // Mapa: nombreEvento -> lista de tiquetes asociados
    private Map<String, List<TiqueteEjemplo>> tiquetesPorEvento;

    public ventanaComprarTiquete() {
        getContentPane().setLayout(null);

        // ---------- 1. Datos de ejemplo ----------
        inicializarDatosEjemplo();

        // ---------- 2. Etiquetas ----------
        JLabel lblNewLabel = new JLabel("Escoja el evento de su interés:");
        lblNewLabel.setBounds(175, 22, 250, 20);
        getContentPane().add(lblNewLabel);

        JLabel lblTiquetesAsociadosAl = new JLabel("Tiquetes asociados al evento:");
        lblTiquetesAsociadosAl.setBounds(175, 156, 250, 20);
        getContentPane().add(lblTiquetesAsociadosAl);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(233, 313, 70, 33);
        getContentPane().add(lblCantidad);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 581, 10);
        getContentPane().add(panel);

        // ---------- 3. Lista scrolleable de eventos ----------
        modeloEventos = new DefaultListModel<>();
        for (String evento : tiquetesPorEvento.keySet()) {
            modeloEventos.addElement(evento);
        }

        listaEventos = new JList<>(modeloEventos);
        listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollEventos = new JScrollPane(listaEventos);
        scrollEventos.setBounds(126, 50, 302, 75);
        getContentPane().add(scrollEventos);

        // ---------- 4. Lista de tiquetes (SIMPLE / MÚLTIPLE) ----------
        modeloTiquetes = new DefaultListModel<>();
        listaTiquetes = new JList<>(modeloTiquetes);
        listaTiquetes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollTiquetes = new JScrollPane(listaTiquetes);
        scrollTiquetes.setBounds(126, 201, 302, 75);
        getContentPane().add(scrollTiquetes);

        // ---------- 5. Spinner de cantidad (solo positivos) ----------
        SpinnerNumberModel modelCantidad = new SpinnerNumberModel(1, 1, 100, 1);
        JSpinner spinner = new JSpinner(modelCantidad);
        spinner.setBounds(301, 316, 50, 26);
        getContentPane().add(spinner);

        // ---------- 6. Botón confirmar (lógica se implementará luego) ----------
        JButton btnNewButton = new JButton("Confirmar");
        btnNewButton.setBounds(218, 387, 117, 29);
        getContentPane().add(btnNewButton);

        // ---------- 7. Listener: seleccionar evento -> cargar tiquetes ----------
        listaEventos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String eventoSeleccionado = listaEventos.getSelectedValue();
                    actualizarTiquetesAsociados(eventoSeleccionado);
                }
            }
        });

        setTitle("BOLETAMASTER: Comprar Tiquete");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(581, 471);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Clase interna de ejemplo para representar un tipo de tiquete
     * con su tipo (SIMPLE o MÚLTIPLE) y su descripción.
     */
    private static class TiqueteEjemplo {
        private String nombre;
        private String tipo;   // "SIMPLE" o "MÚLTIPLE"
        private String precio; // para mostrar bonito

        public TiqueteEjemplo(String nombre, String tipo, String precio) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.precio = precio;
        }

        @Override
        public String toString() {
            // Así es como se verá en la lista
            return "[" + tipo + "] " + nombre + " - " + precio;
        }

        // Getters si luego quieres usarlos para la lógica real
        public String getNombre() { return nombre; }
        public String getTipo() { return tipo; }
        public String getPrecio() { return precio; }
    }

    /**
     * Inicializa una lista de eventos de ejemplo y sus tiquetes asociados,
     * marcando cada tiquete como SIMPLE o MÚLTIPLE.
     */
    private void inicializarDatosEjemplo() {
        tiquetesPorEvento = new HashMap<>();

        // --- Evento 1 ---
        tiquetesPorEvento.put("Concierto Rock Bogotá 2025", List.of(
            new TiqueteEjemplo("General", "SIMPLE", "$80.000"),
            new TiqueteEjemplo("VIP", "SIMPLE", "$150.000"),
            new TiqueteEjemplo("Combo 4 personas", "MÚLTIPLE", "$300.000")
        ));

        // --- Evento 2 ---
        tiquetesPorEvento.put("Obra de Teatro - Hamlet", List.of(
            new TiqueteEjemplo("Platea", "SIMPLE", "$60.000"),
            new TiqueteEjemplo("Balcón", "SIMPLE", "$40.000"),
            new TiqueteEjemplo("Paquete Cena + Obra", "MÚLTIPLE", "$180.000")
        ));

        // --- Evento 3 ---
        tiquetesPorEvento.put("Partido Final Liga Colombiana", List.of(
            new TiqueteEjemplo("Lateral Norte", "SIMPLE", "$50.000"),
            new TiqueteEjemplo("Oriental", "SIMPLE", "$90.000"),
            new TiqueteEjemplo("Occidental VIP", "SIMPLE", "$120.000"),
            new TiqueteEjemplo("Abono 3 partidos", "MÚLTIPLE", "$250.000")
        ));

        // --- MUCHOS EVENTOS PARA EL SCROLL ---
        tiquetesPorEvento.put("Festival de Jazz Medellín", List.of(
            new TiqueteEjemplo("General", "SIMPLE", "$70.000"),
            new TiqueteEjemplo("Backstage", "MÚLTIPLE", "$220.000")
        ));

        tiquetesPorEvento.put("Feria del Libro Bogotá", List.of(
            new TiqueteEjemplo("Ingreso Día", "SIMPLE", "$15.000"),
            new TiqueteEjemplo("Pasaporte 5 días", "MÚLTIPLE", "$55.000")
        ));

        tiquetesPorEvento.put("Comic Con Colombia", List.of(
            new TiqueteEjemplo("General", "SIMPLE", "$50.000"),
            new TiqueteEjemplo("Meet & Greet", "SIMPLE", "$200.000"),
            new TiqueteEjemplo("Full Pass 3 días", "MÚLTIPLE", "$120.000")
        ));

        tiquetesPorEvento.put("Festival Vallenato Valledupar", List.of(
            new TiqueteEjemplo("Preferencial", "SIMPLE", "$120.000"),
            new TiqueteEjemplo("VIP", "SIMPLE", "$180.000")
        ));

        tiquetesPorEvento.put("Carrera 10K Bogotá", List.of(
            new TiqueteEjemplo("Inscripción General", "SIMPLE", "$40.000"),
            new TiqueteEjemplo("Kit Competidor", "MÚLTIPLE", "$80.000")
        ));

        tiquetesPorEvento.put("Expo Fitness Colombia", List.of(
            new TiqueteEjemplo("Ingreso Diario", "SIMPLE", "$25.000"),
            new TiqueteEjemplo("Full Weekend", "MÚLTIPLE", "$60.000")
        ));

        tiquetesPorEvento.put("Torneo Gamer Medellín", List.of(
            new TiqueteEjemplo("Ingreso General", "SIMPLE", "$35.000"),
            new TiqueteEjemplo("Competidor Oficial", "MÚLTIPLE", "$95.000")
        ));

        tiquetesPorEvento.put("Clásicos del Metal Tour", List.of(
            new TiqueteEjemplo("General", "SIMPLE", "$90.000"),
            new TiqueteEjemplo("VIP", "SIMPLE", "$160.000")
        ));

        tiquetesPorEvento.put("Festival de Salsa Cali", List.of(
            new TiqueteEjemplo("Platea", "SIMPLE", "$70.000"),
            new TiqueteEjemplo("VIP", "SIMPLE", "$140.000"),
            new TiqueteEjemplo("Pareja Full Experience", "MÚLTIPLE", "$200.000")
        ));

        tiquetesPorEvento.put("Maratón de las Flores Medellín", List.of(
            new TiqueteEjemplo("5K", "SIMPLE", "$30.000"),
            new TiqueteEjemplo("10K", "SIMPLE", "$40.000"),
            new TiqueteEjemplo("21K + Kit Premium", "MÚLTIPLE", "$90.000")
        ));

        tiquetesPorEvento.put("Feria Gastronómica Sabor a Colombia", List.of(
            new TiqueteEjemplo("Ingreso Día", "SIMPLE", "$20.000"),
            new TiqueteEjemplo("Full Weekend", "MÚLTIPLE", "$45.000")
        ));

        tiquetesPorEvento.put("Festival EDM Bogotá", List.of(
            new TiqueteEjemplo("General", "SIMPLE", "$100.000"),
            new TiqueteEjemplo("VIP", "SIMPLE", "$180.000"),
            new TiqueteEjemplo("Combo Amigos (5)", "MÚLTIPLE", "$420.000")
        ));

        tiquetesPorEvento.put("Copa Libertadores – Partido Internacional", List.of(
            new TiqueteEjemplo("Norte", "SIMPLE", "$50.000"),
            new TiqueteEjemplo("Sur", "SIMPLE", "$50.000"),
            new TiqueteEjemplo("Occidental", "SIMPLE", "$150.000"),
            new TiqueteEjemplo("Abono Libertadores", "MÚLTIPLE", "$300.000")
        ));

        tiquetesPorEvento.put("Conferencia Innovación Tech", List.of(
            new TiqueteEjemplo("General", "SIMPLE", "$80.000"),
            new TiqueteEjemplo("Full Pass + Workshops", "MÚLTIPLE", "$180.000")
        ));

        tiquetesPorEvento.put("Festival de Cine Independiente", List.of(
            new TiqueteEjemplo("Entrada Individual", "SIMPLE", "$12.000"),
            new TiqueteEjemplo("Pasaporte Cine", "MÚLTIPLE", "$45.000")
        ));

        tiquetesPorEvento.put("Concierto Sinfónica Filarmónica", List.of(
            new TiqueteEjemplo("General", "SIMPLE", "$55.000"),
            new TiqueteEjemplo("Preferencial", "SIMPLE", "$85.000")
        ));

        tiquetesPorEvento.put("Show de Stand-Up Comedy", List.of(
            new TiqueteEjemplo("General", "SIMPLE", "$30.000"),
            new TiqueteEjemplo("Pareja", "MÚLTIPLE", "$55.000")
        ));

        tiquetesPorEvento.put("Festival Urbano Medellín", List.of(
            new TiqueteEjemplo("General", "SIMPLE", "$90.000"),
            new TiqueteEjemplo("VIP", "SIMPLE", "$180.000"),
            new TiqueteEjemplo("Backstage", "MÚLTIPLE", "$350.000")
        ));
    }

    /**
     * Actualiza la lista de tiquetes según el evento seleccionado.
     * Aquí ya se ve si el tiquete es SIMPLE o MÚLTIPLE.
     */
    private void actualizarTiquetesAsociados(String eventoSeleccionado) {
        modeloTiquetes.clear();
        if (eventoSeleccionado == null) {
            return;
        }

        List<TiqueteEjemplo> tiquetes = tiquetesPorEvento.get(eventoSeleccionado);
        if (tiquetes != null) {
            for (TiqueteEjemplo t : tiquetes) {
                modeloTiquetes.addElement(t);
            }
        }
    }

    public static void main(String[] args) {
        new ventanaComprarTiquete();
    }
}
