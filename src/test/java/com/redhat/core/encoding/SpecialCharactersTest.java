package com.redhat.core.encoding;

import com.redhat.core.encoding.resource.SpecialCharactersProxy;
import com.redhat.core.encoding.resource.SpecialCharactersResource;
import com.redhat.utils.HttpResponseCodes;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter Encoding
 * @tpChapter Integration tests
 * @tpTestCaseDetails Regression test for RESTEASY-208 and RESTEASY-214
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class SpecialCharactersTest {

    protected static Client client;

    @Before
    public void setup() throws Exception {
        client = ClientBuilder.newClient();
    }

    @After
    public void shutdown() throws Exception {
        client.close();
    }

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(SpecialCharactersTest.class.getSimpleName());
        war.addClass(SpecialCharactersProxy.class);
        return TestUtil.finishContainerPrepare(war, null, SpecialCharactersResource.class);
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, SpecialCharactersTest.class.getSimpleName());
    }

    private static final String SPACES_REQUEST = "something something";
    private static final String QUERY = "select p from VirtualMachineEntity p where guest.guestId = :id";

    @Test
    public void testPlus() throws Exception {
        Response response = client.target(generateURL("/sayhello/plus/foo+bar")).request().get();
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        // assert is in resource
        response.close();
    }

    @Test
    public void testPlus2() throws Exception {
        Response response = client.target(generateURL("/sayhello/plus/foo+bar")).request().get();
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        // assert is in resource
        response.close();
    }
}

