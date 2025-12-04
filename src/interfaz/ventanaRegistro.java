package interfaz;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;




public class ventanaRegistro extends JFrame {
	private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton_1;
    private JList list;
    
    

    public ventanaRegistro() {
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
        textField.setBounds(167, 55, 134, 26);
        getContentPane().add(textField);
        textField.setColumns(10);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(167, 93, 134, 26);
        getContentPane().add(passwordField);
        
        btnNewButton_1 = new JButton("Registrar Usuario");
        btnNewButton_1.setBounds(123, 277, 148, 26);
        getContentPane().add(btnNewButton_1);
        
        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setBounds(97, 60, 61, 16);
        getContentPane().add(lblNewLabel);
        
        JLabel lblContrasea = new JLabel("Contraseña");
        lblContrasea.setBounds(79, 98, 79, 16);
        getContentPane().add(lblContrasea);

        list = new JList<>(tiposUsuario.toArray(new String[0]));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // SOLO UNA OPCIÓN
        list.setVisibleRowCount(4);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBounds(142, 143, 108, 98);
        getContentPane().add(scrollPane);

        JLabel lblTipoUsuario = new JLabel("Tipo de Usuario");
        lblTipoUsuario.setBounds(30, 178, 100, 16);
        getContentPane().add(lblTipoUsuario);

       
        setTitle("BOLETAMASTER: Registrar Usuario");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(399, 351);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ventanaRegistro();
    }
}