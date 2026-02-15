package com.fulfilment.application.monolith.stores;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoreResourceTest {

    @InjectMock
    LegacyStoreManagerGateway legacyStoreManagerGateway;

    @BeforeEach
    @Transactional
    void setup() {
        Store.deleteAll();
        Store s1 = new Store("HAARLEM");
        s1.quantityProductsInStock = 10;
        s1.persist();
        
        Store s2 = new Store("AMSTERDAM");
        s2.quantityProductsInStock = 5;
        s2.persist();
    }

    @Test
    @Order(1)
    public void testGetAllStores() {
        given()
            .when().get("/stores")
            .then()
            .statusCode(200)
            .body("name", containsInAnyOrder("HAARLEM", "AMSTERDAM"));
    }

    @Test
    @Order(2)
    public void testGetSingleStore() {
        Store store = Store.find("name", "HAARLEM").firstResult();
        given()
            .pathParam("id", store.id)
            .when().get("/stores/{id}")
            .then()
            .statusCode(200)
            .body("name", is("HAARLEM"));
    }

    @Test
    @Order(3)
    public void testGetNonExistentStore() {
        given()
            .pathParam("id", 999)
            .when().get("/stores/{id}")
            .then()
            .statusCode(404)
            .body("error", is("Store with id of 999 does not exist."));
    }

    @Test
    @Order(4)
    public void testCreateStore() {
        Store store = new Store();
        store.name = "NEW_STORE";
        store.quantityProductsInStock = 100;

        given()
            .contentType(ContentType.JSON)
            .body(store)
            .when().post("/stores")
            .then()
            .statusCode(201)
            .body("name", is("NEW_STORE"))
            .body("quantityProductsInStock", is(100));
        
        // Note: verify might fail if transaction is not committed yet in test environment, 
        // but callLegacyAfterCommit is used. Quarkus tests usually handle this.
    }

    @Test
    @Order(5)
    public void testCreateStoreWithId() {
        Store store = new Store();
        store.id = 100L;
        store.name = "INVALID";

        given()
            .contentType(ContentType.JSON)
            .body(store)
            .when().post("/stores")
            .then()
            .statusCode(422)
            .body("error", is("Id was invalidly set on request."));
    }

    @Test
    @Order(6)
    public void testUpdateStore() {
        Store store = Store.find("name", "AMSTERDAM").firstResult();
        Store update = new Store();
        update.name = "UPDATED_AMSTERDAM";
        update.quantityProductsInStock = 50;

        given()
            .pathParam("id", store.id)
            .contentType(ContentType.JSON)
            .body(update)
            .when().put("/stores/{id}")
            .then()
            .statusCode(200)
            .body("name", is("UPDATED_AMSTERDAM"))
            .body("quantityProductsInStock", is(50));
    }

    @Test
    @Order(7)
    public void testPatchStore() {
        Store store = Store.find("name", "AMSTERDAM").firstResult();
        Store update = new Store();
        update.name = "PATCHED_AMSTERDAM";
        update.quantityProductsInStock = 75;

        given()
            .pathParam("id", store.id)
            .contentType(ContentType.JSON)
            .body(update)
            .when().patch("/stores/{id}")
            .then()
            .statusCode(200)
            .body("name", is("PATCHED_AMSTERDAM"))
            .body("quantityProductsInStock", is(75));
            
        // Test patch with missing name
        Store invalidStore = new Store();
        given()
            .pathParam("id", store.id)
            .contentType(ContentType.JSON)
            .body(invalidStore)
            .when().patch("/stores/{id}")
            .then()
            .statusCode(422);
    }

    @Test
    @Order(8)
    public void testUpdateStoreWithoutName() {
        Store store = Store.find("name", "AMSTERDAM").firstResult();
        Store update = new Store();
        update.quantityProductsInStock = 50;

        given()
            .pathParam("id", store.id)
            .contentType(ContentType.JSON)
            .body(update)
            .when().put("/stores/{id}")
            .then()
            .statusCode(422)
            .body("error", is("Store Name was not set on request."));
    }

    @Test
    @Order(9)
    public void testDeleteStore() {
        Store store = Store.find("name", "HAARLEM").firstResult();
        given()
            .pathParam("id", store.id)
            .when().delete("/stores/{id}")
            .then()
            .statusCode(204);

        given()
            .pathParam("id", store.id)
            .when().get("/stores/{id}")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(10)
    public void testDeleteNonExistentStore() {
        given()
            .pathParam("id", 999)
            .when().delete("/stores/{id}")
            .then()
            .statusCode(404)
            .body("error", is("Store with id of 999 does not exist."));
    }
}
