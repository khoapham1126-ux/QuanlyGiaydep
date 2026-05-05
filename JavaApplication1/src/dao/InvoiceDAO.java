package dao;

import model.Invoice;
import model.InvoiceDetail;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    public boolean insertInvoice(Invoice invoice) {
        String sqlInvoice = "INSERT INTO Invoice(CustomerID, EmployeeID, InvoiceDate, TotalAmount, Discount, FinalAmount) "
                          + "VALUES (?, ?, NOW(), ?, ?, ?)";
        String sqlDetail = "INSERT INTO InvoiceDetail(InvoiceID, ProductID, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE Inventory SET Quantity = Quantity - ? WHERE ProductID = ?";

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) return false;
            conn.setAutoCommit(false);

            try (PreparedStatement psInvoice = conn.prepareStatement(sqlInvoice, Statement.RETURN_GENERATED_KEYS)) {
                psInvoice.setInt(1, invoice.getCustomerId());
                psInvoice.setInt(2, invoice.getEmployeeId());
                psInvoice.setDouble(3, invoice.getTotalAmount());
                psInvoice.setDouble(4, invoice.getDiscount());
                psInvoice.setDouble(5, invoice.getFinalAmount());

                int rows = psInvoice.executeUpdate();
                if (rows == 0) { conn.rollback(); return false; }

                int invoiceId = 0;
                try (ResultSet rs = psInvoice.getGeneratedKeys()) {
                    if (rs.next()) invoiceId = rs.getInt(1);
                }

                try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
                     PreparedStatement psStock = conn.prepareStatement(sqlUpdateStock)) {

                    for (InvoiceDetail d : invoice.getDetails()) {
                        // Lưu chi tiết hóa đơn
                        psDetail.setInt(1, invoiceId);
                        psDetail.setInt(2, d.getProductId());
                        psDetail.setInt(3, d.getQuantity());
                        psDetail.setDouble(4, d.getUnitPrice());
                        psDetail.executeUpdate();

                        // Trừ tồn kho
                        psStock.setInt(1, d.getQuantity());
                        psStock.setInt(2, d.getProductId());
                        psStock.executeUpdate();
                    }
                }

                conn.commit();
                return true;

            } catch (Exception e) {
                conn.rollback();
                System.out.println("Lỗi insert Invoice: " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            System.out.println("Lỗi DB Invoice: " + e.getMessage());
            return false;
        }
    }

    public List<Invoice> selectAll() {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM Invoice ORDER BY InvoiceDate DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Invoice(
                    rs.getInt("InvoiceID"),
                    rs.getInt("CustomerID"),
                    rs.getInt("EmployeeID"),
                    rs.getString("InvoiceDate"),
                    rs.getDouble("TotalAmount"),
                    rs.getDouble("Discount"),
                    rs.getDouble("FinalAmount")
                ));
            }
        } catch (Exception e) {
            System.out.println("Lỗi selectAll Invoice: " + e.getMessage());
        }
        return list;
    }

    public List<InvoiceDetail> getDetailsByInvoiceId(int invoiceId) {
        List<InvoiceDetail> list = new ArrayList<>();
        String sql = "SELECT * FROM InvoiceDetail WHERE InvoiceID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new InvoiceDetail(
                        rs.getInt("DetailID"),
                        rs.getInt("InvoiceID"),
                        rs.getInt("ProductID"),
                        rs.getInt("Quantity"),
                        rs.getDouble("UnitPrice")
                    ));
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi getDetails Invoice: " + e.getMessage());
        }
        return list;
    }
}