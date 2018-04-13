package com.redhat.providers.file;

import com.redhat.providers.file.resource.TempFileDeletionResource;
import com.redhat.utils.HttpResponseCodes;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.File;

/**
 * @tpSubChapter File provider
 * @tpChapter Integration tests
 * @tpSince RESTEasy 3.1.3.Final
 */
@RunWith(Arquillian.class)
@RunAsClient
public class TempFileDeletionTest {

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(TempFileDeletionTest.class.getSimpleName());
        return TestUtil.finishContainerPrepare(war, null, TempFileDeletionResource.class);
    }

    /**
     * @tpTestDetails Resource method contains parameter of the type File. This triggers File provider, which creates
     * temporary file on the server side, which is automatically deleted in the end of the resource method invocation.
     * @tpInfo Regression test for RESTEASY-1464
     * @tpSince RESTEasy 3.1.3.Final
     */
    @Test
    public void testDeleteOnServer() throws Exception {
        Client client = ClientBuilder.newClient();
        WebTarget base = client.target(PortProviderUtil.generateURL("/test/post", TempFileDeletionTest.class.getSimpleName()));
        Response response = base.request().post(Entity.entity("hello", "text/plain"));
        Assert.assertEquals(response.getStatus(), HttpResponseCodes.SC_OK);
        String path = response.readEntity(String.class);
        File file = new File(path);
        Assert.assertFalse(file.exists());
        client.close();
    }
}
