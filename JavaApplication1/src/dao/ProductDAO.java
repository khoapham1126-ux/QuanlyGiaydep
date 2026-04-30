package dao;

import model.Product;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public boolean insert(Product p) {
        String sqlProduct = "INSERT INTO Product(ProductName, CategoryID, SupplierID, Size, Color, Price, Quantity, ImagePath, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlInventory = "INSERT INTO Inventory(ProductID, Quantity, MinQuantity, LastUpdated) VALUES (?, ?, ?, GETDATE())";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Không thể kết nối DB");
                return false;
            }

            conn.setAutoCommit(false);

            try (
                PreparedStatement psProduct = conn.prepareStatement(sqlProduct, Statement.RETURN_GENERATED_KEYS)
            ) {
                psProduct.setString(1, p.getProductName());
                psProduct.setInt(2, p.getCategoryId());
                psProduct.setInt(3, p.getSupplierId());
                psProduct.setString(4, p.getSize());
                psProduct.setString(5, p.getColor());
                psProduct.setDouble(6, p.getPrice());
                psProduct.setInt(7, p.getQuantity());
                psProduct.setString(8, p.getImagePath());
                psProduct.setBoolean(9, p.isStatus());

                int rows = psProduct.executeUpdate();
                if (rows == 0) {
                    conn.rollback();
                    return false;
                }

                int productId;
                try (ResultSet rs = psProduct.getGeneratedKeys()) {
                    if (rs.next()) {
                        productId = rs.getInt(1);
                    } else {
                        conn.rollback();
                        return false;
                    }
                }

                try (PreparedStatement psInventory = conn.prepareStatement(sqlInventory)) {
                    psInventory.setInt(1, productId);
                    psInventory.setInt(2, p.getQuantity());
                    psInventory.setInt(3, 5); // ngưỡng tối thiểu mặc định
                    psInventory.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (Exception e) {
                conn.rollback();
                System.out.println("Lỗi insert Product: " + e.getMessage());
                return false;
            }

        } catch (Exception e) {
            System.out.println("Lỗi insert Product: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Product p) {
        String sql = "UPDATE Product SET ProductName = ?, CategoryID = ?, SupplierID = ?, Size = ?, Color = ?, Price = ?, Quantity = ?, ImagePath = ?, Status = ? WHERE ProductID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getProductName());
            ps.setInt(2, p.getCategoryId());
            ps.setInt(3, p.getSupplierId());
            ps.setString(4, p.getSize());
            ps.setString(5, p.getColor());
            ps.setDouble(6, p.getPrice());
            ps.setInt(7, p.getQuantity());
            ps.setString(8, p.getImagePath());
            ps.setBoolean(9, p.isStatus());
            ps.setInt(10, p.getProductId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Lỗi update Product: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(int id) {
        String sqlDeleteInventory = "DELETE FROM Inventory WHERE ProductID = ?";
        String sqlDeleteProduct = "DELETE FROM Product WHERE ProductID = ?";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Không thể kết nối DB");
                return false;
            }

            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlDeleteInventory);
                 PreparedStatement ps2 = conn.prepareStatement(sqlDeleteProduct)) {

                // Xóa inventory trước
                ps1.setInt(1, id);
                ps1.executeUpdate();

                // Sau đó xóa product
                ps2.setInt(1, id);
                int rows = ps2.executeUpdate();

                conn.commit();
                return rows > 0;

            } catch (Exception e) {
                conn.rollback();
                System.out.println("Lỗi delete Product: " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Lỗi delete Product: " + e.getMessage());
            return false;
        }
    }

    public List<Product> selectAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Product";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("ProductID"),
                        rs.getString("ProductName"),
                        rs.getInt("CategoryID"),
                        rs.getInt("SupplierID"),
                        rs.getString("Size"),
                        rs.getString("Color"),
                        rs.getDouble("Price"),
                        rs.getInt("Quantity"),
                        rs.getString("ImagePath"),
                        rs.getBoolean("Status")
                ));
            }
        } catch (Exception e) {
            System.out.println("Lỗi selectAll Product: " + e.getMessage());
        }
        return list;
    }

    public List<Product> searchByName(String keyword) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Product WHERE ProductName LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Product(
                            rs.getInt("ProductID"),
                            rs.getString("ProductName"),
                            rs.getInt("CategoryID"),
                            rs.getInt("SupplierID"),
                            rs.getString("Size"),
                            rs.getString("Color"),
                            rs.getDouble("Price"),
                            rs.getInt("Quantity"),
                            rs.getString("ImagePath"),
                            rs.getBoolean("Status")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi search Product: " + e.getMessage());
        }
        return list;
    }
}