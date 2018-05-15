package com.redhat.client;

import com.redhat.utils.PermissionUtil;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import java.io.File;
import java.io.FilePermission;
import java.lang.reflect.ReflectPermission;
import java.util.PropertyPermission;
import java.util.logging.LoggingPermission;

/**
 * @tpSubChapter Resteasy-client
 * @tpChapter Unit tests
 * @tpSince RESTEasy 3.0.17
 */
@RunWith(Arquillian.class)
public class ClientBuilderTest {

    @SuppressWarnings(value = "unchecked")
    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(ClientBuilderTest.class.getSimpleName());
        war.addClass(TestUtil.class);
        // Arquillian in the deployment and use of TestUtil
        war.addAsManifestResource(PermissionUtil.createPermissionsXmlAsset(new ReflectPermission("suppressAccessChecks"),
                new FilePermission(TestUtil.getJbossHome() + File.separator + "standalone" + File.separator + "log" +
                        File.separator + "server.log", "read"),
                new LoggingPermission("control", ""),
                new PropertyPermission("arquillian.*", "read"),
                new PropertyPermission("jboss.home.dir", "read"),
                new RuntimePermission("accessDeclaredMembers")
        ), "permissions.xml");
        return TestUtil.finishContainerPrepare(war, null, (Class<?>[]) null);
    }


    public static class FeatureReturningFalse implements Feature {
        @Override
        public boolean configure(FeatureContext context) {
            // false returning feature is not to be registered
            return false;
        }
    }

    private int getWarningCount() {
        return TestUtil.getWarningCount("RESTEASY002155", true);
    }

    /**
     * @tpTestDetails Register class twice to the client
     * @tpPassCrit Warning will be raised that second class registration is ignored
     * @tpSince RESTEasy 3.0.17
     */
    @Test
    public void testDoubleClassRegistration() {
        Client client = ClientBuilder.newClient();
        client.register(FeatureReturningFalse.class).register(FeatureReturningFalse.class);
        client.close();
    }

    /**
     * @tpTestDetails Register provider instance twice to the client
     * @tpPassCrit Warning will be raised that second provider instance registration is ignored
     * @tpSince RESTEasy 3.0.17
     */
    @Test
    public void testDoubleRegistration() {
        Client client = ClientBuilder.newClient();
        Object reg = new FeatureReturningFalse();
        client.register(reg).register(reg);
        client.register(FeatureReturningFalse.class).register(FeatureReturningFalse.class);
        client.close();
    }
}
