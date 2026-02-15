package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import com.fulfilment.application.monolith.warehouses.api.WarehouseResource;
import com.fulfilment.application.monolith.warehouses.api.beans.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.ArchiveWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;

@jakarta.enterprise.context.ApplicationScoped
public class WarehouseResourceImpl implements WarehouseResource {

  @Inject
  WarehouseStore warehouseStore;
  @Inject
  CreateWarehouseOperation createWarehouseOperation;
  @Inject
  ArchiveWarehouseOperation archiveWarehouseOperation;

  @Override
  public List<Warehouse> listAllWarehousesUnits() {
    return warehouseStore.listByLocation(null).stream().map(domainModel -> {
      Warehouse apiModel = new Warehouse();
      apiModel.setId(domainModel.businessUnitCode);
      apiModel.setLocation(domainModel.location);
      apiModel.setCapacity(domainModel.capacity);
      apiModel.setStock(domainModel.stock);
      return apiModel;
    }).collect(java.util.stream.Collectors.toList());
  }

  @Override
  @jakarta.transaction.Transactional
  public Warehouse createANewWarehouseUnit(@NotNull Warehouse data) {
    com.fulfilment.application.monolith.warehouses.domain.models.Warehouse domainModel = new com.fulfilment.application.
            monolith.warehouses.domain.models.Warehouse();
    domainModel.businessUnitCode = data.getId();
    domainModel.location = data.getLocation();
    domainModel.capacity = data.getCapacity();
    domainModel.stock = data.getStock();
    
    createWarehouseOperation.create(domainModel);
    return data;
  }

  @Override
  public Warehouse getAWarehouseUnitByID(String id) {
    com.fulfilment.application.monolith.warehouses.domain.models.Warehouse domainModel = warehouseStore.findByBusinessUnitCode(id);
    if (domainModel == null) {
      throw new WebApplicationException(404);
    }
    Warehouse apiModel = new Warehouse();
    apiModel.setId(domainModel.businessUnitCode);
    apiModel.setLocation(domainModel.location);
    apiModel.setCapacity(domainModel.capacity);
    apiModel.setStock(domainModel.stock);
    return apiModel;
  }

  @Override
  @jakarta.transaction.Transactional
  public void archiveAWarehouseUnitByID(String id) {
    com.fulfilment.application.monolith.warehouses.domain.models.Warehouse domainModel = new com.fulfilment.application.
            monolith.warehouses.domain.models.Warehouse();
    domainModel.businessUnitCode = id;
    archiveWarehouseOperation.archive(domainModel);
  }
}
