package view;

import dao.CategoryDAO;
import dao.ProductDAO;
import dao.SupplierDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.Category;
import model.Product;
import model.Supplier;

public class ProductForm extends JFrame {

    private JPanel pnMain, pnTop, pnForm, pnButton, pnTable, pnImage;
    private JLabel lblTitle, lblProductId, lblProductName, lblCategory, lblSupplier, lblSize, lblColor, lblPrice, lblQuantity, lblStatus, lblImage;
    private JTextField txtProductId, txtProductName, txtSize, txtColor, txtPrice, txtQuantity, txtSearch, txtImagePath;
    private JComboBox<String> cboCategory, cboSupplier;
    private JCheckBox chkStatus;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch, btnChooseImage;
    private JTable tblProduct;
    private DefaultTableModel tableModel;

    private CategoryDAO categoryDAO = new CategoryDAO();
    private SupplierDAO supplierDAO = new SupplierDAO();
    private ProductDAO productDAO = new ProductDAO();

    private String selectedImagePath = "";

    public ProductForm() {
        initComponents();
        setLocationRelativeTo(null);
        loadCategories();
        loadSuppliers();
        loadProducts();
        clearForm();
    }

    private void initComponents() {
        setTitle("Quản lý sản phẩm");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
        setLayout(new BorderLayout());

        pnMain = new JPanel(new BorderLayout(10, 10));
        pnMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnTop = new JPanel(new BorderLayout(10, 10));

        lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        pnTop.add(lblTitle, BorderLayout.NORTH);

        pnForm = new JPanel(new java.awt.GridBagLayout());
        pnForm.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(6, 8, 6, 8);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        lblProductId = new JLabel("Mã sản phẩm:");
        lblProductName = new JLabel("Tên sản phẩm:");
        lblCategory = new JLabel("Loại giày:");
        lblSupplier = new JLabel("Nhà cung cấp:");
        lblSize = new JLabel("Size:");
        lblColor = new JLabel("Màu:");
        lblPrice = new JLabel("Giá:");
        lblQuantity = new JLabel("Số lượng:");
        lblStatus = new JLabel("Trạng thái:");
        lblImage = new JLabel("Ảnh sản phẩm", SwingConstants.CENTER);

        txtProductId = new JTextField(15);
        txtProductId.setEditable(false);

        txtProductName = new JTextField(15);
        cboCategory = new JComboBox<>();
        cboSupplier = new JComboBox<>();
        txtSize = new JTextField(15);
        txtColor = new JTextField(15);
        txtPrice = new JTextField(15);
        txtQuantity = new JTextField(15);
        chkStatus = new JCheckBox("Còn bán");
        txtImagePath = new JTextField(15);
        txtImagePath.setEditable(false);
        btnChooseImage = new JButton("Chọn ảnh");

        pnImage = new JPanel(new BorderLayout());
        pnImage.setPreferredSize(new Dimension(180, 180));
        pnImage.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        pnImage.add(lblImage, BorderLayout.CENTER);

        gbc.gridx = 0; gbc.gridy = 0;
        pnForm.add(lblProductId, gbc);
        gbc.gridx = 1;
        pnForm.add(txtProductId, gbc);

        gbc.gridx = 2;
        pnForm.add(lblPrice, gbc);
        gbc.gridx = 3;
        pnForm.add(txtPrice, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnForm.add(lblProductName, gbc);
        gbc.gridx = 1;
        pnForm.add(txtProductName, gbc);

        gbc.gridx = 2;
        pnForm.add(lblQuantity, gbc);
        gbc.gridx = 3;
        pnForm.add(txtQuantity, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnForm.add(lblCategory, gbc);
        gbc.gridx = 1;
        pnForm.add(cboCategory, gbc);

        gbc.gridx = 2;
        pnForm.add(lblStatus, gbc);
        gbc.gridx = 3;
        pnForm.add(chkStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        pnForm.add(lblSupplier, gbc);
        gbc.gridx = 1;
        pnForm.add(cboSupplier, gbc);

        gbc.gridx = 2;
        pnForm.add(new JLabel("Đường dẫn ảnh:"), gbc);
        gbc.gridx = 3;
        pnForm.add(txtImagePath, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        pnForm.add(lblSize, gbc);
        gbc.gridx = 1;
        pnForm.add(txtSize, gbc);

        gbc.gridx = 2;
        pnForm.add(new JLabel("Ảnh:"), gbc);
        gbc.gridx = 3;
        pnForm.add(btnChooseImage, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        pnForm.add(lblColor, gbc);
        gbc.gridx = 1;
        pnForm.add(txtColor, gbc);

        gbc.gridx = 2;
        pnForm.add(new JLabel(""), gbc);
        gbc.gridx = 3;
        pnForm.add(pnImage, gbc);

        pnTop.add(pnForm, BorderLayout.CENTER);

        pnButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Sửa");
        btnDelete = new JButton("Xóa");
        btnClear = new JButton("Làm mới");
        btnSearch = new JButton("Tìm");

        txtSearch = new JTextField(20);
        txtSearch.setToolTipText("Nhập tên sản phẩm cần tìm");

        pnButton.add(btnAdd);
        pnButton.add(btnUpdate);
        pnButton.add(btnDelete);
        pnButton.add(btnClear);
        pnButton.add(new JLabel("Tìm kiếm:"));
        pnButton.add(txtSearch);
        pnButton.add(btnSearch);

        pnTop.add(pnButton, BorderLayout.SOUTH);

        pnTable = new JPanel(new BorderLayout());
        pnTable.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));

        tableModel = new DefaultTableModel(
                new Object[]{"Mã", "Tên", "Loại", "Nhà cung cấp", "Size", "Màu", "Giá", "Số lượng", "Trạng thái", "Ảnh"}, 0
        );
        tblProduct = new JTable(tableModel);
        tblProduct.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tblProduct);
        pnTable.add(scrollPane, BorderLayout.CENTER);

        pnMain.add(pnTop, BorderLayout.NORTH);
        pnMain.add(pnTable, BorderLayout.CENTER);

        add(pnMain, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> btnAddActionPerformed());
        btnUpdate.addActionListener(e -> btnUpdateActionPerformed());
        btnDelete.addActionListener(e -> btnDeleteActionPerformed());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> btnSearchActionPerformed());
        btnChooseImage.addActionListener(e -> btnChooseImageActionPerformed());
        tblProduct.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tblProductMouseClicked();
            }
        });
    }

    private void loadCategories() {
        cboCategory.removeAllItems();
        List<Category> list = categoryDAO.selectAll();
        for (Category c : list) {
            cboCategory.addItem(c.getCategoryId() + " - " + c.getCategoryName());
        }
    }

    private void loadSuppliers() {
        cboSupplier.removeAllItems();
        List<Supplier> list = supplierDAO.selectAll();
        for (Supplier s : list) {
            cboSupplier.addItem(s.getSupplierId() + " - " + s.getSupplierName());
        }
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        List<Product> list = productDAO.selectAll();
        for (Product p : list) {
            tableModel.addRow(new Object[]{
                p.getProductId(),
                p.getProductName(),
                getCategoryNameById(p.getCategoryId()),
                getSupplierNameById(p.getSupplierId()),
                p.getSize(),
                p.getColor(),
                p.getPrice(),
                p.getQuantity(),
                p.isStatus() ? "Còn bán" : "Ngừng bán",
                p.getImagePath()
            });
        }
    }

    private String getCategoryNameById(int id) {
        List<Category> list = categoryDAO.selectAll();
        for (Category c : list) {
            if (c.getCategoryId() == id) {
                return c.getCategoryName();
            }
        }
        return "";
    }

    private String getSupplierNameById(int id) {
        List<Supplier> list = supplierDAO.selectAll();
        for (Supplier s : list) {
            if (s.getSupplierId() == id) {
                return s.getSupplierName();
            }
        }
        return "";
    }

    private int getSelectedCategoryId() {
        String item = cboCategory.getSelectedItem().toString();
        return Integer.parseInt(item.split(" - ")[0]);
    }

    private int getSelectedSupplierId() {
        String item = cboSupplier.getSelectedItem().toString();
        return Integer.parseInt(item.split(" - ")[0]);
    }

    private void clearForm() {
        txtProductId.setText("");
        txtProductName.setText("");
        if (cboCategory.getItemCount() > 0) cboCategory.setSelectedIndex(0);
        if (cboSupplier.getItemCount() > 0) cboSupplier.setSelectedIndex(0);
        txtSize.setText("");
        txtColor.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
        chkStatus.setSelected(true);
        txtImagePath.setText("");
        lblImage.setText("Ảnh sản phẩm");
        lblImage.setIcon(null);
        selectedImagePath = "";
        tblProduct.clearSelection();
    }

    private boolean validateForm() {
        if (txtProductName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm");
            return false;
        }
        if (txtSize.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập size");
            return false;
        }
        if (txtColor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập màu");
            return false;
        }
        if (txtPrice.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá");
            return false;
        }
        if (txtQuantity.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng");
            return false;
        }
        return true;
    }

    private void showImage(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(img));
            lblImage.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể load ảnh");
        }
    }

    private Product getFormData() {
        Product p = new Product();
        if (!txtProductId.getText().trim().isEmpty()) {
            p.setProductId(Integer.parseInt(txtProductId.getText().trim()));
        }
        p.setProductName(txtProductName.getText().trim());
        p.setCategoryId(getSelectedCategoryId());
        p.setSupplierId(getSelectedSupplierId());
        p.setSize(txtSize.getText().trim());
        p.setColor(txtColor.getText().trim());
        p.setPrice(Double.parseDouble(txtPrice.getText().trim()));
        p.setQuantity(Integer.parseInt(txtQuantity.getText().trim()));
        p.setImagePath(selectedImagePath);
        p.setStatus(chkStatus.isSelected());
        return p;
    }

    private void setFormData(Product p) {
        txtProductId.setText(String.valueOf(p.getProductId()));
        txtProductName.setText(p.getProductName());
        txtSize.setText(p.getSize());
        txtColor.setText(p.getColor());
        txtPrice.setText(String.valueOf(p.getPrice()));
        txtQuantity.setText(String.valueOf(p.getQuantity()));
        chkStatus.setSelected(p.isStatus());
        txtImagePath.setText(p.getImagePath());
        selectedImagePath = p.getImagePath();

        if (p.getImagePath() != null && !p.getImagePath().isEmpty()) {
            showImage(p.getImagePath());
        } else {
            lblImage.setIcon(null);
            lblImage.setText("Ảnh sản phẩm");
        }

        selectComboByCategoryId(p.getCategoryId());
        selectComboBySupplierId(p.getSupplierId());
    }

    private void selectComboByCategoryId(int id) {
        for (int i = 0; i < cboCategory.getItemCount(); i++) {
            String item = cboCategory.getItemAt(i);
            if (Integer.parseInt(item.split(" - ")[0]) == id) {
                cboCategory.setSelectedIndex(i);
                break;
            }
        }
    }

    private void selectComboBySupplierId(int id) {
        for (int i = 0; i < cboSupplier.getItemCount(); i++) {
            String item = cboSupplier.getItemAt(i);
            if (Integer.parseInt(item.split(" - ")[0]) == id) {
                cboSupplier.setSelectedIndex(i);
                break;
            }
        }
    }

    private void btnAddActionPerformed() {
        if (!validateForm()) return;
        try {
            Product p = getFormData();
            if (productDAO.insert(p)) {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công");
                loadProducts();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm sản phẩm: " + e.getMessage());
        }
    }

    private void btnUpdateActionPerformed() {
        if (txtProductId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm cần sửa");
            return;
        }
        if (!validateForm()) return;
        try {
            Product p = getFormData();
            if (productDAO.update(p)) {
                JOptionPane.showMessageDialog(this, "Sửa sản phẩm thành công");
                loadProducts();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Sửa sản phẩm thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa sản phẩm: " + e.getMessage());
        }
    }

    private void btnDeleteActionPerformed() {
        if (txtProductId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm cần xóa");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int id = Integer.parseInt(txtProductId.getText().trim());
            if (productDAO.delete(id)) {
                JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công");
                loadProducts();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa sản phẩm: " + e.getMessage());
        }
    }

    private void btnSearchActionPerformed() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        List<Product> list = productDAO.searchByName(keyword);
        for (Product p : list) {
            tableModel.addRow(new Object[]{
                p.getProductId(),
                p.getProductName(),
                getCategoryNameById(p.getCategoryId()),
                getSupplierNameById(p.getSupplierId()),
                p.getSize(),
                p.getColor(),
                p.getPrice(),
                p.getQuantity(),
                p.isStatus() ? "Còn bán" : "Ngừng bán",
                p.getImagePath()
            });
        }
    }

    private void btnChooseImageActionPerformed() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            selectedImagePath = file.getAbsolutePath();
            txtImagePath.setText(selectedImagePath);
            showImage(selectedImagePath);
        }
    }

    private void tblProductMouseClicked() {
        int row = tblProduct.getSelectedRow();
        if (row != -1) {
            int id = Integer.parseInt(tblProduct.getValueAt(row, 0).toString());
            List<Product> list = productDAO.selectAll();
            for (Product p : list) {
                if (p.getProductId() == id) {
                    setFormData(p);
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductForm().setVisible(true));
    }
}