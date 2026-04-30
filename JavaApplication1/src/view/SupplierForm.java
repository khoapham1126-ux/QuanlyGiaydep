package view;

import dao.SupplierDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import model.Supplier;

public class SupplierForm extends JFrame {

    private JTextField txtSupplierId, txtSupplierName, txtPhone, txtEmail, txtAddress, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JTable tblSupplier;
    private DefaultTableModel tableModel;
    private SupplierDAO supplierDAO = new SupplierDAO();

    public SupplierForm() {
        initComponents();
        setLocationRelativeTo(null);
        loadSuppliers();
        clearForm();
    }

    private void initComponents() {
        setTitle("Quản lý nhà cung cấp");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 650);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÀ CUNG CẤP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle, BorderLayout.NORTH);

        // Panel chính chứa form + button
        JPanel pnTop = new JPanel(new BorderLayout(10, 10));

        // Panel nhập liệu
        JPanel pnForm = new JPanel(new java.awt.GridBagLayout());
        pnForm.setBorder(BorderFactory.createTitledBorder("Thông tin nhà cung cấp"));
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(8, 10, 8, 10);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        JLabel lblSupplierId = new JLabel("Mã NCC:");
        JLabel lblSupplierName = new JLabel("Tên NCC:");
        JLabel lblPhone = new JLabel("Số điện thoại:");
        JLabel lblEmail = new JLabel("Email:");
        JLabel lblAddress = new JLabel("Địa chỉ:");

        txtSupplierId = new JTextField(18);
        txtSupplierId.setEditable(false);
        txtSupplierName = new JTextField(18);
        txtPhone = new JTextField(18);
        txtEmail = new JTextField(18);
        txtAddress = new JTextField(18);

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0;
        pnForm.add(lblSupplierId, gbc);
        gbc.gridx = 1;
        pnForm.add(txtSupplierId, gbc);

        gbc.gridx = 2;
        pnForm.add(lblSupplierName, gbc);
        gbc.gridx = 3;
        pnForm.add(txtSupplierName, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1;
        pnForm.add(lblPhone, gbc);
        gbc.gridx = 1;
        pnForm.add(txtPhone, gbc);

        gbc.gridx = 2;
        pnForm.add(lblEmail, gbc);
        gbc.gridx = 3;
        pnForm.add(txtEmail, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        pnForm.add(lblAddress, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        pnForm.add(txtAddress, gbc);
        gbc.gridwidth = 1;

        pnTop.add(pnForm, BorderLayout.CENTER);

        // Panel nút
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

        // Bảng
        tableModel = new DefaultTableModel(new Object[]{"Mã NCC", "Tên NCC", "SĐT", "Email", "Địa chỉ"}, 0);
        tblSupplier = new JTable(tableModel);
        tblSupplier.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tblSupplier);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách nhà cung cấp"));
        add(scrollPane, BorderLayout.CENTER);

        // Event
        btnAdd.addActionListener(e -> btnAddActionPerformed());
        btnUpdate.addActionListener(e -> btnUpdateActionPerformed());
        btnDelete.addActionListener(e -> btnDeleteActionPerformed());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> btnSearchActionPerformed());

        tblSupplier.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tblSupplierMouseClicked();
            }
        });
    }

    private void loadSuppliers() {
        tableModel.setRowCount(0);
        List<Supplier> list = supplierDAO.selectAll();
        for (Supplier s : list) {
            tableModel.addRow(new Object[]{
                s.getSupplierId(),
                s.getSupplierName(),
                s.getPhone(),
                s.getEmail(),
                s.getAddress()
            });
        }
    }

    private void clearForm() {
        txtSupplierId.setText("");
        txtSupplierName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtSearch.setText("");
        tblSupplier.clearSelection();
    }

    private boolean validateForm() {
        if (txtSupplierName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhà cung cấp");
            return false;
        }
        return true;
    }

    private Supplier getFormData() {
        Supplier s = new Supplier();
        if (!txtSupplierId.getText().trim().isEmpty()) {
            s.setSupplierId(Integer.parseInt(txtSupplierId.getText().trim()));
        }
        s.setSupplierName(txtSupplierName.getText().trim());
        s.setPhone(txtPhone.getText().trim());
        s.setEmail(txtEmail.getText().trim());
        s.setAddress(txtAddress.getText().trim());
        return s;
    }

    private void setFormData(Supplier s) {
        txtSupplierId.setText(String.valueOf(s.getSupplierId()));
        txtSupplierName.setText(s.getSupplierName());
        txtPhone.setText(s.getPhone());
        txtEmail.setText(s.getEmail());
        txtAddress.setText(s.getAddress());
    }

    private void btnAddActionPerformed() {
        if (!validateForm()) return;
        try {
            if (supplierDAO.insert(getFormData())) {
                JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công");
                loadSuppliers();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm: " + e.getMessage());
        }
    }

    private void btnUpdateActionPerformed() {
        if (txtSupplierId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn nhà cung cấp cần sửa");
            return;
        }
        if (!validateForm()) return;

        try {
            if (supplierDAO.update(getFormData())) {
                JOptionPane.showMessageDialog(this, "Sửa thành công");
                loadSuppliers();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Sửa thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa: " + e.getMessage());
        }
    }

    private void btnDeleteActionPerformed() {
        if (txtSupplierId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn nhà cung cấp cần xóa");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int id = Integer.parseInt(txtSupplierId.getText().trim());
            if (supplierDAO.delete(id)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                loadSuppliers();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi xóa: " + e.getMessage());
        }
    }

    private void btnSearchActionPerformed() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        List<Supplier> list = supplierDAO.selectAll();
        for (Supplier s : list) {
            if (s.getSupplierName().toLowerCase().contains(keyword)) {
                tableModel.addRow(new Object[]{
                    s.getSupplierId(),
                    s.getSupplierName(),
                    s.getPhone(),
                    s.getEmail(),
                    s.getAddress()
                });
            }
        }
    }

    private void tblSupplierMouseClicked() {
        int row = tblSupplier.getSelectedRow();
        if (row != -1) {
            Supplier s = new Supplier(
                    Integer.parseInt(tblSupplier.getValueAt(row, 0).toString()),
                    tblSupplier.getValueAt(row, 1).toString(),
                    tblSupplier.getValueAt(row, 2).toString(),
                    tblSupplier.getValueAt(row, 3).toString(),
                    tblSupplier.getValueAt(row, 4).toString()
            );
            setFormData(s);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SupplierForm().setVisible(true));
    }
}