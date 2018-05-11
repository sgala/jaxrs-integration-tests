package com.redhat.core.encoding;

import com.redhat.core.encoding.resource.MatrixParamEncodingResource;
import com.redhat.utils.HttpResponseCodes;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * @tpSubChapter Encoding
 * @tpChapter Integration tests
 * @tpTestCaseDetails Regression test for RESTEASY-729
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class MatrixParamEncodingTest {

    protected static final Logger logger = LogManager.getLogger(MatrixParamEncodingTest.class.getName());

    protected static Client client;

    @Before
    public void setup() throws Exception {
        client = ClientBuilder.newClient();
    }


    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, MatrixParamEncodingTest.class.getSimpleName());
    }

    @Deployment
    public static Archive<?> createTestArchive() {
        WebArchive war = TestUtil.prepareArchive(MatrixParamEncodingTest.class.getSimpleName());
        return TestUtil.finishContainerPrepare(war, null, MatrixParamEncodingResource.class);
    }

    @After
    public void shutdown() throws Exception {
        client.close();
        client = null;
    }

    /**
     * @tpTestDetails Check decoded request, do not use UriBuilder
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testMatrixParamRequestDecoded() throws Exception {
        WebTarget target = client.target(generateURL("/decoded")).matrixParam("param", "ac/dc");
        Response response = target.request().get();
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertEquals("Wrong response", "ac/dc", response.readEntity(String.class));
        response.close();
    }

    /**
     * @tpTestDetails Check decoded request, one matrix param is not defined, do not use UriBuilder
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testMatrixParamNullRequestDecoded() throws Exception {
        WebTarget target = client.target(generateURL("/decodedMultipleParam")).matrixParam("param1", "").matrixParam("param2", "abc");
        Response response = target.request().get();
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertEquals("Wrong response", "null abc", response.readEntity(String.class));
        response.close();
    }

    /**
     * @tpTestDetails Check encoded request, do not use UriBuilder
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testMatrixParamRequestEncoded() throws Exception {
        WebTarget target = client.target(generateURL("/encoded")).matrixParam("param", "ac/dc");
        Response response = target.request().get();
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertEquals("Wrong response", "ac%2Fdc", response.readEntity(String.class));
        response.close();
    }
}
