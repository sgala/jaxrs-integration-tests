package com.redhat.cdi.modules;

import com.redhat.cdi.modules.resource.CDIModulesInjectable;
import com.redhat.cdi.modules.resource.CDIModulesInjectableBinder;
import com.redhat.cdi.modules.resource.CDIModulesInjectableIntf;
import com.redhat.cdi.modules.resource.CDIModulesModulesResource;
import com.redhat.cdi.modules.resource.CDIModulesModulesResourceIntf;
import com.redhat.cdi.util.UtilityProducer;
import com.redhat.utils.HttpResponseCodes;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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
 * @tpTestCaseDetails Test bean injection from war to lib in war.
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class WarClassesIntoWarLibTest {
    protected static final Logger log = LogManager.getLogger(WarClassesIntoWarLibTest.class.getName());


    @Deployment
    public static Archive<?> createTestArchive() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "test.jar")
                .addClasses(CDIModulesModulesResourceIntf.class, CDIModulesModulesResource.class)
                .add(EmptyAsset.INSTANCE, "META-INF/beans.xml");
        WebArchive war = TestUtil.prepareArchive(WarClassesIntoWarLibTest.class.getSimpleName())
                .addClasses(UtilityProducer.class)
                .addClasses(CDIModulesInjectableBinder.class, CDIModulesInjectableIntf.class, CDIModulesInjectable.class)
                .addAsLibrary(jar)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        return war;
    }

    /**
     * @tpTestDetails Test bean injection from war to lib in war.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testModules() throws Exception {
        log.info("starting testModules()");

        Client client = ClientBuilder.newClient();
        WebTarget base = client.target(PortProviderUtil.generateURL("/modules/test/", WarClassesIntoWarLibTest.class.getSimpleName()));
        Response response = base.request().get();
        log.info("Status: " + response.getStatus());
        response.close();
        client.close();
    }
}
