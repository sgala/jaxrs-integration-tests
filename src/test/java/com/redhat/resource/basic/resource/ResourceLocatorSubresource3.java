package com.redhat.resource.basic.resource;

import org.junit.Assert;

import java.util.List;

public class ResourceLocatorSubresource3 implements ResourceLocatorSubresource3Interface {
   
   @SuppressWarnings("unused")
   @Override
   public String get(List<Double> params)
   {
      Assert.assertNotNull(params);
      Assert.assertEquals(2, params.size());
      double p1 = params.get(0);
      double p2 = params.get(1);
      return "Subresource3";
   }
}
