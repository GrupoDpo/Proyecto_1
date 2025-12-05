package interfaz;

import java.awt.*;
import javax.swing.*;

import Persistencia.SistemaPersistencia;
import usuario.Administrador;
import usuario.Cliente;
import usuario.IDuenoTiquetes;
import usuario.Organizador;
import usuario.Promotor;
import usuario.Usuario;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;




public class ventanaRecargarSaldo extends JFrame {
	private static final long serialVersionUID = 1L;
    private JButton btnNewButton_1;
    private JTextField textField;
    private SistemaPersistencia sistema;
    private Usuario usuarioActual;
    
    

    public ventanaRecargarSaldo(SistemaPersistencia sistema, Usuario usuarioActual) {
        
    	this.usuarioActual = usuarioActual;
    	this.sistema = sistema;
    	
    	IDuenoTiquetes metodosComprador = (IDuenoTiquetes) usuarioActual;
   
    	
    	getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 400, 10);
        getContentPane().add(panel);
        
        btnNewButton_1 = new JButton("Recargar");
        btnNewButton_1.setBounds(177, 129, 131, 26);
        getContentPane().add(btnNewButton_1);
        
        btnNewButton_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recargarSaldo();
            }
        });
        
        
        
        
        JLabel lblNewLabel = new JLabel("Saldo actual: " + metodosComprador.getSaldo());
        lblNewLabel.setBounds(40, 27, 149, 16);
        getContentPane().add(lblNewLabel);
        
        JLabel lblContrasea = new JLabel("Cantidad a recargar:");
        lblContrasea.setBounds(40, 55, 137, 16);
        getContentPane().add(lblContrasea);
        
        JButton btnNewButton_1_1 = new JButton("Salir");
        btnNewButton_1_1.setBounds(6, 129, 79, 26);
        getContentPane().add(btnNewButton_1_1);
       
        
        btnNewButton_1_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volverAlMenu();
            }
        });
        
        textField = new JTextField();
        textField.setBounds(177, 50, 106, 26);
        getContentPane().add(textField);
        textField.setColumns(10);

       
        setTitle("BOLETAMASTER: Recargo Saldo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(318, 206);
        setVisible(true);
    }
    
    private void recargarSaldo() {
    	String valorRecarga = textField.getText();
    	int recarga = Integer.parseInt(valorRecarga);
    	IDuenoTiquetes usuario = (IDuenoTiquetes) usuarioActual;
    	usuario.actualizarSaldo(recarga);
    	
    	JOptionPane.showMessageDialog(this, "Saldo exitosamente recargado");
    		
    	
    }
    
    private void volverAlMenu() {
        
    	
    	dispose();
    	
        if (usuarioActual instanceof Promotor) {
            new ventanaMenuPromotor((Promotor) usuarioActual, sistema).setVisible(true);
        } else if (usuarioActual instanceof Organizador) {
            new ventanaMenuOrganizador((Organizador) usuarioActual, sistema).setVisible(true);
        } else if (usuarioActual instanceof Cliente) {
        	 new ventanaMenuComprador((Cliente) usuarioActual ,sistema).setVisible(true);
        }
             
    }
    
}