package com.redhat.resource.param;

import com.redhat.resource.param.resource.ComplexPathParamExtensionResource;
import com.redhat.resource.param.resource.ComplexPathParamRegressionResteasy145;
import com.redhat.resource.param.resource.ComplexPathParamSubRes;
import com.redhat.resource.param.resource.ComplexPathParamSubResSecond;
import com.redhat.resource.param.resource.ComplexPathParamTrickyResource;
import com.redhat.resource.param.resource.ComplexPathParamUnlimitedResource;
import com.redhat.utils.HttpResponseCodes;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;

/**
 * @tpSubChapter Parameters
 * @tpChapter Integration tests
 * @tpTestCaseDetails Test for complex path parameters
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ComplexPathParamTest {

    public static final String WRONG_REQUEST_ERROR_MESSAGE = "Wrong content of request";

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(ComplexPathParamTest.class.getSimpleName());
        war.addClass(ComplexPathParamTest.class);
        war.addClass(PortProviderUtil.class);
        war.addClass(TestUtil.class);
        war.addClass(ComplexPathParamSubRes.class);
        war.addClass(ComplexPathParamSubResSecond.class);
        return TestUtil.finishContainerPrepare(war, null, ComplexPathParamExtensionResource.class,
                ComplexPathParamRegressionResteasy145.class, ComplexPathParamTrickyResource.class,
                ComplexPathParamUnlimitedResource.class);
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, ComplexPathParamTest.class.getSimpleName());
    }

    private void basicTest(String path, String body) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        Response response = client.target(generateURL(path)).request().get();
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertEquals("Wrong content of response, url may not be decoded correctly", body, response.readEntity(String.class));
        response.close();
        client.close();
    }

    /**
     * @tpTestDetails Check special characters and various path combination
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testIt() throws Exception {
        basicTest("/1,2/3/blah4-5ttt", "hello");
        basicTest("/tricky/1,2", "2Groups");
        basicTest("/tricky/h1", "prefixed");
        basicTest("/tricky/1", "hello");
        basicTest("/unlimited/1-on/and/on", "ok");
        basicTest("/repository/workspaces/aaaaaaxvi/wdddd", "sub2");
    }

}