package com.redhat.cdi.basic;

import com.redhat.cdi.basic.resource.CDILocatorResource;
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
import javax.ws.rs.client.WebTarget;

/**
 * @tpSubChapter CDI
 * @tpChapter Integration tests
 * @tpTestCaseDetails Test for CDI locator
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class CDILocatorTest {
    static Client client;
    static WebTarget baseTarget;

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(CDILocatorTest.class.getSimpleName());
        return TestUtil.finishContainerPrepare(war, null, CDILocatorResource.class);
    }

    private static String generateURL() {
        return PortProviderUtil.generateBaseUrl(CDILocatorTest.class.getSimpleName());
    }

    @BeforeClass
    public static void initClient() {
        client = ClientBuilder.newClient();
        baseTarget = client.target(generateURL());
    }

    @AfterClass
    public static void closeClient() {
        client.close();
    }

    /**
     * @tpTestDetails Check generic type
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void genericTypeTest() throws Exception {
        String result = baseTarget.path("test").queryParam("foo", "yo").request().get(String.class);
        Assert.assertEquals("Wrong response", "OK", result);
    }
}
