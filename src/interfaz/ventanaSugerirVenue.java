package interfaz;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import Evento.Venue;
import Persistencia.SistemaPersistencia;
import usuario.Cliente;
import usuario.Organizador;
import usuario.Promotor;
import usuario.Usuario;

public class ventanaSugerirVenue extends JFrame {
    private static final long serialVersionUID = 1L;

    private SistemaPersistencia sistema;
    private Usuario usuario;

    // Componentes
    private JTextField txtUbicacion;
    private JComboBox<String> comboEstado;
    private JTextField txtCapacidad;
    private JTextArea txtMensaje;
    private JButton btnEnviar;
    private JButton btnCancelar;
    private JButton btnLimpiar;

    public ventanaSugerirVenue(Usuario usuario, SistemaPersistencia sistema) {
        this.usuario = usuario;
        this.sistema = sistema;

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("BOLETAMASTER: Sugerir Nuevo Venue");
        setSize(600, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        // ========================================
        // PANEL SUPERIOR
        // ========================================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(70, 130, 180));
        panelSuperior.setBounds(0, 0, 600, 60);
        panelSuperior.setLayout(null);
        add(panelSuperior);

        JLabel lblTitulo = new JLabel(" SUGERIR NUEVO VENUE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(20, 20, 400, 25);
        panelSuperior.add(lblTitulo);

        // ========================================
        // INFORMACIÓN DEL USUARIO
        // ========================================
        JLabel lblUsuarioInfo = new JLabel("Enviado por: " + usuario.getLogin() + " (" + usuario.getLogin() + ")");
        lblUsuarioInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblUsuarioInfo.setForeground(Color.GRAY);
        lblUsuarioInfo.setBounds(40, 75, 500, 20);
        add(lblUsuarioInfo);

        // ========================================
        // FORMULARIO DE VENUE
        // ========================================
        JPanel panelFormulario = new JPanel();
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "Información del Venue",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 13),
            new Color(155, 89, 182)
        ));
        panelFormulario.setBounds(30, 110, 540, 250);
        panelFormulario.setLayout(null);
        add(panelFormulario);

        // Ubicación
        JLabel lblUbicacion = new JLabel("Ubicación: *");
        lblUbicacion.setFont(new Font("Arial", Font.BOLD, 13));
        lblUbicacion.setBounds(20, 40, 200, 25);
        panelFormulario.add(lblUbicacion);

        txtUbicacion = new JTextField();
        txtUbicacion.setFont(new Font("Arial", Font.PLAIN, 13));
        txtUbicacion.setBounds(20, 65, 500, 30);
        txtUbicacion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panelFormulario.add(txtUbicacion);

        // Capacidad
        JLabel lblCapacidad = new JLabel("Capacidad: *");
        lblCapacidad.setFont(new Font("Arial", Font.BOLD, 13));
        lblCapacidad.setBounds(20, 110, 200, 25);
        panelFormulario.add(lblCapacidad);

        txtCapacidad = new JTextField();
        txtCapacidad.setFont(new Font("Arial", Font.PLAIN, 13));
        txtCapacidad.setBounds(20, 135, 150, 30);
        txtCapacidad.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panelFormulario.add(txtCapacidad);

        JLabel lblPersonas = new JLabel("personas");
        lblPersonas.setFont(new Font("Arial", Font.ITALIC, 11));
        lblPersonas.setForeground(Color.GRAY);
        lblPersonas.setBounds(180, 135, 100, 30);
        panelFormulario.add(lblPersonas);

