package dao;

import model.User;
import utils.DBConnection;
import java.sql.*;

public class UserDAO {
	public User login(String username, String password) {
		String sql = "SELECT * FROM User WHERE Username = ? AND Password = ? AND Status = 1";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			ps.setString(2, password); 
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new User(rs.getInt("UserID"), rs.getInt("EmployeeID"), rs.getString("Username"),
							rs.getString("Password"), rs.getString("Role"), rs.getBoolean("Status"));
				}
			}
		} catch (Exception e) {
			System.out.println("Lỗi login UserDAO: " + e.getMessage());
		}
		return null; 
	}
}