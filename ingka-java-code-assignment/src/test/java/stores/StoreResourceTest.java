package stores;

import com.fulfilment.application.monolith.stores.LegacyStoreManagerGateway;
import com.fulfilment.application.monolith.stores.Store;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class StoreResourceTest {

    @InjectMock
    LegacyStoreManagerGateway legacyStoreManagerGateway;

    @BeforeEach
    @Transactional
    void setup() {
        Store.deleteAll();
    }

    @Test
    public void testCreateStore() {
        Store store = new Store();
        store.name = "Test Store";
        store.quantityProductsInStock = 10;

        given()
          .contentType("application/json")
          .body(store)
          .when().post("/stores")
          .then()
             .statusCode(201)
             .body("name", is("Test Store"));
             
        org.mockito.Mockito.verify(legacyStoreManagerGateway).createStoreOnLegacySystem(org.mockito.ArgumentMatchers.any(Store.class));
    }

    @Test
    public void testGetStores() {
        given()
          .when().get("/stores")
          .then()
             .statusCode(200);
    }
}
