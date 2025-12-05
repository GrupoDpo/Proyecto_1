package interfaz;

import javax.swing.*;

import Evento.Evento;
import Evento.Venue;

import java.awt.event.*;
import java.util.*;
import Persistencia.SistemaPersistencia;
import usuario.Cliente;
import usuario.Organizador;
import usuario.Promotor;
import excepciones.VenueNoPresente;
import tiquete.Tiquete;

public class ventanaCrearEvento extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtNombreEvento;
    private JTextField txtHora;
    private JTextField txtFecha;
    private JList<String> listaVenues;

    private SistemaPersistencia sistema;
    private Organizador usuarioActual;
    private List<Venue> lstVenue;

    public ventanaCrearEvento(SistemaPersistencia sistema, Organizador usuarioActual) {

        this.usuarioActual = usuarioActual;
        this.sistema = sistema;
        this.lstVenue = sistema.getVenues();

        setLayout(null);

        // ---------------------
        // NOMBRE DE EVENTO
        // ---------------------
        JLabel lblNombreEvento = new JLabel("Nombre del Evento:");
        lblNombreEvento.setBounds(40, 27, 140, 20);
        add(lblNombreEvento);

        txtNombreEvento = new JTextField();
        txtNombreEvento.setBounds(185, 22, 150, 26);
        add(txtNombreEvento);

        // ---------------------
        // HORA
        // ---------------------
        JLabel lblHora = new JLabel("Hora:");
        lblHora.setBounds(40, 60, 100, 20);
        add(lblHora);

        txtHora = new JTextField();
        txtHora.setBounds(185, 55, 150, 26);
        add(txtHora);

        // ---------------------
        // FECHA
        // ---------------------
        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        lblFecha.setBounds(40, 93, 160, 20);
        add(lblFecha);

        txtFecha = new JTextField();
        txtFecha.setBounds(185, 88, 150, 26);
        add(txtFecha);

        // ---------------------
        // LISTA DE VENUES
        // ---------------------
        JLabel lblVenues = new JLabel("Venues disponibles:");
        lblVenues.setBounds(40, 135, 150, 20);
        add(lblVenues);

        String[] nombresVenues = new String[lstVenue.size()];
        for (int i = 0; i < lstVenue.size(); i++) {
            nombresVenues[i] = lstVenue.get(i).getUbicacion();
        }

        listaVenues = new JList<>(nombresVenues);
        JScrollPane scrollPane = new JScrollPane(listaVenues);
        scrollPane.setBounds(185, 125, 150, 100);
        add(scrollPane);

        // ---------------------
        // BOTÓN SALIR
        // ---------------------
        JButton btnSalir = new JButton("Salir");
        btnSalir.setBounds(10, 290, 80, 26);
        add(btnSalir);

        btnSalir.addActionListener(e -> volverAlMenu());

        // ---------------------
        // BOTÓN CREAR EVENTO
        // ---------------------
        JButton btnCrearEvento = new JButton("Crear Evento");
        btnCrearEvento.setBounds(360, 290, 150, 26);
        add(btnCrearEvento);

        btnCrearEvento.addActionListener(e -> crearEvento());

        // ---------------------
        // CONFIG VENTANA
        // ---------------------
        setTitle("BOLETAMASTER: Crear Evento");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(550, 360);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // =====================================================
    // MÉTODO PARA CREAR EVENTO ← YA FUNCIONAL
    // =====================================================
    private void crearEvento() {

        String nombreStr = txtNombreEvento.getText().trim();
        String fechaStr = txtFecha.getText().trim();
        String horaStr = txtHora.getText().trim();

        if (nombreStr.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        int index = listaVenues.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un venue.");
            return;
        }

        Venue venueSeleccionado = lstVenue.get(index);

        try {

            HashMap<String, Tiquete> tiquetesDisponibles = new HashMap<>();

            Evento nuevo = usuarioActual.crearEvento(
                    nombreStr,
                    fechaStr,
                    horaStr,
                    tiquetesDisponibles,
                    venueSeleccionado,
                    usuarioActual.getLogin(),
                    sistema
            );

            JOptionPane.showMessageDialog(this,
                    "El evento \"" + nuevo.getNombre() + "\" ha sido creado con éxito.");


        } catch (VenueNoPresente e) {
            JOptionPane.showMessageDialog(this,
                    "Error: El venue no está disponible.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
 private void volverAlMenu() {
        
    	
	 	dispose();
       new ventanaMenuOrganizador( usuarioActual, sistema).setVisible(true);
       
             
    }
}
