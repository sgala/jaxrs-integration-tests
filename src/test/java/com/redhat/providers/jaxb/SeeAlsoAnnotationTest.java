package com.redhat.providers.jaxb;

import com.redhat.providers.jaxb.resource.SeeAlsoAnnotationBaseFoo;
import com.redhat.providers.jaxb.resource.SeeAlsoAnnotationFooIntf;
import com.redhat.providers.jaxb.resource.SeeAlsoAnnotationRealFoo;
import com.redhat.providers.jaxb.resource.SeeAlsoAnnotationResource;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.xml.bind.JAXBContext;
import java.io.StringWriter;

/**
 * @tpSubChapter Jaxb provider
 * @tpChapter Integration tests
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class SeeAlsoAnnotationTest {

    private final Logger logger = Logger.getLogger(SeeAlsoAnnotationTest.class.getName());
    static Client client;

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(SeeAlsoAnnotationTest.class.getSimpleName());
        return TestUtil.finishContainerPrepare(war, null, SeeAlsoAnnotationResource.class, SeeAlsoAnnotationRealFoo.class,
                SeeAlsoAnnotationBaseFoo.class, SeeAlsoAnnotationFooIntf.class);
    }

    @Before
    public void init() {
        client = ClientBuilder.newClient();
    }

    @After
    public void after() throws Exception {
        client.close();
        client = null;
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, SeeAlsoAnnotationTest.class.getSimpleName());
    }

    /**
     * @tpTestDetails Tests jaxb @SeeAlsoAnnotation
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testIntf() throws Exception {
        String url = generateURL("/see/intf");
        runTest(url);
    }

    /**
     * @tpTestDetails Tests jaxb @SeeAlsoAnnotation
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testTest() throws Exception {
        String url = generateURL("/see/base");
        runTest(url);
    }

    private void runTest(String url) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(SeeAlsoAnnotationRealFoo.class);
        StringWriter writer = new StringWriter();
        SeeAlsoAnnotationRealFoo foo = new SeeAlsoAnnotationRealFoo();
        foo.setName("bill");

        ctx.createMarshaller().marshal(foo, writer);

        String s = writer.getBuffer().toString();
        logger.info(s);

        WebTarget target = client.target(generateURL(url));
        target.request().header("Content-Type", "application/xml").put(Entity.xml(s));
    }

}
