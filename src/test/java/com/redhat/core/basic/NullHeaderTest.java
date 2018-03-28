package com.redhat.core.basic;

import com.redhat.core.basic.resources.NullHeaderFilter;
import com.redhat.core.basic.resources.NullHeaderResource;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter RESTEASY-1565
 * @tpChapter Integration tests
 * @tpSince RESTEasy 3.1.1.Final
 */
@RunWith(Arquillian.class)
@RunAsClient
public class NullHeaderTest {

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(NullHeaderTest.class.getSimpleName());
        war.addClass(NullHeaderFilter.class);
        return TestUtil.finishContainerPrepare(war, null, NullHeaderResource.class);
    }

    @Test
    public void testNullHeader() throws Exception {
  
       Client client = ClientBuilder.newClient();
       WebTarget base = client.target(PortProviderUtil.generateURL("/test", NullHeaderTest.class.getSimpleName()));
       Response response = base.register(NullHeaderFilter.class).request().header("X-Auth-User", null).get();
       Assert.assertNotNull(response);
       Assert.assertEquals(200, response.getStatus());
       String serverHeader = response.getHeaderString("X-Server-Header");
       Assert.assertTrue(serverHeader == null || "".equals(serverHeader));
       client.close();
    }
}
