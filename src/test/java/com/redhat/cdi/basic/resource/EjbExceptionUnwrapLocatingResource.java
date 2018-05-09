package com.redhat.cdi.basic.resource;

import javax.ws.rs.Path;

@Path("/")
public interface EjbExceptionUnwrapLocatingResource {
    @Path("locating")
    EjbExceptionUnwrapSimpleResource getLocating();
}
