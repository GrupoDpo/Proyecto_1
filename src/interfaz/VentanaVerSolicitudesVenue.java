package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import Evento.Venue;
import Persistencia.SistemaPersistencia;
import excepciones.TransferenciaNoPermitidaException;
import usuario.Administrador;
import usuario.Usuario;

public class VentanaVerSolicitudesVenue extends JFrame {

    private static final long serialVersionUID = 1L;

    private SistemaPersistencia sistema;
    private Administrador admin;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JCheckBox seleccionarTodasCheck;
    private JButton btnAprobar;
    private JButton btnRechazar;
    private JButton btnActualizar;
    private JButton btnSalir;
    private JTextArea txtDetalle;

    // Para saber qué solicitud corresponde a cada fila
    private List<SolicitudInfo> solicitudesLista;

    public VentanaVerSolicitudesVenue(SistemaPersistencia sistema) {
        this.sistema = sistema;

        Usuario usuarioAdmin = sistema != null ? sistema.getAdministrador() : null;

        if (!(usuarioAdmin instanceof Administrador)) {
            JOptionPane.showMessageDialog(null,
                "No se encontró un administrador en el sistema",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.admin = (Administrador) usuarioAdmin;

        inicializarComponentes();
        cargarSolicitudes();
    }

    private void inicializarComponentes() {
        setTitle("BOLETAMASTER: Gestionar Solicitudes de Venues");
        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ========================================
        // PANEL SUPERIOR
        // ========================================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(70, 130, 180));
        panelSuperior.setPreferredSize(new Dimension(950, 60));
        panelSuperior.setLayout(null);

        JLabel lblTitulo = new JLabel(" GESTIÓN DE SOLICITUDES DE VENUES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(20, 20, 500, 25);
        panelSuperior.add(lblTitulo);

        btnActualizar = new JButton(" Actualizar");
        btnActualizar.setBounds(810, 15, 120, 35);
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> cargarSolicitudes());
        panelSuperior.add(btnActualizar);

        add(panelSuperior, BorderLayout.NORTH);

        // ========================================
        // PANEL CENTRAL - TABLA SOLICITUDES VENUE
        // ========================================
        JPanel panelCentro = new JPanel(new BorderLayout(5, 5));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de control
        JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.LEFT));

        seleccionarTodasCheck = new JCheckBox("Seleccionar todas");
        seleccionarTodasCheck.setFont(new Font("Arial", Font.BOLD, 12));
        seleccionarTodasCheck.addItemListener(e -> {
            boolean seleccionar = e.getStateChange() == ItemEvent.SELECTED;
            for (int i = 0; i < tabla.getRowCount(); i++) {
                tabla.setValueAt(seleccionar, i, 0);
            }
        });
        panelControl.add(seleccionarTodasCheck);

        JLabel lblInfo = new JLabel("  |  Marca las solicitudes de venue que deseas aprobar o rechazar");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);
        panelControl.add(lblInfo);

        panelCentro.add(panelControl, BorderLayout.NORTH);

        // ========================================
        // TABLA DE SOLICITUDES DE VENUE (AHORA 5 COLUMNAS)
        // ========================================
        String[] columnas = { "☑", "Ubicación", "Capacidad", "Estado", "Motivo" };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0; // Solo el checkbox
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Monospaced", Font.PLAIN, 11));
        tabla.setRowHeight(25);

