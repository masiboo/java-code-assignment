package com.fulfilment.application.monolith.stores;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class LegacyStoreManagerGatewayTest {

    @Inject
    LegacyStoreManagerGateway gateway;

    @Test
    public void testCreateStoreOnLegacySystem() {
        Store store = new Store("TEST_LEGACY_CREATE");
        store.quantityProductsInStock = 10;
        gateway.createStoreOnLegacySystem(store);
        // Method writes to temp file and deletes it, no exception means success
    }

    @Test
    public void testUpdateStoreOnLegacySystem() {
        Store store = new Store("TEST_LEGACY_UPDATE");
        store.quantityProductsInStock = 20;
        gateway.updateStoreOnLegacySystem(store);
        // Method writes to temp file and deletes it, no exception means success
    }
}
