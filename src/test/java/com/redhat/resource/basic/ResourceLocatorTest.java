package com.redhat.resource.basic;

import com.redhat.resource.basic.resource.ResourceLocatorAbstractAnnotationFreeResouce;
import com.redhat.resource.basic.resource.ResourceLocatorAnnotationFreeSubResource;
import com.redhat.resource.basic.resource.ResourceLocatorBaseResource;
import com.redhat.resource.basic.resource.ResourceLocatorCollectionResource;
import com.redhat.resource.basic.resource.ResourceLocatorDirectory;
import com.redhat.resource.basic.resource.ResourceLocatorQueueReceiver;
import com.redhat.resource.basic.resource.ResourceLocatorReceiver;
import com.redhat.resource.basic.resource.ResourceLocatorRootInterface;
import com.redhat.resource.basic.resource.ResourceLocatorSubInterface;
import com.redhat.resource.basic.resource.ResourceLocatorSubresource;
import com.redhat.resource.basic.resource.ResourceLocatorSubresource2;
import com.redhat.resource.basic.resource.ResourceLocatorSubresource3;
import com.redhat.resource.basic.resource.ResourceLocatorSubresource3Interface;
import com.redhat.utils.HttpServletResponse;
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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter Resource
 * @tpChapter Integration tests
 * @tpTestCaseDetails Tests path encoding
 * @tpSince RESTEasy 3.0.20
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ResourceLocatorTest
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
       WebArchive war = TestUtil.prepareArchive(ResourceLocatorTest.class.getSimpleName());
       war.addClass(ResourceLocatorQueueReceiver.class)
          .addClass(ResourceLocatorReceiver.class)
          .addClass(ResourceLocatorRootInterface.class)
          .addClass(ResourceLocatorSubInterface.class)
          .addClass(ResourceLocatorSubresource3Interface.class)
          ;
       return TestUtil.finishContainerPrepare(war, null,
             ResourceLocatorAbstractAnnotationFreeResouce.class,
             ResourceLocatorAnnotationFreeSubResource.class,
             ResourceLocatorBaseResource.class,
             ResourceLocatorCollectionResource.class,
             ResourceLocatorDirectory.class,
             ResourceLocatorSubresource.class,
             ResourceLocatorSubresource2.class,
             ResourceLocatorSubresource3.class
             );
   }

   private String generateURL(String path) {
       return PortProviderUtil.generateURL(path, ResourceLocatorTest.class.getSimpleName());
   }


   /**
    * @tpTestDetails Resource locator returns proxied resource.
    * @tpSince RESTEasy 3.0.20
    */
   @Test
   public void testProxiedSubresource() throws Exception
   {
      WebTarget target = client.target(generateURL("/proxy/3"));
      Response res = target.queryParam("foo", "1.2").queryParam("foo", "1.3").request().get();
      res.close();
   }


   /**
    * @tpTestDetails 1) Resource locator returns resource; 2) Resource locator returns resource locator.
    * @tpSince RESTEasy 3.0.20
    */
   @Test
   public void testSubresource() throws Exception
   {
      {
         Response response = client.target(generateURL("/base/1/resources")).request().get();
         Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
         Assert.assertEquals(ResourceLocatorSubresource.class.getName(), response.readEntity(String.class));
      }

      {
         Response response = client.target(generateURL("/base/1/resources/subresource2/stuff/2/bar")).request().get();
         Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
         Assert.assertEquals(ResourceLocatorSubresource2.class.getName() + "-2", response.readEntity(String.class));
      }
   }


   /**
    * @tpTestDetails Two matching metods, one a resource locator, the other a resource method.
    * @tpSince RESTEasy 3.0.20
    */
   @Test
   public void testSameUri() throws Exception
   {
      Response response = client.target(generateURL("/directory/receivers/1")).request().delete();
      Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
      Assert.assertEquals(ResourceLocatorDirectory.class.getName(), response.readEntity(String.class));
   }


   /**
    * @tpTestDetails Locator returns resource which inherits annotations from an interface.
    * @tpSince RESTEasy 3.0.20
    */
   @Test
   public void testAnnotationFreeSubresource() throws Exception
   {
      {
         Response response = client.target(generateURL("/collection/annotation_free_subresource")).request().get();
         Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
         Assert.assertEquals("got", response.readEntity(String.class));
         Assert.assertNotNull(response.getHeaderString("Content-Type"));
         Assert.assertNotNull(response.getHeaderString("Content-Type"));
         Assert.assertTrue(response.getHeaderString("Content-Type").contains(MediaType.TEXT_PLAIN));
      }

      {
         Builder request = client.target(generateURL("/collection/annotation_free_subresource")).request();
         Response response = request.post(Entity.entity("hello!".getBytes(), MediaType.TEXT_PLAIN));
         Assert.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
         Assert.assertEquals("posted: hello!", response.readEntity(String.class));
      }
   }
}
