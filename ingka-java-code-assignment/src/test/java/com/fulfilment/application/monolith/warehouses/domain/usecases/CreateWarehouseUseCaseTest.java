package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateWarehouseUseCaseTest {

  private WarehouseStore warehouseStore;
  private LocationResolver locationResolver;
  private CreateWarehouseUseCase createWarehouseUseCase;

  @BeforeEach
  void setUp() {
    warehouseStore = org.mockito.Mockito.mock(WarehouseStore.class);
    locationResolver = org.mockito.Mockito.mock(LocationResolver.class);
    createWarehouseUseCase = new CreateWarehouseUseCase(warehouseStore, locationResolver);
  }

  @Test
  void testCreateSuccess() {
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "BU001";
    warehouse.location = "LOC001";
    warehouse.capacity = 100;
    warehouse.stock = 50;

    Location location = new Location("LOC001", 5, 500);

    org.mockito.Mockito.when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(null);
    org.mockito.Mockito.when(locationResolver.resolveByIdentifier("LOC001")).thenReturn(location);
    org.mockito.Mockito.when(warehouseStore.listByLocation("LOC001")).thenReturn(Collections.emptyList());

    createWarehouseUseCase.create(warehouse);

    org.mockito.Mockito.verify(warehouseStore).create(warehouse);
  }

  @Test
  void testCreateDuplicateBUCode() {
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "BU001";

    org.mockito.Mockito.when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(new Warehouse());

    assertThrows(WebApplicationException.class, () -> createWarehouseUseCase.create(warehouse));
    org.mockito.Mockito.verify(warehouseStore, org.mockito.Mockito.never()).create(org.mockito.ArgumentMatchers.any());
  }

  @Test
  void testCreateInvalidLocation() {
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "BU001";
    warehouse.location = "INVALID";

    org.mockito.Mockito.when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(null);
    org.mockito.Mockito.when(locationResolver.resolveByIdentifier("INVALID")).thenReturn(null);

    assertThrows(WebApplicationException.class, () -> createWarehouseUseCase.create(warehouse));
    org.mockito.Mockito.verify(warehouseStore, org.mockito.Mockito.never()).create(org.mockito.ArgumentMatchers.any());
  }

  @Test
  void testCreateMaxWarehousesReached() {
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "BU001";
    warehouse.location = "LOC001";

    Location location = new Location("LOC001", 1, 500);
    Warehouse existing = new Warehouse();
    existing.businessUnitCode = "EX001";

    org.mockito.Mockito.when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(null);
    org.mockito.Mockito.when(locationResolver.resolveByIdentifier("LOC001")).thenReturn(location);
    org.mockito.Mockito.when(warehouseStore.listByLocation("LOC001")).thenReturn(List.of(existing));

    assertThrows(WebApplicationException.class, () -> createWarehouseUseCase.create(warehouse));
    org.mockito.Mockito.verify(warehouseStore, org.mockito.Mockito.never()).create(org.mockito.ArgumentMatchers.any());
  }

  @Test
  void testCreateMaxCapacityReached() {
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "BU001";
    warehouse.location = "LOC001";
    warehouse.capacity = 200;

    Location location = new Location("LOC001", 5, 300);
    Warehouse existing = new Warehouse();
    existing.capacity = 200;

    org.mockito.Mockito.when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(null);
    org.mockito.Mockito.when(locationResolver.resolveByIdentifier("LOC001")).thenReturn(location);
    org.mockito.Mockito.when(warehouseStore.listByLocation("LOC001")).thenReturn(List.of(existing));

    assertThrows(WebApplicationException.class, () -> createWarehouseUseCase.create(warehouse));
    org.mockito.Mockito.verify(warehouseStore, org.mockito.Mockito.never()).create(org.mockito.ArgumentMatchers.any());
  }

  @Test
  void testCreateStockExceedsCapacity() {
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "BU001";
    warehouse.location = "LOC001";
    warehouse.capacity = 100;
    warehouse.stock = 150;

    Location location = new Location("LOC001", 5, 500);

    org.mockito.Mockito.when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(null);
    org.mockito.Mockito.when(locationResolver.resolveByIdentifier("LOC001")).thenReturn(location);
    org.mockito.Mockito.when(warehouseStore.listByLocation("LOC001")).thenReturn(Collections.emptyList());

    assertThrows(WebApplicationException.class, () -> createWarehouseUseCase.create(warehouse));
    org.mockito.Mockito.verify(warehouseStore, org.mockito.Mockito.never()).create(org.mockito.ArgumentMatchers.any());
  }
}
