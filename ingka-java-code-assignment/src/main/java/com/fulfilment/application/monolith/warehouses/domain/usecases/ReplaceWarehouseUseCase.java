package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.ReplaceWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;

@ApplicationScoped
public class ReplaceWarehouseUseCase implements ReplaceWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public ReplaceWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void replace(Warehouse newWarehouse) {
    Warehouse existingWarehouse = warehouseStore.findByBusinessUnitCode(newWarehouse.businessUnitCode);
    if (existingWarehouse == null) {
      throw new WebApplicationException("Warehouse to replace not found", 404);
    }

    // Location Validation
    Location location = locationResolver.resolveByIdentifier(newWarehouse.location);
    if (location == null) {
      throw new WebApplicationException("Invalid location", 400);
    }

    // Capacity Accommodation
    if (newWarehouse.capacity < existingWarehouse.stock) {
      throw new WebApplicationException("New warehouse capacity cannot accommodate existing stock", 400);
    }

    // Stock Matching
    if (!newWarehouse.stock.equals(existingWarehouse.stock)) {
      throw new WebApplicationException("New warehouse stock must match previous warehouse stock", 400);
    }

    // Capacity Validation against Location Max
    List<Warehouse> otherWarehouses = warehouseStore.listByLocation(newWarehouse.location).stream()
        .filter(w -> !w.businessUnitCode.equals(newWarehouse.businessUnitCode))
        .filter(w -> w.archivedAt == null)
        .toList();
    
    int otherTotalCapacity = otherWarehouses.stream().mapToInt(w -> w.capacity).sum();
    if (otherTotalCapacity + newWarehouse.capacity > location.maxCapacity) {
      throw new WebApplicationException("Maximum capacity reached for this location", 400);
    }

    newWarehouse.creationAt = existingWarehouse.creationAt;
    newWarehouse.archivedAt = existingWarehouse.archivedAt;
    warehouseStore.update(newWarehouse);
  }
}
