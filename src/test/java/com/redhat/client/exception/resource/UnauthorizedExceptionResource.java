package com.redhat.client.exception.resource;


import com.redhat.utils.HttpResponseCodes;

import javax.ws.rs.WebApplicationException;

public class UnauthorizedExceptionResource implements UnauthorizedExceptionInterface {
    public void postIt(String msg) {
        throw new WebApplicationException(HttpResponseCodes.SC_UNAUTHORIZED);
    }
}
