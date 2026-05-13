package dao;

import model.Inventory;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

	public boolean insert(Inventory i) {
		String sql = "INSERT INTO Inventory(ProductID, Quantity, MinQuantity, LastUpdated) VALUES (?, ?, ?, NOW())";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, i.getProductId());
			ps.setInt(2, i.getQuantity());
			ps.setInt(3, i.getMinQuantity());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			System.out.println("Lỗi insert Inventory: " + e.getMessage());
			return false;
		}
	}

	public boolean update(Inventory i) {
		String sql = "UPDATE Inventory SET ProductID = ?, Quantity = ?, MinQuantity = ?, LastUpdated = NOW() WHERE InventoryID = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, i.getProductId());
			ps.setInt(2, i.getQuantity());
			ps.setInt(3, i.getMinQuantity());
			ps.setInt(4, i.getInventoryId());
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			System.out.println("Lỗi update Inventory: " + e.getMessage());
			return false;
		}
	}

	public boolean delete(int id) {
		String sql = "DELETE FROM Inventory WHERE InventoryID = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			System.out.println("Lỗi delete Inventory: " + e.getMessage());
			return false;
		}
	}

	public List<Inventory> selectAll() {
		List<Inventory> list = new ArrayList<>();
		String sql = "SELECT * FROM Inventory";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(new Inventory(rs.getInt("InventoryID"), rs.getInt("ProductID"), rs.getInt("Quantity"),
						rs.getInt("MinQuantity"), rs.getString("LastUpdated")));
			}
		} catch (Exception e) {
			System.out.println("Lỗi selectAll Inventory: " + e.getMessage());
		}
		return list;
	}

	public List<Inventory> lowStock() {
		List<Inventory> list = new ArrayList<>();
		String sql = "SELECT * FROM Inventory WHERE Quantity <= MinQuantity";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				list.add(new Inventory(rs.getInt("InventoryID"), rs.getInt("ProductID"), rs.getInt("Quantity"),
						rs.getInt("MinQuantity"), rs.getString("LastUpdated")));
			}
		} catch (Exception e) {
			System.out.println("Lỗi lowStock Inventory: " + e.getMessage());
		}
		return list;
	}
}