package com.redhat.cdi.basic;

import com.redhat.cdi.basic.resource.EjbExceptionUnwrapFooException;
import com.redhat.cdi.basic.resource.EjbExceptionUnwrapFooExceptionMapper;
import com.redhat.cdi.basic.resource.EjbExceptionUnwrapFooResource;
import com.redhat.cdi.basic.resource.EjbExceptionUnwrapFooResourceBean;
import com.redhat.cdi.basic.resource.EjbExceptionUnwrapLocatingResource;
import com.redhat.cdi.basic.resource.EjbExceptionUnwrapLocatingResourceBean;
import com.redhat.cdi.basic.resource.EjbExceptionUnwrapSimpleResource;
import com.redhat.cdi.basic.resource.EjbExceptionUnwrapSimpleResourceBean;
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
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter CDI
 * @tpChapter Integration tests
 * @tpTestCaseDetails Test for unwrapping EJB exception
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class EjbExceptionUnwrapTest {

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
        WebArchive war = TestUtil.prepareArchive(EjbExceptionUnwrapTest.class.getSimpleName());
        war.addClasses(EjbExceptionUnwrapFooException.class, EjbExceptionUnwrapFooResource.class,
                EjbExceptionUnwrapLocatingResource.class, EjbExceptionUnwrapSimpleResource.class);
        return TestUtil.finishContainerPrepare(war, null, EjbExceptionUnwrapFooExceptionMapper.class,
                EjbExceptionUnwrapSimpleResourceBean.class,
                EjbExceptionUnwrapLocatingResourceBean.class, EjbExceptionUnwrapFooResourceBean.class);
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, EjbExceptionUnwrapTest.class.getSimpleName());
    }

    /**
     * @tpTestDetails No default resource for exception
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testNoDefaultsResourceForException() {
        Response response = client.target(generateURL("/exception")).request().get();
        Assert.assertEquals(HttpResponseCodes.SC_CONFLICT, response.getStatus());
        response.close();
    }

    /**
     * @tpTestDetails No default resource without exception mapping
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testNoDefaultsResource() throws Exception {
        {
            Response response = client.target(generateURL("/basic")).request().get();
            Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
            response.close();
        }
        {
            Response response = client.target(generateURL("/basic")).request().put(Entity.entity("basic", "text/plain"));
            Assert.assertEquals(HttpResponseCodes.SC_NO_CONTENT, response.getStatus());
            response.close();
        }
        {
            Response response = client.target(generateURL("/queryParam")).queryParam("param", "hello world").request().get();
            Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
            Assert.assertEquals("hello world", response.readEntity(String.class));
            response.close();
        }
        {
            Response response = client.target(generateURL("/uriParam/1234")).request().get();
            Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
            Assert.assertEquals("1234", response.readEntity(String.class));
            response.close();
        }
    }
}
