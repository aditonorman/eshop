package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;


import java.util.List;
import java.util.Map;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository; // So we can update Order status

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment();
        // Set fields
        payment.setId(generatePaymentId()); // you can use a random UUID
        payment.setOrderId(order.getId());
        payment.setMethod(method);
        payment.setPaymentData(paymentData);

        // Decide initial status (Voucher or Bank Transfer)
        if ("VOUCHER".equalsIgnoreCase(method)) {
            String voucherCode = paymentData.get("voucherCode");
            if (isVoucherValid(voucherCode)) {
                payment.setStatus("SUCCESS");
                order.setStatus(OrderStatus.SUCCESS.getValue());
            } else {
                payment.setStatus("REJECTED");
                order.setStatus(OrderStatus.FAILED.getValue());
            }
        } else if ("BANK_TRANSFER".equalsIgnoreCase(method)) {
            String bankName = paymentData.get("bankName");
            String referenceCode = paymentData.get("referenceCode");

            if (isNullOrEmpty(bankName) || isNullOrEmpty(referenceCode)) {
                payment.setStatus("REJECTED");
                order.setStatus(OrderStatus.FAILED.getValue());
            } else {
                // Possibly "SUCCESS" right away or "WAITING_PAYMENT" if you prefer
                payment.setStatus("SUCCESS");
                order.setStatus(OrderStatus.SUCCESS.getValue());
            }
        } else {
            // Fallback for unknown method
            payment.setStatus("REJECTED");
            order.setStatus(OrderStatus.FAILED.getValue());
        }

        // Save Payment
        Payment savedPayment = paymentRepository.save(payment);
        // Update Order
        orderRepository.save(order);

        return savedPayment;
    }

    @Override
    public Payment setStatus(Payment payment, String newStatus) {
        payment.setStatus(newStatus);
        Payment updatedPayment = paymentRepository.save(payment);

        // Then update the Order’s status accordingly
        Order order = orderRepository.findById(payment.getOrderId());
        if (order != null) {
            if ("SUCCESS".equalsIgnoreCase(newStatus)) {
                order.setStatus(OrderStatus.SUCCESS.getValue());
            } else if ("REJECTED".equalsIgnoreCase(newStatus)) {
                order.setStatus(OrderStatus.FAILED.getValue());
            }
            orderRepository.save(order);
        }

        return updatedPayment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        if (payment == null) {
            throw new java.util.NoSuchElementException("Payment not found with ID: " + paymentId);
        }
        return payment;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // --- Helpers ---

    private String generatePaymentId() {
        // You can replace this with UUID.randomUUID().toString() if you prefer
        return "PAY-" + System.nanoTime();
    }

    private boolean isVoucherValid(String code) {
        if (code == null) return false;
        // Must be 16 chars, start with "ESHOP", and have exactly 8 digits
        if (code.length() != 16) return false;
        if (!code.startsWith("ESHOP")) return false;
        long digitCount = code.chars().filter(Character::isDigit).count();
        return digitCount == 8;
    }

    private boolean isNullOrEmpty(String s) {
        return (s == null || s.trim().isEmpty());
    }
}
