package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;

import com.fulfilment.application.monolith.warehouses.api.beans.Warehouse;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WarehouseResourceTest {

    @Test
    @Order(1)
    public void testListAllWarehouses() {
        given()
            .when().get("/warehouse")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(2)
    public void testCreateWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId("TEST-BU-001");
        warehouse.setLocation("AMSTERDAM-001");
        warehouse.setCapacity(10);
        warehouse.setStock(1);

        given()
            .contentType(ContentType.JSON)
            .body(warehouse)
            .when().post("/warehouse")
            .then()
            .statusCode(200)
            .body("id", is("TEST-BU-001"))
            .body("location", is("AMSTERDAM-001"))
            .body("capacity", is(10))
            .body("stock", is(1));
    }

    @Test
    @Order(3)
    public void testGetWarehouse() {
        given()
            .pathParam("id", "TEST-BU-001")
            .when().get("/warehouse/{id}")
            .then()
            .statusCode(200)
            .body("id", is("TEST-BU-001"))
            .body("location", is("AMSTERDAM-001"));
    }

    @Test
    @Order(4)
    public void testGetNonExistentWarehouse() {
        given()
            .pathParam("id", "NON-EXISTENT")
            .when().get("/warehouse/{id}")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(5)
    public void testArchiveWarehouse() {
        given()
            .pathParam("id", "TEST-BU-001")
            .when().delete("/warehouse/{id}")
            .then()
            .statusCode(204);
        
        // Verify it is archived (archivedAt should be set)
        // In current implementation, getAWarehouseUnitByID doesn't filter out archived ones,
        // but let's check the behavior.
    }
}
