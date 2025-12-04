package interfaz;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;




public class ventanaCrearTiqueteSimple extends JFrame {
	private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JButton btnNewButton_1;
    private JTextField textField_1;
    
    

    public ventanaCrearTiqueteSimple() {
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
        textField.setBounds(217, 22, 134, 26);
        getContentPane().add(textField);
        textField.setColumns(10);
        
        btnNewButton_1 = new JButton("Crear Tiquete");
        btnNewButton_1.setBounds(207, 291, 174, 26);
        getContentPane().add(btnNewButton_1);
        
        JLabel lblNewLabel = new JLabel("Nombre de Localidad:");
        lblNewLabel.setBounds(40, 27, 149, 16);
        getContentPane().add(lblNewLabel);
        
        JLabel lblContrasea = new JLabel("Precio base: ");
        lblContrasea.setBounds(40, 55, 79, 16);
        getContentPane().add(lblContrasea);
        
        JButton btnNewButton_1_1 = new JButton("Salir");
        btnNewButton_1_1.setBounds(6, 291, 79, 26);
        getContentPane().add(btnNewButton_1_1);
        
        JLabel lblFechayyyymmdd = new JLabel("Capacidad de localidad:");
        lblFechayyyymmdd.setBounds(40, 83, 161, 16);
        getContentPane().add(lblFechayyyymmdd);
        
        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(217, 50, 134, 26);
        getContentPane().add(textField_1);
                
                JSpinner spinner = new JSpinner();
                spinner.setBounds(217, 78, 128, 26);
                getContentPane().add(spinner);
                
                JCheckBox chckbxNewCheckBox = new JCheckBox("Numerada");
                chckbxNewCheckBox.setBounds(31, 174, 128, 23);
                getContentPane().add(chckbxNewCheckBox);
                
                JCheckBox chckbxNoNumerada = new JCheckBox("No numerada");
                chckbxNoNumerada.setBounds(185, 174, 128, 23);
                getContentPane().add(chckbxNoNumerada);
                
                JSpinner spinner_1 = new JSpinner();
                spinner_1.setBounds(217, 116, 128, 26);
                getContentPane().add(spinner_1);
                
                JLabel lblRecargo = new JLabel("Recargo:");
                lblRecargo.setBounds(50, 121, 161, 16);
                getContentPane().add(lblRecargo);

       
        setTitle("BOLETAMASTER: Crear Tiquete Simple");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(392, 357);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ventanaCrearTiqueteSimple();
    }
}