package model;

public class ImportReceiptDetail {
	private int detailId;
	private int importId;
	private int productId;
	private int quantity;
	private double importPrice;

	public ImportReceiptDetail() {
	}

	public ImportReceiptDetail(int detailId, int importId, int productId, int quantity, double importPrice) {
		this.detailId = detailId;
		this.importId = importId;
		this.productId = productId;
		this.quantity = quantity;
		this.importPrice = importPrice;
	}

	public ImportReceiptDetail(int importId, int productId, int quantity, double importPrice) {
		this.importId = importId;
		this.productId = productId;
		this.quantity = quantity;
		this.importPrice = importPrice;
	}

	public int getDetailId() {
		return detailId;
	}

	public void setDetailId(int detailId) {
		this.detailId = detailId;
	}

	public int getImportId() {
		return importId;
	}

	public void setImportId(int importId) {
		this.importId = importId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getImportPrice() {
		return importPrice;
	}

	public void setImportPrice(double importPrice) {
		this.importPrice = importPrice;
	}
}