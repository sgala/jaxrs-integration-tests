package com.redhat.resource.param.resource;

import org.jboss.resteasy.annotations.Query;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("search")
public class QueryResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String get(@Query QuerySearchQuery searchQuery) {
        return searchQuery.toString();
    }
}
