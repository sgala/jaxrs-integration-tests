package com.redhat.providers.jaxb;

import com.redhat.providers.jaxb.resource.InheritanceAnimal;
import com.redhat.providers.jaxb.resource.InheritanceCat;
import com.redhat.providers.jaxb.resource.InheritanceDog;
import com.redhat.providers.jaxb.resource.InheritanceResource;
import com.redhat.providers.jaxb.resource.InheritanceZoo;
import com.redhat.utils.HttpResponseCodes;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter Jaxb provider
 * @tpChapter Integration tests
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class InheritanceTest {

    static Client client;

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(InheritanceTest.class.getSimpleName());
        return TestUtil.finishContainerPrepare(war, null, InheritanceAnimal.class, InheritanceCat.class, InheritanceDog.class,
                InheritanceZoo.class, InheritanceResource.class);
    }

    @Before
    public void init() {
        client = ClientBuilder.newClient();
    }

    @After
    public void after() throws Exception {
        client.close();
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, InheritanceTest.class.getSimpleName());
    }

    /**
     * @tpTestDetails Tests Jaxb object with inheritance structure
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testInheritance() throws Exception {
        WebTarget target = client.target(generateURL("/zoo"));
        Response response = target.request().get();
        Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        InheritanceZoo zoo = response.readEntity(InheritanceZoo.class);
        Assert.assertEquals("The number of animals in the zoo doesn't match the expected count", 2, zoo.getAnimals().size());
    }

}
