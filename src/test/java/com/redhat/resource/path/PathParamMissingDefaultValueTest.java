package com.redhat.resource.path;

import com.redhat.resource.path.resource.PathParamMissingDefaultValueBeanParamEntity;
import com.redhat.resource.path.resource.PathParamMissingDefaultValueResource;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * @tpSubChapter Resource
 * @tpChapter Integration tests
 * @tpSince RESTEasy 4.0.0
 * @tpTestCaseDetails Check for slash in URL
 */
@RunWith(Arquillian.class)
@RunAsClient
public class PathParamMissingDefaultValueTest {

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(PathParamMissingDefaultValueTest.class.getSimpleName());
        war.addClass(PathParamMissingDefaultValueBeanParamEntity.class);
        return TestUtil.finishContainerPrepare(war, null, PathParamMissingDefaultValueResource.class);
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, PathParamMissingDefaultValueTest.class.getSimpleName());
    }

    /**
     * @tpTestDetails Missing @PathParam in BeanParam with no @DefaultValue should get java default value.
     * @tpSince RESTEasy 4.0.0
     */
    @Test
    public void testTrailingSlash() throws Exception {
        Client client = ClientBuilder.newClient();
        Response response = client.target(generateURL("/resource/test/")).request().get();
        assertEquals(200, response.getStatus());
        assertEquals("Wrong response", "nullnullnullnullnullnull", response.readEntity(String.class));
        client.close();
    }
}