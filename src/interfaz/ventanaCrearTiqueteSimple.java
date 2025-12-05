package interfaz;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import Evento.Evento;
import Evento.Localidad;
import Persistencia.SistemaPersistencia;
import tiquete.TiqueteSimple;

public class ventanaCrearTiqueteSimple extends JFrame {
    private static final long serialVersionUID = 1L;

    // Campos de la UI
    private JTextField txtNombreLocalidad;
    private JTextField txtPrecioBase;
    private JSpinner spinnerCapacidad;
    private JSpinner spinnerRecargo;
    private JSpinner spinnerCantidad;
    private JCheckBox chkNumerada;
    private JCheckBox chkNoNumerada;
    private JButton btnCrearTiquete;
    private JButton btnSalir;
    private JComboBox<String> comboEventos;

    // Modelo
    private SistemaPersistencia sistema;
    private List<Evento> eventosDisponibles;

    /**
     * Recibe el sistema de persistencia y despliega los eventos disponibles
     * para asociar los tiquetes simples.
     */
    public ventanaCrearTiqueteSimple(SistemaPersistencia sistema) {
        this.sistema = sistema;
        this.eventosDisponibles = (sistema != null) ? sistema.getEventos() : new ArrayList<>();

        getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 10);
        getContentPane().add(panel);

        // Nombre de localidad
        txtNombreLocalidad = new JTextField();
        txtNombreLocalidad.setBounds(217, 22, 134, 26);
        getContentPane().add(txtNombreLocalidad);
        txtNombreLocalidad.setColumns(10);

        JLabel lblNombreLocalidad = new JLabel("Nombre de Localidad:");
        lblNombreLocalidad.setBounds(40, 27, 149, 16);
        getContentPane().add(lblNombreLocalidad);

        // Precio base
        JLabel lblPrecioBase = new JLabel("Precio base: ");
        lblPrecioBase.setBounds(40, 55, 79, 16);
        getContentPane().add(lblPrecioBase);

        txtPrecioBase = new JTextField();
        txtPrecioBase.setColumns(10);
        txtPrecioBase.setBounds(217, 50, 134, 26);
        getContentPane().add(txtPrecioBase);

        // Capacidad de localidad
        JLabel lblCapacidad = new JLabel("Capacidad de localidad:");
        lblCapacidad.setBounds(40, 83, 161, 16);
        getContentPane().add(lblCapacidad);

        spinnerCapacidad = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        spinnerCapacidad.setBounds(217, 78, 128, 26);
        getContentPane().add(spinnerCapacidad);

        // Recargo
        JLabel lblRecargo = new JLabel("Recargo (%):");
        lblRecargo.setBounds(40, 121, 161, 16);
        getContentPane().add(lblRecargo);

        spinnerRecargo = new JSpinner(new SpinnerNumberModel(5.0, 0.0, 100.0, 0.5));
        spinnerRecargo.setBounds(217, 116, 128, 26);
        getContentPane().add(spinnerRecargo);

        // Cantidad de tiquetes
        JLabel lblCantidad = new JLabel("Cantidad de tiquetes:");
        lblCantidad.setBounds(40, 149, 161, 16);
        getContentPane().add(lblCantidad);

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        spinnerCantidad.setBounds(217, 144, 128, 26);
        getContentPane().add(spinnerCantidad);

        // Tipo de localidad: numerada / no numerada
        chkNumerada = new JCheckBox("Numerada");
        chkNumerada.setBounds(31, 174, 128, 23);
        getContentPane().add(chkNumerada);

        chkNoNumerada = new JCheckBox("No numerada");
        chkNoNumerada.setBounds(185, 174, 128, 23);
        getContentPane().add(chkNoNumerada);

        // Hacerlos mutuamente excluyentes
        chkNumerada.addActionListener(e -> {
            if (chkNumerada.isSelected()) {
                chkNoNumerada.setSelected(false);
            }
        });

        chkNoNumerada.addActionListener(e -> {
            if (chkNoNumerada.isSelected()) {
                chkNumerada.setSelected(false);
            }
        });

        // NUEVO: selección de evento
        JLabel lblEvento = new JLabel("Evento asociado:");
        lblEvento.setBounds(40, 210, 149, 16);
        getContentPane().add(lblEvento);

        comboEventos = new JComboBox<>();
        comboEventos.setBounds(40, 230, 311, 26);
        getContentPane().add(comboEventos);

        cargarEventosEnCombo();

        // Botón Crear Tiquete
        btnCrearTiquete = new JButton("Crear Tiquete");
        btnCrearTiquete.setBounds(207, 291, 174, 26);
        getContentPane().add(btnCrearTiquete);
        
        
        

