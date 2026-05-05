package dao;

import model.Employee;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

	// Thêm nhân viên mới
	public boolean insert(Employee e) {
		String sql = "INSERT INTO Employee(FullName, Phone, Email, Address, Status) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, e.getFullName());
			ps.setString(2, e.getPhone());
			ps.setString(3, e.getEmail());
			ps.setString(4, e.getAddress());
			ps.setBoolean(5, e.isStatus());
			return ps.executeUpdate() > 0;
		} catch (Exception ex) {
			System.out.println("Lỗi insert Employee: " + ex.getMessage());
			return false;
		}
	}

	// Cập nhật thông tin nhân viên
	public boolean update(Employee e) {
		String sql = "UPDATE Employee SET FullName = ?, Phone = ?, Email = ?, Address = ?, Status = ? WHERE EmployeeID = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, e.getFullName());
			ps.setString(2, e.getPhone());
			ps.setString(3, e.getEmail());
			ps.setString(4, e.getAddress());
			ps.setBoolean(5, e.isStatus());
			ps.setInt(6, e.getEmployeeId());
			return ps.executeUpdate() > 0;
		} catch (Exception ex) {
			System.out.println("Lỗi update Employee: " + ex.getMessage());
			return false;
		}
	}

	// Xóa nhân viên theo mã (Lưu ý: Thực tế thường dùng "xóa mềm" bằng cách đổi
	// Status = 0)
	public boolean delete(int id) {
		String sql = "DELETE FROM Employee WHERE EmployeeID = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (Exception ex) {
			System.out.println("Lỗi delete Employee: " + ex.getMessage());
			return false;
		}
	}

	// Lấy danh sách toàn bộ nhân viên
	public List<Employee> selectAll() {
		List<Employee> list = new ArrayList<>();
		String sql = "SELECT * FROM Employee";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(new Employee(rs.getInt("EmployeeID"), rs.getString("FullName"), rs.getString("Phone"),
						rs.getString("Email"), rs.getString("Address"), rs.getBoolean("Status")));
			}
		} catch (Exception ex) {
			System.out.println("Lỗi selectAll Employee: " + ex.getMessage());
		}
		return list;
	}
}