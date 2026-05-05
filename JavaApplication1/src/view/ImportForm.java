package view;

import dao.ImportReceiptDAO;
import dao.ProductDAO;
import dao.SupplierDAO;
import model.ImportReceipt;
import model.ImportReceiptDetail;
import model.Product;
import model.Supplier;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ImportForm extends JFrame {
	private JComboBox<String> cboSupplier, cboProduct;
	private JTextField txtQuantity, txtImportPrice;
	private JButton btnAddToList, btnSaveReceipt;
	private JTable tblImport;
	private DefaultTableModel tableModel;

	private ProductDAO productDAO = new ProductDAO();
	private SupplierDAO supplierDAO = new SupplierDAO();
	private ImportReceiptDAO importDAO = new ImportReceiptDAO();
	private List<ImportReceiptDetail> pendingDetails = new ArrayList<>();

	public ImportForm() {
		initComponents();
		setLocationRelativeTo(null);
		loadCombos();
	}

	private void initComponents() {
		setTitle("Nhập kho hàng hóa");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(950, 650);
		setLayout(new BorderLayout(10, 10));

		JPanel pnTop = new JPanel(new GridBagLayout());
		pnTop.setBorder(BorderFactory.createTitledBorder("Thông tin nhập hàng"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		cboSupplier = new JComboBox<>();
		cboProduct = new JComboBox<>();
		txtQuantity = new JTextField(10);
		txtImportPrice = new JTextField(15);
		btnAddToList = new JButton("Thêm vào phiếu");

		gbc.gridx = 0;
		gbc.gridy = 0;
		pnTop.add(new JLabel("Nhà Cung Cấp:"), gbc);
		gbc.gridx = 1;
		pnTop.add(cboSupplier, gbc);
		gbc.gridx = 2;
		pnTop.add(new JLabel("Sản phẩm:"), gbc);
		gbc.gridx = 3;
		pnTop.add(cboProduct, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		pnTop.add(new JLabel("Số lượng:"), gbc);
		gbc.gridx = 1;
		pnTop.add(txtQuantity, gbc);
		gbc.gridx = 2;
		pnTop.add(new JLabel("Giá nhập:"), gbc);
		gbc.gridx = 3;
		pnTop.add(txtImportPrice, gbc);
		gbc.gridx = 4;
		pnTop.add(btnAddToList, gbc);

		add(pnTop, BorderLayout.NORTH);

		tableModel = new DefaultTableModel(new Object[] { "Mã SP", "Tên SP", "Số lượng", "Giá nhập", "Thành tiền" }, 0);
		tblImport = new JTable(tableModel);
		add(new JScrollPane(tblImport), BorderLayout.CENTER);

		JPanel pnBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		btnSaveReceipt = new JButton("Lưu Phiếu Nhập");
		pnBottom.add(btnSaveReceipt);
		add(pnBottom, BorderLayout.SOUTH);

		btnAddToList.addActionListener(e -> addToPendingList());
		btnSaveReceipt.addActionListener(e -> saveReceipt());
	}

	private void loadCombos() {
		for (Supplier s : supplierDAO.selectAll())
			cboSupplier.addItem(s.getSupplierId() + " - " + s.getSupplierName());
		for (Product p : productDAO.selectAll())
			cboProduct.addItem(p.getProductId() + " - " + p.getProductName());
	}

	private void addToPendingList() {
		try {
			int productId = Integer.parseInt(cboProduct.getSelectedItem().toString().split(" - ")[0]);
			String productName = cboProduct.getSelectedItem().toString().split(" - ")[1];
			int qty = Integer.parseInt(txtQuantity.getText().trim());
			double price = Double.parseDouble(txtImportPrice.getText().trim());

			ImportReceiptDetail detail = new ImportReceiptDetail(0, productId, qty, price);
			pendingDetails.add(detail);

			tableModel.addRow(new Object[] { productId, productName, qty, price, qty * price });
			txtQuantity.setText("");
			txtImportPrice.setText("");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng số lượng và giá!");
		}
	}

	private void saveReceipt() {
		if (pendingDetails.isEmpty())
			return;
		try {
			int supplierId = Integer.parseInt(cboSupplier.getSelectedItem().toString().split(" - ")[0]);
			double total = 0;
			for (ImportReceiptDetail d : pendingDetails)
				total += (d.getQuantity() * d.getImportPrice());

			ImportReceipt receipt = new ImportReceipt();
			receipt.setSupplierId(supplierId);
			receipt.setTotalAmount(total);
			receipt.setCreatedBy(LoginForm.currentUser.getEmployeeId());
			receipt.setDetails(pendingDetails);

			if (importDAO.insertImportAndInventory(receipt)) {
				JOptionPane.showMessageDialog(this, "Lưu phiếu nhập thành công! Kho đã được cộng.");
				tableModel.setRowCount(0);
				pendingDetails.clear();
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Lỗi khi lưu phiếu nhập!");
		}
	}
}