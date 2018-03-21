package com.redhat.core.basic.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/resources")
public class ApplicationTestResourceA {
    @Path("a")
    @GET
    public String get() {
        return "a";
    }
}
