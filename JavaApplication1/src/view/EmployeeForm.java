package view;

import dao.EmployeeDAO;
import model.Employee;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EmployeeForm extends JFrame {
	private JTextField txtEmpId, txtFullName, txtPhone, txtEmail, txtAddress;
	private JCheckBox chkStatus;
	private JButton btnAdd, btnUpdate, btnDelete, btnClear;
	private JTable tblEmployee;
	private DefaultTableModel tableModel;
	private EmployeeDAO employeeDAO = new EmployeeDAO();

	public EmployeeForm() {
		initComponents();
		setLocationRelativeTo(null);
		loadData();
	}

	private void initComponents() {
		setTitle("Quản lý Nhân viên");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(900, 600);
		setLayout(new BorderLayout(10, 10));

		JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
		lblTitle.setForeground(new Color(0, 102, 204));
		add(lblTitle, BorderLayout.NORTH);

		JPanel pnTop = new JPanel(new BorderLayout(10, 10));
		JPanel pnForm = new JPanel(new GridBagLayout());
		pnForm.setBorder(BorderFactory.createTitledBorder("Thông tin nhân viên"));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 10, 8, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		txtEmpId = new JTextField(15);
		txtEmpId.setEditable(false);
		txtFullName = new JTextField(15);
		txtPhone = new JTextField(15);
		txtEmail = new JTextField(15);
		txtAddress = new JTextField(15);
		chkStatus = new JCheckBox("Đang làm việc", true);

		gbc.gridx = 0;
		gbc.gridy = 0;
		pnForm.add(new JLabel("Mã NV:"), gbc);
		gbc.gridx = 1;
		pnForm.add(txtEmpId, gbc);
		gbc.gridx = 2;
		pnForm.add(new JLabel("Họ tên:"), gbc);
		gbc.gridx = 3;
		pnForm.add(txtFullName, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		pnForm.add(new JLabel("SĐT:"), gbc);
		gbc.gridx = 1;
		pnForm.add(txtPhone, gbc);
		gbc.gridx = 2;
		pnForm.add(new JLabel("Email:"), gbc);
		gbc.gridx = 3;
		pnForm.add(txtEmail, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		pnForm.add(new JLabel("Địa chỉ:"), gbc);
		gbc.gridx = 1;
		pnForm.add(txtAddress, gbc);
		gbc.gridx = 2;
		pnForm.add(new JLabel("Trạng thái:"), gbc);
		gbc.gridx = 3;
		pnForm.add(chkStatus, gbc);

		pnTop.add(pnForm, BorderLayout.CENTER);

		JPanel pnButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
		btnAdd = new JButton("Thêm");
		btnUpdate = new JButton("Sửa");
		btnDelete = new JButton("Xóa");
		btnClear = new JButton("Làm mới");
		pnButton.add(btnAdd);
		pnButton.add(btnUpdate);
		pnButton.add(btnDelete);
		pnButton.add(btnClear);
		pnTop.add(pnButton, BorderLayout.SOUTH);
		add(pnTop, BorderLayout.NORTH);

		tableModel = new DefaultTableModel(new Object[] { "Mã NV", "Họ Tên", "SĐT", "Email", "Địa chỉ", "Trạng thái" },
				0);
		tblEmployee = new JTable(tableModel);
		tblEmployee.setRowHeight(25);
		add(new JScrollPane(tblEmployee), BorderLayout.CENTER);

	}

	private void loadData() {
		tableModel.setRowCount(0);
		List<Employee> list = employeeDAO.selectAll();
		for (Employee e : list) {
			tableModel.addRow(new Object[] { e.getEmployeeId(), e.getFullName(), e.getPhone(), e.getEmail(),
					e.getAddress(), e.isStatus() ? "Đang làm việc" : "Đã nghỉ" });
		}
	}
}