package view;

import dao.CustomerDAO;
import dao.InvoiceDAO;
import model.Customer;
import model.Invoice;
import model.InvoiceDetail;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InvoiceForm extends JFrame {

    private JTable tblInvoice, tblDetail;
    private DefaultTableModel modelInvoice, modelDetail;
    private JLabel lblTotal, lblDiscount, lblFinal;

    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private CustomerDAO customerDAO = new CustomerDAO();

    public InvoiceForm() {
        initComponents();
        setLocationRelativeTo(null);
        loadInvoices();
    }

    private void initComponents() {
        setTitle("Lịch sử hóa đơn");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("LỊCH SỬ HÓA ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle, BorderLayout.NORTH);

        // Bảng hóa đơn (trên)
        modelInvoice = new DefaultTableModel(
            new Object[]{"Mã HĐ", "Khách hàng", "Nhân viên", "Ngày lập",
                         "Tổng tiền", "Giảm giá(%)", "Thành tiền"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblInvoice = new JTable(modelInvoice);
        tblInvoice.setRowHeight(25);
        JScrollPane spInvoice = new JScrollPane(tblInvoice);
        spInvoice.setBorder(BorderFactory.createTitledBorder("Danh sách hóa đơn"));
        spInvoice.setPreferredSize(new Dimension(0, 280));

        // Bảng chi tiết (dưới)
        modelDetail = new DefaultTableModel(
            new Object[]{"Mã SP", "Số lượng", "Đơn giá", "Thành tiền"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblDetail = new JTable(modelDetail);
        tblDetail.setRowHeight(25);
        JScrollPane spDetail = new JScrollPane(tblDetail);
        spDetail.setBorder(BorderFactory.createTitledBorder("Chi tiết hóa đơn"));

        // Panel tóm tắt
        JPanel pnSummary = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        lblTotal = new JLabel("Tổng tiền: 0 đ");
        lblDiscount = new JLabel("Giảm giá: 0%");
        lblFinal = new JLabel("Thành tiền: 0 đ");
        lblFinal.setFont(new Font("Arial", Font.BOLD, 14));
        lblFinal.setForeground(new Color(204, 0, 0));
        pnSummary.add(lblTotal);
        pnSummary.add(lblDiscount);
        pnSummary.add(lblFinal);

        // Ghép layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spInvoice, spDetail);
        splitPane.setDividerLocation(280);
        add(splitPane, BorderLayout.CENTER);
        add(pnSummary, BorderLayout.SOUTH);

        // Click vào hóa đơn → hiện chi tiết
        tblInvoice.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadDetail();
        });
    }

    private void loadInvoices() {
        modelInvoice.setRowCount(0);
        List<Invoice> list = invoiceDAO.selectAll();
        for (Invoice inv : list) {
            modelInvoice.addRow(new Object[]{
                inv.getInvoiceId(),
                getCustomerName(inv.getCustomerId()),
                inv.getEmployeeId(),
                inv.getInvoiceDate(),
                String.format("%,.0f đ", inv.getTotalAmount()),
                inv.getDiscount() + "%",
                String.format("%,.0f đ", inv.getFinalAmount())
            });
        }
    }

    private void loadDetail() {
        int row = tblInvoice.getSelectedRow();
        if (row == -1) return;

        int invoiceId = Integer.parseInt(modelInvoice.getValueAt(row, 0).toString());
        double total = Double.parseDouble(
            modelInvoice.getValueAt(row, 4).toString().replace(" đ", "").replace(",", ""));
        double discount = Double.parseDouble(
            modelInvoice.getValueAt(row, 5).toString().replace("%", ""));
        double finalAmt = Double.parseDouble(
            modelInvoice.getValueAt(row, 6).toString().replace(" đ", "").replace(",", ""));

        modelDetail.setRowCount(0);
        List<InvoiceDetail> details = invoiceDAO.getDetailsByInvoiceId(invoiceId);
        for (InvoiceDetail d : details) {
            modelDetail.addRow(new Object[]{
                d.getProductId(),
                d.getQuantity(),
                String.format("%,.0f đ", d.getUnitPrice()),
                String.format("%,.0f đ", d.getQuantity() * d.getUnitPrice())
            });
        }

        lblTotal.setText(String.format("Tổng tiền: %,.0f đ", total));
        lblDiscount.setText("Giảm giá: " + discount + "%");
        lblFinal.setText(String.format("Thành tiền: %,.0f đ", finalAmt));
    }

    private String getCustomerName(int id) {
        for (Customer c : customerDAO.selectAll()) {
            if (c.getCustomerId() == id) return c.getFullName();
        }
        return "Khách lẻ";
    }
}