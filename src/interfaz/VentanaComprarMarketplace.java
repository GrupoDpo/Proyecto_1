package interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Evento.Evento;
import Finanzas.Transaccion;
import Finanzas.marketPlaceReventas;
import Persistencia.SistemaPersistencia;
import excepciones.SaldoInsuficienteExeption;
import excepciones.TiquetesNoDisponiblesException;
import excepciones.TransferenciaNoPermitidaException;
import tiquete.Tiquete;
import usuario.Cliente;
import usuario.IDuenoTiquetes;
import usuario.Organizador;
import usuario.Promotor;
import usuario.Usuario;

public class VentanaComprarMarketplace extends JFrame {
	private static final long serialVersionUID = 1L;

	private JList<String> listaOfertas;
    private DefaultListModel<String> modeloOfertas;
    
    private JLabel lblSaldoActual;
    private JButton btnComprar;
    private JButton btnCancelar;
    private JButton btnActualizar;
    private JTextArea txtDetalleOferta;

    // Modelo
    private SistemaPersistencia sistema;
    private Usuario comprador;
    private Map<String, OfertaInfo> mapaOfertas;
	
	public VentanaComprarMarketplace(SistemaPersistencia sistema, Usuario comprador) throws TransferenciaNoPermitidaException {
		this.comprador = comprador;
		this.sistema = sistema;
		this.mapaOfertas = new HashMap<>();

        inicializarComponentes();
        cargarOfertas();
	}
	
	private void inicializarComponentes() {
		setTitle("BOLETAMASTER: Marketplace de Tiquetes");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);  // positions manuales