        // Ajustes de columnas
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);   // Checkbox
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200); // Ubicación
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100); // Capacidad
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100); // Estado
        tabla.getColumnModel().getColumn(4).setPreferredWidth(220); // Motivo

        // Listener para mostrar detalle al seleccionar fila (opcional)
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalleSolicitudVenue();
            }
        });

        // Scroll de tabla
        JScrollPane scrollTabla = new JScrollPane(tabla);
        panelCentro.add(scrollTabla, BorderLayout.CENTER);

        // Agregar al frame (centro)
        add(panelCentro, BorderLayout.CENTER);

        // ========================================
        // PANEL DERECHO - DETALLE
        // ========================================
        JPanel panelDerecha = new JPanel(new BorderLayout(5,5));
        panelDerecha.setPreferredSize(new Dimension(340, 0));
        panelDerecha.setBorder(BorderFactory.createTitledBorder("Detalle"));

        txtDetalle = new JTextArea();
        txtDetalle.setEditable(false);
        txtDetalle.setFont(new Font("Arial", Font.BOLD, 13));
        txtDetalle.setLineWrap(true);
        txtDetalle.setWrapStyleWord(true);

        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);
        panelDerecha.add(scrollDetalle, BorderLayout.CENTER);

        add(panelDerecha, BorderLayout.EAST);

        // ========================================
        // PANEL INFERIOR - BOTONES
        // ========================================
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        btnAprobar = new JButton(" APROBAR SELECCIONADAS");
        btnAprobar.setFont(new Font("Arial", Font.BOLD, 13));
        btnAprobar.setBackground(new Color(46, 204, 113));
        btnAprobar.setForeground(Color.WHITE);
        btnAprobar.setFocusPainted(false);
        btnAprobar.setPreferredSize(new Dimension(220, 40));
        btnAprobar.addActionListener(e -> procesarSolicitudes(true));

        btnRechazar = new JButton(" RECHAZAR SELECCIONADAS");
        btnRechazar.setFont(new Font("Arial", Font.BOLD, 13));
        btnRechazar.setBackground(new Color(231, 76, 60));
        btnRechazar.setForeground(Color.WHITE);
        btnRechazar.setFocusPainted(false);
        btnRechazar.setPreferredSize(new Dimension(220, 40));
        btnRechazar.addActionListener(e -> procesarSolicitudes(false));

        btnSalir = new JButton("CERRAR");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 13));
        btnSalir.setBackground(new Color(149, 165, 166));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setPreferredSize(new Dimension(120, 40));
        btnSalir.addActionListener(e -> {
            dispose();
            new ventanaMenuAdministrador(admin, sistema).setVisible(true);
        });

        panelInferior.add(btnAprobar);
        panelInferior.add(btnRechazar);
        panelInferior.add(btnSalir);

        add(panelInferior, BorderLayout.SOUTH);
    }

    private void cargarSolicitudes() {
        modeloTabla.setRowCount(0);
        solicitudesLista = new ArrayList<>();
        if (txtDetalle != null) txtDetalle.setText("");

        if (admin == null) {
            return;
        }

        Queue<HashMap<Venue, String>> cola = admin.getSolicitudesVenue();

        if (cola == null || cola.isEmpty()) {
            // 5 columnas: checkbox, ubicacion, capacidad, estado, motivo
            modeloTabla.addRow(new Object[] { false, "No hay solicitudes pendientes", "", "", "" });
            return;
        }

        int contador = 1;

        for (HashMap<Venue, String> solicitud : cola) {
            for (Map.Entry<Venue, String> entry : solicitud.entrySet()) {

                Venue venue = entry.getKey();
                String motivo = entry.getValue();

                // ------------------------------
                // DATOS DEL VENUE
                // ------------------------------
                String ubicacion = venue.getUbicacion();
                int capacidad = venue.getCapacidadMax();
                String estado = venue.isAprobado() ? "Aprobado" : "No aprobado";

                // ------------------------------
                // AGREGAR FILA A LA TABLA (5 columnas)
                // ------------------------------
                Object[] fila = {
                    false,              // Checkbox
                    ubicacion,
                    String.valueOf(capacidad),
                    estado,
                    truncar(motivo, 35)
                };

                modeloTabla.addRow(fila);

                // ------------------------------
                // GUARDAR DETALLE COMPLETO
                // ------------------------------
                SolicitudInfo info = new SolicitudInfo(
                    contador++,
                    venue,
                    motivo,
                    solicitud
                );

                solicitudesLista.add(info);
            }
        }

        if (solicitudesLista.isEmpty()) {
            modeloTabla.addRow(new Object[] { false, "No hay solicitudes pendientes", "", "", "" });
        }
    }

    /**
     * Muestra el detalle de la solicitud seleccionada
     */
    private void mostrarDetalleSolicitudVenue() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= solicitudesLista.size()) {
            if (txtDetalle != null) txtDetalle.setText("");
            return;
        }

        SolicitudInfo info = solicitudesLista.get(fila);

        Venue v = info.venue;

        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════\n");
        sb.append("       DETALLE DEL VENUE\n");
        sb.append("══════════════════════════════\n\n");
        sb.append("Ubicación: ").append(v.getUbicacion()).append("\n");
        sb.append("Capacidad: ").append(v.getCapacidadMax()).append("\n");
        sb.append("Estado: ").append(v.isAprobado() ? "Aprobado" : "No aprobado").append("\n\n");
        sb.append("Motivo de la solicitud:\n");
        sb.append(info.motivo).append("\n");

        if (txtDetalle != null) {
            txtDetalle.setText(sb.toString());
            txtDetalle.setCaretPosition(0);
        }
    }

    private void procesarSolicitudes(boolean aprobar) {
        if (solicitudesLista == null || solicitudesLista.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay solicitudes para procesar",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 1. Buscar filas seleccionadas
        List<Integer> filasSeleccionadas = new ArrayList<>();
        for (int i = 0; i < tabla.getRowCount(); i++) {
            Object val = tabla.getValueAt(i, 0);
            if (val instanceof Boolean && (Boolean) val) {
                filasSeleccionadas.add(i);
            }
        }

        if (filasSeleccionadas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No has seleccionado ninguna solicitud",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 2. Confirmación
        String accion = aprobar ? "aprobar" : "rechazar";
        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("¿Confirmas que deseas %s %d solicitud(es)?",
                accion, filasSeleccionadas.size()),
            "Confirmar " + (aprobar ? "Aprobación" : "Rechazo"),
            JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int procesadas = 0;
        int errores = 0;

        // Procesar en orden inverso para no desincronizar índices
        for (int i = filasSeleccionadas.size() - 1; i >= 0; i--) {
            int fila = filasSeleccionadas.get(i);

            if (fila < 0 || fila >= solicitudesLista.size()) continue;

            SolicitudInfo info = solicitudesLista.get(fila);
            Venue venue = info.venue;
            HashMap<Venue, String> solicitudOriginal = info.referencia;

            try {
                // Llamada al método del administrador. Asegúrate que la firma coincida.
                // En tu implementación previa llamabas: admin.verSolicitudVenue(venue, info.motivo, sistema, aprobar);
                admin.verSolicitudVenue(venue, info.motivo, sistema, aprobar);
                procesadas++;
            } catch (Exception ex) {
                errores++;
                JOptionPane.showMessageDialog(this,
                    "Error al procesar solicitud #" + info.id + ":\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }

        // Mostrar resultado
        String mensaje;
        if (aprobar) {
            mensaje = String.format(" Se aprobaron %d solicitud(es) exitosamente", procesadas);
        } else {
            mensaje = String.format(" Se rechazaron %d solicitud(es)", procesadas);
        }

        if (errores > 0) {
            mensaje += String.format("\n⚠ %d solicitud(es) con errores", errores);
        }

        JOptionPane.showMessageDialog(this, mensaje,
            aprobar ? "Aprobación Exitosa" : "Rechazo Completado",
            JOptionPane.INFORMATION_MESSAGE);

        // Recargar tabla
        cargarSolicitudes();
    }

    private String truncar(String texto, int maxLength) {
        if (texto == null) return "";
        if (texto.length() <= maxLength) return texto;
        return texto.substring(0, maxLength - 3) + "...";
    }

    private static class SolicitudInfo {
        public int id;
        public Venue venue;
        public String motivo;
        public HashMap<Venue, String> referencia;

        public SolicitudInfo(int id, Venue venue, String motivo, HashMap<Venue, String> referencia) {
            this.id = id;
            this.venue = venue;
            this.motivo = motivo;
            this.referencia = referencia;
        }
    }
}
