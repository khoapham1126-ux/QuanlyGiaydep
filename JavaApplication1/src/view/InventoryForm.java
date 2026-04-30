package view;

import dao.InventoryDAO;
import dao.ProductDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.Inventory;
import model.Product;

public class InventoryForm extends JFrame {

    private JTextField txtInventoryId, txtQuantity, txtMinQuantity, txtSearch;
    private JComboBox<String> cboProduct;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JTable tblInventory;
    private DefaultTableModel tableModel;

    private InventoryDAO inventoryDAO = new InventoryDAO();
    private ProductDAO productDAO = new ProductDAO();

    public InventoryForm() {
        initComponents();
        setLocationRelativeTo(null);
        loadProducts();
        loadInventories();
        clearForm();
    }

    private void initComponents() {
        setTitle("Quản lý tồn kho");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ TỒN KHO", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnTop = new JPanel(new BorderLayout(10, 10));

        JPanel pnForm = new JPanel(new java.awt.GridBagLayout());
        pnForm.setBorder(BorderFactory.createTitledBorder("Thông tin tồn kho"));
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(8, 10, 8, 10);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        JLabel lblInventoryId = new JLabel("Mã tồn kho:");
        JLabel lblProduct = new JLabel("Sản phẩm:");
        JLabel lblQuantity = new JLabel("Số lượng:");
        JLabel lblMinQuantity = new JLabel("Ngưỡng tối thiểu:");

        txtInventoryId = new JTextField(18);
        txtInventoryId.setEditable(false);
        cboProduct = new JComboBox<>();
        txtQuantity = new JTextField(18);
        txtMinQuantity = new JTextField(18);

        gbc.gridx = 0; gbc.gridy = 0;
        pnForm.add(lblInventoryId, gbc);
        gbc.gridx = 1;
        pnForm.add(txtInventoryId, gbc);

        gbc.gridx = 2;
        pnForm.add(lblProduct, gbc);
        gbc.gridx = 3;
        pnForm.add(cboProduct, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnForm.add(lblQuantity, gbc);
        gbc.gridx = 1;
        pnForm.add(txtQuantity, gbc);

        gbc.gridx = 2;
        pnForm.add(lblMinQuantity, gbc);
        gbc.gridx = 3;
        pnForm.add(txtMinQuantity, gbc);

        pnTop.add(pnForm, BorderLayout.CENTER);

        JPanel pnButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");
        btnSearch = new JButton("Tìm");

        txtSearch = new JTextField(18);

        pnButton.add(btnAdd);
        pnButton.add(btnUpdate);
        pnButton.add(btnDelete);
        pnButton.add(btnClear);
        pnButton.add(new JLabel("Tìm kiếm:"));
        pnButton.add(txtSearch);
        pnButton.add(btnSearch);

        pnTop.add(pnButton, BorderLayout.SOUTH);
        add(pnTop, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new Object[]{"Mã TK", "Sản phẩm", "Số lượng", "Ngưỡng tối thiểu", "Cập nhật lần cuối"}, 0
        );
        tblInventory = new JTable(tableModel);
        tblInventory.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tblInventory);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách tồn kho"));
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> btnAddActionPerformed());
        btnUpdate.addActionListener(e -> btnUpdateActionPerformed());
        btnDelete.addActionListener(e -> btnDeleteActionPerformed());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> btnSearchActionPerformed());

        tblInventory.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tblInventoryMouseClicked();
            }
        });
    }

    private void loadProducts() {
        cboProduct.removeAllItems();
        List<Product> list = productDAO.selectAll();
        for (Product p : list) {
            cboProduct.addItem(p.getProductId() + " - " + p.getProductName());
        }
    }

    private void loadInventories() {
        tableModel.setRowCount(0);
        List<Inventory> list = inventoryDAO.selectAll();
        for (Inventory i : list) {
            tableModel.addRow(new Object[]{
                i.getInventoryId(),
                getProductNameById(i.getProductId()),
                i.getQuantity(),
                i.getMinQuantity(),
                i.getLastUpdated()
            });
        }
    }

    private String getProductNameById(int id) {
        List<Product> list = productDAO.selectAll();
        for (Product p : list) {
            if (p.getProductId() == id) {
                return p.getProductName();
            }
        }
        return "";
    }

    private int getSelectedProductId() {
        String item = cboProduct.getSelectedItem().toString();
        return Integer.parseInt(item.split(" - ")[0]);
    }

    private void clearForm() {
        txtInventoryId.setText("");
        if (cboProduct.getItemCount() > 0) cboProduct.setSelectedIndex(0);
        txtQuantity.setText("");
        txtMinQuantity.setText("");
        txtSearch.setText("");
        tblInventory.clearSelection();
    }

    private boolean validateForm() {
        if (txtQuantity.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng");
            return false;
        }
        if (txtMinQuantity.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ngưỡng tối thiểu");
            return false;
        }
        return true;
    }

    private Inventory getFormData() {
        Inventory i = new Inventory();
        if (!txtInventoryId.getText().trim().isEmpty()) {
            i.setInventoryId(Integer.parseInt(txtInventoryId.getText().trim()));
        }
        i.setProductId(getSelectedProductId());
        i.setQuantity(Integer.parseInt(txtQuantity.getText().trim()));
        i.setMinQuantity(Integer.parseInt(txtMinQuantity.getText().trim()));
        return i;
    }

    private void setFormData(Inventory i) {
        txtInventoryId.setText(String.valueOf(i.getInventoryId()));
        txtQuantity.setText(String.valueOf(i.getQuantity()));
        txtMinQuantity.setText(String.valueOf(i.getMinQuantity()));
        selectComboByProductId(i.getProductId());
    }

    private void selectComboByProductId(int id) {
        for (int i = 0; i < cboProduct.getItemCount(); i++) {
            String item = cboProduct.getItemAt(i);
            if (Integer.parseInt(item.split(" - ")[0]) == id) {
                cboProduct.setSelectedIndex(i);
                break;
            }
        }
    }

    private void btnAddActionPerformed() {
        if (!validateForm()) return;
        try {
            if (inventoryDAO.insert(getFormData())) {
                JOptionPane.showMessageDialog(this, "Thêm tồn kho thành công");
                loadInventories();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm: " + e.getMessage());
        }
    }

    private void btnUpdateActionPerformed() {
        if (txtInventoryId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn bản ghi cần sửa");
            return;
        }
        if (!validateForm()) return;

        try {
            if (inventoryDAO.update(getFormData())) {
                JOptionPane.showMessageDialog(this, "Sửa thành công");
                loadInventories();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Sửa thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa: " + e.getMessage());
        }
    }

    private void btnDeleteActionPerformed() {
        if (txtInventoryId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn bản ghi cần xóa");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int id = Integer.parseInt(txtInventoryId.getText().trim());
            if (inventoryDAO.delete(id)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                loadInventories();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + e.getMessage());
        }
    }

    private void btnSearchActionPerformed() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<Inventory> list = inventoryDAO.selectAll();
        for (Inventory i : list) {
            String productName = getProductNameById(i.getProductId());
            if (productName.toLowerCase().contains(keyword.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    i.getInventoryId(),
                    productName,
                    i.getQuantity(),
                    i.getMinQuantity(),
                    i.getLastUpdated()
                });
            }
        }
    }

    private void tblInventoryMouseClicked() {
        int row = tblInventory.getSelectedRow();
        if (row != -1) {
            Inventory i = new Inventory(
                    Integer.parseInt(tblInventory.getValueAt(row, 0).toString()),
                    getProductIdByName(tblInventory.getValueAt(row, 1).toString()),
                    Integer.parseInt(tblInventory.getValueAt(row, 2).toString()),
                    Integer.parseInt(tblInventory.getValueAt(row, 3).toString()),
                    tblInventory.getValueAt(row, 4).toString()
            );
            setFormData(i);
        }
    }

    private int getProductIdByName(String name) {
        List<Product> list = productDAO.selectAll();
        for (Product p : list) {
            if (p.getProductName().equals(name)) {
                return p.getProductId();
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InventoryForm().setVisible(true));
    }
}