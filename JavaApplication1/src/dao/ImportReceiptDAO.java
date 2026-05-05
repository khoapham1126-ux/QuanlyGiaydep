package dao;

import model.ImportReceipt;
import model.ImportReceiptDetail;
import utils.DBConnection;
import java.sql.*;

public class ImportReceiptDAO {
	public boolean insertImportAndInventory(ImportReceipt receipt) {
		String sqlReceipt = "INSERT INTO ImportReceipt(SupplierID, TotalAmount, CreatedBy, ImportDate) VALUES (?, ?, ?, NOW())";
		String sqlDetail = "INSERT INTO ImportReceiptDetail(ImportID, ProductID, Quantity, ImportPrice) VALUES (?, ?, ?, ?)";
		String sqlUpdateInventory = "UPDATE Inventory SET Quantity = Quantity + ?, LastUpdated = NOW() WHERE ProductID = ?";

		try (Connection conn = DBConnection.getConnection()) {
			if (conn == null)
				return false;
			conn.setAutoCommit(false); // Bắt đầu Transaction

			try (PreparedStatement psReceipt = conn.prepareStatement(sqlReceipt, Statement.RETURN_GENERATED_KEYS)) {
				psReceipt.setInt(1, receipt.getSupplierId());
				psReceipt.setDouble(2, receipt.getTotalAmount());
				psReceipt.setInt(3, receipt.getCreatedBy());

				int rows = psReceipt.executeUpdate();
				if (rows == 0) {
					conn.rollback();
					return false;
				}

				int importId = 0;
				try (ResultSet rs = psReceipt.getGeneratedKeys()) {
					if (rs.next())
						importId = rs.getInt(1);
				}

				try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail);
						PreparedStatement psInventory = conn.prepareStatement(sqlUpdateInventory)) {

					for (ImportReceiptDetail d : receipt.getDetails()) {
						// Lưu chi tiết
						psDetail.setInt(1, importId);
						psDetail.setInt(2, d.getProductId());
						psDetail.setInt(3, d.getQuantity());
						psDetail.setDouble(4, d.getImportPrice());
						psDetail.executeUpdate();

						// Cộng dồn kho
						psInventory.setInt(1, d.getQuantity());
						psInventory.setInt(2, d.getProductId());
						psInventory.executeUpdate();
					}
				}
				conn.commit(); // Thành công thì lưu toàn bộ
				return true;
			} catch (Exception e) {
				conn.rollback();
				System.out.println("Lỗi insert Import: " + e.getMessage());
				return false;
			}
		} catch (Exception e) {
			System.out.println("Lỗi DB Import: " + e.getMessage());
			return false;
		}
	}
}