        // Botón Salir
        btnSalir = new JButton("Salir");
        btnSalir.setBounds(6, 291, 79, 26);
        getContentPane().add(btnSalir);

        // Acción botón salir
        btnSalir.addActionListener(e -> dispose());

        // Acción botón crear
        btnCrearTiquete.addActionListener(e -> crearTiquetesSimplesDesdeUI());

        setTitle("BOLETAMASTER: Crear Tiquete Simple");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(392, 357);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Llena el combo con los eventos disponibles del sistema.
     * Muestra algo como: "NombreEvento (fecha)".
     */
    private void cargarEventosEnCombo() {
        comboEventos.removeAllItems();

        if (sistema == null || eventosDisponibles == null || eventosDisponibles.isEmpty()) {
            comboEventos.addItem("No hay eventos disponibles");
            comboEventos.setEnabled(false);
            btnCrearTiquete.setEnabled(false);
            return;
        }

        for (Evento ev : eventosDisponibles) {
            // Asumimos que Evento tiene getNombre() y getFecha()
            String etiqueta = ev.getNombre() + " (" + ev.getFecha() + ")";
            comboEventos.addItem(etiqueta);
        }
        comboEventos.setSelectedIndex(0);
        comboEventos.setEnabled(true);
        btnCrearTiquete.setEnabled(true);
    }

    /**
     * Lógica equivalente a crearTiquetesSimples(Evento evento, SistemaPersistencia sistema)
     * pero tomando los datos desde la interfaz y usando el evento seleccionado en el combo.
     */
    private void crearTiquetesSimplesDesdeUI() {
        if (sistema == null || eventosDisponibles == null || eventosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay eventos disponibles para asociar tiquetes.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idxEvento = comboEventos.getSelectedIndex();
        if (idxEvento < 0 || idxEvento >= eventosDisponibles.size() || !comboEventos.isEnabled()) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un evento válido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Evento eventoSeleccionado = eventosDisponibles.get(idxEvento);

        // === Nombre de la localidad ===
        String nombreLocalidad = txtNombreLocalidad.getText().trim();
        if (nombreLocalidad.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre de la localidad no puede estar vacío.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // === Precio base ===
        double precio;
        try {
            precio = Double.parseDouble(txtPrecioBase.getText().trim());
            if (precio <= 0) throw new NumberFormatException();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Precio inválido. Ingrese un número positivo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // === Capacidad ===
        int capacidad = (int) spinnerCapacidad.getValue();
        if (capacidad <= 0) {
            JOptionPane.showMessageDialog(this,
                    "La capacidad debe ser un número positivo.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // === Tipo (0 = NUMERADA, 1 = SIN_NUMERAR) ===
        int tipo;
        if (chkNumerada.isSelected() && !chkNoNumerada.isSelected()) {
            tipo = 0;
        } else if (!chkNumerada.isSelected() && chkNoNumerada.isSelected()) {
            tipo = 1;
        } else {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar exactamente un tipo: Numerada o No numerada.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // === Recargo ===
        double recargo;
        try {
            recargo = (double) spinnerRecargo.getValue();
            if (recargo < 0) throw new NumberFormatException();
        } catch (Exception ex) {
            recargo = 5.0; // por defecto, como en la consola
        }

        // === Cantidad de tiquetes ===
        int cantidadTiquetes = (int) spinnerCantidad.getValue();
        if (cantidadTiquetes <= 0 || cantidadTiquetes > capacidad) {
            JOptionPane.showMessageDialog(this,
                    "Cantidad de tiquetes inválida. Debe ser > 0 y <= capacidad.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // === Crear Localidad ===
        Localidad localidad = new Localidad(nombreLocalidad, precio, capacidad, tipo);

        // === Generar tiquetes simples ===
        int generados = 0;
        for (int i = 0; i < cantidadTiquetes; i++) {
            String idTiquete = sistema.generarIdTiquete();

            TiqueteSimple tiquete = new TiqueteSimple(
                    "SIMPLE",                    // tipoTiquete
                    idTiquete,                   // identificador
                    eventoSeleccionado.getFecha(), // fechaExpiracion
                    precio,                      // precio
                    nombreLocalidad,             // nombre
                    false,                       // transferido
                    false,                       // anulado
                    eventoSeleccionado,          // eventoAsociado
                    recargo,                     // recargo
                    localidad                    // localidadAsociada
            );

            try {
                eventoSeleccionado.agregarTiquete(tiquete);
                sistema.agregarTiquete(tiquete);
                generados++;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al generar tiquete: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        sistema.guardarTodo();

        JOptionPane.showMessageDialog(this,
                "Se generaron " + generados + " tiquetes SIMPLES para '" + nombreLocalidad +
                        "' en el evento '" + eventoSeleccionado.getNombre() + "'",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
    }

}