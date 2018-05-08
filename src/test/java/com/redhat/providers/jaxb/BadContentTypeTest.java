package com.redhat.providers.jaxb;

import com.redhat.providers.jaxb.resource.BadContenTypeTestResource;
import com.redhat.providers.jaxb.resource.BadContentTypeTestBean;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @tpSubChapter Jaxb provider
 * @tpChapter Integration tests
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class BadContentTypeTest {

    private static Logger logger = Logger.getLogger(BadContentTypeTest.class.getName());
    static Client client;

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(BadContentTypeTest.class.getSimpleName());
        return TestUtil.finishContainerPrepare(war, null, BadContenTypeTestResource.class, BadContentTypeTestBean.class);
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
        return PortProviderUtil.generateURL(path, BadContentTypeTest.class.getSimpleName());
    }

    /**
     * @tpTestDetails Tests if correct Response code is returned when sending syntactically incorrect xml
     * @tpInfo RESTEASY-519
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testBadRequest() throws Exception {
        WebTarget target = client.target(generateURL("/test"));
        Response response = target.request().post(Entity.entity("<junk", "application/xml"));
        assertEquals("The returned response status is not the expected one",
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    /**
     * @tpTestDetails Tests if correct exception and MessageBodyWriter error is thrown when sending request for which no
     * MessageBodyWriterExists
     * @tpInfo RESTEASY-169
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testHtmlError() throws Exception {
        WebTarget target = client.target(generateURL("/test"));
        Response response = target.request().header("Accept", "text/html").get();
        String stringResp = response.readEntity(String.class);
        logger.info("response: " + stringResp);
        assertEquals("The returned response status is not the expected one",
                HttpResponseCodes.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

    /**
     * @tpTestDetails Tests if correct HTTP 406 status code is returned when the specified accept media type
     * is not supported by the server
     */
    @Test
    public void testNotAcceptable() throws Exception {
        WebTarget target = client.target(generateURL("/test/foo"));
        Response response = target.request().header("Accept", "text/plain").get();
        assertEquals("The returned response status is not the expected one",
                HttpResponseCodes.SC_NOT_ACCEPTABLE, response.getStatus());
    }

    /**
     * @tpTestDetails Tests of receiving Bad Request response code after html error
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testBadRequestAfterHtmlError() throws Exception {
        WebTarget target = client.target(generateURL("/test"));
        Response response = target.request().post(Entity.entity("<junk", "application/xml"));
        assertEquals("The returned response status is not the expected one",
                Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        response.close();

        response = target.request().header("Accept", "text/html").get();
        String stringResp = response.readEntity(String.class);
        logger.info("response: " + stringResp);
        assertEquals("The returned response status is not the expected one",
                HttpResponseCodes.SC_INTERNAL_SERVER_ERROR, response.getStatus());
    }

}
