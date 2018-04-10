package com.redhat.asynch.resource;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;

@Provider
public class AsyncPostProcessingMsgBodyWriterInterceptor implements WriterInterceptor {
    public static boolean called;

   @Override
   public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException
   {
      called = true;
      context.proceed();
   }
}
