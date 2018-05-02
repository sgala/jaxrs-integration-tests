package com.redhat.resource.basic.resource;

import org.junit.Assert;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

public class ResourceLocatorSubresource2{
   
   @GET
   @Path("stuff/{param}/bar")
   public String doGet(@PathParam("param") String param, @Context UriInfo uri) {
      System.out.println("Uri Ancesstors for Subresource2.doGet():");
      Assert.assertEquals(4, uri.getMatchedURIs().size());
      Assert.assertEquals("base/1/resources/subresource2", uri.getMatchedURIs().get(1));
      Assert.assertEquals("base/1/resources", uri.getMatchedURIs().get(2));
      Assert.assertEquals("", uri.getMatchedURIs().get(3));
      for (String ancestor : uri.getMatchedURIs()) System.out.println("   " + ancestor);


      System.out.println("Uri Ancesstors Object for Subresource2.doGet():");
      Assert.assertEquals(3, uri.getMatchedResources().size());
      for (Object ancestor : uri.getMatchedResources()) System.out.println("   " + ancestor.getClass().getName());
      Assert.assertEquals("2", param);
      return this.getClass().getName() + "-" + param;
   }
}
