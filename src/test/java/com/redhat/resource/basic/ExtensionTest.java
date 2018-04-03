package com.redhat.resource.basic;

import com.redhat.resource.basic.resource.ExtensionResource;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @tpSubChapter Resource
 * @tpChapter Integration tests
 * @tpTestCaseDetails Test for resteasy.media.type.mappings and resteasy.language.mappings parameters
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ExtensionTest {

    static Client client;

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(ExtensionTest.class.getSimpleName());

        Map<String, String> params = new HashMap<>();
        params.put("resteasy.media.type.mappings", "xml : application/xml, html : text/html, txt : text/plain");
        params.put("resteasy.language.mappings", "en : en-US");
        return TestUtil.finishContainerPrepare(war, params, ExtensionResource.class);
    }

    @BeforeClass
    public static void init() {
        client = ClientBuilder.newClient();
    }

    @AfterClass
    public static void after() throws Exception {
        client.close();
    }

    /**
     * @tpTestDetails Check wrong value
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testError() {
        Response response = client.target(PortProviderUtil.generateURL("/extension.junk", ExtensionTest.class.getSimpleName())).request().get();
        Assert.assertEquals(HttpResponseCodes.SC_NOT_FOUND, response.getStatus());
        response.close();
    }
}
