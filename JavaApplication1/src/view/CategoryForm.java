package view;

import dao.CategoryDAO;
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
import model.Category;

public class CategoryForm extends JFrame {

    private JTextField txtCategoryId, txtCategoryName, txtDescription, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JTable tblCategory;
    private DefaultTableModel tableModel;
    private CategoryDAO categoryDAO = new CategoryDAO();

    public CategoryForm() {
        initComponents();
        setLocationRelativeTo(null);
        loadCategories();
        clearForm();
    }

    private void initComponents() {
        setTitle("Quản lý loại giày");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ LOẠI GIÀY", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnTop = new JPanel(new BorderLayout(10, 10));

        JPanel pnForm = new JPanel(new java.awt.GridBagLayout());
        pnForm.setBorder(BorderFactory.createTitledBorder("Thông tin loại giày"));
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new java.awt.Insets(8, 10, 8, 10);
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

        JLabel lblCategoryId = new JLabel("Mã loại:");
        JLabel lblCategoryName = new JLabel("Tên loại:");
        JLabel lblDescription = new JLabel("Mô tả:");

        txtCategoryId = new JTextField(18);
        txtCategoryId.setEditable(false);
        txtCategoryName = new JTextField(18);
        txtDescription = new JTextField(18);

        gbc.gridx = 0; gbc.gridy = 0;
        pnForm.add(lblCategoryId, gbc);
        gbc.gridx = 1;
        pnForm.add(txtCategoryId, gbc);

        gbc.gridx = 2;
        pnForm.add(lblCategoryName, gbc);
        gbc.gridx = 3;
        pnForm.add(txtCategoryName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnForm.add(lblDescription, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        pnForm.add(txtDescription, gbc);
        gbc.gridwidth = 1;

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

        tableModel = new DefaultTableModel(new Object[]{"Mã loại", "Tên loại", "Mô tả"}, 0);
        tblCategory = new JTable(tableModel);
        tblCategory.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tblCategory);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách loại giày"));
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> btnAddActionPerformed());
        btnUpdate.addActionListener(e -> btnUpdateActionPerformed());
        btnDelete.addActionListener(e -> btnDeleteActionPerformed());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> btnSearchActionPerformed());

        tblCategory.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                tblCategoryMouseClicked();
            }
        });
    }

    private void loadCategories() {
        tableModel.setRowCount(0);
        List<Category> list = categoryDAO.selectAll();
        for (Category c : list) {
            tableModel.addRow(new Object[]{
                c.getCategoryId(),
                c.getCategoryName(),
                c.getDescription()
            });
        }
    }

    private void clearForm() {
        txtCategoryId.setText("");
        txtCategoryName.setText("");
        txtDescription.setText("");
        txtSearch.setText("");
        tblCategory.clearSelection();
    }

    private boolean validateForm() {
        if (txtCategoryName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên loại");
            return false;
        }
        return true;
    }

    private Category getFormData() {
        Category c = new Category();
        if (!txtCategoryId.getText().trim().isEmpty()) {
            c.setCategoryId(Integer.parseInt(txtCategoryId.getText().trim()));
        }
        c.setCategoryName(txtCategoryName.getText().trim());
        c.setDescription(txtDescription.getText().trim());
        return c;
    }

    private void setFormData(Category c) {
        txtCategoryId.setText(String.valueOf(c.getCategoryId()));
        txtCategoryName.setText(c.getCategoryName());
        txtDescription.setText(c.getDescription());
    }

    private void btnAddActionPerformed() {
        if (!validateForm()) return;
        try {
            if (categoryDAO.insert(getFormData())) {
                JOptionPane.showMessageDialog(this, "Thêm loại giày thành công");
                loadCategories();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm: " + e.getMessage());
        }
    }

    private void btnUpdateActionPerformed() {
        if (txtCategoryId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn loại giày cần sửa");
            return;
        }
        if (!validateForm()) return;

        try {
            if (categoryDAO.update(getFormData())) {
                JOptionPane.showMessageDialog(this, "Sửa thành công");
                loadCategories();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Sửa thất bại");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa: " + e.getMessage());
        }
    }

    private void btnDeleteActionPerformed() {
        if (txtCategoryId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn loại giày cần xóa");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int id = Integer.parseInt(txtCategoryId.getText().trim());
            if (categoryDAO.delete(id)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công");
                loadCategories();
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
        List<Category> list = categoryDAO.selectAll();
        for (Category c : list) {
            if (c.getCategoryName().toLowerCase().contains(keyword)) {
                tableModel.addRow(new Object[]{
                    c.getCategoryId(),
                    c.getCategoryName(),
                    c.getDescription()
                });
            }
        }
    }

    private void tblCategoryMouseClicked() {
        int row = tblCategory.getSelectedRow();
        if (row != -1) {
            Category c = new Category(
                    Integer.parseInt(tblCategory.getValueAt(row, 0).toString()),
                    tblCategory.getValueAt(row, 1).toString(),
                    tblCategory.getValueAt(row, 2).toString()
            );
            setFormData(c);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CategoryForm().setVisible(true));
    }
}