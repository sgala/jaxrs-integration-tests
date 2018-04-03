package com.redhat.resource.basic.resource;

import com.redhat.resource.basic.ResponseCommittedTest;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("")
public class ResponseCommittedResource {

   @GET
   @Path("")
   public Response works() throws Exception {

      Map<Class<?>, Object> contextDataMap = ResteasyProviderFactory.getContextDataMap();
      HttpResponse httpResponse = (HttpResponse) contextDataMap.get(HttpResponse.class);
      httpResponse.sendError(ResponseCommittedTest.TEST_STATUS);
      Response response = Response.ok("ok").build();
      return response;
   }
}
