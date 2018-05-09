package com.redhat.cdi.generic;

import com.redhat.cdi.generic.resource.Animal;
import com.redhat.cdi.generic.resource.Australopithecus;
import com.redhat.cdi.generic.resource.ConcreteDecorator;
import com.redhat.cdi.generic.resource.ConcreteResource;
import com.redhat.cdi.generic.resource.ConcreteResourceIntf;
import com.redhat.cdi.generic.resource.GenericsProducer;
import com.redhat.cdi.generic.resource.HierarchyHolder;
import com.redhat.cdi.generic.resource.HolderBinding;
import com.redhat.cdi.generic.resource.LowerBoundHierarchyHolder;
import com.redhat.cdi.generic.resource.NestedHierarchyHolder;
import com.redhat.cdi.generic.resource.ObjectHolder;
import com.redhat.cdi.generic.resource.Primate;
import com.redhat.cdi.generic.resource.UpperBoundHierarchyHolder;
import com.redhat.cdi.generic.resource.VisitList;
import com.redhat.cdi.util.UtilityProducer;
import com.redhat.utils.HttpResponseCodes;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestApplication;
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
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * @tpSubChapter CDI
 * @tpChapter Integration tests
 * @tpTestCaseDetails RESTEasy integration test for CDI && decorators
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ConcreteDecoratorTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        WebArchive war = TestUtil.prepareArchive("resteasy-cdi-ejb-test");
        war.addClasses(UtilityProducer.class, VisitList.class);
        war.addClasses(ObjectHolder.class, ConcreteResourceIntf.class);
        war.addClasses(HolderBinding.class, HierarchyHolder.class);
        war.addClasses(GenericsProducer.class);
        war.addClasses(ConcreteResource.class);
        war.addClasses(NestedHierarchyHolder.class);
        war.addClasses(UpperBoundHierarchyHolder.class, LowerBoundHierarchyHolder.class);
        war.addClasses(Animal.class, Primate.class, Australopithecus.class);
        war.addClasses(ConcreteDecorator.class);
        war.addAsWebInfResource(ConcreteDecoratorTest.class.getPackage(), "concrete_beans.xml", "beans.xml");

        war.addAsWebInfResource(TestApplication.class.getPackage(), "TestApplication.xml", "web.xml");
        war.addClasses(TestApplication.class);

        return TestUtil.finishContainerPrepare(war, null, ConcreteResource.class);
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, "resteasy-cdi-ejb-test");
    }

    /**
     * @tpTestDetails Run REST point method and check execution of decorators.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testConcreteConcreteDecorator() throws Exception {
        Client client = ClientBuilder.newClient();

        WebTarget base = client.target(generateURL("/concrete/decorators/clear"));
        Response response = base.request().get();
        assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        response.close();

        base = client.target(generateURL("/concrete/decorators/execute"));
        response = base.request().get();
        assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        response.close();

        base = client.target(generateURL("/concrete/decorators/test"));
        response = base.request().get();
        response.close();

        client.close();
    }
}
