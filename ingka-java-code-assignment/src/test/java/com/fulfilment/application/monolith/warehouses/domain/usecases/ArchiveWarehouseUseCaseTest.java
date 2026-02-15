package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArchiveWarehouseUseCaseTest {

  private WarehouseStore warehouseStore;
  private ArchiveWarehouseUseCase archiveWarehouseUseCase;

  @BeforeEach
  void setUp() {
    warehouseStore = org.mockito.Mockito.mock(WarehouseStore.class);
    archiveWarehouseUseCase = new ArchiveWarehouseUseCase(warehouseStore);
  }

  @Test
  void testArchiveSuccess() {
    Warehouse existing = new Warehouse();
    existing.businessUnitCode = "BU001";

    org.mockito.Mockito.when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(existing);

    archiveWarehouseUseCase.archive(existing);

    assertNotNull(existing.archivedAt);
    org.mockito.Mockito.verify(warehouseStore).update(existing);
  }

  @Test
  void testArchiveNotFound() {
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "BU001";

    org.mockito.Mockito.when(warehouseStore.findByBusinessUnitCode("BU001")).thenReturn(null);

    assertThrows(WebApplicationException.class, () -> archiveWarehouseUseCase.archive(warehouse));
    org.mockito.Mockito.verify(warehouseStore, org.mockito.Mockito.never()).update(org.mockito.ArgumentMatchers.any());
  }
}
