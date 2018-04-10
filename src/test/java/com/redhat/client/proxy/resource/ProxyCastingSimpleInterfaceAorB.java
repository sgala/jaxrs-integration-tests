package com.redhat.client.proxy.resource;

public interface ProxyCastingSimpleInterfaceAorB {
    <T> T as(Class<T> iface);
}
