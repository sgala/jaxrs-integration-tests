package com.redhat.resource.basic.resource;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class ApplicationScopeObject
{
   AtomicInteger counter = new AtomicInteger();

   public int getCount()
   {
      return counter.incrementAndGet();
   }
}
