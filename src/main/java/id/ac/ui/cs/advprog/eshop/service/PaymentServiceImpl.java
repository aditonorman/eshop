package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;

import java.util.List;
import java.util.Map;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment();
        payment.setId(generatePaymentId());
        payment.setOrderId(order.getId());
        payment.setMethod(method);
        payment.setPaymentData(paymentData);

        switch (method.toUpperCase()) {
            case "VOUCHER":
                processVoucherPayment(payment, order, paymentData);
                break;
            case "BANK_TRANSFER":
                processBankTransferPayment(payment, order, paymentData);
                break;
            default:
                markPaymentRejected(payment, order);
                break;
        }

        Payment savedPayment = paymentRepository.save(payment);
        orderRepository.save(order);
        return savedPayment;
    }

    @Override
    public Payment setStatus(Payment payment, String newStatus) {
        payment.setStatus(newStatus);
        Payment updatedPayment = paymentRepository.save(payment);
        updateOrderStatus(payment.getOrderId(), newStatus);
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

    // --- Helper Methods ---

    private String generatePaymentId() {
        return "PAY-" + System.nanoTime();
    }

    private void processVoucherPayment(Payment payment, Order order, Map<String, String> paymentData) {
        String voucherCode = paymentData.get("voucherCode");
        if (isVoucherValid(voucherCode)) {
            markPaymentSuccess(payment, order);
        } else {
            markPaymentRejected(payment, order);
        }
    }

    private void processBankTransferPayment(Payment payment, Order order, Map<String, String> paymentData) {
        String bankName = paymentData.get("bankName");
        String referenceCode = paymentData.get("referenceCode");

        if (isNullOrEmpty(bankName) || isNullOrEmpty(referenceCode)) {
            markPaymentRejected(payment, order);
        } else {
            markPaymentSuccess(payment, order);
        }
    }

    private void markPaymentSuccess(Payment payment, Order order) {
        payment.setStatus("SUCCESS");
        order.setStatus(OrderStatus.SUCCESS.getValue());
    }

    private void markPaymentRejected(Payment payment, Order order) {
        payment.setStatus("REJECTED");
        order.setStatus(OrderStatus.FAILED.getValue());
    }

    private void updateOrderStatus(String orderId, String paymentStatus) {
        Order order = orderRepository.findById(orderId);
        if (order != null) {
            if ("SUCCESS".equalsIgnoreCase(paymentStatus)) {
                order.setStatus(OrderStatus.SUCCESS.getValue());
            } else if ("REJECTED".equalsIgnoreCase(paymentStatus)) {
                order.setStatus(OrderStatus.FAILED.getValue());
            }
            orderRepository.save(order);
        }
    }

    private boolean isVoucherValid(String code) {
        if (code == null) return false;
        if (code.length() != 16) return false;
        if (!code.startsWith("ESHOP")) return false;
        long digitCount = code.chars().filter(Character::isDigit).count();
        return digitCount == 8;
    }

    private boolean isNullOrEmpty(String s) {
        return (s == null || s.trim().isEmpty());
    }
}
