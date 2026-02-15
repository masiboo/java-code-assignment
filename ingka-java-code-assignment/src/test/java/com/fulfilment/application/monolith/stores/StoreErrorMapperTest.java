package com.fulfilment.application.monolith.stores;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class StoreErrorMapperTest {

    @Inject
    StoreResource.ErrorMapper errorMapper;

    @Test
    public void testToResponseWithWebApplicationException() {
        WebApplicationException ex = new WebApplicationException("Store Error", 404);
        Response response = errorMapper.toResponse(ex);
        assertEquals(404, response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    public void testToResponseWithGeneralException() {
        Exception ex = new RuntimeException("Unexpected Store Error");
        Response response = errorMapper.toResponse(ex);
        assertEquals(500, response.getStatus());
        assertNotNull(response.getEntity());
    }
}
