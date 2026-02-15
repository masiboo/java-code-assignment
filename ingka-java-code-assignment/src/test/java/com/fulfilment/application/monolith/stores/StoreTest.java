package com.fulfilment.application.monolith.stores;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class StoreTest {

    @Test
    public void testStoreModel() {
        Store s = new Store();
        s.id = 1L;
        s.name = "Test Store";
        s.quantityProductsInStock = 10;
        
        assertEquals(1L, s.id);
        assertEquals("Test Store", s.name);
        assertEquals(10, s.quantityProductsInStock);
    }
    
    @Test
    public void testStoreConstructor() {
        Store s = new Store("Constructor Store");
        assertEquals("Constructor Store", s.name);
    }
}
