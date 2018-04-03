package com.redhat.resource.basic;

import com.redhat.resource.basic.resource.InheritenceParentResource;
import com.redhat.resource.basic.resource.InheritenceParentResourceImpl;
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
 * @tpTestCaseDetails Tests annotation inheritence from interface.
 * @tpSince RESTEasy 3.0.20
 */
@RunWith(Arquillian.class)
@RunAsClient
public class InheritanceTest
{
   private static Client client;

   @Deployment
   public static Archive<?> deploy() {
       WebArchive war = TestUtil.prepareArchive(InheritanceTest.class.getSimpleName());
       war.addClass(InheritenceParentResource.class);
       return TestUtil.finishContainerPrepare(war, null, InheritenceParentResourceImpl.class);
   }

   private String generateURL(String path) {
       return PortProviderUtil.generateURL(path, InheritanceTest.class.getSimpleName());
   }
   
   @BeforeClass
   public static void beforeSub() {
      client = ClientBuilder.newClient();
   }
   
   @AfterClass
   public static void afterSub() {
      client.close();
   }

   @Test
   public void Test1() throws Exception {
      Builder builder = client.target(generateURL("/InheritanceTest")).request();
      builder.header("Accept", "text/plain");
      Response response = builder.get();
      Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
      Assert.assertEquals("First", response.readEntity(String.class));
   }
}