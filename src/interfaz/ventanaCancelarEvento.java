package interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

import Evento.Evento;
import Persistencia.SistemaPersistencia;
import usuario.Administrador;

/**
 * Ventana para que el administrador cancele eventos
 * Al cancelar un evento, todos sus tiquetes quedan anulados
 */
public class ventanaCancelarEvento extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private Administrador admin;
    private SistemaPersistencia sistema;
    
    private JTable tablaEventos;
    private JButton btnCancelar;
    private JLabel lblTotalEventos;
    
    public ventanaCancelarEvento(Administrador admin, SistemaPersistencia sistema) {
        this.admin = admin;
        this.sistema = sistema;
        
        inicializarComponentes();
        cargarEventos();
    }
    
    private void inicializarComponentes() {
        setTitle("Cancelar Evento - Administrador");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        
        // ============================
        // BARRA SUPERIOR
        // ============================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(192, 57, 43));
        panelSuperior.setBounds(0, 0, 900, 60);
        panelSuperior.setLayout(null);
        add(panelSuperior);
        
        JLabel lblTitulo = new JLabel("CANCELAR EVENTO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(30, 15, 300, 30);
        panelSuperior.add(lblTitulo);
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnActualizar.setBounds(750, 15, 120, 30);
        btnActualizar.addActionListener(e -> cargarEventos());
        panelSuperior.add(btnActualizar);
        
        // ============================
        // PANEL RESUMEN
        // ============================
        JPanel panelResumen = new JPanel();
        panelResumen.setBackground(Color.WHITE);
        panelResumen.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(192, 57, 43), 2),
            "Eventos Activos",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(192, 57, 43)
        ));
        panelResumen.setBounds(30, 80, 840, 70);
        panelResumen.setLayout(null);
        add(panelResumen);
        
        lblTotalEventos = new JLabel("0 eventos disponibles");
        lblTotalEventos.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalEventos.setForeground(new Color(41, 128, 185));
        lblTotalEventos.setBounds(30, 25, 400, 25);
        panelResumen.add(lblTotalEventos);
        
        JLabel lblInfo = new JLabel("(Seleccione un evento para cancelarlo)");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setBounds(450, 25, 350, 25);
        panelResumen.add(lblInfo);
        
        // ============================
        // TABLA DE EVENTOS
        // ============================
        JLabel lblTabla = new JLabel("Eventos Activos:");
        lblTabla.setFont(new Font("Arial", Font.BOLD, 14));
        lblTabla.setBounds(30, 165, 250, 25);
        add(lblTabla);
        
        String[] columnas = {"Nombre", "Fecha", "Hora", "Organizador", "Venue"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaEventos = new JTable(modeloTabla);
        tablaEventos.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaEventos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaEventos.setRowHeight(25);
        tablaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Configurar anchos de columnas
        tablaEventos.getColumnModel().getColumn(0).setPreferredWidth(250);  // Nombre
        tablaEventos.getColumnModel().getColumn(1).setPreferredWidth(100);  // Fecha
        tablaEventos.getColumnModel().getColumn(2).setPreferredWidth(80);   // Hora
        tablaEventos.getColumnModel().getColumn(3).setPreferredWidth(150);  // Organizador
        tablaEventos.getColumnModel().getColumn(4).setPreferredWidth(200);  // Venue
        
        // Centrar algunas columnas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaEventos.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tablaEventos.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        JScrollPane scrollTabla = new JScrollPane(tablaEventos);
        scrollTabla.setBounds(30, 195, 840, 280);
        add(scrollTabla);
        
        // Listener para habilitar botón al seleccionar
        tablaEventos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnCancelar.setEnabled(tablaEventos.getSelectedRow() != -1);
            }
        });
        
        // ============================
        // PANEL DE ADVERTENCIA
        // ============================
        JPanel panelAdvertencia = new JPanel();
        panelAdvertencia.setBackground(new Color(255, 243, 205));
        panelAdvertencia.setBorder(BorderFactory.createLineBorder(new Color(243, 156, 18), 2));
        panelAdvertencia.setBounds(30, 490, 840, 50);
        panelAdvertencia.setLayout(null);
        add(panelAdvertencia);
        
        JLabel lblAdvertencia = new JLabel("⚠️ ADVERTENCIA: Al cancelar un evento, todos sus tiquetes quedarán anulados permanentemente");
        lblAdvertencia.setFont(new Font("Arial", Font.BOLD, 12));
        lblAdvertencia.setForeground(new Color(175, 96, 26));
        lblAdvertencia.setBounds(20, 15, 800, 20);
        panelAdvertencia.add(lblAdvertencia);
        
        // ============================
        // BOTONES INFERIORES
        // ============================
        btnCancelar = new JButton("Cancelar Evento");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 13));
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBounds(30, 555, 200, 40);
        btnCancelar.setEnabled(false);
        btnCancelar.addActionListener(e -> cancelarEventoSeleccionado());
        add(btnCancelar);
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCerrar.setBackground(new Color(149, 165, 166));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBounds(720, 555, 150, 40);
        btnCerrar.addActionListener(e -> {dispose();
        new ventanaMenuAdministrador(admin, sistema).setVisible(true);});
        add(btnCerrar);
    }
    
    private void cargarEventos() {
        DefaultTableModel modelo = (DefaultTableModel) tablaEventos.getModel();
        modelo.setRowCount(0);
        
        List<Evento> eventos = sistema.getEventos();
        
        if (eventos == null || eventos.isEmpty()) {
            lblTotalEventos.setText("0 eventos disponibles");
            return;
        }
        
        // Filtrar solo eventos NO cancelados
        List<Evento> eventosActivos = new ArrayList<>();
        for (Evento e : eventos) {
            if (!e.getCancelado()) {
                eventosActivos.add(e);
            }
        }
        
        lblTotalEventos.setText(eventosActivos.size() + " eventos disponibles");
        
        for (Evento evento : eventosActivos) {
            String nombre = evento.getNombre();
            String fecha = evento.getFecha();
            String hora = evento.getHora();
            String organizador = evento.getLoginOrganizador();
            String venue = evento.getVenueAsociado() != null ? 
                          evento.getVenueAsociado().getUbicacion() : "Sin venue";
            
            modelo.addRow(new Object[]{nombre, fecha, hora, organizador, venue});
        }
        
        if (eventosActivos.isEmpty()) {
            modelo.addRow(new Object[]{"No hay eventos activos", "--", "--", "--", "--"});
        }
    }
    
    private void cancelarEventoSeleccionado() {
        int filaSeleccionada = tablaEventos.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione un evento",
                "Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Obtener el nombre del evento de la tabla
        String nombreEvento = (String) tablaEventos.getValueAt(filaSeleccionada, 0);
        
        // Buscar el evento en el sistema
        Evento eventoSeleccionado = null;
        for (Evento e : sistema.getEventos()) {
            if (e.getNombre().equals(nombreEvento) && !e.getCancelado()) {
                eventoSeleccionado = e;
                break;
            }
        }
        
        if (eventoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                "No se encontró el evento seleccionado",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Obtener información del evento
        String fecha = eventoSeleccionado.getFecha();
        String hora = eventoSeleccionado.getHora();
        int totalTiquetes = eventoSeleccionado.getTiquetesDisponibles().size();
        
        // Confirmar cancelación
        String mensaje = String.format(
            "¿Está seguro de cancelar el siguiente evento?\n\n" +
            "Nombre: %s\n" +
            "Fecha: %s\n" +
            "Hora: %s\n" +
            "Organizador: %s\n" +
            "Total de tiquetes: %d\n\n" +
            "⚠️ TODOS los tiquetes de este evento quedarán ANULADOS",
            nombreEvento, fecha, hora, 
            eventoSeleccionado.getLoginOrganizador(),
            totalTiquetes
        );
        
        int confirmacion = JOptionPane.showConfirmDialog(this,
            mensaje,
            "Confirmar Cancelación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }
        
        // Cancelar el evento usando el método del administrador
        admin.cancelarEvento(eventoSeleccionado);
        
        // Guardar cambios
        sistema.guardarTodo();
        
        // Mostrar confirmación
        JOptionPane.showMessageDialog(this,
            String.format(
                "✓ Evento cancelado exitosamente\n\n" +
                "Evento: %s\n" +
                "Tiquetes anulados: %d",
                nombreEvento, totalTiquetes
            ),
            "Cancelación Exitosa",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Recargar tabla
        cargarEventos();
        btnCancelar.setEnabled(false);
    }
}
