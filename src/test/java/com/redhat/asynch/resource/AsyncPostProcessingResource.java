package com.redhat.asynch.resource;

import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

@Path("/")
public class AsyncPostProcessingResource {

    private static Logger logger = Logger.getLogger(AsyncPostProcessingResource.class);

    @GET
    @Path("sync")
    public Response sync() {
        return Response.ok().entity("sync").build();
    }

    @GET
    @Path("async/delay")
    public void asyncDelay(@Suspended final AsyncResponse response) throws Exception {
        response.setTimeout(10000, TimeUnit.MILLISECONDS);
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    Response jaxrs = Response.ok("async/delay").build();
                    response.resume(jaxrs);
                } catch (Exception e) {
                    logger.info("Error: " + e.getStackTrace());
                }
            }
        };
        t.start();
    }

    @GET
    @Path("async/nodelay")
    public void asyncNoDelay(@Suspended final AsyncResponse response) throws Exception {
        response.setTimeout(10000, TimeUnit.MILLISECONDS);
        Response jaxrs = Response.ok("async/nodelay").build();
        response.resume(jaxrs);
    }

}
