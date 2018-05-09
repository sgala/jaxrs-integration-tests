package com.redhat.cdi.inheritence;

import com.redhat.cdi.inheritence.resource.CDIInheritenceBook;
import com.redhat.cdi.inheritence.resource.CDIInheritenceBookVanillaAlternative;
import com.redhat.cdi.inheritence.resource.CDIInheritenceInheritanceResource;
import com.redhat.cdi.inheritence.resource.CDIInheritenceSelectBook;
import com.redhat.cdi.inheritence.resource.CDIInheritenceStereotypeAlternative;
import com.redhat.cdi.util.UtilityProducer;
import com.redhat.utils.EmptyResource;
import com.redhat.utils.HttpResponseCodes;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
 * @tpTestCaseDetails This class tests CDI inheritance (BookVanilaAlternative should not be used)
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class AlternativeVanillaInheritanceTest {
    protected static final Logger log = LogManager.getLogger(AlternativeVanillaInheritanceTest.class.getName());

    @Deployment
    public static Archive<?> createTestArchive() {
        WebArchive war = TestUtil.prepareArchive(AlternativeVanillaInheritanceTest.class.getSimpleName());
        war.addClasses(UtilityProducer.class)
                .addClasses(CDIInheritenceBook.class, CDIInheritenceSelectBook.class, CDIInheritenceStereotypeAlternative.class, CDIInheritenceBookVanillaAlternative.class, CDIInheritenceInheritanceResource.class)
                .addAsWebInfResource(SpecializedInheritanceTest.class.getPackage(), "alternativeVanillaBeans.xml", "beans.xml");
        return TestUtil.finishContainerPrepare(war, null, EmptyResource.class);
    }

    /**
     * @tpTestDetails Client get request. Resource check inheritance bean on server.
     * @tpPassCrit Response status should not contain error.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testAlternative() throws Exception {
        Client client = ClientBuilder.newClient();
        log.info("starting testAlternative()");
        WebTarget base = client.target(PortProviderUtil.generateURL("/alternative/vanilla", AlternativeVanillaInheritanceTest.class.getSimpleName()));
        Response response = base.request().get();
        log.info("Status: " + response.getStatus());
        response.close();
        client.close();
    }
}
