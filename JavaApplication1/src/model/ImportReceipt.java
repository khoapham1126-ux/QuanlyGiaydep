package model;

import java.util.List;

public class ImportReceipt {
	private int importId;
	private int supplierId;
	private String importDate;
	private double totalAmount;
	private int createdBy;

	// Chứa danh sách chi tiết phiếu nhập
	private List<ImportReceiptDetail> details;

	public ImportReceipt() {
	}

	// Getters and Setters
	public int getImportId() {
		return importId;
	}

	public void setImportId(int importId) {
		this.importId = importId;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public String getImportDate() {
		return importDate;
	}

	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public List<ImportReceiptDetail> getDetails() {
		return details;
	}

	public void setDetails(List<ImportReceiptDetail> details) {
		this.details = details;
	}
}