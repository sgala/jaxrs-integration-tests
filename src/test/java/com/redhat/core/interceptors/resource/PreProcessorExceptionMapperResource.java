package com.redhat.core.interceptors.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/interception")
public class PreProcessorExceptionMapperResource {
    @GET
    @Produces("text/plain")
    public String get() {
        return "hello world";
    }
}
