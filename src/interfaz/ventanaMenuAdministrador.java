package interfaz;

import java.awt.*;
import javax.swing.*;

import Persistencia.SistemaPersistencia;
import usuario.Administrador;





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
        JLabel lblBienvenida = new JLabel("Bienvenido Administrador, " + admin.getLogin());
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
        y += 40;
        
        
        JButton btnVerLog = new JButton("Ver log reventas");
        btnVerLog.setBounds(50, y, 180, 30);
        btnVerLog.setBackground(new Color(52, 152, 219));
        btnVerLog.setForeground(Color.WHITE);
        btnVerLog.addActionListener(e -> abrirVentanaVerLogReventas());
        getContentPane().add(btnVerLog);
        y += 40;
        
        
        JButton btnCancelarEvento = new JButton("Cancelar Evento");
        btnCancelarEvento.setBounds(50, y, 180, 30);
        btnCancelarEvento.setBackground(new Color(52, 152, 219));
        btnCancelarEvento.setForeground(Color.WHITE);
        btnCancelarEvento.addActionListener(e -> abrirVentanaCancelarEvento());
        getContentPane().add(btnCancelarEvento);
        y += 40;
        
        
        JButton btnGestionSolCancelarEvento = new JButton("Ver solicitudes Cancelacion evento");
        btnGestionSolCancelarEvento.setBounds(50, y, 180, 30);
        btnGestionSolCancelarEvento.setBackground(new Color(52, 152, 219));
        btnGestionSolCancelarEvento.setForeground(Color.WHITE);
        btnGestionSolCancelarEvento.addActionListener(e -> abrirVentanaVerSolicitudesCancelacionEvento());
        getContentPane().add(btnGestionSolCancelarEvento);
        y += 40;
        
        
        JButton btnFijarCobroEmision = new JButton("Fijar Cobro Emision");
        btnFijarCobroEmision.setBounds(50, y, 180, 30);
        btnFijarCobroEmision.setBackground(new Color(52, 152, 219));
        btnFijarCobroEmision.setForeground(Color.WHITE);
        btnFijarCobroEmision.addActionListener(e -> abrirVentanaFijarCobroEmision());
        getContentPane().add(btnFijarCobroEmision);
        y += 40;
        
        
        JButton btnFijarRecargo = new JButton("Fijar Recargo");
        btnFijarRecargo.setBounds(50, y, 180, 30);
        btnFijarRecargo.setBackground(new Color(52, 152, 219));
        btnFijarRecargo.setForeground(Color.WHITE);
        btnFijarRecargo.addActionListener(e -> abrirVentanaFijarRecargo());
        getContentPane().add(btnFijarRecargo);
        y += 40;

        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBounds(125, y, 180, 30);
        btnCerrarSesion.setBackground(new Color(231, 76, 60));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        getContentPane().add(btnCerrarSesion);
        y += 40;
        
        setTitle("BOLETAMASTER: Menu Organizador");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, y + 80);
        setLocationRelativeTo(null);
    }

    // Métodos específicos de Organizador
    private void abrirVentanaCrearVenue() {
    	dispose();
        new VentanaCrearVenue(sistema, admin);
    }

    private void abrirVentanaRevisarSolicitudVenue() {
    	dispose();
    	new VentanaVerSolicitudesVenue(sistema).setVisible(true);;
    }

    private void abrirVentanaVerSolicitudesReembolsos() {
    	dispose();
    	ventanaVerSolicitudesDeRembolso ventana = new ventanaVerSolicitudesDeRembolso(sistema);
        ventana.setVisible(true);
    }
    
    private void abrirVentanaVerLogReventas() {
    	// TODO: Implementar ventana 
        JOptionPane.showMessageDialog(this, "Ver Solicitudes Reembolsos - En desarrollo");
    }
    
    private void abrirVentanaCancelarEvento() {
    	// TODO: Implementar ventana 
        JOptionPane.showMessageDialog(this, "- - En desarrollo");
    }
    
    private void abrirVentanaVerSolicitudesCancelacionEvento() {
    	dispose();
    	new ventanaVerSolicitudesCancelacionEvento(sistema);
    }
    private void abrirVentanaFijarCobroEmision() {
    	dispose();
    	new VentanaFijarCobroEmision(sistema, admin).setVisible(true);;
    }
    
    private void abrirVentanaFijarRecargo() {
    	dispose();
    	new VentanaRecargo(sistema, admin).setVisible(true);;
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