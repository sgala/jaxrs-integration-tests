package com.redhat.cdi.util;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.Random;
import java.util.logging.Logger;

public class UtilityProducer {
    private static Random random = new Random();

    @Produces
    public Logger produceLog(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    public int randomInt() {
        return random.nextInt();
    }
}
