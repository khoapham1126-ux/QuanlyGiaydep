package view;

import dao.CustomerDAO;
import dao.InvoiceDAO;
import dao.ProductDAO;
import model.Customer;
import model.Invoice;
import model.InvoiceDetail;
import model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SellForm extends JFrame {

    private JComboBox<String> cboCustomer, cboProduct;
    private JTextField txtQuantity, txtDiscount, txtTotal, txtFinal;
    private JButton btnAddToCart, btnSave, btnClear;
    private JTable tblCart;
    private DefaultTableModel tableModel;

    private ProductDAO productDAO = new ProductDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private List<InvoiceDetail> cart = new ArrayList<>();

    public SellForm() {
        initComponents();
        setLocationRelativeTo(null);
        loadCombos();
    }

    private void initComponents() {
        setTitle("Bán hàng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLayout(new BorderLayout(10, 10));

        JPanel pnTop = new JPanel(new GridBagLayout());
        pnTop.setBorder(BorderFactory.createTitledBorder("Thông tin bán hàng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cboCustomer = new JComboBox<>();
        cboProduct = new JComboBox<>();
        txtQuantity = new JTextField("1", 8);
        txtDiscount = new JTextField("0", 8);
        txtTotal = new JTextField(12);
        txtTotal.setEditable(false);
        txtFinal = new JTextField(12);
        txtFinal.setEditable(false);

        btnAddToCart = new JButton("Thêm vào giỏ");
        btnSave = new JButton("Lưu hóa đơn");
        btnClear = new JButton("Làm mới");

        // Màu nổi bật cho nút lưu
        btnSave.setBackground(new Color(0, 153, 76));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Arial", Font.BOLD, 13));

        gbc.gridx = 0; gbc.gridy = 0;
        pnTop.add(new JLabel("Khách hàng:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        pnTop.add(cboCustomer, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 3;
        pnTop.add(new JLabel("Sản phẩm:"), gbc);
        gbc.gridx = 4; gbc.gridwidth = 2;
        pnTop.add(cboProduct, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        pnTop.add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 1;
        pnTop.add(txtQuantity, gbc);

        gbc.gridx = 2;
        pnTop.add(new JLabel("Giảm giá (%):"), gbc);
        gbc.gridx = 3;
        pnTop.add(txtDiscount, gbc);

        gbc.gridx = 4;
        pnTop.add(btnAddToCart, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnTop.add(new JLabel("Tổng tiền:"), gbc);
        gbc.gridx = 1;
        pnTop.add(txtTotal, gbc);

        gbc.gridx = 2;
        pnTop.add(new JLabel("Thành tiền:"), gbc);
        gbc.gridx = 3;
        pnTop.add(txtFinal, gbc);

        gbc.gridx = 4;
        pnTop.add(btnSave, gbc);
        gbc.gridx = 5;
        pnTop.add(btnClear, gbc);

        add(pnTop, BorderLayout.NORTH);

        // Bảng giỏ hàng
        tableModel = new DefaultTableModel(
            new Object[]{"Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblCart = new JTable(tableModel);
        tblCart.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tblCart);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Giỏ hàng"));
        add(scrollPane, BorderLayout.CENTER);

        // Nút xóa sản phẩm khỏi giỏ
        JPanel pnBottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRemove = new JButton("Xóa dòng đã chọn");
        btnRemove.setForeground(Color.RED);
        pnBottom.add(btnRemove);
        add(pnBottom, BorderLayout.SOUTH);

        // Sự kiện
        btnAddToCart.addActionListener(e -> addToCart());
        btnSave.addActionListener(e -> saveInvoice());
        btnClear.addActionListener(e -> clearAll());
        btnRemove.addActionListener(e -> removeFromCart());
        txtDiscount.addActionListener(e -> calculateTotal());
    }

    private void loadCombos() {
        cboCustomer.addItem("-- Khách lẻ --");
        for (Customer c : customerDAO.selectAll())
            cboCustomer.addItem(c.getCustomerId() + " - " + c.getFullName());

        for (Product p : productDAO.selectAll())
            cboProduct.addItem(p.getProductId() + " - " + p.getProductName()
                + " | " + p.getPrice() + "đ");
    }

    private void addToCart() {
        try {
            String item = cboProduct.getSelectedItem().toString();
            int productId = Integer.parseInt(item.split(" - ")[0]);
            String productName = item.split(" - ")[1].split(" \\| ")[0];
            double price = Double.parseDouble(item.split("\\| ")[1].replace("đ", ""));
            int qty = Integer.parseInt(txtQuantity.getText().trim());

            if (qty <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
                return;
            }

            // Kiểm tra sản phẩm đã có trong giỏ chưa
            for (InvoiceDetail d : cart) {
                if (d.getProductId() == productId) {
                    JOptionPane.showMessageDialog(this, "Sản phẩm đã có trong giỏ hàng!");
                    return;
                }
            }

            cart.add(new InvoiceDetail(0, productId, qty, price));
            tableModel.addRow(new Object[]{
                productId, productName, qty, price, qty * price
            });

            calculateTotal();
            txtQuantity.setText("1");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng số lượng!");
        }
    }

    private void calculateTotal() {
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            total += Double.parseDouble(tableModel.getValueAt(i, 4).toString());
        }
        double discount = 0;
        try {
            discount = Double.parseDouble(txtDiscount.getText().trim());
        } catch (Exception e) { discount = 0; }

        double finalAmount = total - (total * discount / 100);
        txtTotal.setText(String.format("%,.0f đ", total));
        txtFinal.setText(String.format("%,.0f đ", finalAmount));
    }

    private void removeFromCart() {
        int row = tblCart.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm cần xóa!");
            return;
        }
        cart.remove(row);
        tableModel.removeRow(row);
        calculateTotal();
    }

    private void saveInvoice() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!");
            return;
        }

        try {
            double total = 0;
            for (int i = 0; i < tableModel.getRowCount(); i++)
                total += Double.parseDouble(tableModel.getValueAt(i, 4).toString());

            double discount = Double.parseDouble(txtDiscount.getText().trim());
            double finalAmount = total - (total * discount / 100);

            Invoice invoice = new Invoice();

            // Lấy CustomerID
            String cusItem = cboCustomer.getSelectedItem().toString();
            if (cusItem.equals("-- Khách lẻ --")) {
                invoice.setCustomerId(1); // mặc định khách lẻ ID=1
            } else {
                invoice.setCustomerId(Integer.parseInt(cusItem.split(" - ")[0]));
            }

            invoice.setEmployeeId(LoginForm.currentUser.getEmployeeId());
            invoice.setTotalAmount(total);
            invoice.setDiscount(discount);
            invoice.setFinalAmount(finalAmount);
            invoice.setDetails(new ArrayList<>(cart));

            if (invoiceDAO.insertInvoice(invoice)) {
                JOptionPane.showMessageDialog(this,
                    "Lưu hóa đơn thành công!\nThành tiền: "
                    + String.format("%,.0f đ", finalAmount));
                clearAll();
            } else {
                JOptionPane.showMessageDialog(this, "Lưu hóa đơn thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void clearAll() {
        cart.clear();
        tableModel.setRowCount(0);
        txtQuantity.setText("1");
        txtDiscount.setText("0");
        txtTotal.setText("");
        txtFinal.setText("");
        cboCustomer.setSelectedIndex(0);
        cboProduct.setSelectedIndex(0);
    }
}