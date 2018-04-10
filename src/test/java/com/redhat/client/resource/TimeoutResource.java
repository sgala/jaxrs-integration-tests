package com.redhat.client.resource;


import com.redhat.client.TimeoutTest;

import javax.ws.rs.QueryParam;

public class TimeoutResource implements TimeoutTest.TimeoutResourceInterface {
    @Override
    public String get(@QueryParam("sleep") int sleep) throws Exception {
        Thread.sleep(sleep * 1000);
        return "OK";
    }
}
