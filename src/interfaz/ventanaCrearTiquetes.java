package interfaz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import Evento.Evento;
import Evento.Localidad;
import Persistencia.SistemaPersistencia;
import tiquete.TiqueteSimple;
import tiquete.TiqueteMultiple;
import usuario.Organizador;

public class ventanaCrearTiquetes extends JFrame {
    private static final long serialVersionUID = 1L;

    // Componentes UI - Comunes
    private JComboBox<String> comboTipoTiquete;
    private JComboBox<String> comboEventos;
    private JTextField txtNombreLocalidad;
    private JTextField txtPrecioBase;
    private JSpinner spinnerCapacidad;
    private JSpinner spinnerRecargo;
    private JSpinner spinnerCantidad;
    private JCheckBox chkNumerada;
    private JCheckBox chkNoNumerada;
    
    // Componentes UI - Específicos para Múltiple
    private JTextField txtNombrePaquete;
    private JTextField txtPrecioPaquete;
    private JSpinner spinnerRecargoPaquete;
    private JSpinner spinnerTiquetesPorPaquete;
    private JTextField txtPrecioIndividual;
    
    private JLabel lblNombrePaquete;
    private JLabel lblPrecioPaquete;
    private JLabel lblRecargoPaquete;
    private JLabel lblTiquetesPorPaquete;
    private JLabel lblPrecioIndividual;
    
    private JButton btnCrearTiquetes;
    private JButton btnSalir;

    // Modelo
    private SistemaPersistencia sistema;
    private List<Evento> eventosDisponibles;
    private Organizador organizador;

    public ventanaCrearTiquetes(SistemaPersistencia sistema, Organizador org) {
        this.sistema = sistema;
        this.organizador = org;
        
        this.eventosDisponibles = new ArrayList<>();
        if (sistema != null && sistema.getEventos() != null) {
            for (Evento ev : sistema.getEventos()) {
                if (ev.getLoginOrganizador().equals(organizador.getLogin()) 
                    && !ev.getCancelado()) {
                    this.eventosDisponibles.add(ev);
                }
            }
        }

        inicializarComponentes();
        cargarEventosEnCombo();
    }

