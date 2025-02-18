package id.ac.ui.cs.advprog.eshop.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    @Test
    void testHome() {
        // Arrange
        HomeController controller = new HomeController();

        // Act
        String viewName = controller.home();

        // Assert
        assertEquals("index", viewName);
    }
}
