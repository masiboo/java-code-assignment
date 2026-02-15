package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  @Override
  public void create(Warehouse warehouse) {
    persist(toDb(warehouse));
  }

  @Override
  public void update(Warehouse warehouse) {
    DbWarehouse entity = find("businessUnitCode", warehouse.businessUnitCode).firstResult();
    if (entity != null) {
      entity.location = warehouse.location;
      entity.capacity = warehouse.capacity;
      entity.stock = warehouse.stock;
      entity.createdAt = warehouse.creationAt != null ? warehouse.creationAt.toLocalDateTime() : null;
      entity.archivedAt = warehouse.archivedAt != null ? warehouse.archivedAt.toLocalDateTime() : null;
    }
  }

  @Override
  public void remove(Warehouse warehouse) {
    delete("businessUnitCode", warehouse.businessUnitCode);
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    DbWarehouse entity = find("businessUnitCode", buCode).firstResult();
    return entity != null ? fromDb(entity) : null;
  }

  @Override
  public List<Warehouse> listByLocation(String location) {
    return list("location", location).stream()
        .map(this::fromDb)
        .collect(Collectors.toList());
  }

  private DbWarehouse toDb(Warehouse warehouse) {
    DbWarehouse db = new DbWarehouse();
    db.businessUnitCode = warehouse.businessUnitCode;
    db.location = warehouse.location;
    db.capacity = warehouse.capacity;
    db.stock = warehouse.stock;
    db.createdAt = warehouse.creationAt != null ? warehouse.creationAt.toLocalDateTime() : null;
    db.archivedAt = warehouse.archivedAt != null ? warehouse.archivedAt.toLocalDateTime() : null;
    return db;
  }

  private Warehouse fromDb(DbWarehouse db) {
    Warehouse w = new Warehouse();
    w.businessUnitCode = db.businessUnitCode;
    w.location = db.location;
    w.capacity = db.capacity;
    w.stock = db.stock;
    w.creationAt = db.createdAt != null ? db.createdAt.atZone(ZoneId.systemDefault()) : null;
    w.archivedAt = db.archivedAt != null ? db.archivedAt.atZone(ZoneId.systemDefault()) : null;
    return w;
  }
}
