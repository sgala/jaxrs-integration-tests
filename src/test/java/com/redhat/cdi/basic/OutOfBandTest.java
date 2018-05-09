package com.redhat.cdi.basic;

import com.redhat.cdi.basic.resource.OutOfBandResource;
import com.redhat.cdi.basic.resource.OutOfBandResourceIntf;
import com.redhat.utils.HttpResponseCodes;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestApplication;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * @tpSubChapter CDI
 * @tpChapter Integration tests
 * @tpTestCaseDetails Regression test for RESTEASY-1049.
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class OutOfBandTest {

    @Deployment
    public static Archive<?> createTestArchive() {
        WebArchive war = TestUtil.prepareArchive("RESTEASY-1008")
                .addClasses(OutOfBandResourceIntf.class, OutOfBandResource.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        war.addAsWebInfResource(TestApplication.class.getPackage(), "TestApplication.xml", "web.xml");
        war.addClass(TestApplication.class);

        return war;
    }

    /**
     * @tpTestDetails JAX-RS resource methods can be called outside the context of a servlet request, leading to NPEs.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testTimerInterceptor() throws Exception {
        Client client = ClientBuilder.newClient();

        // Schedule timer.
        WebTarget base = client.target(PortProviderUtil.generateURL("/timer/schedule", "RESTEASY-1008"));
        Response response = base.request().get();
        response.close();

        // Verify timer expired and timer interceptor was executed.
        base = client.target(PortProviderUtil.generateURL("/timer/test", "RESTEASY-1008"));
        response = base.request().get();
        response.close();

        client.close();
    }
}
