package interfaz;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;




public class ventanaCrearEvento extends JFrame {
	private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JButton btnNewButton_1;
    private JList list;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    
    

    public ventanaCrearEvento() {
        ArrayList<String> tiposUsuario = new ArrayList<String>();
    	
    	tiposUsuario.add("Cliente");
    	tiposUsuario.add("Promotor");
    	tiposUsuario.add("Organizador");
    	tiposUsuario.add("Administrador");
    	
    	getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 10);
        getContentPane().add(panel);
        
        textField = new JTextField();
        textField.setBounds(185, 22, 134, 26);
        getContentPane().add(textField);
        textField.setColumns(10);
        
        btnNewButton_1 = new JButton("Crear Evento");
        btnNewButton_1.setBounds(403, 291, 148, 26);
        getContentPane().add(btnNewButton_1);
        
        JLabel lblNewLabel = new JLabel("Nombre de Evento:");
        lblNewLabel.setBounds(40, 27, 119, 16);
        getContentPane().add(lblNewLabel);
        
        JLabel lblContrasea = new JLabel("Hora:");
        lblContrasea.setBounds(40, 60, 79, 16);
        getContentPane().add(lblContrasea);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(198, 145, 108, 98);
        getContentPane().add(scrollPane);

        JLabel lblTipoUsuario = new JLabel("Venues disponibles");
        lblTipoUsuario.setBounds(49, 177, 124, 16);
        getContentPane().add(lblTipoUsuario);
        
        JButton btnNewButton_1_1 = new JButton("Salir");
        btnNewButton_1_1.setBounds(6, 291, 79, 26);
        getContentPane().add(btnNewButton_1_1);
        
        JLabel lblFechayyyymmdd = new JLabel("Fecha (YYYY-MM-DD)");
        lblFechayyyymmdd.setBounds(12, 93, 161, 16);
        getContentPane().add(lblFechayyyymmdd);
        
        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(185, 55, 134, 26);
        getContentPane().add(textField_1);
        
        textField_2 = new JTextField();
        textField_2.setColumns(10);
        textField_2.setBounds(185, 88, 134, 26);
        getContentPane().add(textField_2);
        
                list = new JList<>(tiposUsuario.toArray(new String[0]));
                list.setBounds(202, 145, 104, 94);
                getContentPane().add(list);
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // SOLO UNA OPCIÓN
                list.setVisibleRowCount(4);
                
                JLabel lblCapacidadDeEvento = new JLabel("Capacidad de evento: ");
                lblCapacidadDeEvento.setBounds(331, 27, 148, 16);
                getContentPane().add(lblCapacidadDeEvento);
                
                JSpinner spinner = new JSpinner();
                spinner.setBounds(493, 22, 34, 26);
                getContentPane().add(spinner);
                
                textField_3 = new JTextField();
                textField_3.setColumns(10);
                textField_3.setBounds(403, 55, 134, 26);
                getContentPane().add(textField_3);
                
                JLabel lblUbicacion = new JLabel("Ubicacion");
                lblUbicacion.setBounds(331, 60, 148, 16);
                getContentPane().add(lblUbicacion);

       
        setTitle("BOLETAMASTER: Crear Evento");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(568, 357);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ventanaCrearEvento();
    }
}