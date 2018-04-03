package com.redhat.resource.basic;

import com.redhat.resource.basic.resource.LogHandler;
import com.redhat.resource.basic.resource.MultipleEndpointsWarningResource;
import com.redhat.utils.PermissionUtil;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.LoggingPermission;

/**
 * @tpSubChapter Resources
 * @tpChapter Integration tests
 * @tpTestCaseDetails Regression tests for RESTEASY-1398
 * @tpSince RESTEasy 3.0.20
 */
@RunWith(Arquillian.class)
@RunAsClient
public class MultipleEndpointsWarningTest
{
   private static Client client;
   
   @Deployment
   public static Archive<?> deploy() {
       WebArchive war = TestUtil.prepareArchive(MultipleEndpointsWarningTest.class.getSimpleName());
       war.addClass(LogHandler.class);
      // Test registers it's own LogHandler
      war.addAsManifestResource(PermissionUtil.createPermissionsXmlAsset(new LoggingPermission("control", "")), "permissions.xml");
       return TestUtil.finishContainerPrepare(war, null, MultipleEndpointsWarningResource.class);
   }

   private static String generateURL(String path) {
       return PortProviderUtil.generateURL(path, MultipleEndpointsWarningTest.class.getSimpleName());
   }
   
   @BeforeClass
   public static void setUp() throws Exception {
      client = ClientBuilder.newClient();
      client.target(generateURL("/setup")).request().get();
   }

   @AfterClass
   public static void tearDown() {
      client.target(generateURL("/teardown")).request().get();
      client.close();
   }
   
   @Test
   public void testUnique() throws Exception {
      Response response = client.target(generateURL("/unique/")).request().accept(MediaType.TEXT_PLAIN).get();
      Assert.assertEquals("Incorrectly logged " + LogHandler.MESSAGE_CODE, new Long(0), response.readEntity(long.class));

      response = client.target(generateURL("/unique")).request().get();
      Assert.assertEquals("Incorrectly logged " + LogHandler.MESSAGE_CODE, new Long(0), response.readEntity(long.class));

      response = client.target(generateURL("/unique")).request().accept(MediaType.TEXT_PLAIN).get();
      Assert.assertEquals("Incorrectly logged " + LogHandler.MESSAGE_CODE, new Long(0), response.readEntity(long.class));

      response = client.target(generateURL("/1")).request().get();
      Assert.assertEquals("Incorrectly logged " + LogHandler.MESSAGE_CODE, new Long(0), response.readEntity(long.class));
   }

   @Test
   public void testDifferentVerbs() throws Exception {
      Response response = client.target(generateURL("/verbs")).request().accept(MediaType.TEXT_PLAIN).get();
      Assert.assertEquals("Incorrectly logged " + LogHandler.MESSAGE_CODE, new Long(0), response.readEntity(long.class));

      response = client.target(generateURL("/verbs")).request().accept(MediaType.TEXT_PLAIN, MediaType.WILDCARD).get();
      Assert.assertEquals("Incorrectly logged " + LogHandler.MESSAGE_CODE, new Long(0), response.readEntity(long.class));

      response = client.target(generateURL("/verbs")).request().get();
      Assert.assertEquals("Incorrectly logged " + LogHandler.MESSAGE_CODE, new Long(0), response.readEntity(long.class));
   }
}
