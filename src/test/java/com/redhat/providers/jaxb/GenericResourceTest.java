package com.redhat.providers.jaxb;

import com.redhat.providers.jaxb.resource.GenericResourceAbstractResource;
import com.redhat.providers.jaxb.resource.GenericResourceModel;
import com.redhat.providers.jaxb.resource.GenericResourceOtherAbstractResource;
import com.redhat.providers.jaxb.resource.GenericResourceResource;
import com.redhat.providers.jaxb.resource.GenericResourceResource2;
import com.redhat.utils.HttpResponseCodes;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter Jaxb provider
 * @tpChapter Integration tests
 * @tpTestCaseDetails Regression test for RESTEASY-1125. Jaxb message body reader not recognized when using generics in
 * complex inheritance structure
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class GenericResourceTest {

    String str = "<genericResourceModel></genericResourceModel>";

    static Client client;
    protected static final Logger logger = Logger.getLogger(GenericResourceTest.class.getName());

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(GenericResourceTest.class.getSimpleName());
        return TestUtil.finishContainerPrepare(war, null, GenericResourceResource.class, GenericResourceResource2.class,
                GenericResourceModel.class, GenericResourceOtherAbstractResource.class, GenericResourceAbstractResource.class);
    }

    @Before
    public void init() {
        client = ClientBuilder.newClient();
    }

    @After
    public void after() throws Exception {
        client.close();
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, GenericResourceTest.class.getSimpleName());
    }

    /**
     * @tpTestDetails Tests Jaxb object with resource using inheritance, generics and abstract classes
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGenericInheritingResource() throws Exception {
        WebTarget target = client.target(generateURL("/test"));
        Response response = target.request().post(Entity.entity(str, "application/xml"));
        logger.info("status: " + response.getStatus());
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        String answer = response.readEntity(String.class);
        Assert.assertEquals("The response from the server is not the expected one", "Success!", answer);
        logger.info(answer);
    }

    /**
     * @tpTestDetails Tests Jaxb object with resource using inheritance, generics and abstract classes
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGenericResource() throws Exception {
        WebTarget target = client.target(generateURL("/test2"));
        Response response = target.request().post(Entity.entity(str, "application/xml"));
        logger.info("status: " + response.getStatus());
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        String answer = response.readEntity(String.class);
        Assert.assertEquals("The response from the server is not the expected one", "Success!", answer);
        logger.info(answer);
    }
}
