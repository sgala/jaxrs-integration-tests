package com.redhat.client.proxy.resource;

import com.redhat.client.proxy.DefaultMediaTypesTest;
import org.jboss.resteasy.spi.HttpRequest;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("foo")
public class DefaultMediaTypesResource implements DefaultMediaTypesTest.Foo {
    @Context
    HttpRequest request;

    @Override
    public String getFoo() {
        return request.getHttpHeaders().getAcceptableMediaTypes().toString();
    }

    @Override
    public String setFoo(String value) {
        return request.getHttpHeaders().getMediaType().toString();
    }
}
