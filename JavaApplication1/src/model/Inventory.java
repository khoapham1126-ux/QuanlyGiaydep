package model;

public class Inventory {
    private int inventoryId;
    private int productId;
    private int quantity;
    private int minQuantity;
    private String lastUpdated;

    public Inventory() {
    }

    public Inventory(int inventoryId, int productId, int quantity, int minQuantity, String lastUpdated) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
        this.lastUpdated = lastUpdated;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
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

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}   