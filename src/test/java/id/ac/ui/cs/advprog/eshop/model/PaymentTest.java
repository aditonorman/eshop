package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Payment payment;

    @BeforeEach
    void setUp() {
        Map<String, String> data = new HashMap<>();
        data.put("exampleKey", "exampleValue");

        payment = new Payment(
                "pay-123",
                "ord-ABC",
                "BANK_TRANSFER",
                "WAITING_PAYMENT",
                data
        );
    }

    @Test
    void testPaymentFields() {
        assertEquals("pay-123", payment.getId());
        assertEquals("ord-ABC", payment.getOrderId());
        assertEquals("BANK_TRANSFER", payment.getMethod());
        assertEquals("WAITING_PAYMENT", payment.getStatus());
        assertNotNull(payment.getPaymentData());
        assertEquals("exampleValue", payment.getPaymentData().get("exampleKey"));
    }

    @Test
    void testSetStatus() {
        payment.setStatus("SUCCESS");
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testSetPaymentData() {
        Map<String, String> newData = new HashMap<>();
        newData.put("bankName", "EShopBank");
        payment.setPaymentData(newData);
        assertEquals("EShopBank", payment.getPaymentData().get("bankName"));
    }
}
