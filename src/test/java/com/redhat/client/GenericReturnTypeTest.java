package com.redhat.client;

import com.redhat.client.resource.GenericReturnTypeInterface;
import com.redhat.client.resource.GenericReturnTypeReader;
import com.redhat.client.resource.GenericReturnTypeResource;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.jaxrs.ProxyBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;

/**
 * @tpSubChapter Resteasy-client
 * @tpChapter Client tests
 * @tpSince RESTEasy 3.0.17
 * @tpTestCaseDetails Regression for JBEAP-4699
 */
@RunWith(Arquillian.class)
@RunAsClient
public class GenericReturnTypeTest extends ClientTestBase{

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(GenericReturnTypeTest.class.getSimpleName());
        war.addClasses(GenericReturnTypeInterface.class);
        return TestUtil.finishContainerPrepare(war, null, GenericReturnTypeResource.class, GenericReturnTypeReader.class);
    }

    /**
     * @tpTestDetails Test generic type of proxy
     * @tpSince RESTEasy 3.0.17
     */
    @Test
    public void testGenericReturnType() {
        Client client = ResteasyClientBuilder.newClient();
        ResteasyWebTarget target = (ResteasyWebTarget) client.target(generateURL("")).register(GenericReturnTypeReader.class);
        GenericReturnTypeInterface<?> server = ProxyBuilder.builder(GenericReturnTypeInterface.class, target).build();
        Object result = server.t();
        Assert.assertEquals("abc", result);
    }
}
