package sistema_yhor;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

public class FormConsultarVentas extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaVentas;

	public FormConsultarVentas() {
		setTitle("Historial de Ventas Realizadas - VendeMass");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 430);
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 242, 245));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(51, 65, 85));
		headerPanel.setBounds(0, 0, 800, 55);
		contentPane.add(headerPanel);
		headerPanel.setLayout(null);

		JLabel lblTitulo = new JLabel("📋 HISTORIAL CRONOLÓGICO DE VENTAS");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setBounds(25, 12, 500, 30);
		headerPanel.add(lblTitulo);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(25, 80, 735, 250);
		contentPane.add(scrollPane);

		tablaVentas = new JTable();
		tablaVentas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		tablaVentas.setRowHeight(22);
		scrollPane.setViewportView(tablaVentas);

		JButton btnCerrar = new JButton("Volver");
		btnCerrar.setBackground(new Color(71, 85, 105));
		btnCerrar.setForeground(Color.WHITE);
		btnCerrar.setBounds(590, 345, 170, 35);
		btnCerrar.addActionListener(e -> dispose());
		contentPane.add(btnCerrar);

		cargarHistorialVentas();
	}

	private void cargarHistorialVentas() {
		String[] columnas = {"ID Venta", "Cliente", "Cajero", "Turno", "Pago", "Total Pagado", "Fecha / Hora"};
		DefaultTableModel modelo = new DefaultTableModel(null, columnas) {
			private static final long serialVersionUID = 1L;
			@Override public boolean isCellEditable(int r, int c) { return false; }
		};

		try {
			Connection cn = Conexion.conectar();
			String sql = "SELECT id_venta, cliente, cajero, turno, metodo_pago, total_pagado, fecha_hora FROM venta ORDER BY fecha_hora DESC";
			PreparedStatement pst = cn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();

			Object[] fila = new Object[7];
			while (rs.next()) {
				fila[0] = rs.getInt("id_venta");
				fila[1] = rs.getString("cliente");
				fila[2] = rs.getString("cajero");
				fila[3] = rs.getString("turno");
				fila[4] = rs.getString("metodo_pago");
				fila[5] = "S/. " + String.format("%.2f", rs.getDouble("total_pagado"));
				fila[6] = rs.getString("fecha_hora");
				modelo.addRow(fila);
			}
			tablaVentas.setModel(modelo);
			cn.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}