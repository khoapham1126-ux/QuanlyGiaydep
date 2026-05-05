package model;

import java.util.List;

public class Invoice {
    private int invoiceId;
    private int customerId;
    private int employeeId;
    private String invoiceDate;
    private double totalAmount;
    private double discount;
    private double finalAmount;
    private List<InvoiceDetail> details;

    public Invoice() {}

    public Invoice(int invoiceId, int customerId, int employeeId, String invoiceDate,
                   double totalAmount, double discount, double finalAmount) {
        this.invoiceId = invoiceId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.finalAmount = finalAmount;
    }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(String invoiceDate) { this.invoiceDate = invoiceDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getFinalAmount() { return finalAmount; }
    public void setFinalAmount(double finalAmount) { this.finalAmount = finalAmount; }

    public List<InvoiceDetail> getDetails() { return details; }
    public void setDetails(List<InvoiceDetail> details) { this.details = details; }
}