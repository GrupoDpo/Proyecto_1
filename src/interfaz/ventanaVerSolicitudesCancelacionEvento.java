package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
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

import Evento.Evento;
import Evento.SolicitudCancelacion;
import Persistencia.SistemaPersistencia;
import usuario.Administrador;
import usuario.Usuario;
import tiquete.Tiquete;

public class ventanaVerSolicitudesCancelacionEvento extends JFrame {

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

    // Lista de solicitudes para mapeo con tabla
    private List<SolicitudCancelacion> solicitudesLista;

    public ventanaVerSolicitudesCancelacionEvento(SistemaPersistencia sistema) {
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
        setTitle("BOLETAMASTER: Gestionar Solicitudes de Cancelación de Eventos");
        setSize(1037, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout(10, 10));

        // ========================================
        // PANEL SUPERIOR
        // ========================================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(70, 130, 180));
        panelSuperior.setPreferredSize(new Dimension(1000, 60));
        panelSuperior.setLayout(null);

        JLabel lblTitulo = new JLabel("⚠ GESTIÓN DE SOLICITUDES DE CANCELACIÓN");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(20, 20, 550, 25);
        panelSuperior.add(lblTitulo);

        btnActualizar = new JButton("🔄 Actualizar");
        btnActualizar.setBounds(860, 15, 120, 35);
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> cargarSolicitudes());
        panelSuperior.add(btnActualizar);

        getContentPane().add(panelSuperior, BorderLayout.NORTH);

