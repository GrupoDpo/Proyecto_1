package interfaz;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;

import Evento.Evento;
import Finanzas.EstadosFinancieros;
import Persistencia.SistemaPersistencia;
import tiquete.Tiquete;
import usuario.Organizador;

public class ventanaVerGananciasOrganizador extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private Organizador organizador;
    private SistemaPersistencia sistema;
    
    private JLabel lblGananciaTotal;
    private JLabel lblTiquetesVendidos;
    private JLabel lblTiquetesDisponibles;
    private JLabel lblPorcentajeVentas;
    
    private JTable tablaPorEvento;
    
    public ventanaVerGananciasOrganizador(Organizador organizador, SistemaPersistencia sistema) {
        this.organizador = organizador;
        this.sistema = sistema;
        
        inicializarComponentes();
        cargarDatos();
    }
    
    private void inicializarComponentes() {
        setTitle("Ganancias del Organizador - " + organizador.getLogin());
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        
        // ============================
        // BARRA SUPERIOR
        // ============================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(230, 126, 34));
        panelSuperior.setBounds(0, 0, 900, 60);
        panelSuperior.setLayout(null);
        add(panelSuperior);
        
        JLabel lblTitulo = new JLabel("ESTADO FINANCIERO DEL ORGANIZADOR");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(30, 15, 500, 30);
        panelSuperior.add(lblTitulo);
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 12));
        btnActualizar.setBounds(750, 15, 120, 30);
        btnActualizar.addActionListener(e -> cargarDatos());
        panelSuperior.add(btnActualizar);
        
        // ============================
        // PANEL RESUMEN
        // ============================
        JPanel panelResumen = new JPanel();
        panelResumen.setBackground(Color.WHITE);
        panelResumen.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(230, 126, 34), 2),
            "Resumen General",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(230, 126, 34)
        ));
        panelResumen.setBounds(30, 80, 840, 140);
        panelResumen.setLayout(null);
        add(panelResumen);
        
        // Ganancia Total
        JLabel lblTxtTotal = new JLabel("Ganancia Total:");
        lblTxtTotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblTxtTotal.setBounds(30, 30, 150, 25);
        panelResumen.add(lblTxtTotal);
        
        lblGananciaTotal = new JLabel("$0.00");
        lblGananciaTotal.setFont(new Font("Arial", Font.BOLD, 20));
        lblGananciaTotal.setForeground(new Color(39, 174, 96));
        lblGananciaTotal.setBounds(30, 60, 250, 35);
        panelResumen.add(lblGananciaTotal);
        
        // Porcentaje de Ventas
        JLabel lblTxtPorcentaje = new JLabel("Porcentaje de Ventas:");
        lblTxtPorcentaje.setFont(new Font("Arial", Font.BOLD, 14));
        lblTxtPorcentaje.setBounds(450, 30, 180, 25);
        panelResumen.add(lblTxtPorcentaje);
        
        lblPorcentajeVentas = new JLabel("0%");
        lblPorcentajeVentas.setFont(new Font("Arial", Font.BOLD, 20));
        lblPorcentajeVentas.setForeground(new Color(52, 152, 219));
        lblPorcentajeVentas.setBounds(450, 60, 150, 35);
        panelResumen.add(lblPorcentajeVentas);
        
        // Tiquetes Vendidos
        JLabel lblTxtVendidos = new JLabel("Tiquetes Vendidos:");
        lblTxtVendidos.setFont(new Font("Arial", Font.BOLD, 13));
        lblTxtVendidos.setBounds(30, 105, 150, 20);
        panelResumen.add(lblTxtVendidos);
        
        lblTiquetesVendidos = new JLabel("0");
        lblTiquetesVendidos.setFont(new Font("Arial", Font.BOLD, 16));
        lblTiquetesVendidos.setForeground(new Color(46, 204, 113));
        lblTiquetesVendidos.setBounds(190, 105, 100, 20);
        panelResumen.add(lblTiquetesVendidos);
        
        // Tiquetes Disponibles
        JLabel lblTxtDisponibles = new JLabel("Tiquetes Disponibles:");
        lblTxtDisponibles.setFont(new Font("Arial", Font.BOLD, 13));
        lblTxtDisponibles.setBounds(320, 105, 180, 20);
        panelResumen.add(lblTxtDisponibles);
        
        lblTiquetesDisponibles = new JLabel("0");
        lblTiquetesDisponibles.setFont(new Font("Arial", Font.BOLD, 16));
        lblTiquetesDisponibles.setForeground(new Color(241, 196, 15));
        lblTiquetesDisponibles.setBounds(510, 105, 100, 20);
        panelResumen.add(lblTiquetesDisponibles);
        
        // ============================
        // TABLA POR EVENTO
        // ============================
        JLabel lblEvento = new JLabel("Detalle por Evento:");
        lblEvento.setFont(new Font("Arial", Font.BOLD, 14));
        lblEvento.setBounds(30, 240, 250, 25);
        add(lblEvento);
        
        String[] columnasEvento = {"Evento", "Ganancia", "Vendidos", "Disponibles", "% Venta"};
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
        
        // Configurar anchos de columnas
        tablaPorEvento.getColumnModel().getColumn(0).setPreferredWidth(300);
        tablaPorEvento.getColumnModel().getColumn(1).setPreferredWidth(120);
        tablaPorEvento.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaPorEvento.getColumnModel().getColumn(3).setPreferredWidth(100);
        tablaPorEvento.getColumnModel().getColumn(4).setPreferredWidth(80);
        
        JScrollPane scrollEvento = new JScrollPane(tablaPorEvento);
        scrollEvento.setBounds(30, 270, 840, 250);
        add(scrollEvento);
        
        // Alinear números a la derecha
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        for (int i = 1; i < 5; i++) {
            tablaPorEvento.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }
        
        // ============================
        // BOTONES INFERIORES
        // ============================
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCerrar.setBackground(new Color(231, 76, 60));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBounds(720, 550, 150, 40);
        btnCerrar.addActionListener(e -> volverAlMenu());
        add(btnCerrar);
        
        // Nota informativa
        JLabel lblNota = new JLabel("<html><i>Las ganancias corresponden al precio base de los tiquetes (sin recargos)</i></html>");
        lblNota.setFont(new Font("Arial", Font.PLAIN, 11));
        lblNota.setForeground(Color.GRAY);
        lblNota.setBounds(30, 550, 600, 30);
        add(lblNota);
    }
    
    private void cargarDatos() {
        // Recalcular ganancias
        organizador.verGanancias(sistema.getAdministrador().getCobroEmision());
        
        // Obtener estados financieros
        EstadosFinancieros ef = organizador.getEstadosFinancieros();
        
        if (ef != null) {
            lblGananciaTotal.setText(String.format("$%,.2f", ef.getGanancias()));
        }
        
        // Contar tiquetes
        int vendidos = 0;
        int disponibles = 0;
        
        for (Tiquete t : organizador.getTiquetes()) {
            if (t.isTransferido()) {
                vendidos++;
            } else if (!t.isAnulado()) {
                disponibles++;
            }
        }
        
        lblTiquetesVendidos.setText(String.valueOf(vendidos));
        lblTiquetesDisponibles.setText(String.valueOf(disponibles));
        
        // Calcular porcentaje global
        int total = vendidos + disponibles;
        double porcentaje = 0.0;
        if (total > 0) {
            porcentaje = (double) vendidos / total * 100.0;
        }
        lblPorcentajeVentas.setText(String.format("%.1f%%", porcentaje));
        
        // Cargar tabla
        cargarTablaPorEvento();
    }
    
    private void cargarTablaPorEvento() {
        DefaultTableModel modelo = (DefaultTableModel) tablaPorEvento.getModel();
        modelo.setRowCount(0);
        
        HashMap<Evento, Double> gananciaPorEvento = new HashMap<>();
        HashMap<Evento, Integer> vendidosPorEvento = new HashMap<>();
        HashMap<Evento, Integer> disponiblesPorEvento = new HashMap<>();
        
        for (Tiquete t : organizador.getTiquetes()) {
            if (t.getEvento() != null) {
                Evento evento = t.getEvento();
                double precioBase = t.getPrecioBaseSinCalcular();
                
                if (t.isTransferido()) {
                    gananciaPorEvento.put(evento, 
                        gananciaPorEvento.getOrDefault(evento, 0.0) + precioBase);
                    vendidosPorEvento.put(evento, 
                        vendidosPorEvento.getOrDefault(evento, 0) + 1);
                } else if (!t.isAnulado()) {
                    disponiblesPorEvento.put(evento, 
                        disponiblesPorEvento.getOrDefault(evento, 0) + 1);
                }
            }
        }
        
        // Consolidar eventos únicos
        HashMap<Evento, Object[]> datosEvento = new HashMap<>();
        
        for (Tiquete t : organizador.getTiquetes()) {
            if (t.getEvento() != null) {
                Evento evento = t.getEvento();
                if (!datosEvento.containsKey(evento)) {
                    double ganancia = gananciaPorEvento.getOrDefault(evento, 0.0);
                    int vendidos = vendidosPorEvento.getOrDefault(evento, 0);
                    int disponibles = disponiblesPorEvento.getOrDefault(evento, 0);
                    int total = vendidos + disponibles;
                    double porcentaje = total > 0 ? (double) vendidos / total * 100.0 : 0.0;
                    
                    datosEvento.put(evento, new Object[]{
                        evento.getNombre(),
                        String.format("$%,.2f", ganancia),
                        vendidos,
                        disponibles,
                        String.format("%.1f%%", porcentaje)
                    });
                }
            }
        }
        
        for (Object[] fila : datosEvento.values()) {
            modelo.addRow(fila);
        }
        
        if (modelo.getRowCount() == 0) {
            modelo.addRow(new Object[]{"Sin datos", "$0.00", "0", "0", "0%"});
        }
    }
    
    private void volverAlMenu() {
        
    	
   	 dispose();
        new ventanaMenuOrganizador( organizador, sistema).setVisible(true);
          
                
       }
}
