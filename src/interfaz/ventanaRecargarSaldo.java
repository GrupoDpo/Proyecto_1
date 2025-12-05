package interfaz;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;




public class ventanaRecargarSaldo extends JFrame {
	private static final long serialVersionUID = 1L;
    private JButton btnNewButton_1;
    
    

    public ventanaRecargarSaldo() {
        ArrayList<String> tiposUsuario = new ArrayList<String>();
    	
    	tiposUsuario.add("Cliente");
    	tiposUsuario.add("Promotor");
    	tiposUsuario.add("Organizador");
    	tiposUsuario.add("Administrador");
    	
    	getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 10);
        getContentPane().add(panel);
        
        btnNewButton_1 = new JButton("Recargar");
        btnNewButton_1.setBounds(177, 129, 131, 26);
        getContentPane().add(btnNewButton_1);
        
        JLabel lblNewLabel = new JLabel("Saldo actual: ");
        lblNewLabel.setBounds(40, 27, 149, 16);
        getContentPane().add(lblNewLabel);
        
        JLabel lblContrasea = new JLabel("Cantidad a recargar:");
        lblContrasea.setBounds(40, 55, 137, 16);
        getContentPane().add(lblContrasea);
        
        JButton btnNewButton_1_1 = new JButton("Salir");
        btnNewButton_1_1.setBounds(6, 129, 79, 26);
        getContentPane().add(btnNewButton_1_1);
        
        JTextPane textPane = new JTextPane();
        textPane.setBounds(127, 27, 124, 16);
        getContentPane().add(textPane);
        
        JSpinner spinner = new JSpinner();
        spinner.setBounds(177, 50, 95, 26);
        getContentPane().add(spinner);
        
        
   
       
        setTitle("BOLETAMASTER: Recargo Saldo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(318, 206);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ventanaRecargarSaldo();
    }
}