     // ============================
        // BARRA SUPERIOR
        // ============================
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(70, 130, 180)); // Azul más suave
        panelSuperior.setBounds(0, 0, 900, 50);
        panelSuperior.setLayout(null);
        add(panelSuperior);

        JLabel lblBienvenida = new JLabel(" MARKETPLACE DE TIQUETES");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 13));
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setBounds(20, 15, 250, 20);
        panelSuperior.add(lblBienvenida);

        lblSaldoActual = new JLabel("Saldo: $0.00");
        lblSaldoActual.setFont(new Font("Arial", Font.BOLD, 13));
        lblSaldoActual.setForeground(Color.WHITE);
        lblSaldoActual.setBounds(700, 15, 180, 20);
        panelSuperior.add(lblSaldoActual);
        actualizarSaldo();
        
        
        JLabel lblOfertas = new JLabel("Ofertas Disponibles:");
        lblOfertas.setFont(new Font("Arial", Font.BOLD, 13));
        lblOfertas.setBounds(30, 65, 200, 20);
        getContentPane().add(lblOfertas);

        // Botón actualizar
        btnActualizar = new JButton(" Actualizar");
        btnActualizar.setFont(new Font("Arial", Font.PLAIN, 11));
        btnActualizar.setBounds(570, 60, 110, 30);
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> {
			try {
				cargarOfertas();
			} catch (TransferenciaNoPermitidaException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        getContentPane().add(btnActualizar);
        
        
     // Lista scrolleable de ofertas
        modeloOfertas = new DefaultListModel<>();
        listaOfertas = new JList<>(modeloOfertas);
        listaOfertas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaOfertas.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollOfertas = new JScrollPane(listaOfertas);
        scrollOfertas.setBounds(30, 95, 640, 200);
        getContentPane().add(scrollOfertas);

        // ========================================
        // DETALLE DE LA OFERTA
        // ========================================
        JLabel lblDetalle = new JLabel("Detalle de la Oferta:");
        lblDetalle.setFont(new Font("Arial", Font.BOLD, 13));
        lblDetalle.setBounds(30, 310, 200, 20);
        getContentPane().add(lblDetalle);

        txtDetalleOferta = new JTextArea();
        txtDetalleOferta.setEditable(false);
        txtDetalleOferta.setFont(new Font("Arial", Font.PLAIN, 12));
        txtDetalleOferta.setLineWrap(true);
        txtDetalleOferta.setWrapStyleWord(true);
        txtDetalleOferta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtDetalleOferta.setBackground(new Color(245, 245, 245));

        JScrollPane scrollDetalle = new JScrollPane(txtDetalleOferta);
        scrollDetalle.setBounds(30, 335, 640, 100);
        getContentPane().add(scrollDetalle);
        
        // ============================
        // BOTONES
        // ============================
        
        btnComprar = new JButton("COMPRAR");
        btnComprar.setFont(new Font("Arial", Font.BOLD, 14));
        btnComprar.setBackground(new Color(46, 204, 113));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFocusPainted(false);
        btnComprar.setBounds(380, 460, 140, 40);
        btnComprar.setEnabled(false);
        getContentPane().add(btnComprar);

        btnCancelar = new JButton("CERRAR");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBounds(530, 460, 140, 40);
        getContentPane().add(btnCancelar);
        
        
        listaOfertas.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    ofertaSeleccionada();
                }
            }
        });

        // Listener: botón comprar
        btnComprar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					realizarCompra();
				} catch (TransferenciaNoPermitidaException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        // Listener: botón cancelar
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                volverAlMenu();
            }
        });
	}
        
      private void cargarOfertas() throws TransferenciaNoPermitidaException {
            modeloOfertas.clear();
            mapaOfertas.clear();
            txtDetalleOferta.setText("");
            btnComprar.setEnabled(false);

            marketPlaceReventas marketplace = sistema.getMarketplace();
            
            if (marketplace == null) {
                modeloOfertas.addElement("El marketplace no está disponible");
                return;
            }

            Queue<HashMap<Tiquete, String>> todasOfertas = marketplace.getOfertas();

            if (todasOfertas == null || todasOfertas.isEmpty()) {
                modeloOfertas.addElement("No hay ofertas disponibles en el marketplace");
                return;
            }

            int contador = 1;
            
            // Recorrer todas las ofertas
            for (HashMap<Tiquete, String> oferta : todasOfertas) {
                for (Map.Entry<Tiquete, String> entry : oferta.entrySet()) {
                    Tiquete tiquete = entry.getKey();
                    String etiqueta = entry.getValue();
                    
                    // Buscar el vendedor (dueño actual del tiquete)
                    Usuario vendedor = buscarVendedor(tiquete);
                    
                    if (vendedor == null) continue; // Oferta inválida
                    
                    // No mostrar mis propias ofertas
                    if (vendedor.getLogin().equals(comprador.getLogin())) {
                        continue;
                    }
                    
                    // Extraer precio de la etiqueta
                    double precio = marketPlaceReventas.extraerPrecio(etiqueta);
                    
                    // Obtener evento del tiquete
                    Evento evento = tiquete.getEventoAsociado();
                    String nombreEvento = evento != null ? evento.getNombre() : "Sin evento";
                    
                    // Formato: #001 | Evento | Localidad | Precio | Vendedor
                    String key = String.format("#%-3d | %-25s | %-15s | $%-8.2f | %s",
                        contador++,
                        truncar(nombreEvento, 25),
                        truncar(tiquete.getLocalidadAsociada().getNombre(), 15),
                        precio,
                        truncar(vendedor.getLogin(), 20));
                    
                    modeloOfertas.addElement(key);
                    
                    // Guardar información completa de la oferta
                    OfertaInfo info = new OfertaInfo(tiquete, vendedor, precio, etiqueta, oferta);
                    mapaOfertas.put(key, info);
                }
            }

            if (modeloOfertas.isEmpty()) {
                modeloOfertas.addElement("No hay ofertas de otros usuarios disponibles");
            }
        
	
	
    
      }
      
      private Usuario buscarVendedor(Tiquete tiquete) {
          for (Usuario u : sistema.getUsuarios()) {
              if (u instanceof IDuenoTiquetes) {
                  IDuenoTiquetes dueno = (IDuenoTiquetes) u;
                  
                  for (Tiquete t : dueno.getTiquetes()) {
                      if (t.getId().equals(tiquete.getId())) {
                          return u;
                      }
                  }
              }
          }
          return null;
      }

      /**
       * Maneja la selección de una oferta
       */
      private void ofertaSeleccionada() {
          String seleccion = listaOfertas.getSelectedValue();
          
          if (seleccion == null || !mapaOfertas.containsKey(seleccion)) {
              txtDetalleOferta.setText("");
              btnComprar.setEnabled(false);
              return;
          }

          OfertaInfo info = mapaOfertas.get(seleccion);
          Tiquete tiquete = info.tiquete;
          Usuario vendedor = info.vendedor;
          double precio = info.precio;
          
          // Mostrar detalle completo de la oferta
          StringBuilder detalle = new StringBuilder();
          detalle.append("═══════════════════════════════════════════════\n");
          detalle.append("           DETALLE DE LA OFERTA\n");
          detalle.append("═══════════════════════════════════════════════\n\n");
          
          // Información del tiquete
          detalle.append("🎫 TIQUETE\n");
          detalle.append("   ID: ").append(tiquete.getId()).append("\n");
          detalle.append("   Tipo: ").append(tiquete.getTipoTiquete()).append("\n");
          detalle.append("   Localidad: ").append(tiquete.getLocalidadAsociada().getNombre()).append("\n");
          detalle.append("   Fecha Expiración: ").append(tiquete.getFechaExpiracion()).append("\n\n");
          
          // Información del evento
          Evento evento = tiquete.getEventoAsociado();
          if (evento != null) {
              detalle.append("📅 EVENTO\n");
              detalle.append("   Nombre: ").append(evento.getNombre()).append("\n");
              detalle.append("   Fecha: ").append(evento.getFecha()).append("\n");
              detalle.append("   Lugar: ").append(evento.getVenueAsociado().getUbicacion()).append("\n\n");
          }
          
          // Información del vendedor
          detalle.append("👤 VENDEDOR\n");
          detalle.append("   Usuario: ").append(vendedor.getLogin()).append("\n\n");
          
          // Precio
          detalle.append("═══════════════════════════════════════════════\n");
          detalle.append("💰 PRECIO: $").append(String.format("%.2f", precio)).append("\n");
          detalle.append("═══════════════════════════════════════════════\n\n");
          
          // Verificar saldo
          if (comprador instanceof IDuenoTiquetes) {
              IDuenoTiquetes dueno = (IDuenoTiquetes) comprador;
              double saldo = dueno.getSaldo();
              
              if (precio > saldo) {
                  detalle.append("⚠️ SALDO INSUFICIENTE\n");
                  detalle.append("   Saldo actual: $").append(String.format("%.2f", saldo)).append("\n");
                  detalle.append("   Faltante: $").append(String.format("%.2f", precio - saldo));
                  btnComprar.setEnabled(false);
              } else {
                  detalle.append("✓ Saldo suficiente para comprar");
                  btnComprar.setEnabled(true);
              }
          }
          
          txtDetalleOferta.setText(detalle.toString());
          txtDetalleOferta.setCaretPosition(0); // Scroll al inicio
      }

      /**
       * Realiza la compra en el marketplace
     * @throws TransferenciaNoPermitidaException 
       */
      private void realizarCompra() throws TransferenciaNoPermitidaException {
          String seleccion = listaOfertas.getSelectedValue();
          
          if (seleccion == null || !mapaOfertas.containsKey(seleccion)) {
              JOptionPane.showMessageDialog(this,
                  "Debe seleccionar una oferta",
                  "Error",
                  JOptionPane.WARNING_MESSAGE);
              return;
          }

          OfertaInfo info = mapaOfertas.get(seleccion);
          Tiquete tiquete = info.tiquete;
          Usuario vendedor = info.vendedor;
          double precio = info.precio;

          // Verificar saldo nuevamente
          if (comprador instanceof IDuenoTiquetes) {
              IDuenoTiquetes dueno = (IDuenoTiquetes) comprador;
              if (precio > dueno.getSaldo()) {
                  JOptionPane.showMessageDialog(this,
                      String.format("Saldo insuficiente\n\nSaldo actual: $%.2f\nPrecio: $%.2f",
                          dueno.getSaldo(), precio),
                      "Saldo Insuficiente",
                      JOptionPane.ERROR_MESSAGE);
                  return;
              }
          }

          // Confirmar compra
          int confirmacion = JOptionPane.showConfirmDialog(this,
              String.format("¿Confirmar compra en marketplace?\n\n" +
                           "Tiquete: %s\n" +
                           "Evento: %s\n" +
                           "Localidad: %s\n" +
                           "Vendedor: %s\n" +
                           "Precio: $%.2f",
                           tiquete.getId(),
                           tiquete.getEventoAsociado() != null ? 
                               tiquete.getEventoAsociado().getNombre() : "Sin evento",
                           tiquete.getLocalidadAsociada().getNombre(),
                           vendedor.getLogin(),
                           precio),
              "Confirmar Compra en Marketplace",
              JOptionPane.YES_NO_OPTION);
          
          if (confirmacion != JOptionPane.YES_OPTION) {
              return;
          }

          // Realizar compra usando el método del sistema
          try {
              // Usar el método comprarEnMarketplace
        	  Transaccion trans = new Transaccion("NA", null, null, null, null, 0);
              ArrayList<Tiquete> comprados = trans.comprarEnMarketplace(
                  tiquete, vendedor, comprador, sistema);

              // Actualizar saldo
              actualizarSaldo();

              // Mostrar éxito
              JOptionPane.showMessageDialog(this,
                  String.format("✓ Compra realizada exitosamente!\n\n" +
                               "Tiquete comprado: %s\n" +
                               "Evento: %s\n" +
                               "Localidad: %s\n" +
                               "Vendedor: %s\n" +
                               "Precio pagado: $%.2f\n\n" +
                               "Nuevo saldo: $%.2f",
                               tiquete.getId(),
                               tiquete.getEventoAsociado() != null ? 
                                   tiquete.getEventoAsociado().getNombre() : "Sin evento",
                               tiquete.getLocalidadAsociada().getNombre(),
                               vendedor.getLogin(),
                               precio,
                               ((IDuenoTiquetes)comprador).getSaldo()),
                  "Compra Exitosa",
                  JOptionPane.INFORMATION_MESSAGE);

              // Recargar ofertas
              cargarOfertas();

          } catch (TiquetesNoDisponiblesException e) {
              JOptionPane.showMessageDialog(this,
                  "Oferta no disponible\n\n" + e.getMessage(),
                  "Error",
                  JOptionPane.ERROR_MESSAGE);
              cargarOfertas(); // Actualizar lista
          } catch (TransferenciaNoPermitidaException e) {
              JOptionPane.showMessageDialog(this,
                  "Transferencia no permitida\n\n" + e.getMessage(),
                  "Error",
                  JOptionPane.ERROR_MESSAGE);
          } catch (SaldoInsuficienteExeption e) {
              JOptionPane.showMessageDialog(this,
                  "Saldo insuficiente\n\n" + e.getMessage(),
                  "Error",
                  JOptionPane.ERROR_MESSAGE);
          } catch (Exception e) {
              JOptionPane.showMessageDialog(this,
                  "Error al realizar la compra:\n" + e.getMessage(),
                  "Error",
                  JOptionPane.ERROR_MESSAGE);
              e.printStackTrace();
          }
      }
	
	




   private void volverAlMenu() {
   
	dispose();
	
    if (comprador instanceof Promotor) {
        new ventanaMenuPromotor((Promotor) comprador, sistema).setVisible(true);
    } else if (comprador instanceof Organizador) {
        new ventanaMenuOrganizador((Organizador) comprador, sistema).setVisible(true);
    } else if (comprador instanceof Cliente) {
    	 new ventanaMenuComprador((Cliente) comprador ,sistema).setVisible(true);
    }
         
}
   
   private void actualizarSaldo() {
       if (comprador instanceof IDuenoTiquetes) {
           IDuenoTiquetes dueno = (IDuenoTiquetes) comprador;
           lblSaldoActual.setText(String.format("Saldo: $%.2f", dueno.getSaldo()));
       }
   }

   /**
    * Trunca un texto a una longitud máxima
    */
   private String truncar(String texto, int maxLength) {
       if (texto == null) return "";
       if (texto.length() <= maxLength) return texto;
       return texto.substring(0, maxLength - 3) + "...";
   }

  private static class OfertaInfo {
    Tiquete tiquete;
    Usuario vendedor;
    double precio;
    String etiqueta;
    HashMap<Tiquete, String> ofertaOriginal;

    public OfertaInfo(Tiquete tiquete, Usuario vendedor, double precio, 
                     String etiqueta, HashMap<Tiquete, String> ofertaOriginal) {
        this.tiquete = tiquete;
        this.vendedor = vendedor;
        this.precio = precio;
        this.etiqueta = etiqueta;
        this.ofertaOriginal = ofertaOriginal;
    }
}
	
	
	
}
