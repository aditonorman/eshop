package id.ac.ui.cs.advprog.eshop.model;

import java.util.Map;

public class Payment {
    private String id;                // e.g., UUID string
    private String orderId;           // reference to Order's ID
    private String method;            // e.g. "VOUCHER", "BANK_TRANSFER"
    private String status;            // e.g. "SUCCESS", "REJECTED", etc.
    private Map<String, String> paymentData;  // sub-feature data

    public Payment() {
        // Default constructor (if needed for frameworks or Jackson)
    }

    public Payment(String id, String orderId, String method, String status, Map<String, String> paymentData) {
        this.id = id;
        this.orderId = orderId;
        this.method = method;
        this.status = status;
        this.paymentData = paymentData;
    }

    // --- Getters and setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getPaymentData() {
        return paymentData;
    }

    public void setPaymentData(Map<String, String> paymentData) {
        this.paymentData = paymentData;
    }
}

