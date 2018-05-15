package com.redhat.resource.request;

import com.redhat.core.basic.resources.DuplicateDeploymentResource;
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
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * @tpSubChapter Core
 * @tpChapter Integration tests
 * @tpSince RESTEasy 3.0.17
 * @tpTestCaseDetails Regression test for JBEAP-3725
 */
@RunWith(Arquillian.class)
@RunAsClient
public class NotFoundErrorMessageTest {
    static Client client;

    @BeforeClass
    public static void before() throws Exception {
        client = ClientBuilder.newClient();
    }

    @AfterClass
    public static void close() {
        client.close();
    }

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(NotFoundErrorMessageTest.class.getSimpleName());
        return TestUtil.finishContainerPrepare(war, null, DuplicateDeploymentResource.class);
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, NotFoundErrorMessageTest.class.getSimpleName());
    }

    /**
     * @tpTestDetails Check that no ERROR message was in logs after 404.
     * @tpSince RESTEasy 3.0.17
     */
    @Test
    public void testDeploy() throws IOException {
        Response response = client.target(generateURL("/nonsence")).request().get();
        Assert.assertEquals(HttpResponseCodes.SC_NOT_FOUND, response.getStatus());
        response.close();
    }
}