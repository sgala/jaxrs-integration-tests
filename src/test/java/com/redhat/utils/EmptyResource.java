package com.redhat.utils;

import javax.ws.rs.Path;



@Path("empty")
public class EmptyResource {

    @Path("resource")
    public void empty() {

    }

}
