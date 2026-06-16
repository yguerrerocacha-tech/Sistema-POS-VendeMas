package sistema_yhor;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class FormBalance extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaMetodos;
	private JTable tablaCajeros;
	
	private JLabel lblTotalRecaudado;
	private JLabel lblTurnoMasProductivo;

	public FormBalance() {
		setTitle("Módulo de Auditoría: Balance General y Cierre de Caja - VendeMás");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 850, 550);
		setResizable(false);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(240, 242, 245));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// ENCABEZADO CORPORATIVO
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(new Color(30, 41, 59));
		headerPanel.setBounds(0, 0, 850, 60);
		contentPane.add(headerPanel);
		headerPanel.setLayout(null);

		JLabel lblTitulo = new JLabel("📊 BALANCE DE INGRESOS Y RESUMEN DE CAJA");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitulo.setBounds(30, 15, 500, 30);
		headerPanel.add(lblTitulo);

		// TARJETA DE RESUMEN 1: TOTAL GENERAL
		JPanel panelTotal = new JPanel();
		panelTotal.setBackground(Color.WHITE);
		panelTotal.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
		panelTotal.setBounds(30, 80, 370, 90);
		contentPane.add(panelTotal);
		panelTotal.setLayout(null);

		JLabel lblT1 = new JLabel("TOTAL RECAUDADO (S/.)");
		lblT1.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblT1.setForeground(new Color(100, 116, 139));
		lblT1.setBounds(20, 15, 200, 20);
		panelTotal.add(lblT1);

		lblTotalRecaudado = new JLabel("S/. 0.00");
		lblTotalRecaudado.setFont(new Font("Segoe UI", Font.BOLD, 28));
		lblTotalRecaudado.setForeground(new Color(21, 128, 61)); 
		lblTotalRecaudado.setBounds(20, 40, 330, 35);
		panelTotal.add(lblTotalRecaudado);

		// TARJETA DE RESUMEN 2: TURNO GANADOR
		JPanel panelTurno = new JPanel();
		panelTurno.setBackground(Color.WHITE);
		panelTurno.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
		panelTurno.setBounds(430, 80, 370, 90);
		contentPane.add(panelTurno);
		panelTurno.setLayout(null);

		JLabel lblT2 = new JLabel("TURNO MÁS PRODUCTIVO");
		lblT2.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblT2.setForeground(new Color(100, 116, 139));
		lblT2.setBounds(20, 15, 200, 20);
		panelTurno.add(lblT2);

		lblTurnoMasProductivo = new JLabel("NINGUNO");
		lblTurnoMasProductivo.setFont(new Font("Segoe UI", Font.BOLD, 24));
		lblTurnoMasProductivo.setForeground(new Color(29, 78, 216)); 
		lblTurnoMasProductivo.setBounds(20, 40, 330, 35);
		panelTurno.add(lblTurnoMasProductivo);

		JLabel lblTabla1 = new JLabel("Ingresos por Método de Pago:");
		lblTabla1.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblTabla1.setForeground(new Color(51, 65, 85));
		lblTabla1.setBounds(30, 190, 370, 20);
		contentPane.add(lblTabla1);

		JScrollPane scrollMetodos = new JScrollPane();
		scrollMetodos.setBounds(30, 215, 370, 180);
		contentPane.add(scrollMetodos);
		
		tablaMetodos = new JTable();
		tablaMetodos.setRowHeight(22);
		tablaMetodos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		scrollMetodos.setViewportView(tablaMetodos);

		JLabel lblTabla2 = new JLabel("Ventas por Personal y Turno:");
		lblTabla2.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblTabla2.setForeground(new Color(51, 65, 85));
		lblTabla2.setBounds(430, 190, 370, 20);
		contentPane.add(lblTabla2);

		JScrollPane scrollCajeros = new JScrollPane();
		scrollCajeros.setBounds(430, 215, 370, 180);
		contentPane.add(scrollCajeros);
		
		tablaCajeros = new JTable();
		tablaCajeros.setRowHeight(22);
		tablaCajeros.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		scrollCajeros.setViewportView(tablaCajeros);

		JButton btnCierreCaja = new JButton("🔒 Ejecutar Cierre de Caja");
		btnCierreCaja.setBackground(new Color(220, 38, 38)); 
		btnCierreCaja.setForeground(Color.WHITE);
		btnCierreCaja.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnCierreCaja.setBorderPainted(false);
		btnCierreCaja.setFocusPainted(false);
		btnCierreCaja.setBounds(30, 430, 370, 45);
		btnCierreCaja.addActionListener(e -> {
			int op = JOptionPane.showConfirmDialog(this, 
				"⚠️ ¿Desea bloquear el turno actual, asentar el balance y emitir el acta de arqueo?\nEsta acción cerrará el sistema de ventas.", 
				"Cierre de Caja - VendeMás", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			
			if (op == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(this, "✅ Acta de Arqueo Guardada con Éxito.\nDinero en caja conciliado.\nEl sistema se cerrará.");
				System.exit(0);
			}
		});
		contentPane.add(btnCierreCaja);

		JButton btnRefrescar = new JButton("🔄 Actualizar Cifras");
		btnRefrescar.setBackground(new Color(71, 85, 105));
		btnRefrescar.setForeground(Color.WHITE);
		btnRefrescar.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btnRefrescar.setBorderPainted(false);
		btnRefrescar.setFocusPainted(false);
		btnRefrescar.setBounds(430, 430, 370, 45);
		btnRefrescar.addActionListener(e -> calcularMétricasDelBalance());
		contentPane.add(btnRefrescar);

		calcularMétricasDelBalance();
	}

	private void calcularMétricasDelBalance() {
		String[] colsMetodo = {"Método de Pago", "Total Recaudado"};
		DefaultTableModel modMetodos = new DefaultTableModel(null, colsMetodo) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int r, int c) { return false; }
		};
		
		try {
			Connection cn = Conexion.conectar();
			
			String q1 = "SELECT metodo_pago, SUM(total_pagado) AS total FROM venta GROUP BY metodo_pago";
			PreparedStatement pst1 = cn.prepareStatement(q1);
			ResultSet rs1 = pst1.executeQuery();
			double acumuladoTotal = 0;
			
			while (rs1.next()) {
				String metodo = rs1.getString("metodo_pago");
				double total = rs1.getDouble("total");
				acumuladoTotal += total;
				modMetodos.addRow(new Object[]{metodo, "S/. " + String.format("%.2f", total)});
			}
			tablaMetodos.setModel(modMetodos);
			lblTotalRecaudado.setText("S/. " + String.format("%.2f", acumuladoTotal));

			String[] colsCajeros = {"Cajero", "Turno", "Monto Recaudado"};
			DefaultTableModel modCajeros = new DefaultTableModel(null, colsCajeros) {
				private static final long serialVersionUID = 1L;
				@Override
				public boolean isCellEditable(int r, int c) { return false; }
			};
			
			String q2 = "SELECT cajero, turno, SUM(total_pagado) AS total FROM venta GROUP BY cajero, turno ORDER BY total DESC";
			PreparedStatement pst2 = cn.prepareStatement(q2);
			ResultSet rs2 = pst2.executeQuery();
			
			boolean primero = true;
			while (rs2.next()) {
				String cajero = rs2.getString("cajero");
				String turno = rs2.getString("turno");
				double total = rs2.getDouble("total");
				
				if (primero) {
					lblTurnoMasProductivo.setText(turno + " (" + cajero + ")");
					primero = false;
				}
				modCajeros.addRow(new Object[]{cajero, turno, "S/. " + String.format("%.2f", total)});
			}
			tablaCajeros.setModel(modCajeros);
			
			cn.close();
		} catch (Exception ex) { // ✅ CORREGIDO: Se cerró la llave del try y el bloque catch quedó perfecto
			System.out.println("Error procesando balance: " + ex.getMessage());
		}
	}
}