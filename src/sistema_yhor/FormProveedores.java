package sistema_yhor;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class FormProveedores extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtRuc;
	private JTextField txtRazonSocial;
	private JTextField txtTelefono;
	private JTextField txtDireccion;
	private JTable tablaProveedores;
	private DefaultTableModel modeloTabla;

	public FormProveedores() {
		setTitle("Gestión de Proveedores - VendeMás");
		// DISPOSE_ON_CLOSE es el correcto para que no destruya la máquina virtual de Java
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 780, 480);
		setResizable(false);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("REGISTRO DE PROVEEDORES");
		lblTitulo.setForeground(new Color(33, 37, 41));
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTitulo.setBounds(20, 15, 300, 25);
		contentPane.add(lblTitulo);

		JLabel lblRuc = new JLabel("RUC (Obligatorio):");
		lblRuc.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRuc.setForeground(new Color(73, 80, 87));
		lblRuc.setBounds(20, 60, 150, 20);
		contentPane.add(lblRuc);

		txtRuc = new JTextField();
		txtRuc.setBounds(20, 82, 220, 30);
		contentPane.add(txtRuc);
		txtRuc.setColumns(10);

		JLabel lblRazonSocial = new JLabel("Razón Social:");
		lblRazonSocial.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRazonSocial.setForeground(new Color(73, 80, 87));
		lblRazonSocial.setBounds(20, 125, 150, 20);
		contentPane.add(lblRazonSocial);

		txtRazonSocial = new JTextField();
		txtRazonSocial.setBounds(20, 147, 220, 30);
		contentPane.add(txtRazonSocial);

		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTelefono.setForeground(new Color(73, 80, 87));
		lblTelefono.setBounds(20, 190, 150, 20);
		contentPane.add(lblTelefono);

		txtTelefono = new JTextField();
		txtTelefono.setBounds(20, 212, 220, 30);
		contentPane.add(txtTelefono);

		JLabel lblDireccion = new JLabel("Dirección:");
		lblDireccion.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDireccion.setForeground(new Color(73, 80, 87));
		lblDireccion.setBounds(20, 255, 150, 20);
		contentPane.add(lblDireccion);

		txtDireccion = new JTextField();
		txtDireccion.setBounds(20, 277, 220, 30);
		contentPane.add(txtDireccion);

		JButton btnGuardar = new JButton("GUARDAR");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardarProveedor();
			}
		});
		btnGuardar.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnGuardar.setForeground(Color.WHITE);
		btnGuardar.setBackground(new Color(40, 167, 69));
		btnGuardar.setBorderPainted(false);
		btnGuardar.setFocusPainted(false);
		btnGuardar.setBounds(20, 335, 220, 35);
		contentPane.add(btnGuardar);

		// CORRECCIÓN DE NAVEGACIÓN AQUÍ
		JButton btnCancelar = new JButton("CANCELAR / SALIR");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 1. Instanciamos la ventana de Almacén para regresar de forma segura
				V1 ventanaAlmacen = new V1();
				ventanaAlmacen.setVisible(true);
				
				// 2. Cerramos únicamente la ventana actual de proveedores
				dispose();
			}
		});
		btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnCancelar.setForeground(Color.WHITE);
		btnCancelar.setBackground(new Color(220, 53, 69));
		btnCancelar.setBorderPainted(false);
		btnCancelar.setFocusPainted(false);
		btnCancelar.setBounds(20, 385, 220, 35);
		contentPane.add(btnCancelar);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(270, 60, 475, 360);
		contentPane.add(scrollPane);

		tablaProveedores = new JTable();
		modeloTabla = new DefaultTableModel(
			new Object[][] {},
			new String[] { "RUC", "Razón Social", "Teléfono", "Dirección" }
		) {
			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] { false, false, false, false };
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		tablaProveedores.setModel(modeloTabla);
		tablaProveedores.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(tablaProveedores);

		listarProveedores();
	}

	private void guardarProveedor() {
		String ruc = txtRuc.getText().trim();
		String razonSocial = txtRazonSocial.getText().trim();
		String telefono = txtTelefono.getText().trim();
		String direccion = txtDireccion.getText().trim();

		if (ruc.isEmpty() || razonSocial.isEmpty()) {
			JOptionPane.showMessageDialog(null, "El RUC y la Razón Social son obligatorios.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			Connection cn = Conexion.conectar(); 
			PreparedStatement pst = cn.prepareStatement("INSERT INTO proveedores (ruc, razon_social, telefono, direccion) VALUES (?,?,?,?)");
			
			pst.setString(1, ruc);
			pst.setString(2, razonSocial);
			pst.setString(3, telefono);
			pst.setString(4, direccion);
			
			pst.executeUpdate();
			JOptionPane.showMessageDialog(null, "Proveedor registrado con éxito.");
			
			txtRuc.setText("");
			txtRazonSocial.setText("");
			txtTelefono.setText("");
			txtDireccion.setText("");
			
			listarProveedores();
			
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al guardar el proveedor: " + ex.getMessage());
		}
	}

	private void listarProveedores() {
		modeloTabla.setRowCount(0); 
		try {
			Connection cn = Conexion.conectar(); 
			PreparedStatement pst = cn.prepareStatement("SELECT ruc, razon_social, telefono, direccion FROM proveedores");
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				Object[] fila = new Object[4];
				fila[0] = rs.getString("ruc");
				fila[1] = rs.getString("razon_social");
				fila[2] = rs.getString("telefono");
				fila[3] = rs.getString("direccion");
				modeloTabla.addRow(fila);
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al listar proveedores: " + ex.getMessage());
		}
	}

	public void reportarProductosPorProveedor(DefaultTableModel modeloTablaReporte, String rucProveedor) {
		modeloTablaReporte.setRowCount(0); 
		String sql = "SELECT p.id_producto, p.nombre_producto, p.stock, p.precio " +
		             "FROM productos p " +
		             "INNER JOIN proveedores pr ON p.proveedor_ruc = pr.ruc " +
		             "WHERE pr.ruc = ?";
		try {
			Connection cn = Conexion.conectar();
			PreparedStatement pst = cn.prepareStatement(sql);
			pst.setString(1, rucProveedor);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				Object[] fila = new Object[4];
				fila[0] = rs.getInt("id_producto");
				fila[1] = rs.getString("nombre_producto");
				fila[2] = rs.getInt("stock");
				fila[3] = rs.getDouble("precio");
				modeloTablaReporte.addRow(fila);
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + ex.getMessage());
		}
	}

	public void sumarStockInventario(int idProducto, int cantidadEntrante) {
		String sql = "UPDATE productos SET stock = stock + ? WHERE id_producto = ?";
		try {
			Connection cn = Conexion.conectar();
			PreparedStatement pst = cn.prepareStatement(sql);
			pst.setInt(1, cantidadEntrante);
			pst.setInt(2, idProducto);
			
			int filas = pst.executeUpdate();
			if (filas > 0) {
				JOptionPane.showMessageDialog(null, "¡Almacén Actualizado! Se añadieron " + cantidadEntrante + " unidades al producto.");
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al sumar stock: " + ex.getMessage());
		}
	}
}