package com.fulfilment.application.monolith.products;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    public void testProductModel() {
        Product p = new Product();
        p.id = 1L;
        p.name = "Test Product";
        p.description = "Test Description";
        p.price = new BigDecimal("10.00");
        p.stock = 5;
        
        assertEquals(1L, p.id);
        assertEquals("Test Product", p.name);
        assertEquals("Test Description", p.description);
        assertEquals(new BigDecimal("10.00"), p.price);
        assertEquals(5, p.stock);
    }
    
    @Test
    public void testProductConstructor() {
        Product p = new Product("Constructor Product");
        assertEquals("Constructor Product", p.name);
    }
}
