package com.redhat.cdi.basic.resource;

import javax.ws.rs.GET;

public interface SingletonLocalIF {
    @GET
    String get();
}
