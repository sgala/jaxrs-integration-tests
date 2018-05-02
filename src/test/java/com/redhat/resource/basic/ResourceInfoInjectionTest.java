package com.redhat.resource.basic;

import com.redhat.resource.basic.resource.ResourceInfoInjectionFilter;
import com.redhat.resource.basic.resource.ResourceInfoInjectionResource;
import com.redhat.utils.HttpResponseCodes;
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
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter Resources
 * @tpChapter Integration tests
 * @tpTestCaseDetails Regression test for JBEAP-4701
 * @tpSince RESTEasy 3.0.17
 */
@RunWith(Arquillian.class)
@RunAsClient
@Category(com.redhat.utils.RCategory.class)
public class ResourceInfoInjectionTest {
    protected static Client client;

    @BeforeClass
    public static void init() {
        client = ClientBuilder.newClient();
    }

    @AfterClass
    public static void close() {
        client.close();
    }

    private static String generateURL(String path) {
        return PortProviderUtil.generateURL(path, ResourceInfoInjectionTest.class.getSimpleName());
    }

    @Deployment
    public static Archive<?> deployUriInfoSimpleResource() {
        WebArchive war = TestUtil.prepareArchive(ResourceInfoInjectionTest.class.getSimpleName());
        return TestUtil.finishContainerPrepare(war, null, ResourceInfoInjectionFilter.class,
                ResourceInfoInjectionResource.class);
    }

    /**
     * @tpTestDetails Check for injecting ResourceInfo object in ContainerResponseFilter
     * @tpSince RESTEasy 3.0.17
     */
    @Test
    public void testNotFound() throws Exception {
        WebTarget target = client.target(generateURL("/bogus"));
        Response response = target.request().get();
        String entity = response.readEntity(String.class);
        Assert.assertEquals("ResponseFilter was probably not applied to response", HttpResponseCodes.SC_NOT_FOUND * 2, response.getStatus());
        Assert.assertEquals("Wrong body of response", "", entity);
    }
}
