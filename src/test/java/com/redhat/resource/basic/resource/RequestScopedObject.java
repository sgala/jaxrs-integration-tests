package com.redhat.resource.basic.resource;

import javax.enterprise.context.RequestScoped;
import java.util.concurrent.atomic.AtomicInteger;

@RequestScoped
public class RequestScopedObject
{

   AtomicInteger count = new AtomicInteger();
   public int getCount() {
      return count.incrementAndGet();
   }
}
