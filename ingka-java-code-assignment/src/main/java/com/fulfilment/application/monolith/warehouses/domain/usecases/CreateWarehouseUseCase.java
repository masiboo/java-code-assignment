package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;

import java.time.ZonedDateTime;
import java.util.List;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void create(Warehouse warehouse) {
    // Business Unit Code Verification
    if (warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode) != null) {
      throw new WebApplicationException("Warehouse with business unit code already exists", 409);
    }

    // Location Validation
    Location location = locationResolver.resolveByIdentifier(warehouse.location);
    if (location == null) {
      throw new WebApplicationException("Invalid location", 400);
    }

    // Warehouse Creation Feasibility
    List<Warehouse> existingWarehouses = warehouseStore.listByLocation(warehouse.location);
    long activeCount = existingWarehouses.stream().filter(w -> w.archivedAt == null).count();
    if (activeCount >= location.maxNumberOfWarehouses) {
      throw new WebApplicationException("Maximum number of warehouses reached for this location", 400);
    }

    // Capacity and Stock Validation
    int currentTotalCapacity = existingWarehouses.stream()
        .filter(w -> w.archivedAt == null)
        .mapToInt(w -> w.capacity)
        .sum();
    if (currentTotalCapacity + warehouse.capacity > location.maxCapacity) {
      throw new WebApplicationException("Maximum capacity reached for this location", 400);
    }

    if (warehouse.stock > warehouse.capacity) {
      throw new WebApplicationException("Stock informed exceeds warehouse capacity", 400);
    }

    warehouse.creationAt = ZonedDateTime.now();
    warehouseStore.create(warehouse);
  }
}
