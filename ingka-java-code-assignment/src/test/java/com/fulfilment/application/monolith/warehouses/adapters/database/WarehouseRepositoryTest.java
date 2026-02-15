package com.fulfilment.application.monolith.warehouses.adapters.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class WarehouseRepositoryTest {

    @Inject
    WarehouseRepository repository;

    @Test
    @Transactional
    public void testRepositoryOperations() {
        Warehouse w = new Warehouse();
        w.businessUnitCode = "REPO-TEST-001";
        w.location = "ZWOLLE-001";
        w.capacity = 100;
        w.stock = 50;

        // Create
        repository.create(w);

        // Find
        Warehouse found = repository.findByBusinessUnitCode("REPO-TEST-001");
        assertNotNull(found);
        assertEquals("ZWOLLE-001", found.location);

        // Update
        found.location = "ZWOLLE-002";
        repository.update(found);
        Warehouse updated = repository.findByBusinessUnitCode("REPO-TEST-001");
        assertEquals("ZWOLLE-002", updated.location);

        // List
        List<Warehouse> list = repository.listByLocation("ZWOLLE-002");
        assertNotNull(list);
        
        // Remove
        repository.remove(updated);
        assertNull(repository.findByBusinessUnitCode("REPO-TEST-001"));
    }
    
    @Test
    @Transactional
    public void testListByLocationNull() {
        List<Warehouse> all = repository.listByLocation(null);
        assertNotNull(all);
    }
}
