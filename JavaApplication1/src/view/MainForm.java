package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MainForm extends JFrame {

    private JButton btnProduct, btnCategory, btnSupplier, btnInventory, btnExit;

    public MainForm() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Hệ thống quản lý cửa hàng giày");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG GIÀY", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        pnCenter.setBorder(BorderFactory.createTitledBorder("Chức năng chính"));

        btnProduct = new JButton("Quản lý sản phẩm");
        btnCategory = new JButton("Quản lý loại giày");
        btnSupplier = new JButton("Quản lý nhà cung cấp");
        btnInventory = new JButton("Quản lý tồn kho");
        btnExit = new JButton("Thoát");

        btnProduct.setPreferredSize(new java.awt.Dimension(180, 40));
        btnCategory.setPreferredSize(new java.awt.Dimension(180, 40));
        btnSupplier.setPreferredSize(new java.awt.Dimension(180, 40));
        btnInventory.setPreferredSize(new java.awt.Dimension(180, 40));
        btnExit.setPreferredSize(new java.awt.Dimension(180, 40));

        pnCenter.add(btnProduct);
        pnCenter.add(btnCategory);
        pnCenter.add(btnSupplier);
        pnCenter.add(btnInventory);
        pnCenter.add(btnExit);

        add(pnCenter, BorderLayout.CENTER);

        btnProduct.addActionListener(e -> new ProductForm().setVisible(true));
        btnCategory.addActionListener(e -> new CategoryForm().setVisible(true));
        btnSupplier.addActionListener(e -> new SupplierForm().setVisible(true));
        btnInventory.addActionListener(e -> new InventoryForm().setVisible(true));
        btnExit.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainForm().setVisible(true));
    }
}