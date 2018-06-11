package com.redhat.providers.jaxb;

import com.redhat.providers.jaxb.resource.CharacterSetData;
import com.redhat.providers.jaxb.resource.CharacterSetResource;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.RCategory;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @tpSubChapter Jaxb provider
 * @tpChapter Integration tests
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
@Category(RCategory.class)
public class CharacterSetTest {

    private final String[] characterSets = {"US-ASCII", "UTF-8", "ISO-8859-1"};
    static Client client;

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(CharacterSetTest.class.getSimpleName());
        return TestUtil.finishContainerPrepare(war, null, CharacterSetData.class, CharacterSetResource.class);
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
        return PortProviderUtil.generateURL(path, CharacterSetTest.class.getSimpleName());
    }

    /**
     * @tpTestDetails Tests if correct Variant is chosen for given combination of mediatype xml and charsets.
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void variantSelection() throws URISyntaxException {
        assertCharset("/variant-selection");
    }

    private void assertCharset(String path) throws URISyntaxException {
        for (String characterSet : characterSets) {
            WebTarget target = client.target(generateURL(path));
            Response response = target.request().accept("application/xml").header("Accept-Charset", characterSet).get();

            assertEquals("Status code", 200, response.getStatus());

            String contentType = response.getHeaders().getFirst("Content-Type").toString();
            String charsetPattern = "application/xml\\s*;\\s*charset\\s*=\\s*\"?" + characterSet + "\"?";
            String charsetErrorMessage = contentType + " does not match " + charsetPattern;
            assertTrue(charsetErrorMessage, contentType.contains("application/xml"));

            String xml = response.readEntity(String.class);
            String encodingPattern = "<\\?xml[^>]*encoding\\s*=\\s*['\"]" + characterSet + "['\"].*";
            String encodingErrorMessage = xml + " does not match " + encodingPattern;
            assertTrue(encodingErrorMessage, contentType.contains("application/xml"));

            response.close();
        }
    }

}
