package com.redhat.client.proxy;

import com.redhat.client.proxy.resource.ProxyWithGenericReturnTypeInvocationHandler;
import com.redhat.client.proxy.resource.ProxyWithGenericReturnTypeMessageBodyWriter;
import com.redhat.client.proxy.resource.ProxyWithGenericReturnTypeResource;
import com.redhat.client.proxy.resource.ProxyWithGenericReturnTypeSubResourceIntf;
import com.redhat.client.proxy.resource.ProxyWithGenericReturnTypeSubResourceSubIntf;
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
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @tpSubChapter Resteasy-client
 * @tpChapter Client tests
 * @tpTestCaseDetails Test for generic proxy return type. Proxy is set on server (not on client).
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ProxyWithGenericReturnTypeTest {

    @Deployment
    public static Archive<?> deploySimpleResource() {
        WebArchive war = TestUtil.prepareArchive(ProxyWithGenericReturnTypeTest.class.getSimpleName());

        war.addClass(ProxyWithGenericReturnTypeInvocationHandler.class);
        war.addClass(ProxyWithGenericReturnTypeSubResourceIntf.class);
        war.addClass(ProxyWithGenericReturnTypeSubResourceSubIntf.class);

        List<Class<?>> singletons = new ArrayList<>();
        singletons.add(ProxyWithGenericReturnTypeMessageBodyWriter.class);
        return TestUtil.finishContainerPrepare(war, null, singletons, ProxyWithGenericReturnTypeResource.class);
    }

    /**
     * @tpTestDetails Test for new client
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void newClientTest() throws Exception {
        Client client  = ClientBuilder.newClient();

        WebTarget base = client.target(PortProviderUtil.generateURL("/test/list/", ProxyWithGenericReturnTypeTest.class.getSimpleName()));
        Response response = base.request().get();

        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        Assert.assertTrue("Wrong content of response, list was not decoden on server", response.readEntity(String.class).indexOf("List<String>") >= 0);

        client.close();
    }
}
