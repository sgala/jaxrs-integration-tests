package com.redhat.resource.basic.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class MediaTypeNegotiationClientResource {

    @Path("/test1/{id}.xml.{lang}")
    @GET
    public String getComplex() {
        return "complex";
    }

}
