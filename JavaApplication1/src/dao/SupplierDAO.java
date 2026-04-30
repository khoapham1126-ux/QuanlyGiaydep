package dao;

import model.Supplier;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    public boolean insert(Supplier s) {
        String sql = "INSERT INTO Supplier(SupplierName, Phone, Email, Address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getSupplierName());
            ps.setString(2, s.getPhone());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getAddress());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi insert Supplier: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Supplier s) {
        String sql = "UPDATE Supplier SET SupplierName = ?, Phone = ?, Email = ?, Address = ? WHERE SupplierID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getSupplierName());
            ps.setString(2, s.getPhone());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getAddress());
            ps.setInt(5, s.getSupplierId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi update Supplier: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sqlDeleteInventory = "DELETE FROM Inventory WHERE ProductID IN (SELECT ProductID FROM Product WHERE SupplierID = ?)";
        String sqlDeleteProduct = "DELETE FROM Product WHERE SupplierID = ?";
        String sqlDeleteSupplier = "DELETE FROM Supplier WHERE SupplierID = ?";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Không thể kết nối DB");
                return false;
            }

            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlDeleteInventory);
                 PreparedStatement ps2 = conn.prepareStatement(sqlDeleteProduct);
                 PreparedStatement ps3 = conn.prepareStatement(sqlDeleteSupplier)) {

                // Xóa inventory của các sản phẩm thuộc supplier này
                ps1.setInt(1, id);
                ps1.executeUpdate();

                // Xóa sản phẩm của supplier này
                ps2.setInt(1, id);
                ps2.executeUpdate();

                // Xóa supplier
                ps3.setInt(1, id);
                int rows = ps3.executeUpdate();

                conn.commit();
                return rows > 0;

            } catch (Exception e) {
                conn.rollback();
                System.out.println("Lỗi delete Supplier: " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Lỗi delete Supplier: " + e.getMessage());
            return false;
        }
    }

    public List<Supplier> selectAll() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Supplier";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Supplier(
                        rs.getInt("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address")
                ));
            }
        } catch (Exception e) {
            System.out.println("Lỗi selectAll Supplier: " + e.getMessage());
        }
        return list;
    }
}