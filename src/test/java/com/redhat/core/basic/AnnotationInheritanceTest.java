package com.redhat.core.basic;

import com.redhat.core.basic.resources.AnnotationInheritanceNotAResource;
import com.redhat.core.basic.resources.AnnotationInheritanceSomeOtherInterface;
import com.redhat.core.basic.resources.AnnotationInheritanceSomeOtherResource;
import com.redhat.core.basic.resources.AnnotationInheritanceSuperInt;
import com.redhat.core.basic.resources.AnnotationInheritanceSuperIntAbstract;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @tpSubChapter Configuration
 * @tpChapter Integration tests
 * @tpTestCaseDetails Test for resource without @Path annotation.
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class AnnotationInheritanceTest {
    static ResteasyClient client;

    @Deployment
    public static Archive<?> deploySimpleResource() {
        WebArchive war = TestUtil.prepareArchive(AnnotationInheritanceTest.class.getSimpleName());
        war.addClasses(AnnotationInheritanceSuperInt.class, AnnotationInheritanceSuperIntAbstract.class,
                AnnotationInheritanceNotAResource.class, AnnotationInheritanceSomeOtherInterface.class);
        return TestUtil.finishContainerPrepare(war, null, AnnotationInheritanceSomeOtherResource.class);
    }

    @Before
    public void init() {
        client = new ResteasyClientBuilder().build();
    }

    @After
    public void after() throws Exception {
        client.close();
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, AnnotationInheritanceTest.class.getSimpleName());
    }

    /**
     * @tpTestDetails Check wrong resource without @Path annotation
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testDetectionOfNonResource() {
        try {
            AnnotationInheritanceSomeOtherInterface proxy = client.target(generateURL("/somewhere")).proxy(AnnotationInheritanceSomeOtherInterface.class);
            proxy.getFailure().blah();
            Assert.fail();
        } catch (Exception e) {
            // exception thrown
        }
    }
}
