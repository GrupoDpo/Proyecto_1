package interfaz;

import java.awt.*;
import javax.swing.*;

import Finanzas.Transaccion;
import Persistencia.SistemaPersistencia;
import excepciones.TransferenciaNoPermitidaException;
import usuario.IDuenoTiquetes;
import usuario.Usuario;




public class ventanaMenuComprador extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private Usuario usuarioActual;
    private SistemaPersistencia sistema;


    public ventanaMenuComprador(Usuario usuario, SistemaPersistencia sistema) {
        this.usuarioActual = usuario;
        this.sistema = sistema;
        
        getContentPane().setLayout(null);

        // Panel superior
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 449, 10);
        getContentPane().add(panel);

        // Bienvenida personalizada
        JLabel lblBienvenida = new JLabel("Bienvenido, " + usuarioActual.getLogin());
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 14));
        lblBienvenida.setBounds(130, 20, 250, 25);
        getContentPane().add(lblBienvenida);

        // Mostrar saldo si el usuario puede tener saldo
        if (usuarioActual instanceof IDuenoTiquetes) {
            IDuenoTiquetes dueno = (IDuenoTiquetes) usuarioActual;
            JLabel lblSaldo = new JLabel("Saldo: $" + dueno.getSaldo());
            lblSaldo.setFont(new Font("Arial", Font.PLAIN, 12));
            lblSaldo.setForeground(new Color(0, 128, 0));
            lblSaldo.setBounds(180, 45, 150, 20);
            getContentPane().add(lblSaldo);
        }

        int y = 75; // Posición Y inicial para botones

        // ===== BOTONES COMUNES PARA COMPRADORES =====
        
        JButton btnComprarTiquete = new JButton("Comprar Tiquete");
        btnComprarTiquete.setBounds(150, y, 180, 30);
        btnComprarTiquete.addActionListener(e -> abrirVentanaComprarTiquete());
        getContentPane().add(btnComprarTiquete);
        y += 40;

        JButton btnComprarPaqueteDeluxe = new JButton("Comprar Paquete Deluxe");
        btnComprarPaqueteDeluxe.setBounds(150, y, 180, 30);
        btnComprarPaqueteDeluxe.addActionListener(e -> abrirVentanaComprarPaqueteDeluxe());
        getContentPane().add(btnComprarPaqueteDeluxe);
        y += 40;

 
        JButton btnTransferirTiquete = new JButton("Transferir Tiquete");
        btnTransferirTiquete.setBounds(150, y, 180, 30);
        btnTransferirTiquete.addActionListener(e -> abrirVentanaTransferirTiquete());
        getContentPane().add(btnTransferirTiquete);
        y += 40;

        JButton btnCrearOferta = new JButton("Crear Oferta Reventa");
        btnCrearOferta.setBounds(150, y, 180, 30);
        btnCrearOferta.addActionListener(e -> abrirVentanaCrearOferta());
        getContentPane().add(btnCrearOferta);
        y += 40;

        JButton btnEliminarOferta = new JButton("Eliminar Oferta Reventa");
        btnEliminarOferta.setBounds(150, y, 180, 30);
        btnEliminarOferta.addActionListener(e -> abrirVentanaEliminarOferta());
        getContentPane().add(btnEliminarOferta);
        y += 40;

        JButton btnComprarMarketplace = new JButton("Comprar en Marketplace");
        btnComprarMarketplace.setBounds(150, y, 180, 30);
        btnComprarMarketplace.addActionListener(e -> {
			try {
				abrirVentanaMarketplace();
			} catch (TransferenciaNoPermitidaException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        getContentPane().add(btnComprarMarketplace);
        y += 40;

        JButton btnContraofertar = new JButton("Contraofertar");
        btnContraofertar.setBounds(150, y, 180, 30);
        btnContraofertar.addActionListener(e -> {
			try {
				abrirVentanaContraofertar();
			} catch (TransferenciaNoPermitidaException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        getContentPane().add(btnContraofertar);
        y += 40;

        JButton btnVerContraofertas = new JButton("Ver Contraofertas");
        btnVerContraofertas.setBounds(150, y, 180, 30);
        btnVerContraofertas.addActionListener(e -> abrirVentanaVerContraofertas());
        getContentPane().add(btnVerContraofertas);
        y += 40;

        JButton btnRecargarSaldo = new JButton("Recargar Saldo");
        btnRecargarSaldo.setBounds(150, y, 180, 30);
        btnRecargarSaldo.addActionListener(e -> abrirVentanaRecargarSaldo());
        getContentPane().add(btnRecargarSaldo);
        y += 40;
        
     
        
        
        

        JButton btnSolicitarReembolso = new JButton("Solicitar Reembolso");
        btnSolicitarReembolso.setBounds(150, y, 180, 30);
        btnSolicitarReembolso.addActionListener(e -> abrirVentanaSolicitarReembolso());
        getContentPane().add(btnSolicitarReembolso);
        y += 40;


        // Botón Cerrar Sesión
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBounds(150, y, 180, 30);
        btnCerrarSesion.setBackground(new Color(231, 76, 60));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        getContentPane().add(btnCerrarSesion);

        setTitle("BOLETAMASTER: Menu Comprador");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(449, 439);
        setLocationRelativeTo(null);
    }

    // ===== MÉTODOS PARA ABRIR VENTANAS =====
    
    private void abrirVentanaComprarTiquete() {
    	dispose();
    	ventanaComprarTiquete ventana = new ventanaComprarTiquete(sistema, usuarioActual);
        ventana.setVisible(true);
    }

    private void abrirVentanaComprarPaqueteDeluxe() {
        // TODO: Implementar ventana de compra de paquete deluxe
        JOptionPane.showMessageDialog(this, "Ventana Comprar Paquete Deluxe - En desarrollo");
    }

    private void abrirVentanaTransferirTiquete() {
    	dispose();
    	Transaccion trans = new Transaccion("NA", null, null, null, null, 0);
        new ventanaTransferirTiquete((IDuenoTiquetes) usuarioActual,sistema,trans).setVisible(true);
    }
    private void abrirVentanaCrearOferta() {
        dispose();
        Transaccion trans = new Transaccion("NA", null, null, null, null, 0);
        new ventanaCrearOferta((IDuenoTiquetes) usuarioActual, sistema, trans).setVisible(true);
    }

    private void abrirVentanaEliminarOferta() {
    	dispose();
        new ventanaCancelarOferta((IDuenoTiquetes) usuarioActual, sistema).setVisible(true);
        
    }

    private void abrirVentanaMarketplace() throws TransferenciaNoPermitidaException {

    	dispose();
        new VentanaComprarMarketplace(sistema, usuarioActual).setVisible(true);;

    }

    private void abrirVentanaContraofertar() throws TransferenciaNoPermitidaException {
    	dispose();
        new VentanaContraofertar(sistema, usuarioActual).setVisible(true);
    }

    private void abrirVentanaVerContraofertas() {
        new ventanaVerContraOfertas((IDuenoTiquetes) usuarioActual, sistema);
    }

    private void abrirVentanaRecargarSaldo() {
    	dispose();
    	new ventanaRecargarSaldo(sistema, usuarioActual).setVisible(true);
    }

    private void abrirVentanaSolicitarReembolso() {
     	dispose();
    	new VentanaSolicitarReembolsos(sistema, usuarioActual).setVisible(true);;
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