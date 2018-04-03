package com.redhat.resource.path;

import com.redhat.resource.path.resource.LocatorWithClassHierarchyLocatorResource;
import com.redhat.resource.path.resource.LocatorWithClassHierarchyMiddleResource;
import com.redhat.resource.path.resource.LocatorWithClassHierarchyParamEntityPrototype;
import com.redhat.resource.path.resource.LocatorWithClassHierarchyParamEntityWithConstructor;
import com.redhat.resource.path.resource.LocatorWithClassHierarchyPathParamResource;
import com.redhat.resource.path.resource.LocatorWithClassHierarchyPathSegmentImpl;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter Resteasy-client
 * @tpChapter Integration tests
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class LocatorWithClassHierarchyTest {

    static Client client;

    @BeforeClass
    public static void setup() {
        client = ClientBuilder.newClient();
    }

    @AfterClass
    public static void close() {
        client.close();
    }

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(LocatorWithClassHierarchyTest.class.getSimpleName());
        war.addClasses(LocatorWithClassHierarchyPathSegmentImpl.class, LocatorWithClassHierarchyMiddleResource.class,
                LocatorWithClassHierarchyPathParamResource.class, LocatorWithClassHierarchyParamEntityWithConstructor.class,
                LocatorWithClassHierarchyParamEntityPrototype.class);
        return TestUtil.finishContainerPrepare(war, null, LocatorWithClassHierarchyLocatorResource.class);
    }

    /**
     * @tpTestDetails Client sends POST request with null entity for the resource Locator, which creates the targeted
     * resource object.
     * @tpPassCrit Correct response is returned from the server
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testLocatorWithSubWithPathAnnotation() {
        Response response = client.target(PortProviderUtil.generateURL("/resource/locator/ParamEntityWithConstructor/ParamEntityWithConstructor=JAXRS", LocatorWithClassHierarchyTest.class.getSimpleName())).request().post(null);
        Assert.assertEquals(200, response.getStatus());
        response.close();
    }
}
