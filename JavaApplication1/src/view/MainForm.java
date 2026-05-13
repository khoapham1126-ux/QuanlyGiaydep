package view;

import java.awt.*;
import javax.swing.*;

public class MainForm extends JFrame {
	private JButton btnProduct, btnCategory, btnSupplier, btnInventory;
        private JButton btnEmployee, btnImport, btnReport, btnLogout;
        private JButton btnCustomer, btnSell, btnInvoice;

	public MainForm() {
		initComponents();
		setLocationRelativeTo(null);
	}

	private void initComponents() {
		setTitle("Hệ thống Quản lý Giày dép");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setSize(800, 500);
		setLayout(new BorderLayout(10, 10));

		JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
		lblTitle.setForeground(new Color(0, 102, 204));
		lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
		add(lblTitle, BorderLayout.NORTH);

		JPanel pnCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
		pnCenter.setBorder(BorderFactory.createTitledBorder("Chọn chức năng"));

		btnProduct = new JButton("Quản lý Sản phẩm");
		btnCategory = new JButton("Quản lý Danh mục");
		btnSupplier = new JButton("Quản lý Nhà cung cấp");
		btnInventory = new JButton("Quản lý Tồn kho");
		btnEmployee = new JButton("Quản lý Nhân viên");
		btnImport = new JButton("Nhập Kho");
		btnReport = new JButton("Báo Cáo Thống Kê");
		btnLogout = new JButton("Đăng xuất");
                btnCustomer = new JButton("Quản lý Khách hàng");
                btnSell = new JButton("Bán Hàng");
                btnInvoice = new JButton("Lịch sử Hóa đơn");

		Dimension btnSize = new Dimension(200, 45);
		JButton[] buttons = { btnProduct, btnCategory, btnSupplier, btnInventory,
                                    btnEmployee, btnImport, btnReport,
                                    btnCustomer, btnSell, btnInvoice,
                                    btnLogout };
		for (JButton btn : buttons) {
			btn.setPreferredSize(btnSize);
			pnCenter.add(btn);
		}

		add(pnCenter, BorderLayout.CENTER);

		btnProduct.addActionListener(e -> new ProductForm().setVisible(true));
		btnCategory.addActionListener(e -> new CategoryForm().setVisible(true));
		btnSupplier.addActionListener(e -> new SupplierForm().setVisible(true));
		btnInventory.addActionListener(e -> new InventoryForm().setVisible(true));
		btnEmployee.addActionListener(e -> new EmployeeForm().setVisible(true));
		btnImport.addActionListener(e -> new ImportForm().setVisible(true));
		btnReport.addActionListener(e -> new ReportForm().setVisible(true));
                btnCustomer.addActionListener(e -> new CustomerForm().setVisible(true));
                btnSell.addActionListener(e -> new SellForm().setVisible(true));
                btnInvoice.addActionListener(e -> new InvoiceForm().setVisible(true));

		btnLogout.addActionListener(e -> {
			int confirm = JOptionPane.showConfirmDialog(this, "Bạn có muốn đăng xuất?", "Xác nhận",
					JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				new LoginForm().setVisible(true);
				this.dispose(); 
			}
		});
	}
}