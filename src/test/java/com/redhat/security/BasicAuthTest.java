package com.redhat.security;

import com.redhat.security.resource.BasicAuthBaseProxy;
import com.redhat.security.resource.BasicAuthBaseResource;
import com.redhat.security.resource.BasicAuthBaseResourceAnybody;
import com.redhat.security.resource.BasicAuthBaseResourceMoreSecured;
import com.redhat.security.resource.CustomAuthenticator;
import com.redhat.utils.AbstractUsersRolesSecurityDomainSetup;
import com.redhat.utils.HttpResponseCodes;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.api.ServerSetup;
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
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @tpSubChapter Security
 * @tpChapter Integration tests
 * @tpTestCaseDetails Basic test for RESTEasy authentication.
 * @tpSince RESTEasy 3.0.16
 */
@ServerSetup({BasicAuthTest.SecurityDomainSetup.class})
@RunWith(Arquillian.class)
@RunAsClient
public class BasicAuthTest {

    private static final String WRONG_RESPONSE = "Wrong response content.";
    private static final String ACCESS_FORBIDDEN_MESSAGE = "Access forbidden: role not allowed";

    private static Client authorizedClient;
    private static Client unauthorizedClient;
    private static Client noAutorizationClient;

    @BeforeClass
    public static void init() {
        // authorizedClient
        authorizedClient = ClientBuilder.newClient().register(new CustomAuthenticator("bill", "password1"));
        unauthorizedClient = ClientBuilder.newClient().register(new CustomAuthenticator("ordinaryUser", "password2"));
        noAutorizationClient = ClientBuilder.newClient();
    }

    @AfterClass
    public static void after() throws Exception {
        authorizedClient.close();
        unauthorizedClient.close();
        noAutorizationClient.close();
    }

    @Deployment
    public static Archive<?> deployLocatingResource() {
        WebArchive war = TestUtil.prepareArchive(BasicAuthTest.class.getSimpleName());
        war.addClass(BasicAuthBaseProxy.class)
                .addAsWebInfResource(BasicAuthTest.class.getPackage(), "web.xml", "web.xml");
        return TestUtil.finishContainerPrepare(war, null, BasicAuthBaseResource.class,
                BasicAuthBaseResourceMoreSecured.class, BasicAuthBaseResourceAnybody.class);
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, BasicAuthTest.class.getSimpleName());
    }

    /**
     * @tpTestDetails Regression test for RESTEASY-579
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void test579() throws Exception {
        Response response = authorizedClient.target(generateURL("/secured2")).request().get();
        Assert.assertEquals(HttpResponseCodes.SC_NOT_FOUND, response.getStatus());
        response.close();
    }

    /**
     * @tpTestDetails Check failures for secured resource.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testSecurityFailure() throws Exception {
        {
            Response response = noAutorizationClient.target(generateURL("/secured")).request().get();
            Assert.assertThat(response.getStatus(), Matchers.either(Matchers.is(HttpResponseCodes.SC_FORBIDDEN)).or(Matchers.is(HttpResponseCodes.SC_UNAUTHORIZED)));
            response.close();
        }

        {
            Response response = unauthorizedClient.target(generateURL("/secured/authorized")).request().get();
            Assert.assertThat(response.getStatus(), Matchers.either(Matchers.is(HttpResponseCodes.SC_FORBIDDEN)).or(Matchers.is(HttpResponseCodes.SC_UNAUTHORIZED)));
        }
    }

    /**
     * @tpTestDetails Test Content-type when forbidden exception is raised, RESTEASY-1563
     * @tpSince RESTEasy 3.1.1
     */
    @Test
    public void testContentTypeWithForbiddenMessage() {
        Response response = unauthorizedClient.target(generateURL("/secured/denyWithContentType")).request().get();
        Assert.assertThat(response.getStatus(), Matchers.either(Matchers.is(HttpResponseCodes.SC_FORBIDDEN)).or(Matchers.is(HttpResponseCodes.SC_UNAUTHORIZED)));
        Assert.assertTrue("Incorrect Content-type header",response.getHeaderString("Content-type").contains("text/html"));
    }

    /**
     * @tpTestDetails Test Content-type when unauthorized exception is raised
     * @tpSince RESTEasy 3.1.1
     */
    @Test
    public void testContentTypeWithUnauthorizedMessage() {
        Response response = noAutorizationClient.target(generateURL("/secured/denyWithContentType")).request().get();
        Assert.assertThat(response.getStatus(), Matchers.either(Matchers.is(HttpResponseCodes.SC_FORBIDDEN)).or(Matchers.is(HttpResponseCodes.SC_UNAUTHORIZED)));
        Assert.assertTrue("Incorrect Content-type header", response.getHeaderString("Content-type").contains("text/html"));
    }

    static class SecurityDomainSetup extends AbstractUsersRolesSecurityDomainSetup {

        @Override
        public void setConfigurationPath() throws URISyntaxException {
            Path filepath= Paths.get(BasicAuthTest.class.getResource("users.properties").toURI());
            Path parent = filepath.getParent();
            createPropertiesFiles(new File(parent.toUri()));
        }

    }
}
