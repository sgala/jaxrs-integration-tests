package com.redhat.client.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface GenericReturnTypeInterface<T> {
    @GET
    @Path("t")
    T t();
}
