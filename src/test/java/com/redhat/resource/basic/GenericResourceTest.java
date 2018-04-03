package com.redhat.resource.basic;

import com.redhat.resource.basic.resource.GenericResourceCrudResource;
import com.redhat.resource.basic.resource.GenericResourceStudent;
import com.redhat.resource.basic.resource.GenericResourceStudentCrudResource;
import com.redhat.resource.basic.resource.GenericResourceStudentInterface;
import com.redhat.resource.basic.resource.GenericResourceStudentReader;
import com.redhat.resource.basic.resource.GenericResourceStudentWriter;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.ClientBuilder;

/**
 * @tpSubChapter Resource
 * @tpChapter Integration tests
 * @tpTestCaseDetails Tests generic resource class
 * @tpSince RESTEasy 3.0.20
 */
@RunWith(Arquillian.class)
@RunAsClient
public class GenericResourceTest {

   private static GenericResourceStudentInterface proxy;

   @BeforeClass
   public static void setup() {
      ResteasyWebTarget target = (ResteasyWebTarget) ClientBuilder.newClient().target(generateURL(""));
      proxy = target.register(GenericResourceStudentReader.class).register(GenericResourceStudentWriter.class).proxy(GenericResourceStudentInterface.class);
   }

   @Deployment
   public static Archive<?> deploy() {
       WebArchive war = TestUtil.prepareArchive(GenericResourceTest.class.getSimpleName());
       war.addClass(GenericResourceStudent.class);
       war.addClass(GenericResourceStudentInterface.class);
       war.addClass(GenericResourceCrudResource.class);
       return TestUtil.finishContainerPrepare(war, null, GenericResourceStudentCrudResource.class, GenericResourceStudentReader.class, GenericResourceStudentWriter.class);
   }

   private static String generateURL(String path) {
       return PortProviderUtil.generateURL(path, GenericResourceTest.class.getSimpleName());
   }

   @Test
   public void testGet()
   {
      Assert.assertTrue(proxy.get(1).getName().equals("Jozef Hartinger"));
   }

   @Test
   public void testPut()
   {
      proxy.put(2, new GenericResourceStudent("John Doe"));
      Assert.assertTrue(proxy.get(2).getName().equals("John Doe"));
   }
}
