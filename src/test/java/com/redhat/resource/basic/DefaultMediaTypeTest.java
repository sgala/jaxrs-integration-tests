package com.redhat.resource.basic;

import com.redhat.resource.basic.resource.DefaultMediaTypeCustomObject;
import com.redhat.resource.basic.resource.DefaultMediaTypeResource;
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
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;

/**
 * @tpSubChapter Resources
 * @tpChapter Integration tests
 * @tpTestCaseDetails Regression test for JBEAP-2847. DefaultTextPlain should be used, if produce annotation annotation is not used in end-point.
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class DefaultMediaTypeTest {

    protected final Logger logger = Logger.getLogger(DefaultMediaTypeResource.class.getName());

    static Client client;

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(DefaultMediaTypeResource.class.getSimpleName());
        war.addClass(DefaultMediaTypeCustomObject.class);
        return TestUtil.finishContainerPrepare(war, null, DefaultMediaTypeResource.class);
    }

    @Before
    public void init() {
        client = ClientBuilder.newClient();
    }

    @After
    public void after() throws Exception {
        client.close();
        client = null;
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, DefaultMediaTypeResource.class.getSimpleName());
    }

    /**
     * @tpTestDetails Test Date object with produce annotation
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void postDateProduce() throws Exception {
        WebTarget target = client.target(generateURL("/postDateProduce"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream(5000);
        for (int i = 0; i < 5000; i++) {
            baos.write(i);
        }
        Response response = target.request().post(Entity.entity(baos.toByteArray(), MediaType.APPLICATION_OCTET_STREAM));
        String responseContent = response.readEntity(String.class);
        logger.info(String.format("Response: %s", responseContent));
    }

    /**
     * @tpTestDetails Test Foo object with produce annotation
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void postFooProduce() throws Exception {
        WebTarget target = client.target(generateURL("/postFooProduce"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream(5000);
        for (int i = 0; i < 5000; i++) {
            baos.write(i);
        }
        Response response = target.request().post(Entity.entity(baos.toByteArray(), MediaType.APPLICATION_OCTET_STREAM));
        String responseContent = response.readEntity(String.class);
        logger.info(String.format("Response: %s", responseContent));
    }

    /**
     * @tpTestDetails Test int primitive with produce annotation
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void postIntProduce() throws Exception {
        WebTarget target = client.target(generateURL("/postIntProduce"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream(5000);
        for (int i = 0; i < 5000; i++) {
            baos.write(i);
        }
        Response response = target.request().post(Entity.entity(baos.toByteArray(), MediaType.APPLICATION_OCTET_STREAM));
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        String responseContent = response.readEntity(String.class);
        logger.info(String.format("Response: %s", responseContent));
    }

    /**
     * @tpTestDetails Test int primitive without produce annotation
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void postInt() throws Exception {
        WebTarget target = client.target(generateURL("/postInt"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream(5000);
        for (int i = 0; i < 5000; i++) {
            baos.write(i);
        }
        Response response = target.request().post(Entity.entity(baos.toByteArray(), MediaType.APPLICATION_OCTET_STREAM));
        Assert.assertEquals(TestUtil.getErrorMessageForKnownIssue("JBEAP-2847"), HttpResponseCodes.SC_OK, response.getStatus());
        String responseContent = response.readEntity(String.class);
        logger.info(String.format("Response: %s", responseContent));
    }

    /**
     * @tpTestDetails Test Integer object with produce annotation
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void postIntegerProduce() throws Exception {
        WebTarget target = client.target(generateURL("/postIntegerProduce"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream(5000);
        for (int i = 0; i < 5000; i++) {
            baos.write(i);
        }
        Response response = target.request().post(Entity.entity(baos.toByteArray(), MediaType.APPLICATION_OCTET_STREAM));
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        String responseContent = response.readEntity(String.class);
        logger.info(String.format("Response: %s", responseContent));
    }

    /**
     * @tpTestDetails Test Integer object without produce annotation
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void postInteger() throws Exception {
        WebTarget target = client.target(generateURL("/postInteger"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream(5000);
        for (int i = 0; i < 5000; i++) {
            baos.write(i);
        }
        Response response = target.request().post(Entity.entity(baos.toByteArray(), MediaType.APPLICATION_OCTET_STREAM));
        Assert.assertEquals(TestUtil.getErrorMessageForKnownIssue("JBEAP-2847"), HttpResponseCodes.SC_OK, response.getStatus());
        String responseContent = response.readEntity(String.class);
        logger.info(String.format("Response: %s", responseContent));
    }
}