        // Estado
        JLabel lblEstado = new JLabel("Estado del Venue:");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 13));
        lblEstado.setBounds(300, 110, 200, 25);
        panelFormulario.add(lblEstado);

        comboEstado = new JComboBox<>(new String[] { "Aprobado", "No Aprobado" });
        comboEstado.setFont(new Font("Arial", Font.PLAIN, 13));
        comboEstado.setBounds(300, 135, 220, 30);
        comboEstado.setBackground(Color.WHITE);
        comboEstado.setFocusable(false);
        panelFormulario.add(comboEstado);

        // ========================================
        // MENSAJE / JUSTIFICACIÓN
        // ========================================
        JPanel panelMensaje = new JPanel();
        panelMensaje.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "Justificación de la Solicitud",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 13),
            new Color(155, 89, 182)
        ));
        panelMensaje.setBounds(30, 375, 540, 160);
        panelMensaje.setLayout(new BorderLayout(5, 5));
        add(panelMensaje);

        JLabel lblMensaje = new JLabel("Explica por qué este venue debería ser agregado: *");
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        panelMensaje.add(lblMensaje, BorderLayout.NORTH);

        txtMensaje = new JTextArea();
        txtMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        txtMensaje.setLineWrap(true);
        txtMensaje.setWrapStyleWord(true);
        txtMensaje.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollMensaje = new JScrollPane(txtMensaje);
        panelMensaje.add(scrollMensaje, BorderLayout.CENTER);

        // ========================================
        // NOTA DE CAMPOS REQUERIDOS
        // ========================================
        JLabel lblRequeridos = new JLabel("* Campos obligatorios");
        lblRequeridos.setFont(new Font("Arial", Font.ITALIC, 11));
        lblRequeridos.setForeground(Color.RED);
        lblRequeridos.setBounds(40, 545, 200, 20);
        add(lblRequeridos);

        // ========================================
        // BOTONES
        // ========================================
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelBotones.setBounds(30, 560, 540, 50);
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
        btnEnviar.setBackground(new Color(46, 204, 113));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFocusPainted(false);
        btnEnviar.setPreferredSize(new Dimension(180, 35));
        btnEnviar.addActionListener(e -> enviarSolicitud());
        panelBotones.add(btnEnviar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(120, 35));
        btnCancelar.addActionListener(e -> volverAlMenu());
        panelBotones.add(btnCancelar);
    }

    /**
     * Valida y envía la solicitud de venue
     */
    private void enviarSolicitud() {
        // Obtener datos del formulario
        String capacidadTexto = txtCapacidad.getText().trim();
        
        String ubicacion = txtUbicacion.getText().trim();
        String estadoTexto = (String) comboEstado.getSelectedItem();
        boolean estado = estadoTexto.equals("Aprobado");

        String mensaje = txtMensaje.getText().trim();

        // ========================================
        // VALIDACIONES
        // ========================================
       

        if (ubicacion.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La ubicación del venue es obligatoria",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            txtUbicacion.requestFocus();
            return;
        }
        

        if (capacidadTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La capacidad del venue es obligatoria",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            txtCapacidad.requestFocus();
            return;
        }

        int capacidad;
        try {
            capacidad = Integer.parseInt(capacidadTexto);
            if (capacidad <= 0) {
                JOptionPane.showMessageDialog(this,
                    "La capacidad debe ser mayor a 0",
                    "Capacidad Inválida",
                    JOptionPane.ERROR_MESSAGE);
                txtCapacidad.selectAll();
                txtCapacidad.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "La capacidad debe ser un número entero válido",
                "Formato Inválido",
                JOptionPane.ERROR_MESSAGE);
            txtCapacidad.selectAll();
            txtCapacidad.requestFocus();
            return;
        }

        if (mensaje.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Debes escribir una justificación para tu solicitud",
                "Campo Requerido",
                JOptionPane.WARNING_MESSAGE);
            txtMensaje.requestFocus();
            return;
        }

        // ========================================
        // CONFIRMAR ENVÍO
        // ========================================
        int confirmacion = JOptionPane.showConfirmDialog(this,
            String.format("¿Enviar solicitud de nuevo venue?\n\n" +
                         "Ubicación: %s\n" +
                         "Capacidad: %d personas\n\n" +
                         "Justificación:\n%s",
                          ubicacion, capacidad, 
                         mensaje.length() > 100 ? mensaje.substring(0, 97) + "..." : mensaje),
            "Confirmar Solicitud",
            JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        // ========================================
        // CREAR VENUE Y ENVIAR SOLICITUD
        // ========================================
        try {
            // Crear el venue sugerido
            Venue venueNuevo = new Venue(ubicacion, capacidad,estado);

            // Enviar solicitud al administrador
            
            Promotor pro = (Promotor) usuario;
            pro.sugerirVenue(venueNuevo, mensaje, sistema);
             

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(this,
            	    String.format("✓ Solicitud enviada exitosamente!\n\n" +
            	                 "Ubicación: %s\n" +
            	                 "Capacidad: %d personas\n" +
            	                 "Estado: %s\n\n" +
            	                 "El administrador revisará tu solicitud.",
            	                  ubicacion, capacidad, estadoTexto),
            	    "Solicitud Enviada",
            	    JOptionPane.INFORMATION_MESSAGE);

            // Limpiar formulario
            limpiarFormulario();

    

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al enviar la solicitud:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

   
    private void volverAlMenu() {
    	   
    	dispose();
    	
        if (usuario instanceof Promotor) {
            new ventanaMenuPromotor((Promotor) usuario, sistema).setVisible(true);
        } else if (usuario instanceof Organizador) {
            new ventanaMenuOrganizador((Organizador) usuario, sistema).setVisible(true);
        } else if (usuario instanceof Cliente) {
        	 new ventanaMenuComprador((Cliente) usuario ,sistema).setVisible(true);
        }
    }
             
    /**
     * Limpia todos los campos del formulario
     */
    private void limpiarFormulario() {
        txtUbicacion.setText("");
        txtCapacidad.setText("");
        txtMensaje.setText("");
    }
}