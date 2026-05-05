package view;

import dao.CustomerDAO;
import model.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerForm extends JFrame {

    private JTextField txtId, txtFullName, txtPhone, txtEmail, txtAddress, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;
    private JTable tblCustomer;
    private DefaultTableModel tableModel;
    private CustomerDAO customerDAO = new CustomerDAO();

    public CustomerForm() {
        initComponents();
        setLocationRelativeTo(null);
        loadData();
        clearForm();
    }

    private void initComponents() {
        setTitle("Quản lý khách hàng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnTop = new JPanel(new BorderLayout(10, 10));

        JPanel pnForm = new JPanel(new GridBagLayout());
        pnForm.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtId = new JTextField(15);
        txtId.setEditable(false);
        txtFullName = new JTextField(15);
        txtPhone = new JTextField(15);
        txtEmail = new JTextField(15);
        txtAddress = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        pnForm.add(new JLabel("Mã KH:"), gbc);
        gbc.gridx = 1;
        pnForm.add(txtId, gbc);
        gbc.gridx = 2;
        pnForm.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 3;
        pnForm.add(txtFullName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        pnForm.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 1;
        pnForm.add(txtPhone, gbc);
        gbc.gridx = 2;
        pnForm.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        pnForm.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnForm.add(new JLabel("Địa chỉ:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        pnForm.add(txtAddress, gbc);
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

        tableModel = new DefaultTableModel(
            new Object[]{"Mã KH", "Họ tên", "SĐT", "Email", "Địa chỉ"}, 0);
        tblCustomer = new JTable(tableModel);
        tblCustomer.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tblCustomer);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));
        add(scrollPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> btnAddActionPerformed());
        btnUpdate.addActionListener(e -> btnUpdateActionPerformed());
        btnDelete.addActionListener(e -> btnDeleteActionPerformed());
        btnClear.addActionListener(e -> clearForm());
        btnSearch.addActionListener(e -> btnSearchActionPerformed());

        tblCustomer.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) tblMouseClicked();
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (Customer c : customerDAO.selectAll()) {
            tableModel.addRow(new Object[]{
                c.getCustomerId(), c.getFullName(),
                c.getPhone(), c.getEmail(), c.getAddress()
            });
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtFullName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtSearch.setText("");
        tblCustomer.clearSelection();
    }

    private boolean validateForm() {
        if (txtFullName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên khách hàng");
            return false;
        }
        return true;
    }

    private Customer getFormData() {
        Customer c = new Customer();
        if (!txtId.getText().trim().isEmpty())
            c.setCustomerId(Integer.parseInt(txtId.getText().trim()));
        c.setFullName(txtFullName.getText().trim());
        c.setPhone(txtPhone.getText().trim());
        c.setEmail(txtEmail.getText().trim());
        c.setAddress(txtAddress.getText().trim());
        return c;
    }

    private void btnAddActionPerformed() {
        if (!validateForm()) return;
        if (customerDAO.insert(getFormData())) {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            loadData(); clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }
    }

    private void btnUpdateActionPerformed() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn khách hàng cần sửa!"); return;
        }
        if (!validateForm()) return;
        if (customerDAO.update(getFormData())) {
            JOptionPane.showMessageDialog(this, "Sửa thành công!");
            loadData(); clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Sửa thất bại!");
        }
    }

    private void btnDeleteActionPerformed() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chọn khách hàng cần xóa!"); return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa?",
            "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        if (customerDAO.delete(Integer.parseInt(txtId.getText().trim()))) {
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
            loadData(); clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Xóa thất bại!");
        }
    }

    private void btnSearchActionPerformed() {
        String keyword = txtSearch.getText().trim();
        tableModel.setRowCount(0);
        for (Customer c : customerDAO.searchByName(keyword)) {
            tableModel.addRow(new Object[]{
                c.getCustomerId(), c.getFullName(),
                c.getPhone(), c.getEmail(), c.getAddress()
            });
        }
    }

    private void tblMouseClicked() {
        int row = tblCustomer.getSelectedRow();
        if (row != -1) {
            txtId.setText(tblCustomer.getValueAt(row, 0).toString());
            txtFullName.setText(tblCustomer.getValueAt(row, 1).toString());
            txtPhone.setText(tblCustomer.getValueAt(row, 2).toString());
            txtEmail.setText(tblCustomer.getValueAt(row, 3).toString());
            txtAddress.setText(tblCustomer.getValueAt(row, 4).toString());
        }
    }
}