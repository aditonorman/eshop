package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {

    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
    }

    @Test
    void testSaveNewPayment() {
        Payment payment = new Payment("pay1", "order1", "VOUCHER", "WAITING_PAYMENT", new HashMap<>());
        paymentRepository.save(payment);

        Payment found = paymentRepository.findById("pay1");
        assertNotNull(found);
        assertEquals("pay1", found.getId());
    }

    @Test
    void testSaveUpdatePayment() {
        // Create initial payment
        Payment payment = new Payment("pay2", "order2", "BANK_TRANSFER", "WAITING_PAYMENT", new HashMap<>());
        paymentRepository.save(payment);

        // Update
        payment.setStatus("REJECTED");
        paymentRepository.save(payment);

        Payment found = paymentRepository.findById("pay2");
        assertEquals("REJECTED", found.getStatus());
    }

    @Test
    void testFindByIdValidId() {
        Payment p = new Payment("pay3", "order3", "VOUCHER", "SUCCESS", Collections.emptyMap());
        paymentRepository.save(p);

        Payment found = paymentRepository.findById("pay3");
        assertNotNull(found);
        assertEquals("SUCCESS", found.getStatus());
    }

    @Test
    void testFindByIdInvalidId() {
        Payment found = paymentRepository.findById("nonexistent");
        assertNull(found);
    }

    @Test
    void testFindAll() {
        Payment p1 = new Payment("p1", "o1", "VOUCHER", "SUCCESS", new HashMap<>());
        Payment p2 = new Payment("p2", "o2", "BANK_TRANSFER", "REJECTED", new HashMap<>());
        paymentRepository.save(p1);
        paymentRepository.save(p2);

        List<Payment> all = paymentRepository.findAll();
        assertEquals(2, all.size());
    }
}
