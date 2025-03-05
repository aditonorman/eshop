package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentRepository {
    private final Map<String, Payment> paymentMap = new ConcurrentHashMap<>();

    public Payment save(Payment payment) {
        // Adds a new payment or updates an existing one using the payment's id as key.
        paymentMap.put(payment.getId(), payment);
        return payment;
    }

    public Payment findById(String paymentId) {
        return paymentMap.get(paymentId);
    }

    public List<Payment> findAll() {
        // Returns a copy of the payments list to prevent external modifications.
        return new ArrayList<>(paymentMap.values());
    }
}
