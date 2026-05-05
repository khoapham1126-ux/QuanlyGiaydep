package dao;

import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
	// 1. Lấy danh sách sản phẩm bán chạy nhất
	public List<Object[]> getTopSellingProducts(int top) {
		List<Object[]> list = new ArrayList<>();
		// Giả định An dùng bảng InvoiceDetail có ProductID và Quantity
		String sql = "SELECT p.ProductName, SUM(id.Quantity) as TotalSold " + "FROM InvoiceDetail id "
				+ "JOIN Product p ON id.ProductID = p.ProductID " + "GROUP BY id.ProductID "
				+ "ORDER BY TotalSold DESC LIMIT ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, top);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(new Object[] { rs.getString("ProductName"), rs.getInt("TotalSold") });
				}
			}
		} catch (Exception e) {
			System.out.println("Lỗi thống kê sản phẩm bán chạy: " + e.getMessage());
		}
		return list;
	}

	// 2. Thống kê doanh thu theo ngày trong tháng
	public double getRevenueByDate(String date) {
		double total = 0;
		// Giả định An dùng bảng Invoice có TotalAmount và InvoiceDate
		String sql = "SELECT SUM(TotalAmount) as Revenue FROM Invoice WHERE DATE(InvoiceDate) = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, date); // Format yyyy-MM-dd
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					total = rs.getDouble("Revenue");
				}
			}
		} catch (Exception e) {
			System.out.println("Lỗi thống kê doanh thu: " + e.getMessage());
		}
		return total;
	}
}