package com.redhat.cdi.generic.resource;

import java.lang.reflect.Type;

public class HierarchyHolder<T> {
    private Class<T> clazz;

    public HierarchyHolder(final Class<T> clazz) {
        this.clazz = clazz;
    }

    public Type getTypeArgument() {
        return clazz;
    }
}
