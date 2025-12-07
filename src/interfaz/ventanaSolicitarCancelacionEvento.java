package interfaz;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

import Evento.Evento;
import Evento.SolicitudCancelacion;
import Persistencia.SistemaPersistencia;
import usuario.Administrador;
import usuario.Organizador;

public class ventanaSolicitarCancelacionEvento extends JFrame {
    private static final long serialVersionUID = 1L;

    private SistemaPersistencia sistema;
    private Organizador organizador;

    // Componentes
    private JComboBox<String> comboEventos;
    private JTextArea txtMotivo;
    private JTextArea txtDetalle;
    private JButton btnEnviar;
    private JButton btnCancelar;
    private JButton btnLimpiar;
    
    private List<Evento> eventosDisponibles;

    public ventanaSolicitarCancelacionEvento(Organizador organizador, SistemaPersistencia sistema) {
        this.organizador = organizador;
        this.sistema = sistema;
        this.eventosDisponibles = new ArrayList<>();

        inicializarComponentes();
        cargarEventos();
    }

    private void inicializarComponentes() {
        setTitle("BOLETAMASTER: Solicitar Cancelación de Evento");
        setSize(650, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        // ========================================
        // PANEL SUPERIOR
        // ========================================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(70, 130, 180));
        panelSuperior.setBounds(0, 0, 650, 60);
        panelSuperior.setLayout(null);
        add(panelSuperior);

        JLabel lblTitulo = new JLabel("⚠ SOLICITAR CANCELACIÓN DE EVENTO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(20, 20, 500, 25);
        panelSuperior.add(lblTitulo);

        // ========================================
        // INFORMACIÓN DEL ORGANIZADOR
        // ========================================
        JLabel lblUsuarioInfo = new JLabel("Enviado por: " + organizador.getLogin() + " (Organizador)");
        lblUsuarioInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblUsuarioInfo.setForeground(Color.GRAY);
        lblUsuarioInfo.setBounds(40, 75, 500, 20);
        add(lblUsuarioInfo);

        // ========================================
        // FORMULARIO DE EVENTO
        // ========================================
        JPanel panelFormulario = new JPanel();
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(231, 76, 60), 2),
            "Seleccionar Evento a Cancelar",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 13),
            new Color(231, 76, 60)
        ));
        panelFormulario.setBounds(30, 110, 590, 100);
        panelFormulario.setLayout(null);
        add(panelFormulario);

        // Evento
        JLabel lblEvento = new JLabel("Evento: *");
        lblEvento.setFont(new Font("Arial", Font.BOLD, 13));
        lblEvento.setBounds(20, 30, 200, 25);
        panelFormulario.add(lblEvento);

        comboEventos = new JComboBox<>();
        comboEventos.setFont(new Font("Arial", Font.PLAIN, 13));
        comboEventos.setBounds(20, 55, 550, 30);
        comboEventos.setBackground(Color.WHITE);
        comboEventos.addActionListener(e -> mostrarDetalleEvento());
        panelFormulario.add(comboEventos);

        // ========================================
        // DETALLE DEL EVENTO
        // ========================================
        JPanel panelDetalle = new JPanel();
        panelDetalle.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Detalle del Evento",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 13),
            new Color(52, 152, 219)
        ));
        panelDetalle.setBounds(30, 225, 590, 150);
        panelDetalle.setLayout(new BorderLayout());
        add(panelDetalle);

        txtDetalle = new JTextArea();
        txtDetalle.setEditable(false);
        txtDetalle.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDetalle.setLineWrap(true);
        txtDetalle.setWrapStyleWord(true);
        txtDetalle.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txtDetalle.setBackground(new Color(245, 245, 245));

        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);
        panelDetalle.add(scrollDetalle, BorderLayout.CENTER);

        // ========================================
        // MOTIVO / JUSTIFICACIÓN
        // ========================================
        JPanel panelMotivo = new JPanel();
        panelMotivo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(231, 76, 60), 2),
            "Motivo de Cancelación",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 13),
            new Color(231, 76, 60)
        ));
        panelMotivo.setBounds(30, 390, 590, 160);
        panelMotivo.setLayout(new BorderLayout(5, 5));
        add(panelMotivo);

        JLabel lblMotivo = new JLabel("Explica por qué necesitas cancelar este evento: *");
        lblMotivo.setFont(new Font("Arial", Font.PLAIN, 12));
        panelMotivo.add(lblMotivo, BorderLayout.NORTH);

        txtMotivo = new JTextArea();
        txtMotivo.setFont(new Font("Arial", Font.PLAIN, 12));
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        txtMotivo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollMotivo = new JScrollPane(txtMotivo);
        panelMotivo.add(scrollMotivo, BorderLayout.CENTER);

        // ========================================
        // NOTA DE CAMPOS REQUERIDOS
        // ========================================
        JLabel lblRequeridos = new JLabel("* Campos obligatorios");
        lblRequeridos.setFont(new Font("Arial", Font.ITALIC, 11));
        lblRequeridos.setForeground(Color.RED);
        lblRequeridos.setBounds(40, 560, 200, 20);
        add(lblRequeridos);

        // ========================================
        // BOTONES
        // ========================================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBounds(30, 575, 590, 50);
        add(panelBotones);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Arial", Font.BOLD, 13));
        btnLimpiar.setBackground(new Color(149, 165, 166));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setPreferredSize(new Dimension(120, 35));
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        panelBotones.add(btnLimpiar);

        btnEnviar = new JButton("ENVIAR SOLICITUD");
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 13));
        btnEnviar.setBackground(new Color(231, 76, 60));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFocusPainted(false);
        btnEnviar.setPreferredSize(new Dimension(180, 35));
        btnEnviar.addActionListener(e -> enviarSolicitud());
        panelBotones.add(btnEnviar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancelar.setBackground(new Color(149, 165, 166));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(120, 35));
        btnCancelar.addActionListener(e -> volverAlMenu());
        panelBotones.add(btnCancelar);

        setVisible(true);
    }

    /**
     * Carga los eventos del organizador que NO están cancelados
     */
    private void cargarEventos() {
        comboEventos.removeAllItems();
        eventosDisponibles.clear();

        List<Evento> todosLosEventos = sistema.getEventos();

        if (todosLosEventos == null || todosLosEventos.isEmpty()) {
            comboEventos.addItem("No hay eventos en el sistema");
            comboEventos.setEnabled(false);
            btnEnviar.setEnabled(false);
            return;
        }

        // Filtrar solo eventos del organizador que NO estén cancelados
        for (Evento evento : todosLosEventos) {
            if (evento.getLoginOrganizador().equals(organizador.getLogin()) 
                && !evento.getCancelado()) {
                eventosDisponibles.add(evento);
            }
        }

        if (eventosDisponibles.isEmpty()) {
            comboEventos.addItem("No tienes eventos activos para cancelar");
            comboEventos.setEnabled(false);
            btnEnviar.setEnabled(false);
            txtDetalle.setText("No tienes eventos disponibles para cancelar.");
            return;
        }

        // Agregar eventos al combo
        for (Evento ev : eventosDisponibles) {
            String etiqueta = String.format("%s - %s (%s)", 
                ev.getNombre(), 
                ev.getFecha(), 
                ev.getVenueAsociado() != null ? ev.getVenueAsociado().getUbicacion() : "Sin venue"
            );
            comboEventos.addItem(etiqueta);
        }

        comboEventos.setSelectedIndex(0);
        comboEventos.setEnabled(true);
        btnEnviar.setEnabled(true);
    }

    /**
     * Muestra el detalle del evento seleccionado
     */
    private void mostrarDetalleEvento() {
        int index = comboEventos.getSelectedIndex();
        
        if (index < 0 || index >= eventosDisponibles.size()) {
            txtDetalle.setText("");
            return;
        }

        Evento evento = eventosDisponibles.get(index);
        
        StringBuilder detalle = new StringBuilder();
        detalle.append("═══════════════════════════════════════════\n");
        detalle.append("       INFORMACIÓN DEL EVENTO\n");
        detalle.append("═══════════════════════════════════════════\n\n");
        
        detalle.append("Nombre: ").append(evento.getNombre()).append("\n");
        detalle.append("Fecha: ").append(evento.getFecha()).append("\n");
        detalle.append("Hora: ").append(evento.getHora()).append("\n");
        
        if (evento.getVenueAsociado() != null) {
            detalle.append("Venue: ").append(evento.getVenueAsociado().getUbicacion()).append("\n");
            detalle.append("Capacidad venue: ").append(evento.getVenueAsociado().getCapacidadMax()).append(" personas\n");
        } else {
            detalle.append("Venue: No asignado\n");
        }
        
        int tiquetesDisponibles = evento.getTiquetesDisponibles().size();
        detalle.append("\nTiquetes disponibles: ").append(tiquetesDisponibles).append("\n");
        
        detalle.append("\n⚠ ADVERTENCIA:\n");
        detalle.append("Si el administrador aprueba la cancelación:\n");
        detalle.append("• El evento será marcado como CANCELADO\n");
        detalle.append("• Todos los tiquetes serán ANULADOS\n");
        detalle.append("• El evento NO estará disponible para compra\n");

        txtDetalle.setText(detalle.toString());
    }

    /**
     * Valida y envía la solicitud de cancelación
     */
    private void enviarSolicitud() {
        // Validar que hay un evento seleccionado
        int index = comboEventos.getSelectedIndex();
        
        if (index < 0 || index >= eventosDisponibles.size()) {
            JOptionPane.showMessageDialog(this,
                "Debes seleccionar un evento",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Evento eventoSeleccionado = eventosDisponibles.get(index);
        String motivo = txtMotivo.getText().trim();

        // ========================================
        // VALIDACIONES
        // ========================================
        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debes escribir el motivo de la cancelación",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            txtMotivo.requestFocus();
            return;
        }

        if (motivo.length() < 10) {
            JOptionPane.showMessageDialog(this,
                "El motivo debe tener al menos 10 caracteres",
                "Motivo muy corto",
                JOptionPane.WARNING_MESSAGE);
            txtMotivo.requestFocus();
            return;
        }

        // ========================================
        // CONFIRMAR ENVÍO
        // ========================================
        int confirmacion = JOptionPane.showConfirmDialog(this,
            String.format("¿Enviar solicitud de cancelación?\n\n" +
                         "Evento: %s\n" +
                         "Fecha: %s\n\n" +
                         "Motivo:\n%s",
                         eventoSeleccionado.getNombre(),
                         eventoSeleccionado.getFecha(),
                         motivo.length() > 100 ? motivo.substring(0, 97) + "..." : motivo),
            "Confirmar Solicitud",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        // ========================================
        // CREAR Y ENVIAR SOLICITUD AL ADMINISTRADOR
        // ========================================
        try {
            // Obtener el administrador
            Administrador admin = sistema.getAdministrador();
            
            if (admin == null) {
                JOptionPane.showMessageDialog(this,
                    "Error: No se encontró el administrador del sistema.",
                    "Error del Sistema",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear la solicitud de cancelación
            SolicitudCancelacion solicitud = new SolicitudCancelacion(
                eventoSeleccionado,
                organizador.getLogin(),
                motivo
            );

            // Enviar solicitud usando el método del administrador
            admin.recibirSolicitudCancelacionEvento(solicitud);

            // Guardar persistencia
            sistema.guardarTodo();

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(this,
                String.format("✓ Solicitud enviada exitosamente!\n\n" +
                             "Evento: %s\n" +
                             "Fecha: %s\n" +
                             "Fecha de solicitud: %s\n\n" +
                             "El administrador revisará tu solicitud.",
                             eventoSeleccionado.getNombre(),
                             eventoSeleccionado.getFecha(),
                             solicitud.getFechaSolicitud()),
                "Solicitud Enviada",
                JOptionPane.INFORMATION_MESSAGE);

            // Limpiar formulario
            limpiarFormulario();
            cargarEventos();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al enviar la solicitud:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Vuelve al menú del organizador
     */
    private void volverAlMenu() {
        dispose();
        new ventanaMenuOrganizador(organizador, sistema).setVisible(true);
    }

    /**
     * Limpia todos los campos del formulario
     */
    private void limpiarFormulario() {
        txtMotivo.setText("");
        if (comboEventos.getItemCount() > 0) {
            comboEventos.setSelectedIndex(0);
        }
    }
}