        // ========================================
        // PANEL CENTRAL (TABLA)
        // ========================================
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BorderLayout(5, 5));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Checkbox "Seleccionar todas"
        seleccionarTodasCheck = new JCheckBox("Seleccionar todas");
        seleccionarTodasCheck.setFont(new Font("Arial", Font.BOLD, 12));
        seleccionarTodasCheck.addItemListener(e -> {
            boolean seleccionada = seleccionarTodasCheck.isSelected();
            for (int i = 0; i < tabla.getRowCount(); i++) {
                tabla.setValueAt(seleccionada, i, 0);
            }
        });

        JPanel panelCheck = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCheck.add(seleccionarTodasCheck);
        panelCentral.add(panelCheck, BorderLayout.NORTH);

        // Tabla de solicitudes (7 columnas)
        String[] columnas = { "☑", "Evento", "Fecha", "Organizador", "Tiquetes", "Estado", "Motivo" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                if (col == 0) return Boolean.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0; // Solo la columna checkbox es editable
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(52, 152, 219));
        tabla.getTableHeader().setForeground(Color.WHITE);

        // Ajustar anchos de columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(40);  // Checkbox
        tabla.getColumnModel().getColumn(1).setPreferredWidth(180); // Evento
        tabla.getColumnModel().getColumn(2).setPreferredWidth(90);  // Fecha
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100); // Organizador
        tabla.getColumnModel().getColumn(4).setPreferredWidth(70);  // Tiquetes
        tabla.getColumnModel().getColumn(5).setPreferredWidth(80);  // Estado
        tabla.getColumnModel().getColumn(6).setPreferredWidth(280); // Motivo

        // Listener para mostrar detalle al seleccionar fila
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalleSolicitud();
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tabla);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);

        getContentPane().add(panelCentral, BorderLayout.CENTER);

        // ========================================
        // PANEL DERECHO (DETALLE)
        // ========================================
        JPanel panelDetalle = new JPanel();
        panelDetalle.setLayout(new BorderLayout());
        panelDetalle.setPreferredSize(new Dimension(350, 0));
        panelDetalle.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Detalle de la Solicitud",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 13),
            new Color(52, 152, 219)
        ));

        txtDetalle = new JTextArea();
        txtDetalle.setEditable(false);
        txtDetalle.setFont(new Font("Monospaced", Font.PLAIN, 11));
        txtDetalle.setLineWrap(true);
        txtDetalle.setWrapStyleWord(true);
        txtDetalle.setBackground(new Color(245, 245, 245));

        JScrollPane scrollDetalle = new JScrollPane(txtDetalle);
        panelDetalle.add(scrollDetalle, BorderLayout.CENTER);

        getContentPane().add(panelDetalle, BorderLayout.EAST);

        // ========================================
        // PANEL INFERIOR (BOTONES)
        // ========================================
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelInferior.setPreferredSize(new Dimension(1000, 70));

        btnAprobar = new JButton("✓ APROBAR SELECCIONADAS");
        btnAprobar.setFont(new Font("Arial", Font.BOLD, 13));
        btnAprobar.setBackground(new Color(231, 76, 60));
        btnAprobar.setForeground(Color.WHITE);
        btnAprobar.setFocusPainted(false);
        btnAprobar.setPreferredSize(new Dimension(220, 40));
        btnAprobar.addActionListener(e -> procesarSolicitudes(true));

        btnRechazar = new JButton("❌ RECHAZAR SELECCIONADAS");
        btnRechazar.setFont(new Font("Arial", Font.BOLD, 13));
        btnRechazar.setBackground(new Color(149, 165, 166));
        btnRechazar.setForeground(Color.WHITE);
        btnRechazar.setFocusPainted(false);
        btnRechazar.setPreferredSize(new Dimension(220, 40));
        btnRechazar.addActionListener(e -> procesarSolicitudes(false));

        btnSalir = new JButton("CERRAR");
        btnSalir.setFont(new Font("Arial", Font.BOLD, 13));
        btnSalir.setBackground(new Color(44, 62, 80));
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

        getContentPane().add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Carga las solicitudes pendientes desde la cola del administrador
     */
    private void cargarSolicitudes() {
        modeloTabla.setRowCount(0);
        solicitudesLista = new ArrayList<>();
        if (txtDetalle != null) txtDetalle.setText("");

        if (admin == null) {
            return;
        }

        Queue<SolicitudCancelacion> cola = admin.getSolicitudesCancelacionEvento();

        if (cola == null || cola.isEmpty()) {
            modeloTabla.addRow(new Object[] { false, "No hay solicitudes pendientes", "", "", "", "", "" });
            return;
        }

        // Convertir Queue a List
        solicitudesLista.addAll(cola);

        for (SolicitudCancelacion sol : solicitudesLista) {
            Evento evento = sol.getEvento();

            // Datos del evento
            String nombreEvento = evento != null ? evento.getNombre() : "N/A";
            String fecha = evento != null ? evento.getFecha() : "N/A";
            String organizador = sol.getLoginOrganizador();
            int tiquetes = evento != null ? evento.getTiquetesDisponibles().size() : 0;
            String estado = sol.getEstado();
            String motivo = truncar(sol.getMotivo(), 40);

            // Agregar fila a la tabla
            Object[] fila = {
                false,                  // Checkbox
                nombreEvento,
                fecha,
                organizador,
                String.valueOf(tiquetes),
                estado,
                motivo
            };

            modeloTabla.addRow(fila);
        }

        if (solicitudesLista.isEmpty()) {
            modeloTabla.addRow(new Object[] { false, "No hay solicitudes pendientes", "", "", "", "", "" });
        }
    }

    /**
     * Muestra el detalle de la solicitud seleccionada
     */
    private void mostrarDetalleSolicitud() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= solicitudesLista.size()) {
            if (txtDetalle != null) txtDetalle.setText("");
            return;
        }

        SolicitudCancelacion sol = solicitudesLista.get(fila);
        Evento evento = sol.getEvento();

        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════\n");
        sb.append("  SOLICITUD #").append(fila + 1).append("\n");
        sb.append("══════════════════════\n\n");

        sb.append("EVENTO:\n");
        sb.append("───────────────────\n");
        if (evento != null) {
            sb.append("Nombre: ").append(evento.getNombre()).append("\n");
            sb.append("Fecha: ").append(evento.getFecha()).append("\n");
            sb.append("Hora: ").append(evento.getHora()).append("\n");

            if (evento.getVenueAsociado() != null) {
                sb.append("Venue: ").append(evento.getVenueAsociado().getUbicacion()).append("\n");
                sb.append("Capacidad: ").append(evento.getVenueAsociado().getCapacidadMax()).append("\n");
            } else {
                sb.append("Venue: No asignado\n");
            }

            sb.append("Tiquetes: ").append(evento.getTiquetesDisponibles().size()).append("\n");
        } else {
            sb.append("Evento no disponible\n");
        }

        sb.append("\nSOLICITANTE:\n");
        sb.append("───────────────────\n");
        sb.append("Organizador: ").append(sol.getLoginOrganizador()).append("\n");
        sb.append("Fecha solicitud: ").append(sol.getFechaSolicitud()).append("\n");
        sb.append("Estado: ").append(sol.getEstado().toUpperCase()).append("\n");

        sb.append("\nMOTIVO:\n");
        sb.append("───────────────────\n");
        sb.append(sol.getMotivo()).append("\n");

        sb.append("\n⚠ AL APROBAR:\n");
        sb.append("───────────────────\n");
        sb.append("• Evento será\n  CANCELADO\n");
        sb.append("• Tiquetes serán\n  ANULADOS\n");
        sb.append("• Acción\n  IRREVERSIBLE\n");

        txtDetalle.setText(sb.toString());
    }

    /**
     * Procesa las solicitudes seleccionadas
     * @param aprobar true para aprobar, false para rechazar
     */
    private void procesarSolicitudes(boolean aprobar) {
        // 1. Obtener filas seleccionadas
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
        String accion = aprobar ? "APROBAR (CANCELAR eventos)" : "RECHAZAR";
        String advertencia = aprobar ? 
            "\n\n⚠ ADVERTENCIA: Los eventos serán CANCELADOS\ny los tiquetes ANULADOS permanentemente." : "";
            
        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("¿Confirmas que deseas %s %d solicitud(es)?%s",
                accion, filasSeleccionadas.size(), advertencia),
            "Confirmar " + (aprobar ? "Aprobación" : "Rechazo"),
            JOptionPane.YES_NO_OPTION,
            aprobar ? JOptionPane.WARNING_MESSAGE : JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int procesadas = 0;
        int errores = 0;

        // Procesar en orden inverso para no desincronizar índices
        for (int i = filasSeleccionadas.size() - 1; i >= 0; i--) {
            int fila = filasSeleccionadas.get(i);

            if (fila < 0 || fila >= solicitudesLista.size()) continue;

            SolicitudCancelacion sol = solicitudesLista.get(fila);
            Evento evento = sol.getEvento();

            try {
                if (aprobar) {
                    // APROBAR: Cancelar el evento
                    evento.setCancelado(true);
                    
                    // Anular todos los tiquetes
                    ArrayList<Tiquete> tiquetes = new ArrayList<>(evento.getTiquetesDisponibles());
                    for (Tiquete t : tiquetes) {
                        t.setAnulado(true);
                    }
                    
                    // Actualizar el evento en la lista del sistema
                    List<Evento> listaEventos = sistema.getEventos();
                    for (int j = 0; j < listaEventos.size(); j++) {
                        if (listaEventos.get(j).getNombre().equals(evento.getNombre())) {
                            listaEventos.set(j, evento);
                            break;
                        }
                    }

                    // Cambiar estado de la solicitud
                    sol.setEstado("cancelado");
                } else {
                    // RECHAZAR: Solo cambiar estado
                    sol.setEstado("negado");
                }

                // Remover la solicitud de la cola
                admin.getSolicitudesCancelacionEvento().remove(sol);
                
                procesadas++;
                
            } catch (Exception ex) {
                errores++;
                JOptionPane.showMessageDialog(this,
                    "Error al procesar solicitud #" + (fila + 1) + ":\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        // Guardar cambios
        sistema.guardarTodo();

        // Mostrar resultado
        String mensaje;
        if (aprobar) {
            mensaje = String.format("✓ Se aprobaron %d solicitud(es)\n\nEventos CANCELADOS exitosamente", procesadas);
        } else {
            mensaje = String.format("❌ Se rechazaron %d solicitud(es)\n\nEventos permanecen ACTIVOS", procesadas);
        }

        if (errores > 0) {
            mensaje += String.format("\n\n⚠ %d solicitud(es) con errores", errores);
        }

        JOptionPane.showMessageDialog(this, mensaje,
            aprobar ? "Aprobación Exitosa" : "Rechazo Completado",
            JOptionPane.INFORMATION_MESSAGE);

        // Recargar tabla
        cargarSolicitudes();
    }

    /**
     * Trunca un texto a la longitud máxima especificada
     */
    private String truncar(String texto, int maxLength) {
        if (texto == null) return "";
        if (texto.length() <= maxLength) return texto;
        return texto.substring(0, maxLength - 3) + "...";
    }
}