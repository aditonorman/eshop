package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;

import java.util.ArrayList;
import java.util.List;

public class PaymentRepository {
    private final List<Payment> payments = new ArrayList<>();

    public Payment save(Payment payment) {
        // If an existing Payment has the same ID, update it.
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getId().equals(payment.getId())) {
                payments.set(i, payment);
                return payment;
            }
        }
        // If not found, add as new.
        payments.add(payment);
        return payment;
    }

    public Payment findById(String paymentId) {
        return payments.stream()
                .filter(p -> p.getId().equals(paymentId))
                .findFirst()
                .orElse(null);
    }

    public List<Payment> findAll() {
        return payments;
    }
}
