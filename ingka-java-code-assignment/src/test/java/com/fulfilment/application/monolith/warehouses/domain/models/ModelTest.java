package com.fulfilment.application.monolith.warehouses.domain.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ModelTest {

    @Test
    public void testLocationModel() {
        Location loc = new Location("LOC1", 5, 100);
        assertEquals("LOC1", loc.identification);
        assertEquals(5, loc.maxNumberOfWarehouses);
        assertEquals(100, loc.maxCapacity);
    }

    @Test
    public void testWarehouseModel() {
        Warehouse w = new Warehouse();
        w.businessUnitCode = "BU1";
        w.location = "LOC1";
        w.capacity = 50;
        w.stock = 10;
        
        assertEquals("BU1", w.businessUnitCode);
        assertEquals("LOC1", w.location);
        assertEquals(50, w.capacity);
        assertEquals(10, w.stock);
    }
}
