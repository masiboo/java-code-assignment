package com.fulfilment.application.monolith.products;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
class ProductResourceIT {

    @Test
    void getAllProducts() {
        given()
                .when().get("/product")
                .then()
                .statusCode(200)
                .body("$.size()", greaterThan(0));
    }

    @Test
    void deleteExistingProduct() {
        // First create a product to ensure we have a valid ID to delete
        Product product = new Product();
        product.name = "Test Product";
        product.description = "Test Description";
        product.price = BigDecimal.valueOf(10.0);
        product.stock = 100;

        Integer id = given()
                .contentType("application/json")
                .body(product)
                .when().post("/product")
                .then()
                .statusCode(201)
                .extract().path("id");

        given()
                .when().delete("/product/" + id)
                .then()
                .statusCode(204);
    }

    @Test
    void deleteNonExistingProduct() {
        given()
                .when().delete("/product/9999")
                .then()
                .statusCode(404);
    }
}
