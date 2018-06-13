package com.redhat.core.basic;

import com.redhat.core.basic.resources.*;
import com.redhat.utils.HttpResponseCodes;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestApplication;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter Configuration
 * @tpChapter Integration tests
 * @tpTestCaseDetails Test for usage of more application in one deployment
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
@Category(com.redhat.utils.CXFCategory.class)
public class ApplicationTest {

    private static final String CONTENT_ERROR_MESSAGE = "Wrong content of response";

    @Deployment
    public static Archive<?> deploySimpleResource() {
        WebArchive war = TestUtil.prepareArchive(ApplicationTest.class.getSimpleName());
        war.addAsWebInfResource(ApplicationTest.class.getPackage(), "ApplicationWeb.xml", "web.xml");
        war.addClasses(ApplicationTestAExplicitApplication.class,
                ApplicationTestBExplicitApplication.class,
                ApplicationTestIgnoredApplication.class,
                TestApplication.class,
                ApplicationTestResourceA.class,
                ApplicationTestResourceB.class,
                ApplicationTestScannedApplication.class);
        return TestUtil.finishContainerPrepare(war, null, ApplicationTestResourceA.class, ApplicationTestResourceB.class);
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, ApplicationTest.class.getSimpleName());
    }

    /**
     * @tpTestDetails Test scanned application in deployment: getClasses method is not used. This application is mapped to different location.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testMapped() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget base = client.target(generateURL("/mapped"));

        String value = base.path("resources/a").request().get(String.class);
        Assert.assertEquals(CONTENT_ERROR_MESSAGE, "a", value);
        value = base.path("resources/b").request().get(String.class);
        Assert.assertEquals(CONTENT_ERROR_MESSAGE, "b", value);
    }
}
