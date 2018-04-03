package com.redhat.resource.path;

import com.redhat.resource.path.resource.EncodedPathResource;
import com.redhat.utils.HttpResponseCodes;
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
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter Resource
 * @tpChapter Integration tests
 * @tpTestCaseDetails Tests path encoding
 * @tpSince RESTEasy 3.0.20
 */
@RunWith(Arquillian.class)
@RunAsClient
public class EncodedPathTest
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
       WebArchive war = TestUtil.prepareArchive(EncodedPathTest.class.getSimpleName());
       return TestUtil.finishContainerPrepare(war, null, EncodedPathResource.class);
   }

   private String generateURL(String path) {
       return PortProviderUtil.generateURL(path, EncodedPathTest.class.getSimpleName());
   }

   private void _test(String path)
   {
      Builder builder = client.target(generateURL(path)).request();
      Response response = null;
      try
      {
         response = builder.get();
         Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
      finally
      {
         response.close();
      }
   }

   @Test
   public void testEncoded() throws Exception
   {
      _test("/hello%20world");
      _test("/goodbye%7Bworld");
   }
}