package com.redhat.core.basic.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/resources")
public class ApplicationTestResourceB {
    @Path("b")
    @GET
    public String get() {
        return "b";
    }
}
