package interfaz;

import javax.swing.*;

import Persistencia.SistemaPersistencia;
import usuario.Administrador;
import usuario.Organizador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaCrearVenue extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtUbicacion;
    private JSpinner spinnerCapacidad;
    private JCheckBox chkAprobado;
    private SistemaPersistencia sistema;
    private Administrador admin;

    public VentanaCrearVenue(SistemaPersistencia sistema, Administrador admin) {
    	this.sistema = sistema;
    	this.admin = admin;
        setLayout(null);

        // -------------------------
        // PANEL SUPERIOR (DECORATIVO)
        // -------------------------
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 10);
        add(panel);

        // -------------------------
        // UBICACIÓN
        // -------------------------
        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setBounds(60, 40, 100, 20);
        add(lblUbicacion);

        txtUbicacion = new JTextField();
        txtUbicacion.setBounds(160, 38, 150, 26);
        add(txtUbicacion);

        // -------------------------
        // CAPACIDAD
        // -------------------------
        JLabel lblCapacidad = new JLabel("Capacidad:");
        lblCapacidad.setBounds(60, 72, 100, 20);
        add(lblCapacidad);

        spinnerCapacidad = new JSpinner(new SpinnerNumberModel(0, 0, 1000000, 10));
        spinnerCapacidad.setBounds(160, 68, 100, 26);
        add(spinnerCapacidad);

        // -------------------------
        // APROBADO
        // -------------------------
        chkAprobado = new JCheckBox("Aprobado");
        chkAprobado.setBounds(60, 100, 150, 25);
        add(chkAprobado);

        // -------------------------
        // BOTÓN SALIR
        // -------------------------
        JButton btnSalir = new JButton("Salir");
        btnSalir.setBounds(10, 140, 90, 26);
        add(btnSalir);

        btnSalir.addActionListener(e -> volverAlMenu());

        // -------------------------
        // BOTÓN CREAR VENUE
        // -------------------------
        JButton btnCrear = new JButton("Crear Venue");
        btnCrear.setBounds(260, 140, 120, 26);
        add(btnCrear);

        btnCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearVenue();
            }
        });

        // -------------------------
        // CONFIGURACIÓN VENTANA
        // -------------------------
        setTitle("BOLETAMASTER: Crear Venue");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 220);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // =====================================================
    // MÉTODO PARA CREAR VENUE — LISTO PARA IMPLEMENTAR
    // =====================================================
    private void crearVenue() {

        String ubicacion = txtUbicacion.getText().trim();
        int capacidad = (int) spinnerCapacidad.getValue();
        boolean aprobado = chkAprobado.isSelected();
        
        
        admin.crearVenue(ubicacion, capacidad, aprobado, sistema);
        if (ubicacion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar una ubicación.");
            return;
        }
        
        

        // Aquí luego conectas con tu lógica real del sistema
        JOptionPane.showMessageDialog(this,
                "Venue creado:\n" +
                "Ubicación: " + ubicacion + "\n" +
                "Capacidad: " + capacidad + "\n" +
                "Aprobado: " + (aprobado ? "Sí" : "No")
        );

      
    }
private void volverAlMenu() {
        
    	
	 	dispose();
       new ventanaMenuAdministrador( admin, sistema).setVisible(true);
       
             
    }
}
