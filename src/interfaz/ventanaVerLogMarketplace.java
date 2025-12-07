package interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.List;

import Finanzas.marketPlaceReventas;
import Persistencia.SistemaPersistencia;
import usuario.Administrador;

/**
 * Ventana simple para que el administrador vea el log del marketplace
 * SOLO VISUALIZACIÓN - Sin funciones de exportar o modificar
 */
public class ventanaVerLogMarketplace extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private Administrador admin;
    private SistemaPersistencia sistema;
    private marketPlaceReventas marketplace;
    
    private JTable tablaLog;
    private JLabel lblTotalEventos;
    
    public ventanaVerLogMarketplace(Administrador admin, SistemaPersistencia sistema) {
        this.admin = admin;
        this.sistema = sistema;
        this.marketplace = sistema.getMarketplace();
        
        inicializarComponentes();
        cargarLog();
    }
    
    private void inicializarComponentes() {
        setTitle("Log de Marketplace");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        
        // ============================
        // BARRA SUPERIOR
        // ============================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(52, 73, 94));
        panelSuperior.setBounds(0, 0, 950, 60);
        panelSuperior.setLayout(null);
        add(panelSuperior);
        
        JLabel lblTitulo = new JLabel("LOG MARKETPLACE");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(30, 15, 300, 30);
        panelSuperior.add(lblTitulo);
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnActualizar.setBounds(800, 15, 120, 30);
        btnActualizar.addActionListener(e -> cargarLog());
        panelSuperior.add(btnActualizar);
        
        // ============================
        // PANEL RESUMEN
        // ============================
        JPanel panelResumen = new JPanel();
        panelResumen.setBackground(Color.WHITE);
        panelResumen.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 73, 94), 2),
            "Total de Eventos",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(52, 73, 94)
        ));
        panelResumen.setBounds(30, 80, 890, 70);
        panelResumen.setLayout(null);
        add(panelResumen);
        
        lblTotalEventos = new JLabel("0 eventos registrados");
        lblTotalEventos.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalEventos.setForeground(new Color(41, 128, 185));
        lblTotalEventos.setBounds(30, 25, 400, 25);
        panelResumen.add(lblTotalEventos);
        
        // ============================
        // TABLA DE LOG
        // ============================
        JLabel lblTabla = new JLabel("Historial:");
        lblTabla.setFont(new Font("Arial", Font.BOLD, 14));
        lblTabla.setBounds(30, 165, 250, 25);
        add(lblTabla);
        
        String[] columnas = {"#", "Evento"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaLog = new JTable(modeloTabla);
        tablaLog.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaLog.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaLog.setRowHeight(25);
        
        // Configurar anchos de columnas
        tablaLog.getColumnModel().getColumn(0).setPreferredWidth(50);   // #
        tablaLog.getColumnModel().getColumn(1).setPreferredWidth(800);  // Evento
        
        // Centrar columna de número
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaLog.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        
        JScrollPane scrollTabla = new JScrollPane(tablaLog);
        scrollTabla.setBounds(30, 195, 890, 310);
        add(scrollTabla);
        
        // ============================
        // BOTÓN CERRAR
        // ============================
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCerrar.setBackground(new Color(231, 76, 60));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBounds(770, 520, 150, 35);
        btnCerrar.addActionListener(e -> {
        	dispose();
        	new ventanaMenuAdministrador(admin, sistema).setVisible(true);
        	
        });
        add(btnCerrar);
    }
    
    private void cargarLog() {
        DefaultTableModel modelo = (DefaultTableModel) tablaLog.getModel();
        modelo.setRowCount(0);
        
        if (marketplace == null) {
            JOptionPane.showMessageDialog(this,
                "No se pudo cargar el marketplace",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<String> logEventos = marketplace.getLogEventos();
        
        if (logEventos == null || logEventos.isEmpty()) {
            lblTotalEventos.setText("0 eventos registrados");
            modelo.addRow(new Object[]{"--", "No hay eventos registrados"});
            return;
        }
        
        lblTotalEventos.setText(logEventos.size() + " eventos registrados");
        
        int i = 1;
        for (String evento : logEventos) {
            modelo.addRow(new Object[]{i, evento});
            i++;
        }
    }
}