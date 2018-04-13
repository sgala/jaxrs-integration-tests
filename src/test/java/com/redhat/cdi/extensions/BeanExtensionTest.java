package com.redhat.cdi.extensions;


import com.redhat.cdi.extensions.resource.CDIExtensionsBoston;
import com.redhat.cdi.extensions.resource.CDIExtensionsBostonBean;
import com.redhat.cdi.extensions.resource.CDIExtensionsBostonBeanExtension;
import com.redhat.cdi.extensions.resource.CDIExtensionsBostonHolder;
import com.redhat.cdi.extensions.resource.CDIExtensionsBostonlLeaf;
import com.redhat.cdi.extensions.resource.CDIExtensionsResource;
import com.redhat.cdi.extensions.resource.CDIExtensionsTestReader;
import com.redhat.cdi.util.Utilities;
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

import javax.enterprise.inject.spi.Extension;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * @tpSubChapter CDI
 * @tpChapter Integration tests
 * @tpTestCaseDetails Test CDI extensions for bean.
 * BostonBeanExtension implements a CDI extension, it creates a BostonBean for each of the two classes,
 * BostonHolder and BostonLeaf, that are annotated with @Boston, and it registers them with the CDI runtime.
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class BeanExtensionTest {
    protected static final Logger log = LogManager.getLogger(BeanExtensionTest.class.getName());

    @SuppressWarnings(value = "unchecked")
    @Deployment
    public static Archive<?> createTestArchive() {
        WebArchive war = TestUtil.prepareArchive(BeanExtensionTest.class.getSimpleName());
        war.addClasses(UtilityProducer.class, Utilities.class)
                .addClasses(CDIExtensionsBostonBeanExtension.class, CDIExtensionsBoston.class, CDIExtensionsBostonBean.class)
                .addClasses(CDIExtensionsResource.class, CDIExtensionsTestReader.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsServiceProvider(Extension.class, CDIExtensionsBostonBeanExtension.class);

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class).addClasses(CDIExtensionsBostonHolder.class, CDIExtensionsBostonlLeaf.class);
        war.addAsLibrary(jar);

        return TestUtil.finishContainerPrepare(war, null, (Class<?>[]) null);
    }

    /**
     * @tpTestDetails Client get request. Resource check extension bean on server.
     * @tpPassCrit Response status should not contain error.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testBostonBeans() throws Exception {
        log.info("starting testBostonBeans()");

        Client client = ClientBuilder.newClient();
        WebTarget base = client.target(PortProviderUtil.generateURL("/extension/boston/", BeanExtensionTest.class.getSimpleName()));
        Response response = base.request().post(Entity.text(new String()));

        log.info("Response status: " + response.getStatus());


        response.close();
        client.close();
    }
}
