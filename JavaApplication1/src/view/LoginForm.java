package view;

import dao.UserDAO;
import model.User;
import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
	public static User currentUser;

	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnLogin, btnCancel;
	private UserDAO userDAO = new UserDAO();

	public LoginForm() {
		initComponents();
		setLocationRelativeTo(null);
	}

	private void initComponents() {
		setTitle("Đăng nhập hệ thống");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 250);
		setLayout(new BorderLayout());

		JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
		lblTitle.setForeground(new Color(0, 102, 204));
		lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
		add(lblTitle, BorderLayout.NORTH);

		JPanel pnCenter = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		pnCenter.add(new JLabel("Username:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0;
		txtUsername = new JTextField(15);
		txtUsername.setPreferredSize(new Dimension(200, 30));
		txtUsername.setMinimumSize(new Dimension(200, 30));
		pnCenter.add(txtUsername, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.0;
		pnCenter.add(new JLabel("Mật khẩu:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0; // <-- QUAN TRỌNG
		txtPassword = new JPasswordField(15);
		txtPassword.setPreferredSize(new Dimension(200, 30));
		txtPassword.setMinimumSize(new Dimension(200, 30)); 
		pnCenter.add(txtPassword, gbc);

		add(pnCenter, BorderLayout.CENTER);

		JPanel pnBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		btnLogin = new JButton("Đăng nhập");
		btnCancel = new JButton("Hủy");
		pnBottom.add(btnLogin);
		pnBottom.add(btnCancel);
		pnBottom.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		add(pnBottom, BorderLayout.SOUTH);

		btnLogin.addActionListener(e -> login());
		btnCancel.addActionListener(e -> System.exit(0));
	}

	private void login() {
		String username = txtUsername.getText().trim();
		String password = new String(txtPassword.getPassword());

		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
			return;
		}

		User user = userDAO.login(username, password);
		if (user != null) {
			LoginForm.currentUser = user;
			JOptionPane.showMessageDialog(this, "Đăng nhập thành công với quyền: " + user.getRole());
			new MainForm().setVisible(true); // Mở form chính (của Khoa/An)
			this.dispose(); // Đóng form login
		} else {
			JOptionPane.showMessageDialog(this, "Sai Username hoặc Mật khẩu!");
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
	}
}