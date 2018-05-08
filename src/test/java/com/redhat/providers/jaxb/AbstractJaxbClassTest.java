package com.redhat.providers.jaxb;

import com.redhat.providers.jaxb.resource.AbstractJaxbClassCompanyCustomer;
import com.redhat.providers.jaxb.resource.AbstractJaxbClassCustomer;
import com.redhat.providers.jaxb.resource.AbstractJaxbClassPerson;
import com.redhat.providers.jaxb.resource.AbstractJaxbClassPrivatCustomer;
import com.redhat.providers.jaxb.resource.AbstractJaxbClassResource;
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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter Jaxb provider
 * @tpChapter Integration tests
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class AbstractJaxbClassTest {

    static Client client;

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(AbstractJaxbClassTest.class.getSimpleName());
        return TestUtil.finishContainerPrepare(war, null, AbstractJaxbClassCompanyCustomer.class, AbstractJaxbClassCustomer.class,
                AbstractJaxbClassPerson.class, AbstractJaxbClassPrivatCustomer.class, AbstractJaxbClassResource.class);
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
        return PortProviderUtil.generateURL(path, AbstractJaxbClassTest.class.getSimpleName());
    }

    private static final String customerXml = "<?xml version=\"1.0\"?>\n"
            + "<abstractJaxbClassPrivatCustomer>\n"
            + "<nachname>Test</nachname>\n"
            + "<vorname>Theo</vorname>\n"
            + "<seit>2001-01-31T00:00:00+01:00</seit>\n"
            + "<adresse><plz>76133</plz><ort>Karlsruhe</ort><strasse>Moltkestrasse</strasse><hausnr>31</hausnr></adresse>\n"
            + "</abstractJaxbClassPrivatCustomer>";

    /**
     * @tpTestDetails Test for Abstract jaxb class with @XmlSeeAlso annotation
     * @tpInfo RESTEASY-126
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testPost() throws Exception {
        WebTarget target = client.target(generateURL(""));
        String xmlInput = "<?xml version=\"1.0\"?><abstractJaxbClassPerson><name>bill</name></abstractJaxbClassPerson>";
        Response response = target.request().post(Entity.xml(xmlInput));
        Assert.assertEquals(HttpResponseCodes.SC_NO_CONTENT, response.getStatus());
        response.close();

        WebTarget target2 = client.target(generateURL("/customer"));
        Response response2 = target2.request().post(Entity.entity(customerXml, "application/xml"));
        Assert.assertEquals(204, response2.getStatus());
        response2.close();
    }

}
