package sistema_yhor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentAdapter; 
import java.awt.event.ComponentEvent;
import java.awt.Cursor;
import java.awt.Toolkit;
import javax.swing.JScrollPane;

public class V1 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtProd;
	private JTextField txtPrec;
	private JTextField txtStock;
	private JComboBox<String> cboProveedor; 
	private java.io.File archivoOrigen = null;
	
	private String nombreImagenSeleccionada = "NULL";
	
	private JTable tablaProductos; 
	private JScrollPane scrollPane; 
	
	private JTable tablaHistorial;
	private JScrollPane scrollHistorial;
	private DefaultTableModel modeloHistorial;
	
	private JButton btnActualizar;  
	private JButton btnCerrarSesion; 

	public V1() {
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

		setTitle("Módulo de Almacén - VendeMás");
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

		JLabel lblTitulo = new JLabel("GESTIÓN DE INVENTARIO Y HISTORIAL DE AUDITORÍA");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTitulo.setBounds(40, 15, 600, 40);
		headerPanel.add(lblTitulo);

		JButton btnVolverVenta = new JButton(" VOLVER A VENTAS (CAJA)");
		btnVolverVenta.addActionListener(e -> {
			Conexion.usuarioLogueado = true;
			boolean encontrada = false;
			for (java.awt.Window window : java.awt.Window.getWindows()) {
				if (window instanceof Venta) {
					window.setVisible(true); 
					encontrada = true;
					break;
				}
			}
			if (!encontrada) {
				Venta ventanaVenta = new Venta();
				ventanaVenta.setLocationRelativeTo(null);
				ventanaVenta.setVisible(true);
			}
			setVisible(false);
		});
		btnVolverVenta.setBounds(10, 73, 240, 36); 
		btnVolverVenta.setBackground(Color.WHITE); 
		btnVolverVenta.setForeground(Color.BLACK);
		btnVolverVenta.setFont(new Font("Segoe UI", Font.BOLD, 14));
		contentPane.add(btnVolverVenta);

		JPanel formPanel = new JPanel();
		formPanel.setBackground(Color.WHITE);
		formPanel.setBorder(new LineBorder(new Color(218, 224, 230), 1, true)); 
		formPanel.setBounds(50, 110, 400, 540); 
		contentPane.add(formPanel);
		formPanel.setLayout(null);

		JLabel lblIngreso = new JLabel("Datos del Producto");
		lblIngreso.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblIngreso.setForeground(new Color(45, 55, 72));
		lblIngreso.setBounds(30, 15, 200, 30);
		formPanel.add(lblIngreso);

		JLabel lblId = new JLabel("ID:");
		lblId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblId.setForeground(new Color(74, 85, 104));
		lblId.setBounds(30, 55, 80, 25);
		formPanel.add(lblId);

		txtId = new JTextField();
		txtId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtId.setBorder(new LineBorder(new Color(203, 213, 224), 1, true));
		txtId.setBounds(130, 55, 220, 30);
		formPanel.add(txtId);
		configurarSoloNumbers(txtId);

		JLabel lblProd = new JLabel("Producto:");
		lblProd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblProd.setForeground(new Color(74, 85, 104));
		lblProd.setBounds(30, 95, 80, 25);
		formPanel.add(lblProd);

		txtProd = new JTextField();
		txtProd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtProd.setBorder(new LineBorder(new Color(203, 213, 224), 1, true));
		txtProd.setBounds(130, 95, 220, 30);
		formPanel.add(txtProd);

		JLabel lblPrec = new JLabel("Precio (S/):");
		lblPrec.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblPrec.setForeground(new Color(74, 85, 104));
		lblPrec.setBounds(30, 135, 80, 25);
		formPanel.add(lblPrec);

		txtPrec = new JTextField();
		txtPrec.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtPrec.setBorder(new LineBorder(new Color(203, 213, 224), 1, true));
		txtPrec.setBounds(130, 135, 220, 30);
		formPanel.add(txtPrec);

		JLabel lblStock = new JLabel("Stock Inicial:");
		lblStock.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblStock.setForeground(new Color(74, 85, 104));
		lblStock.setBounds(30, 175, 80, 25);
		formPanel.add(lblStock);

		txtStock = new JTextField();
		txtStock.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtStock.setBorder(new LineBorder(new Color(203, 213, 224), 1, true));
		txtStock.setBounds(130, 175, 220, 30);
		formPanel.add(txtStock);
		configurarSoloNumbers(txtStock);

		JLabel lblFoto = new JLabel("Foto Producto:");
		lblFoto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblFoto.setForeground(new Color(74, 85, 104));
		lblFoto.setBounds(30, 215, 100, 25);
		formPanel.add(lblFoto);

		JButton btnCargarFoto = new JButton("📁 Seleccionar Foto");
		btnCargarFoto.setBackground(new Color(241, 245, 249));
		btnCargarFoto.setForeground(new Color(51, 65, 85));
		btnCargarFoto.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnCargarFoto.setBorder(new LineBorder(new Color(148, 163, 184), 1, true));
		btnCargarFoto.setFocusPainted(false);
		btnCargarFoto.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCargarFoto.setBounds(130, 215, 220, 30);
		btnCargarFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
				fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "png", "jpeg"));
				int seleccion = fileChooser.showOpenDialog(V1.this);
				
				if (seleccion == javax.swing.JFileChooser.APPROVE_OPTION) {
					archivoOrigen = fileChooser.getSelectedFile();
					nombreImagenSeleccionada = archivoOrigen.getName(); 
					
					btnCargarFoto.setText("✅ " + archivoOrigen.getName());
					btnCargarFoto.setBackground(new Color(224, 242, 254)); 
					btnCargarFoto.setBorder(new LineBorder(new Color(56, 189, 248), 1, true));
				}
			}
		});
		formPanel.add(btnCargarFoto);

		JLabel lblProveedor = new JLabel("Proveedor:");
		lblProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblProveedor.setForeground(new Color(74, 85, 104));
		lblProveedor.setBounds(30, 255, 80, 25);
		formPanel.add(lblProveedor);

		cboProveedor = new JComboBox<>();
		cboProveedor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		cboProveedor.setBounds(130, 255, 220, 30);
		formPanel.add(cboProveedor);

		JButton btnIrProveedores = new JButton("➕ CONFIGURAR PROVEEDORES");
		btnIrProveedores.setBackground(new Color(71, 85, 105)); 
		btnIrProveedores.setForeground(Color.WHITE);
		btnIrProveedores.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnIrProveedores.setBorderPainted(false);
		btnIrProveedores.setFocusPainted(false);
		btnIrProveedores.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnIrProveedores.setBounds(130, 290, 220, 25);
		btnIrProveedores.addActionListener(e -> {
			FormProveedores ventanaPro = new FormProveedores();
			ventanaPro.setVisible(true);
			dispose(); 
		});
		formPanel.add(btnIrProveedores);

		JButton btnInsertar = new JButton("Registrar Nuevo Producto");
		btnInsertar.setBackground(new Color(30, 141, 85)); 
		btnInsertar.setForeground(Color.WHITE);
		btnInsertar.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnInsertar.setBorderPainted(false);
		btnInsertar.setFocusPainted(false);
		btnInsertar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnInsertar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (nombreImagenSeleccionada.equals("NULL") || archivoOrigen == null) {
						JOptionPane.showMessageDialog(V1.this, "⚠️ ERROR: Debe seleccionar una foto obligatoriamente para cargar el producto nuevo.", "Validación de Foto", JOptionPane.ERROR_MESSAGE);
						btnCargarFoto.requestFocus();
						return;
					}

					try {
						java.io.File carpetaDestino = new java.io.File("src/imagen");
						if (!carpetaDestino.exists()) {
							carpetaDestino.mkdirs(); 
						}
						
						java.io.File archivoDestino = new java.io.File(carpetaDestino, nombreImagenSeleccionada);
						
						java.nio.file.Files.copy(
							archivoOrigen.toPath(), 
							archivoDestino.toPath(), 
							java.nio.file.StandardCopyOption.REPLACE_EXISTING
						);
					} catch (Exception errCopiar) {
						System.out.println("Aviso copia automática: " + errCopiar.getMessage());
					} // ✅ CORREGIDO: Se cerró el bloque try-catch de clonación correctamente

					int id = Integer.parseInt(txtId.getText().trim());
					String nombre = txtProd.getText().trim();
					double precio = Double.parseDouble(txtPrec.getText().trim());
					int stock = Integer.parseInt(txtStock.getText().trim());

					if (cboProveedor.getSelectedItem() == null) {
						JOptionPane.showMessageDialog(V1.this, "⚠️ ERROR: Debe registrar un proveedor primero.");
						return;
					}
					String seleccionCombo = cboProveedor.getSelectedItem().toString();
					String rucProveedor = seleccionCombo.substring(0, 11);

					Connection cn = Conexion.conectar(); 
					PreparedStatement pst = cn.prepareStatement(
						"INSERT INTO productos (id_producto, nombre_producto, precio, stock, proveedor_ruc, imagen_ruta) VALUES(?,?,?,?,?,?)");
					pst.setInt(1, id);
					pst.setString(2, nombre);
					pst.setDouble(3, precio);
					pst.setInt(4, stock);
					pst.setString(5, rucProveedor);
					pst.setString(6, nombreImagenSeleccionada);

					pst.executeUpdate();
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(V1.this, "¡Producto registrado con éxito!\nLa foto se procesó y guardó de forma automática.");
					
					actualizarLista(); 
					limpiarCajas();
					
					btnCargarFoto.setText("📁 Seleccionar Foto");
					btnCargarFoto.setBackground(new Color(241, 245, 249));
					btnCargarFoto.setBorder(new LineBorder(new Color(148, 163, 184), 1, true));
					nombreImagenSeleccionada = "NULL";
					archivoOrigen = null;
					
					cn.close();
				} catch (Exception ex) { 
					JOptionPane.showMessageDialog(V1.this, "❌ Error: " + ex.getMessage());
				}
			}
		});
				JButton btnConsultarVentas = new JButton("📋 Consultar Ventas");
				btnConsultarVentas.setBackground(new Color(51, 65, 85));
				btnConsultarVentas.setForeground(Color.WHITE);
				btnConsultarVentas.setFont(new Font("Segoe UI", Font.BOLD, 14));
				btnConsultarVentas.setBorderPainted(false);
				btnConsultarVentas.setFocusPainted(false);
				btnConsultarVentas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				
			
				btnConsultarVentas.setBounds(30, 500, 320, 40); 
				
				btnConsultarVentas.addActionListener(e -> {
					FormConsultarVentas ventanaHistorial = new FormConsultarVentas();
					ventanaHistorial.setLocationRelativeTo(V1.this); 
					ventanaHistorial.setVisible(true);
				});
				formPanel.add(btnConsultarVentas);
		btnInsertar.setBounds(30, 345, 320, 40);
		formPanel.add(btnInsertar);

		JButton btnModificar = new JButton("Modificar / Sumar Stock");
		btnModificar.setBackground(new Color(0, 123, 255)); 
		btnModificar.setForeground(Color.WHITE);
		btnModificar.setBorderPainted(false);
		btnModificar.setFocusPainted(false);
		btnModificar.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int id = Integer.parseInt(txtId.getText().trim());
					String nombre = txtProd.getText().trim();
					double precio = Double.parseDouble(txtPrec.getText().trim());

					String cantPaquetesStr = JOptionPane.showInputDialog(V1.this, "📦 ¿Cuántos PAQUETES/CAJAS están ingresando?:", "Entrada de Mercadería", JOptionPane.QUESTION_MESSAGE);
					if (cantPaquetesStr == null || cantPaquetesStr.trim().isEmpty()) return;
					int paquetes = Integer.parseInt(cantPaquetesStr.trim());
					
					String unidadesPorPaqueteStr = JOptionPane.showInputDialog(V1.this, "🔢 ¿Cuántas UNIDADES contiene cada paquete? (Mínimo 6):", "Configurar Empaque", JOptionPane.QUESTION_MESSAGE);
					if (unidadesPorPaqueteStr == null || unidadesPorPaqueteStr.trim().isEmpty()) return;
					int unidadesPorPaquete = Integer.parseInt(unidadesPorPaqueteStr.trim());

					if (unidadesPorPaquete < 6) {
						JOptionPane.showMessageDialog(V1.this, "⚠️ REGLA COMERCIAL: El distribuidor solo abastece por paquetes de mínimo 6 unidades.", "Validación", JOptionPane.WARNING_MESSAGE);
						return;
					}

					int totalUnidadesASumar = paquetes * unidadesPorPaquete;
					Connection cn = Conexion.conectar();
					
					int stockAnterior = 0;
					String rucProveedor = null;
					PreparedStatement pstStock = cn.prepareStatement("SELECT stock, proveedor_ruc FROM productos WHERE id_producto = ?");
					pstStock.setInt(1, id);
					ResultSet rsS = pstStock.executeQuery();
					if (rsS.next()) {
						stockAnterior = rsS.getInt("stock");
						rucProveedor = rsS.getString("proveedor_ruc");
					}
					int stockNuevo = stockAnterior + totalUnidadesASumar;

					PreparedStatement pstUpdate = cn.prepareStatement("UPDATE productos SET nombre_producto=?, precio=?, stock = ? WHERE id_producto=?");
					pstUpdate.setString(1, nombre);
					pstUpdate.setDouble(2, precio);
					pstUpdate.setInt(3, stockNuevo);
					pstUpdate.setInt(4, id);
					
					if (pstUpdate.executeUpdate() > 0) {
						PreparedStatement pstHistorial = cn.prepareStatement(
							"INSERT INTO historial_abastecimiento (id_producto, ruc_proveedor, cantidad_paquetes, unidades_por_paquete, stock_anterior, stock_nuevo) VALUES (?,?,?,?,?,?)");
						pstHistorial.setInt(1, id);
						pstHistorial.setString(2, rucProveedor);
						pstHistorial.setInt(3, paquetes);
						pstHistorial.setInt(4, unidadesPorPaquete);
						pstHistorial.setInt(5, stockAnterior);
						pstHistorial.setInt(6, stockNuevo);
						pstHistorial.executeUpdate();

						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(V1.this, "¡Inventario actualizado con éxito!");
						
						actualizarLista();
						listarHistorial(); 
						limpiarCajas();
					}
					cn.close();
				} catch (Exception ex) { 
					JOptionPane.showMessageDialog(V1.this, "❌ Error: " + ex.getMessage());
				}
			}
		});
		btnModificar.setBounds(30, 395, 180, 40);
		formPanel.add(btnModificar);

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setBackground(new Color(220, 53, 69)); 
		btnEliminar.setForeground(Color.WHITE);
		btnEliminar.setBorderPainted(false);
		btnEliminar.setFocusPainted(false);
		btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnEliminar.addActionListener(e -> {
			try {
				int idProducto = Integer.parseInt(txtId.getText().trim());
				
				int confirmar = JOptionPane.showConfirmDialog(V1.this, 
					"⚠️ ¿Está seguro de eliminar este producto? Se borrará también su historial de auditoría.", 
					"Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
				
				if (confirmar == JOptionPane.YES_OPTION) {
					Connection cn = Conexion.conectar();
					
					PreparedStatement pstHistorial = cn.prepareStatement("DELETE FROM historial_abastecimiento WHERE id_producto = ?");
					pstHistorial.setInt(1, idProducto);
					pstHistorial.executeUpdate();
					
					PreparedStatement pstProducto = cn.prepareStatement("DELETE FROM productos WHERE id_producto = ?");
					pstProducto.setInt(1, idProducto);
					
					if (pstProducto.executeUpdate() > 0) {
						JOptionPane.showMessageDialog(V1.this, "¡Producto e historial removidos con éxito!");
						actualizarLista();
						listarHistorial();
						limpiarCajas();
					}
					cn.close();
				}
			} catch (Exception ex) { 
				JOptionPane.showMessageDialog(V1.this, "❌ Error al eliminar: " + ex.getMessage());
			}
		});
		btnEliminar.setBounds(220, 395, 130, 40);
		formPanel.add(btnEliminar);

		scrollPane = new JScrollPane();
		contentPane.add(scrollPane);
		tablaProductos = new JTable();
		tablaProductos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		tablaProductos.setRowHeight(22); 
		scrollPane.setViewportView(tablaProductos);

		tablaProductos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = tablaProductos.getSelectedRow();
				if (fila >= 0) {
					txtId.setText(tablaProductos.getValueAt(fila, 0).toString());
					txtProd.setText(tablaProductos.getValueAt(fila, 1).toString());
					String pre = tablaProductos.getValueAt(fila, 2).toString().replace("S/. ", "").trim();
					txtPrec.setText(pre);
					
					txtStock.setText(tablaProductos.getValueAt(fila, 3).toString());
					txtStock.setEditable(false);
					txtStock.setBackground(new Color(237, 242, 249)); 
				}
			}
		});

		scrollHistorial = new JScrollPane();
		contentPane.add(scrollHistorial);
		
		tablaHistorial = new JTable();
		tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tablaHistorial.setRowHeight(20);
		scrollHistorial.setViewportView(tablaHistorial);

		btnActualizar = new JButton("🔄 Refrescar Todo");
		btnActualizar.setBackground(new Color(108, 117, 125)); 
		btnActualizar.setForeground(Color.WHITE);
		btnActualizar.setBorderPainted(false);
		btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnActualizar.addActionListener(e -> {
			actualizarLista();
			listarHistorial();
		});
		contentPane.add(btnActualizar);
		
		btnCerrarSesion = new JButton("🚪 Cerrar Sesión");
		btnCerrarSesion.setBackground(new Color(220, 53, 69));
		btnCerrarSesion.setForeground(Color.WHITE);
		btnCerrarSesion.setBorderPainted(false);
		btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnCerrarSesion.addActionListener(e -> {
			int op = JOptionPane.showConfirmDialog(V1.this, "¿Desea cerrar sesión y salir al Login?", "Cerrar Sesión - VendeMás", JOptionPane.YES_NO_OPTION);
			if (op == JOptionPane.YES_OPTION) {
				Conexion.usuarioLogueado = false; 
				
				Principal login = new Principal();
				login.setLocationRelativeTo(null);
				login.setVisible(true);
				
				dispose(); 
			}
		});
		contentPane.add(btnCerrarSesion);
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int anchoVentana = contentPane.getWidth();
				int altoVentana = contentPane.getHeight();
				
				int nuevoAnchoScroll = anchoVentana - 530; 
				int altoMitad = (altoVentana - 240) / 2;   
				
				if (nuevoAnchoScroll > 200 && altoMitad > 50) {
					scrollPane.setBounds(480, 110, nuevoAnchoScroll, altoMitad);
					scrollHistorial.setBounds(480, 110 + altoMitad + 20, nuevoAnchoScroll, altoMitad - 20);
					
					btnActualizar.setBounds(480, 110 + (altoMitad * 2) + 15, 200, 35);
					btnCerrarSesion.setBounds(480 + 215, 110 + (altoMitad * 2) + 15, 200, 35); 
				}
			}
		});
		
		cargarComboProveedores(); 
		actualizarLista();
		listarHistorial(); 
	}
	
	private void cargarComboProveedores() {
		try {
			Connection cn = Conexion.conectar();
			PreparedStatement pst = cn.prepareStatement("SELECT ruc, razon_social FROM proveedores");
			ResultSet rs = pst.executeQuery();
			cboProveedor.removeAllItems();
			while (rs.next()) {
				cboProveedor.addItem(rs.getString("ruc") + " - " + rs.getString("razon_social"));
			}
			cn.close();
		} catch (Exception e) {
			System.out.println("Error combo: " + e.getMessage());
		}
	}
	
	private void actualizarLista() {
		String[] columnas = {"ID", "Producto", "Precio (S/.)", "Stock"};
		DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};
		try {
			Connection cn = Conexion.conectar();
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery("SELECT id_producto, nombre_producto, precio, stock FROM productos");
			String[] fila = new String[4];
			while (rs.next()) {
				fila[0] = rs.getString("id_producto");
				fila[1] = rs.getString("nombre_producto");
				fila[2] = "S/. " + String.format("%.2f", rs.getDouble("precio"));
				fila[3] = rs.getString("stock");
				modelo.addRow(fila);
			}
			tablaProductos.setModel(modelo);
			cn.close();
		} catch (Exception e) {
			System.out.println("Error tabla productos: " + e.getMessage());
		}
	}

	private void listarHistorial() {
		String[] columnasHistorial = {"ID Mov", "Producto", "Distribuidor", "Pqtes", "U x Pqte", "Stock Ant.", "Stock Nuevo", "Fecha/Hora"};
		modeloHistorial = new DefaultTableModel(null, columnasHistorial) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};
		try {
			Connection cn = Conexion.conectar();
			String query = "SELECT h.id_historial, p.nombre_producto, pr.razon_social, h.cantidad_paquetes, h.unidades_por_paquete, h.stock_anterior, h.stock_nuevo, h.fecha_hora " +
			               "FROM historial_abastecimiento h " +
			               "INNER JOIN productos p ON h.id_producto = p.id_producto " +
			               "INNER JOIN proveedores pr ON h.ruc_proveedor = pr.ruc " +
			               "ORDER BY h.fecha_hora DESC";
			Statement st = cn.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			Object[] fila = new Object[8];
			while(rs.next()) {
				fila[0] = rs.getInt("id_historial");
				fila[1] = rs.getString("nombre_producto");
				fila[2] = rs.getString("razon_social"); 
				fila[3] = rs.getInt("cantidad_paquetes");
				fila[4] = rs.getInt("unidades_por_paquete");
				fila[5] = rs.getInt("stock_anterior");
				fila[6] = rs.getInt("stock_nuevo");
				fila[7] = rs.getString("fecha_hora");
				modeloHistorial.addRow(fila);
			}
			tablaHistorial.setModel(modeloHistorial);
			cn.close();
		} catch(Exception e) {
			System.out.println("Error tabla historial: " + e.getMessage());
		}
	} 

	private void limpiarCajas() { 
		txtId.setText(""); 
		txtProd.setText(""); 
		txtPrec.setText(""); 
		txtStock.setText(""); 
		txtStock.setEditable(true); 
		txtStock.setBackground(Color.WHITE);
		nombreImagenSeleccionada = "NULL";
		archivoOrigen = null; 
	}

	private void configurarSoloNumbers(JTextField campo) {
		campo.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c)) e.consume();
			}
		});
	}
}