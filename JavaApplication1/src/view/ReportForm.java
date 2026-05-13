package view;

import dao.InventoryDAO;
import dao.ReportDAO;
import dao.ProductDAO;
import model.Inventory;
import model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReportForm extends JFrame {
	private JTable tblLowStock, tblTopSelling;
	private DefaultTableModel modelLowStock, modelTopSelling;

	private InventoryDAO inventoryDAO = new InventoryDAO();
	private ReportDAO reportDAO = new ReportDAO();
	private ProductDAO productDAO = new ProductDAO();

	public ReportForm() {
		initComponents();
		setLocationRelativeTo(null);
		loadLowStock();
		loadTopSelling();
	}

	private void initComponents() {
		setTitle("Báo cáo Thống kê");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 500);
		setLayout(new BorderLayout());

		JLabel lblTitle = new JLabel("BÁO CÁO THỐNG KÊ", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
		add(lblTitle, BorderLayout.NORTH);

		JTabbedPane tabbedPane = new JTabbedPane();

		// Tab 1: Cảnh báo sắp hết hàng
		modelLowStock = new DefaultTableModel(new Object[] { "Mã Kho", "Tên Sản Phẩm", "Tồn kho", "Mức tối thiểu" }, 0);
		tblLowStock = new JTable(modelLowStock);
		tabbedPane.addTab("Cảnh báo Hết hàng", new JScrollPane(tblLowStock));

		// Tab 2: Sản phẩm bán chạy
		modelTopSelling = new DefaultTableModel(new Object[] { "Tên Sản Phẩm", "Tổng số lượng đã bán" }, 0);
		tblTopSelling = new JTable(modelTopSelling);
		tabbedPane.addTab("Sản phẩm Bán chạy", new JScrollPane(tblTopSelling));

		add(tabbedPane, BorderLayout.CENTER);
	}

	private void loadLowStock() {
		modelLowStock.setRowCount(0);
		List<Inventory> list = inventoryDAO.lowStock();
		for (Inventory i : list) {
			String productName = getProductNameById(i.getProductId());
			modelLowStock.addRow(new Object[] { i.getInventoryId(), productName, i.getQuantity(), i.getMinQuantity() });
		}
	}

	private void loadTopSelling() {
		modelTopSelling.setRowCount(0);
		List<Object[]> topProducts = reportDAO.getTopSellingProducts(10); // Top 10
		for (Object[] row : topProducts) {
			modelTopSelling.addRow(row);
		}
	}

	private String getProductNameById(int id) {
		for (Product p : productDAO.selectAll()) {
			if (p.getProductId() == id)
				return p.getProductName();
		}
		return "Unknown";
	}
}