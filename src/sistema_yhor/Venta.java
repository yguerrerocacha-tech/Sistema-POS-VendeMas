package sistema_yhor;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable; 
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Toolkit;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class Venta extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtIdVenta;
	private JTextField txtNombreVenta;
	private JTextField txtPrecioVenta;
	private JTextField txtCantidadVenta;
	private JTextField txtDescuento;
	private JTextField txtDniVenta;
	private JTextField txtClienteNombre;
	private JComboBox<String> cboMetodoPago;
	private JButton btnBuscarCliente;
	
	private JScrollPane scrollCarrito;
	private JTable tablaCarrito;
	private DefaultTableModel modeloCarrito;
	private JButton btnFinalizar;
	private JButton btnCancelar;
	private JLabel lblTotalParcial;
	private JLabel lblFotoProducto;
	
	private JButton btnIrAlmacen;
	private JButton btnConsultarVentas;
	
	private JScrollPane scrollVisorTicket;
	private JTextArea txtVisorTicket;
	
	private double subtotalAcumulado = 0.0;
	private java.util.List<Integer> listaIds = new java.util.ArrayList<>();
	private java.util.List<Integer> listaCantidades = new java.util.ArrayList<>();

	public Venta() {
		if (!Conexion.usuarioLogueado) {
			JOptionPane.showMessageDialog(null, "⚠️ ACCESO DENEGADO. Debe iniciar sesión en el Login primero.", "Seguridad VendeMás", JOptionPane.ERROR_MESSAGE);
			
			boolean encontrada = false;
			for (java.awt.Window window : java.awt.Window.getWindows()) {
				if (window instanceof Principal) {
					window.setVisible(true);
					encontrada = true;
					break;
				}
			}
			if (!encontrada) {
				Principal login = new Principal();
				login.setLocationRelativeTo(null);
				login.setVisible(true);
			}
			
			this.dispose(); 
			return; 
		}

		setTitle("Punto de Venta - VendeMás");
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1366, 768);
   
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 242, 245)); 
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(52, 58, 64)); 
		headerPanel.setBounds(0, 0, 2500, 70);
		contentPane.add(headerPanel);
		headerPanel.setLayout(null);

		JLabel lblTitulo = new JLabel("PUNTO DE VENTA (CAJA)");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitulo.setBounds(40, 15, 400, 40);
		headerPanel.add(lblTitulo);

		JPanel formPanel = new JPanel();
		formPanel.setBackground(Color.WHITE);
		formPanel.setBorder(new LineBorder(new Color(220, 224, 230), 1, true));
		formPanel.setBounds(10, 120, 460, 580);
		contentPane.add(formPanel);
		formPanel.setLayout(null);

		JLabel lblBuscar = new JLabel("Escanear / Buscar Producto");
		lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblBuscar.setBounds(20, 15, 250, 25);
		formPanel.add(lblBuscar);
  
		JLabel lblId = new JLabel("ID Producto:");
		lblId.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblId.setBounds(20, 55, 90, 25);
		formPanel.add(lblId);

		txtIdVenta = new JTextField();
		txtIdVenta.setBounds(110, 55, 110, 30);
		formPanel.add(txtIdVenta);
		configurarSoloNumeros(txtIdVenta);
		
		lblFotoProducto = new JLabel("[ Sin Imagen ]");
		lblFotoProducto.setHorizontalAlignment(JLabel.CENTER);
		lblFotoProducto.setBorder(new LineBorder(new Color(220, 224, 230), 1));
		lblFotoProducto.setBounds(240, 55, 200, 155);
		formPanel.add(lblFotoProducto);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setBackground(new Color(0, 123, 255)); 
		btnBuscar.setForeground(Color.WHITE);
		btnBuscar.setBorderPainted(false);
		btnBuscar.setFocusPainted(false);
		btnBuscar.setBounds(110, 95, 110, 30);
		formPanel.add(btnBuscar);
		
		txtIdVenta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnBuscar.doClick();
				}
			}
		});

		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection cn = Conexion.conectar();
					PreparedStatement pst = cn.prepareStatement("SELECT * FROM productos WHERE id_producto = ?");
					pst.setInt(1, Integer.parseInt(txtIdVenta.getText()));
					ResultSet rs = pst.executeQuery();
					if (rs.next()) {
						txtNombreVenta.setText(rs.getString("nombre_producto")); 
						txtPrecioVenta.setText(String.format("%.2f", rs.getDouble("precio")));
						
						int stockActual = rs.getInt("stock");
						JOptionPane.showMessageDialog(Venta.this, 
							"📦 Producto Seleccionado: " + rs.getString("nombre_producto") + 
							"\n Stock Available: " + stockActual + " unidades.", 
							"Inventario", JOptionPane.INFORMATION_MESSAGE);
						
						String rutaImagen = rs.getString("imagen_ruta");
						colocarImagen(lblFotoProducto, rutaImagen);
						txtCantidadVenta.requestFocus(); 
					} else {
						JOptionPane.showMessageDialog(Venta.this, " Producto no encontrado.");
						txtNombreVenta.setText(""); txtPrecioVenta.setText("");
						lblFotoProducto.setIcon(null); lblFotoProducto.setText("[ Sin Imagen ]");
					}
				} catch (Exception ex) { 
					JOptionPane.showMessageDialog(Venta.this, "❌ Error: ID inválido.");
				}
			}
		});

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNombre.setBounds(20, 140, 90, 25);
		formPanel.add(lblNombre);

		txtNombreVenta = new JTextField();
		txtNombreVenta.setEditable(false);
		txtNombreVenta.setBounds(110, 140, 110, 30);
		formPanel.add(txtNombreVenta);

		JLabel lblPrecio = new JLabel("Precio (S/):");
		lblPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblPrecio.setBounds(20, 180, 90, 25);
		formPanel.add(lblPrecio);

		txtPrecioVenta = new JTextField();
		txtPrecioVenta.setEditable(false);
		txtPrecioVenta.setBounds(110, 180, 110, 30);
		formPanel.add(txtPrecioVenta);

		JLabel lblCantidad = new JLabel("Cantidad:");
		lblCantidad.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblCantidad.setBounds(20, 230, 90, 25);
		formPanel.add(lblCantidad);

		txtCantidadVenta = new JTextField();
		txtCantidadVenta.setBounds(110, 230, 110, 30);
		formPanel.add(txtCantidadVenta);
		configurarSoloNumeros(txtCantidadVenta);

		JLabel lblDesc = new JLabel("Desc (%):");
		lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblDesc.setBounds(20, 275, 90, 25);
		formPanel.add(lblDesc);

		txtDescuento = new JTextField();
		txtDescuento.setBounds(110, 275, 110, 30);
		formPanel.add(txtDescuento);
		configurarSoloNumeros(txtDescuento);

		JLabel lblMetodoPago = new JLabel("Método de Pago:");
		lblMetodoPago.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblMetodoPago.setBounds(20, 320, 110, 25);
		formPanel.add(lblMetodoPago);

		String[] opcionesPago = {"EFECTIVO", "YAPE", "PLIN", "TARJETA"};
		cboMetodoPago = new JComboBox<>(opcionesPago);
		cboMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboMetodoPago.setBounds(130, 320, 120, 30);
		formPanel.add(cboMetodoPago);

		JLabel lblDni = new JLabel("DNI Cliente:");
		lblDni.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblDni.setBounds(20, 370, 90, 25);
		formPanel.add(lblDni);

		txtDniVenta = new JTextField();
		txtDniVenta.setBounds(110, 370, 120, 30);
		formPanel.add(txtDniVenta);
		configurarSoloNumeros(txtDniVenta);

		btnBuscarCliente = new JButton("🔍");
		btnBuscarCliente.setBackground(new Color(240, 242, 245));
		btnBuscarCliente.setFocusPainted(false);
		btnBuscarCliente.setBounds(240, 370, 50, 30);
		formPanel.add(btnBuscarCliente);

		JLabel lblNomCli = new JLabel("Cliente:");
		lblNomCli.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNomCli.setBounds(20, 415, 90, 25);
		formPanel.add(lblNomCli);
		
		txtClienteNombre = new JTextField();
		txtClienteNombre.setBounds(110, 415, 275, 30);
		formPanel.add(txtClienteNombre);

		btnBuscarCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dni = txtDniVenta.getText().trim();
				String nombreEscrito = txtClienteNombre.getText().trim();
				
				if (dni.length() != 8) {
					JOptionPane.showMessageDialog(Venta.this, 
						"⚠️ El DNI debe tener exactamente 8 dígitos para ser válido.", 
						"Error de Validación", JOptionPane.WARNING_MESSAGE);
					txtDniVenta.requestFocus();
					return; 
				}
				
				try {
					Connection cn = Conexion.conectar();
					PreparedStatement pst = cn.prepareStatement("SELECT nombre_completo FROM clientes WHERE dni = ?");
					pst.setString(1, dni);
					ResultSet rs = pst.executeQuery();
					if (rs.next()) {
						txtClienteNombre.setText(rs.getString("nombre_completo"));
						actualizarVistaPreviaLive();
					} else if (!nombreEscrito.isEmpty()) {
						PreparedStatement pstIns = cn.prepareStatement("INSERT INTO clientes (dni, nombre_completo) VALUES (?, ?)");
						pstIns.setString(1, dni);
						pstIns.setString(2, nombreEscrito);
						pstIns.executeUpdate();
						JOptionPane.showMessageDialog(Venta.this, "✨ Cliente registrado.");
						actualizarVistaPreviaLive();
					} else {
						JOptionPane.showMessageDialog(Venta.this, "Cliente nuevo. Escribe su nombre y presiona la lupa.");
						txtClienteNombre.requestFocus();
					}
				} catch (Exception ex) {
					System.out.println("Error cliente: " + ex.getMessage());
				}
			}
		});

		JButton btnCalcular = new JButton("AGREGAR AL CARRITO");
		btnCalcular.setBackground(new Color(0, 123, 255)); 
		btnCalcular.setForeground(Color.WHITE);
		btnCalcular.setBorderPainted(false);
		btnCalcular.setFocusPainted(false);
		btnCalcular.setFont(new Font("Segoe UI", Font.BOLD, 15));
		btnCalcular.setBounds(20, 490, 420, 45);
		formPanel.add(btnCalcular);
		
		txtCantidadVenta.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnCalcular.doClick();
				}
			}
		});

		btnCalcular.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection cn = Conexion.conectar();
					int idProd = Integer.parseInt(txtIdVenta.getText().trim());
					int cant = Integer.parseInt(txtCantidadVenta.getText().trim());

					PreparedStatement pst = cn.prepareStatement("SELECT * FROM productos WHERE id_producto = ?");
					pst.setInt(1, idProd);
					ResultSet rs = pst.executeQuery();

					if (rs.next()) {
						int stockActual = rs.getInt("stock");
						if (stockActual >= cant) {
							listaIds.add(idProd);
							listaCantidades.add(cant);

							double precio = rs.getDouble("precio");
							String nombre = rs.getString("nombre_producto");
							double subtotalItem = precio * cant;

							subtotalAcumulado += subtotalItem;
							
							Object[] nuevaFila = {idProd, nombre, cant, "S/. " + String.format("%.2f", precio), "S/. " + String.format("%.2f", subtotalItem)};
							modeloCarrito.addRow(nuevaFila);

							lblTotalParcial.setText("SUBTOTAL PARCIAL: S/. " + String.format("%.2f", subtotalAcumulado));

							txtIdVenta.setText(""); txtNombreVenta.setText(""); txtPrecioVenta.setText(""); txtCantidadVenta.setText("");
							lblFotoProducto.setIcon(null); lblFotoProducto.setText("[ Sin Imagen ]");
							
							actualizarVistaPreviaLive();
							txtIdVenta.requestFocus(); 
						} else {
							JOptionPane.showMessageDialog(Venta.this, "❌ Stock insuficiente.");
						}
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(Venta.this, "❌ Complete ID y Cantidad.");
				}
			}
		});
		
		scrollCarrito = new JScrollPane();
		contentPane.add(scrollCarrito);

		String[] columnasHeaders = {"ID", "Descripción del Producto", "Cant.", "P. Unitario", "Total"};
		modeloCarrito = new DefaultTableModel(null, columnasHeaders) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};
		
		tablaCarrito = new JTable(modeloCarrito);
		tablaCarrito.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		tablaCarrito.setRowHeight(24);
		scrollCarrito.setViewportView(tablaCarrito);
		
		lblTotalParcial = new JLabel("SUBTOTAL PARCIAL: S/. 0.00");
		lblTotalParcial.setFont(new Font("Segoe UI", Font.BOLD, 16));
		contentPane.add(lblTotalParcial);

		txtVisorTicket = new JTextArea();
		txtVisorTicket.setEditable(false);
		txtVisorTicket.setBackground(new Color(255, 255, 240)); 
		txtVisorTicket.setFont(new Font("Consolas", Font.PLAIN, 12));
		
		scrollVisorTicket = new JScrollPane(txtVisorTicket);
		scrollVisorTicket.setBorder(new LineBorder(new Color(180, 180, 180), 1));
		contentPane.add(scrollVisorTicket);

		btnFinalizar = new JButton("FINALIZAR E IMPRIMIR TXT");
		btnFinalizar.setBackground(new Color(40, 167, 69)); 
		btnFinalizar.setForeground(Color.WHITE);
		btnFinalizar.setBorderPainted(false);
		btnFinalizar.setFocusPainted(false);
		btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 15));
		contentPane.add(btnFinalizar);
		btnFinalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (subtotalAcumulado == 0) return;

				java.sql.Connection cn = null;
				java.sql.PreparedStatement pstVenta = null;
				java.sql.PreparedStatement pstDetalle = null;
				java.sql.PreparedStatement pstStock = null;
				java.sql.ResultSet rsKeys = null;

				try {
					cn = Conexion.conectar();
					cn.setAutoCommit(false);

					double totalFinal = subtotalAcumulado;
					double montoDesc = 0.0;
					String descStr = txtDescuento.getText().trim();
					if(!descStr.isEmpty()){
						int p = Integer.parseInt(descStr);
						totalFinal = Math.round((subtotalAcumulado - (subtotalAcumulado * (p / 100.0))) * 100.0) / 100.0;;
						montoDesc = subtotalAcumulado - totalFinal;
					}
					
					String clienteForm = txtClienteNombre.getText().trim();
					if(clienteForm.isEmpty()) clienteForm = "Público General";
				   
					String queryVenta = "INSERT INTO venta (cliente, subtotal, descuento, total_pagado, cajero, turno, metodo_pago, fecha_hora) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
					pstVenta = cn.prepareStatement(queryVenta, java.sql.Statement.RETURN_GENERATED_KEYS);
					pstVenta.setString(1, clienteForm);
					pstVenta.setDouble(2, subtotalAcumulado);
					pstVenta.setDouble(3, montoDesc);
					pstVenta.setDouble(4, totalFinal);
					
					pstVenta.setString(5, Principal.usuarioActivo);
					pstVenta.setString(6, Principal.turnoActivo);
					pstVenta.setString(7, cboMetodoPago.getSelectedItem().toString());
					
					pstVenta.executeUpdate();

					rsKeys = pstVenta.getGeneratedKeys();
					int idVentaGenerado = 0;
					if (rsKeys.next()) {
						idVentaGenerado = rsKeys.getInt(1);
					}

					String queryDetalle = "INSERT INTO detalle_venta (id_venta, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
					pstDetalle = cn.prepareStatement(queryDetalle);

					String queryStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ?";
					pstStock = cn.prepareStatement(queryStock);

					for (int i = 0; i < tablaCarrito.getRowCount(); i++) {
						int idProd = Integer.parseInt(tablaCarrito.getValueAt(i, 0).toString());
						int cant = Integer.parseInt(tablaCarrito.getValueAt(i, 2).toString());
						String precioStr = tablaCarrito.getValueAt(i, 3).toString().replace("S/. ", "").trim();
						double precioUnit = Double.parseDouble(precioStr);

						pstDetalle.setInt(1, idVentaGenerado);
						pstDetalle.setInt(2, idProd);
						pstDetalle.setInt(3, cant);
						pstDetalle.setDouble(4, precioUnit);
						pstDetalle.addBatch();

						pstStock.setInt(1, cant);
						pstStock.setInt(2, idProd);
						pstStock.addBatch();
					}

					pstDetalle.executeBatch();
					pstStock.executeBatch();

					cn.commit();
					
					BoletaVenta boleta = new BoletaVenta();
				
					String textoFinal = boleta.obtenerTextoTicket(clienteForm, subtotalAcumulado, totalFinal, tablaCarrito);
					boleta.abrirEnBlocDeNotas(textoFinal, clienteForm);

					subtotalAcumulado = 0.0;
					listaIds.clear();
					listaCantidades.clear();
					txtDescuento.setText(""); txtDniVenta.setText(""); txtClienteNombre.setText("");
					modeloCarrito.setRowCount(0); 
					lblTotalParcial.setText("SUBTOTAL PARCIAL: S/. 0.00");
					txtVisorTicket.setText(""); 
					
					Venta.this.revalidate();
					Venta.this.repaint();

				} catch (Exception ex) {
					try { if (cn != null) cn.rollback(); } catch (Exception eRoll) {}
					JOptionPane.showMessageDialog(Venta.this, "❌ Error transaccional.");
				} finally { 
					try {
						if (rsKeys != null) rsKeys.close();
						if (pstVenta != null) pstVenta.close();
						if (pstDetalle != null) pstDetalle.close();
						if (pstStock != null) pstStock.close();
						if (cn != null) cn.close();
					} catch (Exception eClose) {}
				}
				
			}
		});
		
		btnCancelar = new JButton("CANCELAR");
		btnCancelar.setBackground(new Color(220, 53, 69)); 
		btnCancelar.setForeground(Color.WHITE);
		btnCancelar.setBorderPainted(false);
		btnCancelar.setFocusPainted(false);
		btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 15));
		contentPane.add(btnCancelar);
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(subtotalAcumulado == 0) return;
				int op = JOptionPane.showConfirmDialog(Venta.this, "¿Cancelar la venta?", "Confirmar", JOptionPane.YES_NO_OPTION);
				if (op == JOptionPane.YES_OPTION) {
					subtotalAcumulado = 0.0;
					listaIds.clear(); listaCantidades.clear(); txtDescuento.setText("");
					modeloCarrito.setRowCount(0);
					lblTotalParcial.setText("SUBTOTAL PARCIAL: S/. 0.00");
					txtVisorTicket.setText("");
					Toolkit.getDefaultToolkit().beep();
				}
			}
		});

		btnIrAlmacen = new JButton("IR A GESTIÓN ALMACÉN");
		btnIrAlmacen.addActionListener(e -> {
			Conexion.usuarioLogueado = true;
			
			boolean encontrada = false;
			for (java.awt.Window window : java.awt.Window.getWindows()) {
				if (window instanceof V1) {
					window.setVisible(true); 
					encontrada = true;
					break;
				}
			}
			
			if (!encontrada) {
				V1 ventanaAlmacen = new V1(); 
				ventanaAlmacen.setLocationRelativeTo(null);
				ventanaAlmacen.setVisible(true);
			}
			
			setVisible(false); 
		});
		btnIrAlmacen.setBounds(10, 75, 200, 36);
		btnIrAlmacen.setBackground(new Color(108, 117, 125)); 
		btnIrAlmacen.setForeground(Color.WHITE);
		btnIrAlmacen.setBorderPainted(false);
		btnIrAlmacen.setFocusPainted(false);
		btnIrAlmacen.setFont(new Font("Segoe UI", Font.BOLD, 13));
		contentPane.add(btnIrAlmacen);

		btnConsultarVentas = new JButton("📋 Consultar Ventas");
		btnConsultarVentas.setBackground(new Color(51, 65, 85));
		btnConsultarVentas.setForeground(Color.WHITE);
		btnConsultarVentas.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnConsultarVentas.setBorderPainted(false);
		btnConsultarVentas.setFocusPainted(false);
		btnConsultarVentas.setBounds(225, 75, 200, 36);
		btnConsultarVentas.addActionListener(e -> {
			FormConsultarVentas ventanaHistorial = new FormConsultarVentas();
			ventanaHistorial.setLocationRelativeTo(Venta.this);
			ventanaHistorial.setVisible(true);
		});
		contentPane.add(btnConsultarVentas);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int anchoTotal = contentPane.getWidth();
				int altoTotal = contentPane.getHeight();
				
				int anchoVisor = 320; 
				int anchoScrollCarrito = anchoTotal - 490 - anchoVisor - 30;
				int altoScroll = altoTotal - 280;
				
				if (altoScroll < 250) altoScroll = 250;
				if (anchoScrollCarrito < 250) anchoScrollCarrito = 250;
				
				scrollCarrito.setBounds(490, 120, anchoScrollCarrito, altoScroll);
				lblTotalParcial.setBounds(490, 120 + altoScroll + 10, 400, 25);
				
				scrollVisorTicket.setBounds(490 + anchoScrollCarrito + 15, 120, anchoVisor, altoScroll + 35);
				
				int ejeYBotones = 120 + altoScroll + 50;
				btnFinalizar.setBounds(490, ejeYBotones, 280, 50);
				btnCancelar.setBounds(490 + 295, ejeYBotones, 160, 50);
			}
		});
	}

	private void actualizarVistaPreviaLive() {
		String clienteForm = txtClienteNombre.getText().trim();
		if(clienteForm.isEmpty()) clienteForm = "Público General";
		
		double totalFinal = subtotalAcumulado;
		String descStr = txtDescuento.getText().trim();
		if(!descStr.isEmpty()){
			try {
				int p = Integer.parseInt(descStr);
				totalFinal = Math.round((subtotalAcumulado - (subtotalAcumulado * (p / 100.0))) * 100.0) / 100.0;
			} catch(Exception e){}
		}
		
		BoletaVenta visualizador = new BoletaVenta();
		String bosquejo = visualizador.obtenerTextoTicket(clienteForm, subtotalAcumulado, totalFinal, tablaCarrito);
		txtVisorTicket.setText(bosquejo);
	}

	private void colocarImagen(JLabel label, String nombreImagen) {
		try {
			if (nombreImagen == null || nombreImagen.trim().isEmpty() || nombreImagen.equals("NULL")) {
				label.setIcon(null); label.setText("[ Sin Imagen ]"); return;
			}
			URL url = getClass().getResource("/imagen/" + nombreImagen);
			if (url != null) {
				ImageIcon icon = new ImageIcon(url);
				Image img = icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
				label.setIcon(new ImageIcon(img)); label.setText(""); 
			} else {
				label.setIcon(null); label.setText("[ No Encontrada ]");
			}
		} catch (Exception e) {
			label.setIcon(null); label.setText("[ Error Imagen ]");
		}
	}

	private void configurarSoloNumeros(JTextField campo) {
		campo.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c)) {
					e.consume();
					return;
				}
				if (campo == txtDniVenta) {
					if (txtDniVenta.getText().length() >= 8) {
						e.consume(); 
					}
				}
			}
		});
	}
}