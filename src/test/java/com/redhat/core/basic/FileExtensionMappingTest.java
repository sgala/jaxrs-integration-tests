package com.redhat.core.basic;

import com.redhat.core.basic.resources.FileExtensionMappingApplication;
import com.redhat.core.basic.resources.FileExtensionMappingResource;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.RCategory;
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
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter MediaType
 * @tpChapter Integration tests
 * @tpTestCaseDetails Mapping file extensions to media types
 * @tpSince RESTEasy 3.0.20
 */
@RunWith(Arquillian.class)
@RunAsClient
@Category(RCategory.class)
public class FileExtensionMappingTest
{
   static Client client;

   @BeforeClass
   public static void setup() {
       client = ClientBuilder.newClient();
   }

   @AfterClass
   public static void close() {
       client.close();
   }

   @Deployment
   public static Archive<?> deploy() {
       WebArchive war = TestUtil.prepareArchive(FileExtensionMappingTest.class.getSimpleName());
       war.addClass(FileExtensionMappingApplication.class);
       war.addAsWebInfResource(FileExtensionMappingTest.class.getPackage(), "FileExtensionMapping.xml", "web.xml");
       Archive<?> archive = TestUtil.finishContainerPrepare(war, null, FileExtensionMappingResource.class);
       return archive;
   }

   private String generateURL(String path) {
       return PortProviderUtil.generateURL(path, FileExtensionMappingTest.class.getSimpleName());
   }
   
   /**
    * @tpTestDetails Map suffix .txt to Accept: text/plain
    * @tpSince RESTEasy 3.0.20
    */
   @Test
   public void testFileExtensionMappingPlain() throws Exception {
//      System.out.println("url: " + client.target(generateURL("/test.txt")).queryParam("query", "whosOnFirst").getUri());
      Response response = client.target(generateURL("/test.txt")).queryParam("query", "whosOnFirst").request().get();
      String entity = response.readEntity(String.class);
      Assert.assertEquals(200, response.getStatus());
      Assert.assertEquals("plain: whosOnFirst", entity);
   }
   
   /**
    * @tpTestDetails Map suffix .html to Accept: text/html
    * @tpSince RESTEasy 3.0.20
    */
   @Test
   public void testFileExtensionMappingHtml() throws Exception
   {
//      System.out.println("url: " + client.target(generateURL("/test.html")).queryParam("query", "whosOnFirst").getUri());
      Response response = client.target(generateURL("/test.html")).queryParam("query", "whosOnFirst").request().get();
      String entity = response.readEntity(String.class);
      Assert.assertEquals(200, response.getStatus());
      Assert.assertEquals("html: whosOnFirst", entity);
   }
}
