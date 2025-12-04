package interfaz;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;




public class ventanaCrearTiqueteMultiple extends JFrame {
	private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JButton btnNewButton_1;
    private JTextField textField_1;
    
    

    public ventanaCrearTiqueteMultiple() {
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
        
        btnNewButton_1 = new JButton("Continuar");
        btnNewButton_1.setBounds(212, 129, 174, 26);
        getContentPane().add(btnNewButton_1);
        
        JLabel lblNewLabel = new JLabel("Nombre de paquete:");
        lblNewLabel.setBounds(40, 27, 149, 16);
        getContentPane().add(lblNewLabel);
        
        JLabel lblContrasea = new JLabel("Precio base: ");
        lblContrasea.setBounds(40, 55, 79, 16);
        getContentPane().add(lblContrasea);
        
        JButton btnNewButton_1_1 = new JButton("Salir");
        btnNewButton_1_1.setBounds(6, 129, 79, 26);
        getContentPane().add(btnNewButton_1_1);
        
        JLabel lblFechayyyymmdd = new JLabel("Cantidad de tiquetes:");
        lblFechayyyymmdd.setBounds(40, 83, 161, 16);
        getContentPane().add(lblFechayyyymmdd);
        
        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(217, 50, 134, 26);
        getContentPane().add(textField_1);
                
                JSpinner spinner = new JSpinner();
                spinner.setBounds(217, 78, 128, 26);
                getContentPane().add(spinner);

       
        setTitle("BOLETAMASTER: Crear Tiquete Multiple");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(392, 206);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ventanaCrearTiqueteMultiple();
    }
}