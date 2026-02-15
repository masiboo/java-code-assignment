package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collections;

public class ReplaceWarehouseUseCaseTestReal {

    private WarehouseStore warehouseStore;
    private LocationResolver locationResolver;
    private ReplaceWarehouseUseCase replaceWarehouseUseCase;

    @BeforeEach
    public void setup() {
        warehouseStore = mock(WarehouseStore.class);
        locationResolver = mock(LocationResolver.class);
        replaceWarehouseUseCase = new ReplaceWarehouseUseCase(warehouseStore, locationResolver);
    }

    @Test
    public void testReplaceSuccess() {
        Warehouse existingW = new Warehouse();
        existingW.businessUnitCode = "BU1";
        existingW.location = "LOC";
        existingW.capacity = 100;
        existingW.stock = 10;

        Warehouse newW = new Warehouse();
        newW.businessUnitCode = "BU1";
        newW.location = "LOC";
        newW.capacity = 120;
        newW.stock = 10;

        Location loc = new Location("LOC", 5, 500);

        when(warehouseStore.findByBusinessUnitCode("BU1")).thenReturn(existingW);
        when(locationResolver.resolveByIdentifier("LOC")).thenReturn(loc);
        when(warehouseStore.listByLocation("LOC")).thenReturn(Collections.singletonList(existingW));

        replaceWarehouseUseCase.replace(newW);

        verify(warehouseStore).update(newW);
    }
}
