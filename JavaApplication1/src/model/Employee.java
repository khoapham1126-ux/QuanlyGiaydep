package model;

public class Employee {

	private int employeeId;
	private String fullName;
	private String phone;
	private String email;
	private String address;
	private boolean status;

	public Employee() {
	}

	public Employee(int employeeId, String fullName, String phone, String email, String address, boolean status) {
		this.employeeId = employeeId;
		this.fullName = fullName;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.status = status;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
