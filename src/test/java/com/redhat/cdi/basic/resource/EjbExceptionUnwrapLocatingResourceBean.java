package com.redhat.cdi.basic.resource;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class EjbExceptionUnwrapLocatingResourceBean implements EjbExceptionUnwrapLocatingResource {
    @EJB
    EjbExceptionUnwrapSimpleResource simple;

    public EjbExceptionUnwrapSimpleResource getLocating() {
        return simple;
    }
}
