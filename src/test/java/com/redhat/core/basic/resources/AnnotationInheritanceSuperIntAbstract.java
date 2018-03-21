package com.redhat.core.basic.resources;

public abstract class AnnotationInheritanceSuperIntAbstract implements AnnotationInheritanceSuperInt {
    @Override
    public String getFoo() {
        return "Foo: " + getName();
    }

    protected abstract String getName();
}
