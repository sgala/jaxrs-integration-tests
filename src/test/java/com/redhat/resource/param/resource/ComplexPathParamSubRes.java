package com.redhat.resource.param.resource;

import javax.ws.rs.GET;

public class ComplexPathParamSubRes {
    @GET
    public String get() {
        return "sub1";
    }
}
