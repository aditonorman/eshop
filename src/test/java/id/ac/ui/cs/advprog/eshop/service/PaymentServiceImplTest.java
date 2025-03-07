package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    private PaymentRepository paymentRepository;
    private OrderRepository orderRepository;
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(PaymentRepository.class);
        orderRepository = mock(OrderRepository.class);
        paymentService = new PaymentServiceImpl(paymentRepository, orderRepository);
    }

    @Test
    void testAddPayment_Voucher_ValidCode() {
        // Create a product with setters
        Product product = new Product();
        product.setProductId("prod1");
        product.setProductName("TestItem");
        product.setProductQuantity(10);

        // Put it in a list
        List<Product> products = Collections.singletonList(product);

        // Now build the Order with a non-empty products list
        Order mockOrder = new Order("order1", products, 123456789L, "John");
        mockOrder.setStatus(OrderStatus.WAITING_PAYMENT.getValue());

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678"); // valid

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(mockOrder, "VOUCHER", paymentData);

        assertEquals("SUCCESS", result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), mockOrder.getStatus());
    }

    @Test
    void testAddPayment_Voucher_InvalidCode() {
        Product product = new Product();
        product.setProductId("prod2");
        product.setProductName("InvalidVoucher");
        product.setProductQuantity(2);

        List<Product> products = Collections.singletonList(product);

        // This time the voucher code will be invalid
        Order mockOrder = new Order("order2", products, 123456L, "Alice");
        mockOrder.setStatus(OrderStatus.WAITING_PAYMENT.getValue());

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ABC123"); // clearly invalid

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(mockOrder, "VOUCHER", paymentData);

        assertEquals("REJECTED", result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), mockOrder.getStatus());
    }

    @Test
    void testAddPayment_BankTransfer_ValidData() {
        Product product = new Product();
        product.setProductId("prod3");
        product.setProductName("BankTransferItem");
        product.setProductQuantity(5);

        List<Product> products = Collections.singletonList(product);

        Order mockOrder = new Order("order3", products, 1000L, "Bob");
        mockOrder.setStatus(OrderStatus.WAITING_PAYMENT.getValue());

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "MyBank");
        paymentData.put("referenceCode", "REF12345");

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(mockOrder, "BANK_TRANSFER", paymentData);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), mockOrder.getStatus());
    }

    @Test
    void testAddPayment_BankTransfer_InvalidData() {
        Product product = new Product();
        product.setProductId("prod4");
        product.setProductName("BankTransferItem");
        product.setProductQuantity(1);

        List<Product> products = Collections.singletonList(product);

        Order mockOrder = new Order("order4", products, 2000L, "Charlie");
        mockOrder.setStatus(OrderStatus.WAITING_PAYMENT.getValue());

        // Missing 'referenceCode'
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "MyBank");

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.addPayment(mockOrder, "BANK_TRANSFER", paymentData);
        assertEquals("REJECTED", result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), mockOrder.getStatus());
    }

    @Test
    void testSetStatus_Success() {
        Payment payment = new Payment("pay123", "ord123", "VOUCHER", "WAITING_PAYMENT", new HashMap<>());

        // We’ll need an Order with id=ord123
        Product product = new Product();
        product.setProductId("p5");
        product.setProductName("ItemX");
        product.setProductQuantity(3);

        Order order = new Order("ord123", List.of(product), 9999L, "Diana");
        order.setStatus(OrderStatus.WAITING_PAYMENT.getValue());

        when(orderRepository.findById("ord123")).thenReturn(order);
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment updated = paymentService.setStatus(payment, "SUCCESS");
        assertEquals("SUCCESS", updated.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
    }

    @Test
    void testSetStatus_Rejected() {
        Payment payment = new Payment("payABC", "ordXYZ", "BANK_TRANSFER", "WAITING_PAYMENT", new HashMap<>());

        Product product = new Product();
        product.setProductId("p6");
        product.setProductName("ItemY");
        product.setProductQuantity(7);

        Order order = new Order("ordXYZ", List.of(product), 8888L, "Elisa");
        order.setStatus(OrderStatus.WAITING_PAYMENT.getValue());

        when(orderRepository.findById("ordXYZ")).thenReturn(order);
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment updated = paymentService.setStatus(payment, "REJECTED");
        assertEquals("REJECTED", updated.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
    }

    @Test
    void testGetPayment_Valid() {
        Payment payment = new Payment("p888", "o888", "BANK_TRANSFER", "SUCCESS", new HashMap<>());
        when(paymentRepository.findById("p888")).thenReturn(payment);

        Payment found = paymentService.getPayment("p888");
        assertNotNull(found);
        assertEquals("p888", found.getId());
    }

    @Test
    void testGetPayment_Invalid() {
        when(paymentRepository.findById("unknown")).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> {
            paymentService.getPayment("unknown");
        });
    }

    @Test
    void testGetAllPayments() {
        Payment p1 = new Payment("pay1", "ord1", "BANK_TRANSFER", "SUCCESS", new HashMap<>());
        Payment p2 = new Payment("pay2", "ord2", "VOUCHER", "REJECTED", new HashMap<>());
        when(paymentRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Payment> all = paymentService.getAllPayments();
        assertEquals(2, all.size());
    }
}
