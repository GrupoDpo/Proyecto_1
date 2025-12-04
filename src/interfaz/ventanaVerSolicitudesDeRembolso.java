package interfaz;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

public class ventanaVerSolicitudesDeRembolso extends JFrame {

    private static final long serialVersionUID = 1L;

    public ventanaVerSolicitudesDeRembolso() {

        setTitle("BOLETAMASTER: Solicitudes de Reembolso");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // ---------------- DATOS DE EJEMPLO ----------------
        Object[][] datos = {
                { false, "REM-101", "cliente5", "Concierto Rock Bogotá 2025", "Evento cancelado" },
                { false, "REM-102", "cliente6", "Jazz Medellín", "Problema con pago" },
                { false, "REM-103", "cliente7", "Comic Con", "No recibió tiquete" },
                { false, "REM-104", "cliente8", "Feria Libro", "Error fecha" }
        };

        String[] columnas = { "Aceptar", "ID", "Usuario", "Evento", "Motivo" };

        // -------------- TABLA SWING PURA -----------------
        JTable tabla = new JTable(datos, columnas) {

            @Override
            public Class<?> getColumnClass(int col) {
                if (col == 0) return Boolean.class; // checkbox automático
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 0; // solo columna "Aceptar"
            }
        };

        JScrollPane scroll = new JScrollPane(tabla);

        // -------- CHECK DE "Seleccionar todas" -----------
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JCheckBox seleccionarTodas = new JCheckBox("Seleccionar todas");
        panelSuperior.add(seleccionarTodas);

        seleccionarTodas.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean marcar = (e.getStateChange() == ItemEvent.SELECTED);
                for (int i = 0; i < tabla.getRowCount(); i++) {
                    tabla.setValueAt(marcar, i, 0);
                }
            }
        });

        // -------- BOTÓN INFERIOR ---------
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton procesar = new JButton("Procesar seleccionados");
        panelInferior.add(procesar);

        // -------- ARMAR VENTANA ----------
        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        new ventanaVerSolicitudesDeRembolso();
    }
}
