package com.fulfilment.application.monolith.location;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class LocationGatewayTestReal {

    @Inject
    LocationGateway gateway;

    @Test
    public void testResolveByIdentifier() {
        assertNotNull(gateway.resolveByIdentifier("ZWOLLE-001"));
        assertNull(gateway.resolveByIdentifier("NON-EXISTENT"));
    }
}
