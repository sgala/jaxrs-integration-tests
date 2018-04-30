package com.redhat.core.basic;

import com.redhat.core.basic.resources.AppConfigApplication;
import com.redhat.core.basic.resources.AppConfigResources;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.File;


/**
 * @tpSubChapter Configuration
 * @tpChapter Integration tests
 * @tpTestCaseDetails Test for resource and provider defined in one class together.
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class AppConfigTest {

    private static Client client;

    @Deployment
    public static Archive<?> deploySimpleResource() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, AppConfigTest.class.getSimpleName() + ".war");

        File[] files = Maven.resolver().loadPomFromFile(System.getProperty("pom"))
                .importRuntimeDependencies().resolve().withTransitivity().asFile();
        war.addAsLibraries(files);

        war.addClass(AppConfigResources.class);
        war.addClass(AppConfigApplication.class);
        war.addAsWebInfResource(AppConfigTest.class.getPackage(), "AppConfigWeb.xml", "web.xml");
        return war;
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, AppConfigTest.class.getSimpleName());
    }

    @Before
    public void setup() {
        client = ClientBuilder.newClient();
    }

    @After
    public void after() throws Exception {
        client.close();
    }

    /**
     * @tpTestDetails Test for apache client
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void apacheClient() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = generateURL("/my");
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response1 = httpclient.execute(httpGet);

        try {
            Assert.assertEquals(200, response1.getStatusLine().getStatusCode());
            Assert.assertEquals("\"hello\"", TestUtil.readString(response1.getEntity().getContent()));
        } finally {
            response1.close();
        }
    }

}
