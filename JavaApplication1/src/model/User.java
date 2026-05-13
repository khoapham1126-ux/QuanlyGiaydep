package model;

public class User {
	private int userId;
	private int employeeId;
	private String username;
	private String password;
	private String role;
	private boolean status;

	public User() {
	}

	public User(int userId, int employeeId, String username, String password, String role, boolean status) {
		this.userId = userId;
		this.employeeId = employeeId;
		this.username = username;
		this.password = password;
		this.role = role;
		this.status = status;
	}

	// Getters and Setters
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}