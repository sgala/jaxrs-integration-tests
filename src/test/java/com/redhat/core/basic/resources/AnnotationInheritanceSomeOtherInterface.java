package com.redhat.core.basic.resources;

import javax.ws.rs.Path;

public interface AnnotationInheritanceSomeOtherInterface {
    @Path("superint")
    AnnotationInheritanceSuperInt getSuperInt();

    @Path("failure")
    AnnotationInheritanceNotAResource getFailure();
}
