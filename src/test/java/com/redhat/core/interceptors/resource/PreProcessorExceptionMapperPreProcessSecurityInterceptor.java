package com.redhat.core.interceptors.resource;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class PreProcessorExceptionMapperPreProcessSecurityInterceptor implements ContainerRequestFilter {

   @Override
   public void filter(ContainerRequestContext requestContext) throws IOException
   {
      throw new PreProcessorExceptionMapperCandlepinUnauthorizedException();
   }
}
