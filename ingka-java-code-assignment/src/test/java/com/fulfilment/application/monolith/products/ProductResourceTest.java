package com.fulfilment.application.monolith.products;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductResourceTest {

    @Test
    @Order(1)
    public void testGetAllProducts() {
        given()
            .when().get("/product")
            .then()
            .statusCode(200);
    }

    @Test
    @Order(2)
    public void testGetSingleProduct() {
        // Create a product first to ensure it exists
        Product product = new Product();
        product.name = "FIND_ME";
        product.stock = 10;
        
        Long id = ((Number) given()
            .contentType(ContentType.JSON)
            .body(product)
            .when().post("/product")
            .then()
            .statusCode(201)
            .extract().path("id")).longValue();

        given()
            .pathParam("id", id)
            .when().get("/product/{id}")
            .then()
            .statusCode(200)
            .body("name", is("FIND_ME"));
    }

    @Test
    @Order(3)
    public void testGetNonExistentProduct() {
        given()
            .pathParam("id", 999)
            .when().get("/product/{id}")
            .then()
            .statusCode(404)
            .body("error", is("Product with id of 999 does not exist."));
    }

    @Test
    @Order(4)
    public void testCreateProduct() {
        Product product = new Product();
        product.name = "NEW_PRODUCT";
        product.description = "Description";
        product.price = new BigDecimal("19.99");
        product.stock = 100;

        given()
            .contentType(ContentType.JSON)
            .body(product)
            .when().post("/product")
            .then()
            .statusCode(201)
            .body("name", is("NEW_PRODUCT"))
            .body("description", is("Description"))
            .body("price", is(19.99f))
            .body("stock", is(100));
    }

    @Test
    @Order(5)
    public void testCreateProductWithId() {
        Product product = new Product();
        product.id = 100L;
        product.name = "INVALID";

        given()
            .contentType(ContentType.JSON)
            .body(product)
            .when().post("/product")
            .then()
            .statusCode(422)
            .body("error", is("Id was invalidly set on request."));
    }

    @Test
    @Order(6)
    public void testUpdateProduct() {
        Product product = new Product();
        product.name = "UPDATED_KALLAX";
        product.description = "Updated Description";
        product.price = new BigDecimal("29.99");
        product.stock = 50;

        given()
            .pathParam("id", 2)
            .contentType(ContentType.JSON)
            .body(product)
            .when().put("/product/{id}")
            .then()
            .statusCode(200)
            .body("name", is("UPDATED_KALLAX"))
            .body("description", is("Updated Description"))
            .body("price", is(29.99f))
            .body("stock", is(50));
    }

    @Test
    @Order(7)
    public void testUpdateProductWithoutName() {
        Product product = new Product();
        product.description = "No Name";

        given()
            .pathParam("id", 2)
            .contentType(ContentType.JSON)
            .body(product)
            .when().put("/product/{id}")
            .then()
            .statusCode(422)
            .body("error", is("Product Name was not set on request."));
    }

    @Test
    @Order(8)
    public void testUpdateNonExistentProduct() {
        Product product = new Product();
        product.name = "NON_EXISTENT";

        given()
            .pathParam("id", 999)
            .contentType(ContentType.JSON)
            .body(product)
            .when().put("/product/{id}")
            .then()
            .statusCode(404)
            .body("error", is("Product with id of 999 does not exist."));
    }

    @Test
    @Order(9)
    public void testDeleteProduct() {
        given()
            .pathParam("id", 3)
            .when().delete("/product/{id}")
            .then()
            .statusCode(204);

        given()
            .pathParam("id", 3)
            .when().get("/product/{id}")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(10)
    public void testDeleteNonExistentProduct() {
        given()
            .pathParam("id", 999)
            .when().delete("/product/{id}")
            .then()
            .statusCode(404)
            .body("error", is("Product with id of 999 does not exist."));
    }
}
