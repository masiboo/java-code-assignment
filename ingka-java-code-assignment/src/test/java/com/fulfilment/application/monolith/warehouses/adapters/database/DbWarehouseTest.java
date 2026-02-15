package com.fulfilment.application.monolith.warehouses.adapters.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

public class DbWarehouseTest {

    @Test
    public void testDbWarehouseModel() {
        DbWarehouse db = new DbWarehouse();
        db.id = 1L;
        db.businessUnitCode = "BU1";
        db.location = "LOC1";
        db.capacity = 100;
        db.stock = 50;
        LocalDateTime now = LocalDateTime.now();
        db.createdAt = now;
        db.archivedAt = now;
        
        assertEquals(1L, db.id);
        assertEquals("BU1", db.businessUnitCode);
        assertEquals("LOC1", db.location);
        assertEquals(100, db.capacity);
        assertEquals(50, db.stock);
        assertEquals(now, db.createdAt);
        assertEquals(now, db.archivedAt);
    }
}
