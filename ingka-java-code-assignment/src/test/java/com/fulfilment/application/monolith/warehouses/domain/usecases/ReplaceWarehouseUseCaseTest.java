package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReplaceWarehouseUseCaseTest {

  private WarehouseStore warehouseStore;
  private LocationResolver locationResolver;
  private ReplaceWarehouseUseCase replaceWarehouseUseCase;

  @BeforeEach
  void setUp() {
    warehouseStore = org.mockito.Mockito.mock(WarehouseStore.class);
    locationResolver = org.mockito.Mockito.mock(LocationResolver.class);
    replaceWarehouseUseCase = new ReplaceWarehouseUseCase(warehouseStore, locationResolver);
  }

  @Test
  void testReplaceSuccess() {
    Warehouse existing = new Warehouse();
    existing.businessUnitCode = "BU001";
    existing.stock = 50;
    existing.capacity = 100;

    Warehouse replacement = new Warehouse();
    replacement.businessUnitCode = "BU001";
    replacement.location = "LOC001";
    replacement.capacity = 120;
    replacement.stock = 50;

    Location location = new Location("LOC001", 5, 500);

    org.mockito.Mockito.when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(existing);
    org.mockito.Mockito.when(locationResolver.resolveByIdentifier("LOC001")).thenReturn(location);
    org.mockito.Mockito.when(warehouseStore.listByLocation("LOC001")).thenReturn(List.of(existing));

    replaceWarehouseUseCase.replace(replacement);

    org.mockito.Mockito.verify(warehouseStore).update(replacement);
  }

  @Test
  void testReplaceNotFound() {
    Warehouse replacement = new Warehouse();
    replacement.businessUnitCode = "BU001";

    org.mockito.Mockito.when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(null);

    assertThrows(WebApplicationException.class, () -> replaceWarehouseUseCase.replace(replacement));
    org.mockito.Mockito.verify(warehouseStore, org.mockito.Mockito.never()).update(org.mockito.ArgumentMatchers.any());
  }

  @Test
  void testReplaceStockMismatch() {
    Warehouse existing = new Warehouse();
    existing.businessUnitCode = "BU001";
    existing.stock = 50;

    Warehouse replacement = new Warehouse();
    replacement.businessUnitCode = "BU001";
    replacement.stock = 60; // Mismatch

    org.mockito.Mockito.when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(existing);

    assertThrows(WebApplicationException.class, () -> replaceWarehouseUseCase.replace(replacement));
  }

  @Test
  void testReplaceCapacityInsufficientForStock() {
    Warehouse existing = new Warehouse();
    existing.businessUnitCode = "BU001";
    existing.stock = 100;

    Warehouse replacement = new Warehouse();
    replacement.businessUnitCode = "BU001";
    replacement.capacity = 80; // Too small
    replacement.stock = 100;

    org.mockito.Mockito.when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(existing);

    assertThrows(WebApplicationException.class, () -> replaceWarehouseUseCase.replace(replacement));
  }
}
