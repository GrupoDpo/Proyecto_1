package interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Map;

import Evento.Evento;
import Finanzas.EstadosFinancieros;
import Persistencia.SistemaPersistencia;
import tiquete.Tiquete;
import usuario.Administrador;

public class ventanaVerGananciasAdministrador extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private Administrador admin;
    private SistemaPersistencia sistema;
    
    private JLabel lblGananciaTotal;
    private JLabel lblTiquetesVendidos;
    private JLabel lblPorcentajeActual;
    private JLabel lblCobroEmision;
    
    private JTable tablaPorFecha;
    private JTable tablaPorEvento;
    private JTable tablaPorOrganizador;
    
    public ventanaVerGananciasAdministrador(Administrador admin, SistemaPersistencia sistema) {
        this.admin = admin;
        this.sistema = sistema;
        
        inicializarComponentes();
        cargarDatos();
    }
    
    private void inicializarComponentes() {
        setTitle("Ganancias de la Tiquetera - Administrador");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        
        // ============================
        // BARRA SUPERIOR
        // ============================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(41, 128, 185));
        panelSuperior.setBounds(0, 0, 1100, 60);
        panelSuperior.setLayout(null);
        add(panelSuperior);
        
        JLabel lblTitulo = new JLabel("GANANCIAS DE LA TIQUETERA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(30, 15, 500, 30);
        panelSuperior.add(lblTitulo);
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnActualizar.setBounds(950, 15, 120, 30);
        btnActualizar.addActionListener(e -> cargarDatos());
        panelSuperior.add(btnActualizar);
        
        // ============================
        // PANEL RESUMEN
        // ============================
        JPanel panelResumen = new JPanel();
        panelResumen.setBackground(Color.WHITE);
        panelResumen.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "Resumen General",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(52, 152, 219)
        ));
        panelResumen.setBounds(30, 80, 1040, 120);
        panelResumen.setLayout(null);
        add(panelResumen);
        
        // Ganancia Total
        JLabel lblTxtTotal = new JLabel("Ganancia Total:");
        lblTxtTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblTxtTotal.setBounds(30, 30, 150, 25);
        panelResumen.add(lblTxtTotal);
        
        lblGananciaTotal = new JLabel("$0.00");
        lblGananciaTotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblGananciaTotal.setForeground(new Color(39, 174, 96));
        lblGananciaTotal.setBounds(30, 55, 200, 30);
        panelResumen.add(lblGananciaTotal);
        
        // Tiquetes Vendidos
        JLabel lblTxtVendidos = new JLabel("Tiquetes Vendidos:");
        lblTxtVendidos.setFont(new Font("Arial", Font.BOLD, 14));
        lblTxtVendidos.setBounds(280, 30, 170, 25);
        panelResumen.add(lblTxtVendidos);
        
        lblTiquetesVendidos = new JLabel("0");
        lblTiquetesVendidos.setFont(new Font("Arial", Font.BOLD, 18));
        lblTiquetesVendidos.setForeground(new Color(52, 152, 219));
        lblTiquetesVendidos.setBounds(280, 55, 150, 30);
        panelResumen.add(lblTiquetesVendidos);
        
        // Porcentaje Actual
        JLabel lblTxtPorcentaje = new JLabel("Recargo Actual:");
        lblTxtPorcentaje.setFont(new Font("Arial", Font.BOLD, 14));
        lblTxtPorcentaje.setBounds(530, 30, 150, 25);
        panelResumen.add(lblTxtPorcentaje);
        
        lblPorcentajeActual = new JLabel("0%");
        lblPorcentajeActual.setFont(new Font("Arial", Font.BOLD, 18));
        lblPorcentajeActual.setForeground(new Color(142, 68, 173));
        lblPorcentajeActual.setBounds(530, 55, 150, 30);
        panelResumen.add(lblPorcentajeActual);
        
        // Cobro Emisión
        JLabel lblTxtEmision = new JLabel("Cobro Emision:");
        lblTxtEmision.setFont(new Font("Arial", Font.BOLD, 14));
        lblTxtEmision.setBounds(780, 30, 150, 25);
        panelResumen.add(lblTxtEmision);
        
        lblCobroEmision = new JLabel("$0.00");
        lblCobroEmision.setFont(new Font("Arial", Font.BOLD, 18));
        lblCobroEmision.setForeground(new Color(230, 126, 34));
        lblCobroEmision.setBounds(780, 55, 150, 30);
        panelResumen.add(lblCobroEmision);
        
        // ============================
        // TABLAS
        // ============================
        
        // Tabla por Fecha
        JLabel lblFecha = new JLabel("Ganancias por Fecha:");
        lblFecha.setFont(new Font("Arial", Font.BOLD, 13));
        lblFecha.setBounds(30, 215, 250, 20);
        add(lblFecha);
        
        String[] columnasFecha = {"Fecha", "Ganancia"};
        DefaultTableModel modeloFecha = new DefaultTableModel(columnasFecha, 0) {
            private static final long serialVersionUID = 1L;
			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPorFecha = new JTable(modeloFecha);
        tablaPorFecha.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaPorFecha.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scrollFecha = new JScrollPane(tablaPorFecha);
        scrollFecha.setBounds(30, 240, 330, 200);
        add(scrollFecha);
        
        // Tabla por Evento
        JLabel lblEvento = new JLabel("Ganancias por Evento:");
        lblEvento.setFont(new Font("Arial", Font.BOLD, 13));
        lblEvento.setBounds(385, 215, 250, 20);
        add(lblEvento);
        
        String[] columnasEvento = {"Evento", "Ganancia"};
        DefaultTableModel modeloEvento = new DefaultTableModel(columnasEvento, 0) {
            private static final long serialVersionUID = 1L;
			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPorEvento = new JTable(modeloEvento);
        tablaPorEvento.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaPorEvento.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scrollEvento = new JScrollPane(tablaPorEvento);
        scrollEvento.setBounds(385, 240, 330, 200);
        add(scrollEvento);
        
        // Tabla por Organizador
        JLabel lblOrganizador = new JLabel("Ganancias por Organizador:");
        lblOrganizador.setFont(new Font("Arial", Font.BOLD, 13));
        lblOrganizador.setBounds(740, 215, 250, 20);
        add(lblOrganizador);
        
        String[] columnasOrg = {"Organizador", "Ganancia"};
        DefaultTableModel modeloOrg = new DefaultTableModel(columnasOrg, 0) {
            private static final long serialVersionUID = 1L;
			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPorOrganizador = new JTable(modeloOrg);
        tablaPorOrganizador.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaPorOrganizador.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        JScrollPane scrollOrg = new JScrollPane(tablaPorOrganizador);
        scrollOrg.setBounds(740, 240, 330, 200);
        add(scrollOrg);
        
        // Alinear números a la derecha
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        tablaPorFecha.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tablaPorEvento.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        tablaPorOrganizador.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        
        // ============================
        // BOTONES INFERIORES
        // ============================
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCerrar.setBackground(new Color(231, 76, 60));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBounds(920, 660, 150, 40);
        btnCerrar.addActionListener(e -> {
            dispose();
            new ventanaMenuAdministrador(admin, sistema).setVisible(true);
        });
        add(btnCerrar);
        
        // Nota informativa
        JLabel lblNota = new JLabel("<html><i>Las ganancias mostradas corresponden a recargos y cobro de emision</i></html>");
        lblNota.setFont(new Font("Arial", Font.PLAIN, 11));
        lblNota.setForeground(Color.GRAY);
        lblNota.setBounds(30, 460, 600, 30);
        add(lblNota);
    }
    
    private void cargarDatos() {
        // Recalcular ganancias
        admin.verGananciasAdministrador(sistema.getTiquetes());
        
        // Obtener estados financieros
        EstadosFinancieros ef = admin.getEstadosFinancieros();
        
        if (ef != null) {
            lblGananciaTotal.setText(String.format("$%,.2f", ef.getGanancias()));
        }
        
        // Configuración actual
        lblPorcentajeActual.setText(String.format("%.1f%%", admin.getPorcentajeAdicional()));
        lblCobroEmision.setText(String.format("$%,.2f", admin.getCobroEmision()));
        
        // Contar tiquetes vendidos
        int vendidos = 0;
        for (Tiquete t : sistema.getTiquetes()) {
            if (t.isTransferido()) {
                vendidos++;
            }
        }
        lblTiquetesVendidos.setText(String.valueOf(vendidos));
        
        // Cargar tablas
        cargarTablaPorFecha();
        cargarTablaPorEvento();
        cargarTablaPorOrganizador();
    }
    
    private void cargarTablaPorFecha() {
        DefaultTableModel modelo = (DefaultTableModel) tablaPorFecha.getModel();
        modelo.setRowCount(0);
        
        HashMap<String, Double> porFecha = new HashMap<>();
        
        for (Tiquete t : sistema.getTiquetes()) {
            if (t.isTransferido() && t.getEvento() != null) {
                String fecha = t.getEvento().getFecha();
                double ganancia = t.getRecargo() + admin.getCobroEmision();
                porFecha.put(fecha, porFecha.getOrDefault(fecha, 0.0) + ganancia);
            }
        }
        
        for (Map.Entry<String, Double> entry : porFecha.entrySet()) {
            modelo.addRow(new Object[]{
                entry.getKey(),
                String.format("$%,.2f", entry.getValue())
            });
        }
        
        if (modelo.getRowCount() == 0) {
            modelo.addRow(new Object[]{"Sin datos", "$0.00"});
        }
    }
    
    private void cargarTablaPorEvento() {
        DefaultTableModel modelo = (DefaultTableModel) tablaPorEvento.getModel();
        modelo.setRowCount(0);
        
        HashMap<Evento, Double> porEvento = new HashMap<>();
        
        for (Tiquete t : sistema.getTiquetes()) {
            if (t.isTransferido() && t.getEvento() != null) {
                Evento evento = t.getEvento();
                double ganancia = t.getRecargo() + admin.getCobroEmision();
                porEvento.put(evento, porEvento.getOrDefault(evento, 0.0) + ganancia);
            }
        }
        
        for (Map.Entry<Evento, Double> entry : porEvento.entrySet()) {
            modelo.addRow(new Object[]{
                entry.getKey().getNombre(),
                String.format("$%,.2f", entry.getValue())
            });
        }
        
        if (modelo.getRowCount() == 0) {
            modelo.addRow(new Object[]{"Sin datos", "$0.00"});
        }
    }
    
    private void cargarTablaPorOrganizador() {
        DefaultTableModel modelo = (DefaultTableModel) tablaPorOrganizador.getModel();
        modelo.setRowCount(0);
        
        HashMap<String, Double> porOrganizador = new HashMap<>();
        
        for (Tiquete t : sistema.getTiquetes()) {
            if (t.isTransferido() && t.getEvento() != null) {
                String organizador = t.getEvento().getLoginOrganizador();
                double ganancia = t.getRecargo() + admin.getCobroEmision();
                porOrganizador.put(organizador, porOrganizador.getOrDefault(organizador, 0.0) + ganancia);
            }
        }
        
        for (Map.Entry<String, Double> entry : porOrganizador.entrySet()) {
            modelo.addRow(new Object[]{
                entry.getKey(),
                String.format("$%,.2f", entry.getValue())
            });
        }
        
        if (modelo.getRowCount() == 0) {
            modelo.addRow(new Object[]{"Sin datos", "$0.00"});
        }
    }
    
    
}
