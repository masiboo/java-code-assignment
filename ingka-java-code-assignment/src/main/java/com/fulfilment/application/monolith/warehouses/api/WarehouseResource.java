package com.fulfilment.application.monolith.warehouses.api;

import com.fulfilment.application.monolith.warehouses.api.beans.Warehouse;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;

import java.util.List;


@Path("/warehouse")
public interface WarehouseResource {
  @GET
  @Produces("application/json")
  List<Warehouse> listAllWarehousesUnits();

  @POST
  @Produces("application/json")
  @Consumes("application/json")
  Warehouse createANewWarehouseUnit(@NotNull Warehouse data);

  @Path("/{id}")
  @GET
  @Produces("application/json")
  Warehouse getAWarehouseUnitByID(@PathParam("id") String id);

  @Path("/{id}")
  @DELETE
  void archiveAWarehouseUnitByID(@PathParam("id") String id);
}
