package com.redhat.client.proxy.resource;

public class GenericProxyResource implements GenericProxySpecificProxy {
    @Override
    public String sayHi(String in) {
        return in;
    }
}
