package com.redhat.providers.file.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.File;

@Path("/test")
public class TempFileDeletionResource
{
   @POST
   @Path("post")
   public Response post(File file) throws Exception
   {
      return Response.ok(file.getPath()).build();
   }
}