    private void inicializarComponentes() {
        setTitle("BOLETAMASTER: Crear Tiquetes");
        setSize(550, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        // ========================================
        // PANEL SUPERIOR
        // ========================================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(52, 152, 219));
        panelSuperior.setBounds(0, 0, 550, 50);
        panelSuperior.setLayout(null);
        add(panelSuperior);

        JLabel lblTitulo = new JLabel("📋 Crear Tiquetes para Evento");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(20, 15, 400, 20);
        panelSuperior.add(lblTitulo);

        int y = 65;

        // ========================================
        // TIPO DE TIQUETE
        // ========================================
        JLabel lblTipo = new JLabel("Tipo de Tiquete:");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTipo.setBounds(40, y, 150, 25);
        add(lblTipo);

        String[] tipos = {"1. Tiquetes simples", "2. Paquete/Tiquete múltiple"};
        comboTipoTiquete = new JComboBox<>(tipos);
        comboTipoTiquete.setBounds(200, y, 310, 30);
        comboTipoTiquete.addActionListener(e -> actualizarCamposSegunTipo());
        add(comboTipoTiquete);
        y += 45;

        // ========================================
        // EVENTO
        // ========================================
        JLabel lblEvento = new JLabel("Evento asociado:");
        lblEvento.setFont(new Font("Arial", Font.BOLD, 13));
        lblEvento.setBounds(40, y, 150, 25);
        add(lblEvento);

        comboEventos = new JComboBox<>();
        comboEventos.setBounds(40, y + 25, 470, 30);
        add(comboEventos);
        y += 70;

        // ========================================
        // SECCIÓN TIQUETES SIMPLES
        // ========================================
        JLabel lblSeccionSimple = new JLabel("Información del Tiquete Simple:");
        lblSeccionSimple.setFont(new Font("Arial", Font.BOLD, 13));
        lblSeccionSimple.setBounds(40, y, 300, 25);
        add(lblSeccionSimple);
        y += 35;

        // Nombre localidad
        JLabel lblNombreLocalidad = new JLabel("Nombre de la localidad:");
        lblNombreLocalidad.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNombreLocalidad.setBounds(40, y, 180, 25);
        add(lblNombreLocalidad);

        txtNombreLocalidad = new JTextField();
        txtNombreLocalidad.setBounds(230, y, 280, 28);
        add(txtNombreLocalidad);
        y += 40;

        // Precio base
        JLabel lblPrecioBase = new JLabel("Precio base:");
        lblPrecioBase.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPrecioBase.setBounds(40, y, 180, 25);
        add(lblPrecioBase);

        txtPrecioBase = new JTextField();
        txtPrecioBase.setBounds(230, y, 150, 28);
        add(txtPrecioBase);
        y += 40;

        // Capacidad
        JLabel lblCapacidad = new JLabel("Capacidad de la localidad:");
        lblCapacidad.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCapacidad.setBounds(40, y, 180, 25);
        add(lblCapacidad);

        spinnerCapacidad = new JSpinner(new SpinnerNumberModel(100, 1, 100000, 1));
        spinnerCapacidad.setBounds(230, y, 150, 28);
        add(spinnerCapacidad);
        y += 40;

        // Tipo (Numerada/No numerada)
        JLabel lblTipoLocalidad = new JLabel("Tipo:");
        lblTipoLocalidad.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTipoLocalidad.setBounds(40, y, 180, 25);
        add(lblTipoLocalidad);

        chkNumerada = new JCheckBox("0 = Numerada");
        chkNumerada.setFont(new Font("Arial", Font.PLAIN, 11));
        chkNumerada.setBounds(230, y, 140, 25);
        add(chkNumerada);

        chkNoNumerada = new JCheckBox("1 = Sin numerar");
        chkNoNumerada.setFont(new Font("Arial", Font.PLAIN, 11));
        chkNoNumerada.setBounds(380, y, 130, 25);
        add(chkNoNumerada);
        y += 40;

        // Mutuamente excluyentes
        chkNumerada.addActionListener(e -> {
            if (chkNumerada.isSelected()) chkNoNumerada.setSelected(false);
        });
        chkNoNumerada.addActionListener(e -> {
            if (chkNoNumerada.isSelected()) chkNumerada.setSelected(false);
        });

        // Recargo
        JLabel lblRecargo = new JLabel("Recargo (%):");
        lblRecargo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblRecargo.setBounds(40, y, 180, 25);
        add(lblRecargo);

        spinnerRecargo = new JSpinner(new SpinnerNumberModel(5.0, 0.0, 100.0, 0.5));
        spinnerRecargo.setBounds(230, y, 150, 28);
        add(spinnerRecargo);
        y += 40;

        // Cantidad
        JLabel lblCantidad = new JLabel("¿Cuántos tiquetes generar?:");
        lblCantidad.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCantidad.setBounds(40, y, 180, 25);
        add(lblCantidad);

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        spinnerCantidad.setBounds(230, y, 150, 28);
        add(spinnerCantidad);
        y += 50;

        // ========================================
        // SECCIÓN TIQUETES MÚLTIPLES (Ocultos por defecto)
        // ========================================
        
        // Nombre del paquete
        lblNombrePaquete = new JLabel("Nombre del paquete:");
        lblNombrePaquete.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNombrePaquete.setBounds(40, y, 180, 25);
        lblNombrePaquete.setVisible(false);
        add(lblNombrePaquete);

        txtNombrePaquete = new JTextField();
        txtNombrePaquete.setBounds(230, y, 280, 28);
        txtNombrePaquete.setVisible(false);
        add(txtNombrePaquete);
        y += 40;

        // Precio del paquete
        lblPrecioPaquete = new JLabel("Precio base del paquete:");
        lblPrecioPaquete.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPrecioPaquete.setBounds(40, y, 180, 25);
        lblPrecioPaquete.setVisible(false);
        add(lblPrecioPaquete);

        txtPrecioPaquete = new JTextField();
        txtPrecioPaquete.setBounds(230, y, 150, 28);
        txtPrecioPaquete.setVisible(false);
        add(txtPrecioPaquete);
        y += 40;

        // Recargo del paquete
        lblRecargoPaquete = new JLabel("Recargo del paquete (%):");
        lblRecargoPaquete.setFont(new Font("Arial", Font.PLAIN, 12));
        lblRecargoPaquete.setBounds(40, y, 180, 25);
        lblRecargoPaquete.setVisible(false);
        add(lblRecargoPaquete);

        spinnerRecargoPaquete = new JSpinner(new SpinnerNumberModel(5.0, 0.0, 100.0, 0.5));
        spinnerRecargoPaquete.setBounds(230, y, 150, 28);
        spinnerRecargoPaquete.setVisible(false);
        add(spinnerRecargoPaquete);
        y += 40;

        // Tiquetes por paquete
        lblTiquetesPorPaquete = new JLabel("¿Cuántos tiquetes incluye?:");
        lblTiquetesPorPaquete.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTiquetesPorPaquete.setBounds(40, y, 180, 25);
        lblTiquetesPorPaquete.setVisible(false);
        add(lblTiquetesPorPaquete);

        spinnerTiquetesPorPaquete = new JSpinner(new SpinnerNumberModel(2, 1, 50, 1));
        spinnerTiquetesPorPaquete.setBounds(230, y, 150, 28);
        spinnerTiquetesPorPaquete.setVisible(false);
        add(spinnerTiquetesPorPaquete);
        y += 40;

        // Precio individual
        lblPrecioIndividual = new JLabel("Precio base individual:");
        lblPrecioIndividual.setFont(new Font("Arial", Font.PLAIN, 12));
        lblPrecioIndividual.setBounds(40, y, 180, 25);
        lblPrecioIndividual.setVisible(false);
        add(lblPrecioIndividual);

        txtPrecioIndividual = new JTextField();
        txtPrecioIndividual.setBounds(230, y, 150, 28);
        txtPrecioIndividual.setVisible(false);
        add(txtPrecioIndividual);
        y += 50;

        // ========================================
        // BOTONES
        // ========================================
        btnCrearTiquetes = new JButton("CREAR TIQUETES");
        btnCrearTiquetes.setFont(new Font("Arial", Font.BOLD, 14));
        btnCrearTiquetes.setBackground(new Color(46, 204, 113));
        btnCrearTiquetes.setForeground(Color.WHITE);
        btnCrearTiquetes.setFocusPainted(false);
        btnCrearTiquetes.setBounds(300, 665, 210, 40);
        btnCrearTiquetes.addActionListener(e -> crearTiquetes());
        add(btnCrearTiquetes);

        btnSalir = new JButton("CANCELAR");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalir.setBackground(new Color(231, 76, 60));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setBounds(40, 665, 150, 40);
        btnSalir.addActionListener(e -> volverAlMenu());
        add(btnSalir);
    }

    /**
     * Actualiza campos según tipo seleccionado
     */
    private void actualizarCamposSegunTipo() {
        boolean esMultiple = comboTipoTiquete.getSelectedIndex() == 1;
        
        // Mostrar/ocultar campos de múltiple
        lblNombrePaquete.setVisible(esMultiple);
        txtNombrePaquete.setVisible(esMultiple);
        lblPrecioPaquete.setVisible(esMultiple);
        txtPrecioPaquete.setVisible(esMultiple);
        lblRecargoPaquete.setVisible(esMultiple);
        spinnerRecargoPaquete.setVisible(esMultiple);
        lblTiquetesPorPaquete.setVisible(esMultiple);
        spinnerTiquetesPorPaquete.setVisible(esMultiple);
        lblPrecioIndividual.setVisible(esMultiple);
        txtPrecioIndividual.setVisible(esMultiple);
    }

    /**
     * Carga eventos del organizador
     */
    private void cargarEventosEnCombo() {
        comboEventos.removeAllItems();

        if (eventosDisponibles == null || eventosDisponibles.isEmpty()) {
            comboEventos.addItem("No tienes eventos disponibles");
            comboEventos.setEnabled(false);
            btnCrearTiquetes.setEnabled(false);
            return;
        }

        for (Evento ev : eventosDisponibles) {
            comboEventos.addItem(ev.getNombre() + " (" + ev.getFecha() + ")");
        }
        comboEventos.setSelectedIndex(0);
        comboEventos.setEnabled(true);
        btnCrearTiquetes.setEnabled(true);
    }

    /**
     * Crea tiquetes según tipo seleccionado
     */
    private void crearTiquetes() {
        if (comboTipoTiquete.getSelectedIndex() == 0) {
            crearTiquetesSimples();
        } else {
            crearTiquetesMultiples();
        }
    }

    /**
     * CREAR TIQUETES SIMPLES (igual que en consola)
     */
    private void crearTiquetesSimples() {
        // Validar evento
        int idxEvento = comboEventos.getSelectedIndex();
        if (idxEvento < 0 || idxEvento >= eventosDisponibles.size()) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un evento válido",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Evento evento = eventosDisponibles.get(idxEvento);

        // Validar campos
        String nombreLocalidad = txtNombreLocalidad.getText().trim();
        if (nombreLocalidad.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre de la localidad no puede estar vacío",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            txtNombreLocalidad.requestFocus();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(txtPrecioBase.getText().trim());
            if (precio <= 0) throw new NumberFormatException();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Precio inválido. Ingrese un número positivo",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            txtPrecioBase.requestFocus();
            return;
        }

        int capacidad = (int) spinnerCapacidad.getValue();
        
        int tipo;
        if (chkNumerada.isSelected() && !chkNoNumerada.isSelected()) {
            tipo = 0;
        } else if (!chkNumerada.isSelected() && chkNoNumerada.isSelected()) {
            tipo = 1;
        } else {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar exactamente un tipo: Numerada (0) o Sin numerar (1)",
                "Selección Requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        double recargo = (double) spinnerRecargo.getValue();
        int cantidadTiquetes = (int) spinnerCantidad.getValue();

        if (cantidadTiquetes > capacidad || cantidadTiquetes <= 0) {
            JOptionPane.showMessageDialog(this,
                "Cantidad no válida. Debe ser entre 1 y " + capacidad,
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirmar
        int confirmacion = JOptionPane.showConfirmDialog(this,
            String.format("¿Crear %d tiquetes SIMPLES?\n\n" +
                         "Localidad: %s\n" +
                         "Evento: %s\n" +
                         "Precio: $%.2f\n" +
                         "Tipo: %s",
                         cantidadTiquetes, nombreLocalidad, evento.getNombre(), 
                         precio, tipo == 0 ? "NUMERADA" : "SIN NUMERAR"),
            "Confirmar Creación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        // Crear localidad
        Localidad localidad = new Localidad(nombreLocalidad, precio, capacidad, tipo);

        // Generar tiquetes
        int generados = 0;
        for (int i = 0; i < cantidadTiquetes; i++) {
            try {
                String idTiquete = sistema.generarIdTiquete();

                TiqueteSimple tiquete = new TiqueteSimple(
                    "SIMPLE",
                    idTiquete,
                    evento.getFecha(),
                    precio,
                    nombreLocalidad,
                    false,
                    false,
                    evento,
                    recargo,
                    localidad
                );

                evento.agregarTiquete(tiquete);
                sistema.agregarTiquete(tiquete);
                generados++;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Error al generar tiquete: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                break;
            }
        }

        sistema.guardarTodo();

        JOptionPane.showMessageDialog(this,
            String.format("✓ Se generaron %d tiquetes SIMPLES para '%s'",
                         generados, nombreLocalidad),
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);

        limpiarFormulario();
    }

    /**
     * CREAR TIQUETES MÚLTIPLES - CORREGIDO: Ahora asigna localidad al paquete
     */
    private void crearTiquetesMultiples() {
        // Validar evento
        int idxEvento = comboEventos.getSelectedIndex();
        if (idxEvento < 0 || idxEvento >= eventosDisponibles.size()) {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar un evento válido",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Evento evento = eventosDisponibles.get(idxEvento);

        // Validar campos del paquete
        String nombrePaquete = txtNombrePaquete.getText().trim();
        if (nombrePaquete.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre del paquete no puede estar vacío",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            txtNombrePaquete.requestFocus();
            return;
        }

        double precioPaquete;
        try {
            precioPaquete = Double.parseDouble(txtPrecioPaquete.getText().trim());
            if (precioPaquete <= 0) throw new NumberFormatException();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Precio del paquete inválido",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            txtPrecioPaquete.requestFocus();
            return;
        }

        double recargoPaquete = (double) spinnerRecargoPaquete.getValue();
        int cantidadTiquetes = (int) spinnerTiquetesPorPaquete.getValue();

        if (cantidadTiquetes <= 0) {
            JOptionPane.showMessageDialog(this,
                "Debe incluir al menos 1 tiquete en el paquete",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar información de tiquetes internos
        String nombreLocalidad = txtNombreLocalidad.getText().trim();
        if (nombreLocalidad.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El nombre de la localidad no puede estar vacío",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            txtNombreLocalidad.requestFocus();
            return;
        }

        double precioIndividual;
        try {
            precioIndividual = Double.parseDouble(txtPrecioIndividual.getText().trim());
            if (precioIndividual < 0) precioIndividual = 0;
        } catch (Exception ex) {
            precioIndividual = 0;
        }

        int capacidad = (int) spinnerCapacidad.getValue();
        
        int tipoLocalidad;
        if (chkNumerada.isSelected() && !chkNoNumerada.isSelected()) {
            tipoLocalidad = 0;
        } else if (!chkNumerada.isSelected() && chkNoNumerada.isSelected()) {
            tipoLocalidad = 1;
        } else {
            JOptionPane.showMessageDialog(this,
                "Debe seleccionar el tipo de localidad",
                "Selección Requerida",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Confirmar
        int confirmacion = JOptionPane.showConfirmDialog(this,
            String.format("¿Crear paquete MÚLTIPLE?\n\n" +
                         "Paquete: %s\n" +
                         "Evento: %s\n" +
                         "Precio del paquete: $%.2f\n" +
                         "Incluye %d tiquetes de '%s'\n" +
                         "Precio individual: $%.2f",
                         nombrePaquete, evento.getNombre(), precioPaquete,
                         cantidadTiquetes, nombreLocalidad, precioIndividual),
            "Confirmar Creación",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            // CORRECCIÓN: Crear localidad para el PAQUETE
            // Usamos el nombre del paquete como nombre de localidad para el múltiple
            Localidad localidadPaquete = new Localidad(
                nombrePaquete, 
                precioPaquete, 
                1, // Capacidad del paquete (1 paquete)
                tipoLocalidad
            );

            // Crear localidad para los tiquetes internos
            Localidad localidadInterna = new Localidad(
                nombreLocalidad, 
                precioIndividual, 
                capacidad, 
                tipoLocalidad
            );

            // Generar tiquetes internos del paquete
            ArrayList<TiqueteSimple> tiquetesDelPaquete = new ArrayList<>();

            for (int i = 1; i <= cantidadTiquetes; i++) {
                String idTiquete = sistema.generarIdTiquete();

                TiqueteSimple tiqueteInterno = new TiqueteSimple(
                    "SIMPLE",
                    idTiquete,
                    evento.getFecha(),
                    precioIndividual,
                    nombreLocalidad,
                    false,
                    false,
                    evento,
                    0, // Los tiquetes internos no tienen recargo propio
                    localidadInterna
                );

                tiquetesDelPaquete.add(tiqueteInterno);
                sistema.agregarTiquete(tiqueteInterno);
            }

            // Crear el tiquete múltiple - AHORA CON LOCALIDAD
            String idPaquete = sistema.generarIdTiquete();

            TiqueteMultiple paquete = new TiqueteMultiple(
                "MULTIPLE",
                idPaquete,
                evento.getFecha(),
                precioPaquete,
                nombrePaquete,
                false,
                false,
                evento,
                recargoPaquete,
                localidadPaquete, // ← CORRECCIÓN: Ya no es null
                tiquetesDelPaquete
            );

            evento.agregarTiquete(paquete);
            sistema.agregarTiquete(paquete);
            sistema.guardarTodo();

            JOptionPane.showMessageDialog(this,
                String.format("✓ Paquete '%s' creado exitosamente!\n\n" +
                             "ID: %s\n" +
                             "Incluye %d tiquetes de '%s'\n" +
                             "Precio del paquete: $%.2f\n" +
                             "Precio individual: $%.2f",
                             nombrePaquete, idPaquete, cantidadTiquetes,
                             nombreLocalidad, precioPaquete, precioIndividual),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

            limpiarFormulario();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al crear paquete: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Limpia el formulario
     */
    private void limpiarFormulario() {
        txtNombreLocalidad.setText("");
        txtPrecioBase.setText("");
        txtNombrePaquete.setText("");
        txtPrecioPaquete.setText("");
        txtPrecioIndividual.setText("");
        spinnerCapacidad.setValue(100);
        spinnerRecargo.setValue(5.0);
        spinnerRecargoPaquete.setValue(5.0);
        spinnerCantidad.setValue(1);
        spinnerTiquetesPorPaquete.setValue(2);
        chkNumerada.setSelected(false);
        chkNoNumerada.setSelected(false);
        comboTipoTiquete.setSelectedIndex(0);
        actualizarCamposSegunTipo();
    }
    
    private void volverAlMenu() {
        
    	
   	 dispose();
        new ventanaMenuOrganizador( organizador, sistema).setVisible(true);
          
                
       }
}