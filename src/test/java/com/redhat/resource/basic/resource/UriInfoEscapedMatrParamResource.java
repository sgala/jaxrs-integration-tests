package com.redhat.resource.basic.resource;

import org.junit.Assert;

import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;

@Path("/queryEscapedMatrParam")
public class UriInfoEscapedMatrParamResource {
    private static final String ERROR_MSG = "Wrong parameter";

    @GET
    public String doGet(@MatrixParam("a") String a, @MatrixParam("b") String b, @MatrixParam("c") String c, @MatrixParam("d") String d) {
        return "content";
    }
}
