package interfaz;

import java.awt.*;
import javax.swing.*;

import Persistencia.SistemaPersistencia;
import usuario.Administrador;
import usuario.Organizador;
import usuario.Usuario;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;




public class ventanaMenuAdministrador extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Administrador admin;
    private SistemaPersistencia sistema;

    public ventanaMenuAdministrador(Administrador admin, SistemaPersistencia sistema) {
        this.admin = admin;
        this.sistema = sistema;
        
        getContentPane().setLayout(null);

        // Bienvenida
        JLabel lblBienvenida = new JLabel("Bienvenido Organizador, " + admin.getLogin());
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 14));
        lblBienvenida.setBounds(100, 20, 300, 25);
        getContentPane().add(lblBienvenida);

        int y = 75;

        // ===== OPCIONES ESPECÍFICAS DE ADMIN =====
        
        JButton btnCrearEvento = new JButton("Crear Venue");
        btnCrearEvento.setBounds(50, y, 180, 30);
        btnCrearEvento.setBackground(new Color(52, 152, 219));
        btnCrearEvento.setForeground(Color.WHITE);
        btnCrearEvento.addActionListener(e -> abrirVentanaCrearVenue());
        getContentPane().add(btnCrearEvento);

        JButton btnVerMisEventos = new JButton("Revisar Solicitud Venue");
        btnVerMisEventos.setBounds(250, y, 180, 30);
        btnVerMisEventos.setBackground(new Color(52, 152, 219));
        btnVerMisEventos.setForeground(Color.WHITE);
        btnVerMisEventos.addActionListener(e -> abrirVentanaRevisarSolicitudVenue());
        getContentPane().add(btnVerMisEventos);
        y += 40;

        JButton btnAgregarTiquetes = new JButton("Ver solicitudes Reembolsos");
        btnAgregarTiquetes.setBounds(50, y, 180, 30);
        btnAgregarTiquetes.setBackground(new Color(52, 152, 219));
        btnAgregarTiquetes.setForeground(Color.WHITE);
        btnAgregarTiquetes.addActionListener(e -> abrirVentanaVerSolicitudesReembolsos());
        getContentPane().add(btnAgregarTiquetes);
        
        JButton btnVerLog = new JButton("Ver log reventas");
        btnVerLog.setBounds(50, y, 180, 30);
        btnVerLog.setBackground(new Color(52, 152, 219));
        btnVerLog.setForeground(Color.WHITE);
        btnVerLog.addActionListener(e -> abrirVentanaVerLogReventas());
        getContentPane().add(btnVerLog);
        
        JButton btnCancelarEvento = new JButton("Cancelar Evento");
        btnCancelarEvento.setBounds(50, y, 180, 30);
        btnCancelarEvento.setBackground(new Color(52, 152, 219));
        btnCancelarEvento.setForeground(Color.WHITE);
        btnCancelarEvento.addActionListener(e -> abrirVentanaCancelarEvento());
        getContentPane().add(btnCancelarEvento);
        
        JButton btnGestionSolCancelarEvento = new JButton("Ver solicitudes Cancelacion evento");
        btnGestionSolCancelarEvento.setBounds(50, y, 180, 30);
        btnGestionSolCancelarEvento.setBackground(new Color(52, 152, 219));
        btnGestionSolCancelarEvento.setForeground(Color.WHITE);
        btnGestionSolCancelarEvento.addActionListener(e -> abrirVentanaVerSolicitudesCancelacionEvento());
        getContentPane().add(btnGestionSolCancelarEvento);
        
        JButton btnFijarCobroEmision = new JButton("Fijar Cobro Emision");
        btnFijarCobroEmision.setBounds(50, y, 180, 30);
        btnFijarCobroEmision.setBackground(new Color(52, 152, 219));
        btnFijarCobroEmision.setForeground(Color.WHITE);
        btnFijarCobroEmision.addActionListener(e -> abrirVentanaFijarCobroEmision());
        getContentPane().add(btnFijarCobroEmision);
        
        JButton btnFijarRecargo = new JButton("Fijar Recargo");
        btnFijarRecargo.setBounds(50, y, 180, 30);
        btnFijarRecargo.setBackground(new Color(52, 152, 219));
        btnFijarRecargo.setForeground(Color.WHITE);
        btnFijarRecargo.addActionListener(e -> abrirVentanaFijarRecargo());
        getContentPane().add(btnFijarRecargo);


        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBounds(125, y, 180, 30);
        btnCerrarSesion.setBackground(new Color(231, 76, 60));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        getContentPane().add(btnCerrarSesion);

        setTitle("BOLETAMASTER: Menu Organizador");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, y + 80);
        setLocationRelativeTo(null);
    }

    // Métodos específicos de Organizador
    private void abrirVentanaCrearVenue() {
        JOptionPane.showMessageDialog(this, "Crear Evento - En desarrollo");
    }

    private void abrirVentanaRevisarSolicitudVenue() {
        JOptionPane.showMessageDialog(this, "Revisar Solicitud Venue - En desarrollo");
    }

    private void abrirVentanaVerSolicitudesReembolsos() {
        JOptionPane.showMessageDialog(this, "Ver Solicitudes Reembolsos - En desarrollo");
    }
    
    private void abrirVentanaVerLogReventas() {
        JOptionPane.showMessageDialog(this, "Ver Solicitudes Reembolsos - En desarrollo");
    }
    
    private void abrirVentanaCancelarEvento() {
        JOptionPane.showMessageDialog(this, "- - En desarrollo");
    }
    
    private void abrirVentanaVerSolicitudesCancelacionEvento() {
        JOptionPane.showMessageDialog(this, "- - En desarrollo");
    }
    private void abrirVentanaFijarCobroEmision() {
        JOptionPane.showMessageDialog(this, "- - En desarrollo");
    }
    
    private void abrirVentanaFijarRecargo() {
        JOptionPane.showMessageDialog(this, "- - En desarrollo");
    }

   

   

    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar sesión?",
            "Cerrar Sesión",
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            dispose();
            ventanaLogin login = new ventanaLogin(sistema);
            login.setVisible(true);
        }
    }
}