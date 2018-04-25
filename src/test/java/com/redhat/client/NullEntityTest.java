package com.redhat.client;

import com.redhat.client.resource.NullEntityResource;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

/**
 * @tpSubChapter Resteasy-client
 * @tpChapter Client tests
 * @tpSince RESTEasy 3.0.16
 * @tpTestCaseDetails Regression for RESTEASY-1057
 */
@RunWith(Arquillian.class)
@RunAsClient
public class NullEntityTest extends ClientTestBase{

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(NullEntityTest.class.getSimpleName());
        return TestUtil.finishContainerPrepare(war, null, NullEntityResource.class);
    }

    /**
     * @tpTestDetails Test to send null by post request.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testPostNull() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(generateURL("/null"));
        String response = target.request().post(null, String.class);
        Assert.assertEquals("Wrong response", "", response);
        client.close();
    }

    /**
     * @tpTestDetails Test to send null via entity by post request.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testEntity() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(generateURL("/entity"));
        String response = target.request().post(Entity.entity(null, MediaType.WILDCARD), String.class);
        Assert.assertEquals("Wrong response", "", response);
        client.close();
    }

    /**
     * @tpTestDetails Test to send null via form
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testForm() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(generateURL("/form"));
        String response = target.request().post(Entity.form((Form) null), String.class);
        Assert.assertEquals("Wrong response", null, response);
        client.close();
    }

    /**
     * @tpTestDetails Test resource with "text/html" media type
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testHtml() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(generateURL("/html"));
        String response = target.request().post(Entity.html(null), String.class);
        Assert.assertEquals("Wrong response", "", response);
        client.close();
    }

    /**
     * @tpTestDetails Test resource with "application/xhtml+xml" media type
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testXhtml() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(generateURL("/xhtml"));
        String response = target.request().post(Entity.xhtml(null), String.class);
        Assert.assertEquals("Wrong response", "", response);
        client.close();
    }

    /**
     * @tpTestDetails Test resource with "application/xml" media type
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testXml() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(generateURL("/xml"));
        String response = target.request().post(Entity.xml(null), String.class);
        Assert.assertEquals("Wrong response", "", response);
        client.close();
    }
}
