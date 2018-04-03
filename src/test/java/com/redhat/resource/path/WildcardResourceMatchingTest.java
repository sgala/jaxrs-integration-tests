package com.redhat.resource.path;

import com.redhat.resource.path.resource.WildcardMatchingResource;
import com.redhat.resource.path.resource.WildcardMatchingSubResource;
import com.redhat.resource.path.resource.WildcardMatchingSubSubResource;
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
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter Resource
 * @tpChapter Integration tests
 * @tpTestCaseDetails Check class name of sub-resources, which process client request
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class WildcardResourceMatchingTest {

    static Client client;

    @Deployment(name = "UriInfoSimpleResource")
    public static Archive<?> deployUriInfoSimpleResource() {
        WebArchive war = TestUtil.prepareArchive(WildcardResourceMatchingTest.class.getSimpleName());
        return TestUtil.finishContainerPrepare(war, null, WildcardMatchingResource.class,
                 WildcardMatchingSubResource.class, WildcardMatchingSubSubResource.class);
    }

    @BeforeClass
    public static void setup() {
        client = ClientBuilder.newClient();
    }

    @AfterClass
    public static void cleanup() {
        client.close();
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, WildcardResourceMatchingTest.class.getSimpleName());
    }

    /**
     * @tpTestDetails Check root resource.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testMain() {
        Response response = client.target(generateURL("/main")).request().get();
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertEquals("WildcardMatchingResource", response.readEntity(String.class));
        response.close();
    }

    /**
     * @tpTestDetails Check sub-resource.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testMainSub() {
        Response response = client.target(generateURL("/main/sub")).request().get();
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertEquals("WildcardMatchingSubResource", response.readEntity(String.class));
        response.close();
    }

    /**
     * @tpTestDetails Check sub-sub-resource.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testMainSubSub() {
        Response response = client.target(generateURL("/main/sub/sub")).request().get();
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertEquals("WildcardMatchingSubSubResource", response.readEntity(String.class));
        response.close();
    }

}