package com.redhat.resource.param;

import com.redhat.resource.param.resource.MatrixParamAsPrimitiveArray;
import com.redhat.resource.param.resource.MatrixParamAsPrimitiveArrayDefault;
import com.redhat.resource.param.resource.MatrixParamAsPrimitiveArrayDefaultNull;
import com.redhat.resource.param.resource.MatrixParamAsPrimitiveArrayDefaultOverride;
import com.redhat.resource.param.resource.MatrixParamAsPrimitiveDefault;
import com.redhat.resource.param.resource.MatrixParamAsPrimitiveDefaultNull;
import com.redhat.resource.param.resource.MatrixParamAsPrimitiveDefaultOverride;
import com.redhat.resource.param.resource.MatrixParamAsPrimitiveList;
import com.redhat.resource.param.resource.MatrixParamAsPrimitiveListDefault;
import com.redhat.resource.param.resource.MatrixParamAsPrimitiveListDefaultNull;
import com.redhat.resource.param.resource.MatrixParamAsPrimitiveListDefaultOverride;
import com.redhat.resource.param.resource.MatrixParamAsPrimitivePrimitives;
import com.redhat.resource.param.resource.MatrixParamAsPrimitiveWrappers;
import com.redhat.resource.param.resource.MatrixParamAsPrimitiveWrappersDefault;
import com.redhat.resource.param.resource.MatrixParamAsPrimitiveWrappersDefaultNull;
import com.redhat.resource.param.resource.MatrixParamAsPrimitiveWrappersDefaultOverride;
import com.redhat.utils.HttpResponseCodes;
import com.redhat.utils.PortProviderUtil;
import com.redhat.utils.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.util.HttpHeaderNames;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter Parameters
 * @tpChapter Integration tests
 * @tpTestCaseDetails Test all variants of primitive matrix parameters (boolean, int, long, float, etc.)
 * @tpSince RESTEasy 3.0.16
 */
@RunWith(Arquillian.class)
@RunAsClient
public class MatrixParamAsPrimitiveTest {

    public static final String ERROR_MESSAGE = "Wrong content of matrix parameter";

    Client client;

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(MatrixParamAsPrimitiveTest.class.getSimpleName());
        war.addClass(MatrixParamAsPrimitiveTest.class);
        return TestUtil.finishContainerPrepare(war, null,
                MatrixParamAsPrimitivePrimitives.class,
                MatrixParamAsPrimitiveDefault.class,
                MatrixParamAsPrimitiveDefaultOverride.class,
                MatrixParamAsPrimitiveDefaultNull.class,
                MatrixParamAsPrimitiveWrappers.class,
                MatrixParamAsPrimitiveWrappersDefault.class,
                MatrixParamAsPrimitiveWrappersDefaultNull.class,
                MatrixParamAsPrimitiveWrappersDefaultOverride.class,
                MatrixParamAsPrimitiveList.class,
                MatrixParamAsPrimitiveListDefault.class,
                MatrixParamAsPrimitiveListDefaultNull.class,
                MatrixParamAsPrimitiveListDefaultOverride.class,
                MatrixParamAsPrimitiveArray.class,
                MatrixParamAsPrimitiveArrayDefault.class,
                MatrixParamAsPrimitiveArrayDefaultNull.class,
                MatrixParamAsPrimitiveArrayDefaultOverride.class);
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, MatrixParamAsPrimitiveTest.class.getSimpleName());
    }

    public void basicTest(String type, String value) {
        String param = ";" + type + "=" + value;
        {
            client = ClientBuilder.newClient();
            Response response = client.target(generateURL("/" + param)).request()
                    .header(HttpHeaderNames.ACCEPT, "application/" + type)
                    .get();
            Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
            response.close();
            client.close();
        }

        {
            client = ClientBuilder.newClient();
            Response response = client.target(generateURL("/wrappers" + param)).request()
                    .header(HttpHeaderNames.ACCEPT, "application/" + type)
                    .get();
            Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
            response.close();
            client.close();
        }

        {
            client = ClientBuilder.newClient();
            Response response = client.target(generateURL("/list" + param + param + param)).request()
                    .header(HttpHeaderNames.ACCEPT, "application/" + type)
                    .get();
            Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
            response.close();
            client.close();
        }

        {
            client = ClientBuilder.newClient();
            Response response = client.target(generateURL("/array" + param + param + param)).request()
                    .header(HttpHeaderNames.ACCEPT, "application/" + type)
                    .get();
            Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
            response.close();
            client.close();
        }
    }

    public void testDefault(String base, String type, String value) {
        {
            client = ClientBuilder.newClient();
            Response response = client.target(generateURL(base + "default/null")).request()
                    .header(HttpHeaderNames.ACCEPT, "application/" + type)
                    .get();
            Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
            response.close();
            client.close();
        }

        {
            client = ClientBuilder.newClient();
            Response response = client.target(generateURL(base + "default")).request()
                    .header(HttpHeaderNames.ACCEPT, "application/" + type)
                    .get();
            Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
            response.close();
            client.close();
        }

        String param = ";" + type + "=" + value;
        {
            client = ClientBuilder.newClient();
            Response response = client.target(generateURL(base + "default/override" + param)).request()
                    .header(HttpHeaderNames.ACCEPT, "application/" + type)
                    .get();
            Assert.assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
            response.close();
            client.close();
        }
    }

    public void testDefault(String type, String value) {
        testDefault("/", type, value);
    }

    public void testWrappersDefault(String type, String value) {
        testDefault("/wrappers/", type, value);
    }

    public void testListDefault(String type, String value) {
        testDefault("/list/", type, value);
    }

    public void testArrayDefault(String type, String value) {
        testDefault("/array/", type, value);
    }

    /**
     * @tpTestDetails Basic test for boolean
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetBoolean() {
        basicTest("boolean", "true");
    }

    /**
     * @tpTestDetails Test default value for boolean
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetBooleanPrimitivesDefault() {
        testDefault("boolean", "true");
    }

    /**
     * @tpTestDetails Test default value of boolean with wrapper
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetBooleanPrimitiveWrapperDefault() {
        testWrappersDefault("boolean", "true");
    }

    /**
     * @tpTestDetails Test default values of list of boolean
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetBooleanPrimitiveListDefault() {
        testListDefault("boolean", "true");
        testArrayDefault("boolean", "true");
    }

    /**
     * @tpTestDetails Basic test for byte
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetByte() {
        basicTest("byte", "127");
    }

    /**
     * @tpTestDetails Test default value of byte
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetBytePrimitivesDefault() {
        testDefault("byte", "127");
    }

    /**
     * @tpTestDetails Test default value of byte with wrapper
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetBytePrimitiveWrappersDefault() {
        testWrappersDefault("byte", "127");
    }

    /**
     * @tpTestDetails Test default value of list and array of byte
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetBytePrimitiveListDefault() {
        testListDefault("byte", "127");
        testArrayDefault("byte", "127");
    }

    /**
     * @tpTestDetails Basic test for short
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetShort() {
        basicTest("short", "32767");
    }

    /**
     * @tpTestDetails Test default values of short
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetShortPrimtivesDefault() {
        testDefault("short", "32767");
    }

    /**
     * @tpTestDetails Test default values of short with wrapper
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetShortPrimtiveWrappersDefault() {
        testWrappersDefault("short", "32767");
    }

    /**
     * @tpTestDetails Test default values of list and array of short
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetShortPrimtiveListDefault() {
        testListDefault("short", "32767");
        testArrayDefault("short", "32767");
    }

    /**
     * @tpTestDetails Basic test for int
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetInt() {
        basicTest("int", "2147483647");
    }

    /**
     * @tpTestDetails Test default value of int
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetIntPrimitivesDefault() {
        testDefault("int", "2147483647");
    }

    /**
     * @tpTestDetails Test default value of int with wrapper
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetIntPrimitiveWrappersDefault() {
        testWrappersDefault("int", "2147483647");
    }

    /**
     * @tpTestDetails Test default value of list and array of integer
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetIntPrimitiveListDefault() {
        testListDefault("int", "2147483647");
        testArrayDefault("int", "2147483647");
    }

    /**
     * @tpTestDetails Basic test for long
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetLong() {
        basicTest("long", "9223372036854775807");
    }

    /**
     * @tpTestDetails Test default value of long
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetLongPrimitivesDefault() {
        testDefault("long", "9223372036854775807");
    }

    /**
     * @tpTestDetails Test default value of long with wrapper
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetLongPrimitiveWrappersDefault() {
        testWrappersDefault("long", "9223372036854775807");
    }

    /**
     * @tpTestDetails Test default value of list and array of long
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetLongPrimitiveListDefault() {
        testListDefault("long", "9223372036854775807");
        testArrayDefault("long", "9223372036854775807");
    }

    /**
     * @tpTestDetails Basic test for float
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetFloat() {
        basicTest("float", "3.14159265");
    }

    /**
     * @tpTestDetails Test default value of float
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetFloatPrimitivesDefault() {
        testDefault("float", "3.14159265");
    }

    /**
     * @tpTestDetails Test default value of float with wrapper
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetFloatPrimitiveWrappersDefault() {
        testWrappersDefault("float", "3.14159265");
    }

    /**
     * @tpTestDetails Test default value of array and list of float
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetFloatPrimitiveListDefault() {
        testListDefault("float", "3.14159265");
        testArrayDefault("float", "3.14159265");
    }

    /**
     * @tpTestDetails Basic test for double
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetDouble() {
        basicTest("double", "3.14159265358979");
    }

    /**
     * @tpTestDetails Test default value of double
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetDoublePrimitivesDefault() {
        testDefault("double", "3.14159265358979");
    }

    /**
     * @tpTestDetails Test default value of double with wrapper
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetDoublePrimitiveWrappersDefault() {
        testWrappersDefault("double", "3.14159265358979");
    }

    /**
     * @tpTestDetails Test default value of list and array of double
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testGetDoublePrimitiveListDefault() {
        testListDefault("double", "3.14159265358979");
        testArrayDefault("double", "3.14159265358979");
    }
    
    /**
     * @tpTestDetails Basic test for char
     * @tpSince RESTEasy 3.0.24
     */
    @Test
    public void testGetChar() {
        basicTest("char", "a");
    }

    /**
     * @tpTestDetails Test default value of char
     * @tpSince RESTEasy 3.0.24
     */
    @Test
    public void testGetCharPrimitivesDefault() {
        testDefault("char", "a");
    }

    /**
     * @tpTestDetails Test default value of char with wrapper
     * @tpSince RESTEasy 3.0.24
     */
    @Test
    public void testGetCharPrimitiveWrappersDefault() {
        testWrappersDefault("char", "a");
    }

    /**
     * @tpTestDetails Test default value of list and array of char
     * @tpSince RESTEasy 3.0.24
     */
    @Test
    public void testGetCharPrimitiveListDefault() {
        testListDefault("char", "a");
        testArrayDefault("char", "a");
    }

    /**
     * @tpTestDetails Test wrong data stored in int format
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testBadPrimitiveValue() {
        {
            client = ClientBuilder.newClient();
            Response response = client.target(generateURL("/;int=abcdef")).request()
                    .header(HttpHeaderNames.ACCEPT, "application/int")
                    .get();
            Assert.assertEquals(HttpResponseCodes.SC_NOT_FOUND, response.getStatus());
            response.close();
            client.close();
        }
    }

    /**
     * @tpTestDetails Test wrong data stored in int format, wrapper is used
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testBadPrimitiveWrapperValue() {
        {
            client = ClientBuilder.newClient();
            Response response = client.target(generateURL("/wrappers;int=abcdef")).request()
                    .header(HttpHeaderNames.ACCEPT, "application/int")
                    .get();
            Assert.assertEquals(HttpResponseCodes.SC_NOT_FOUND, response.getStatus());
            response.close();
            client.close();
        }
    }

    /**
     * @tpTestDetails Test wrong data stored in list of int
     * @tpSince RESTEasy 3.0.16
     */
    @Test
    public void testBadPrimitiveListValue() {
        {
            client = ClientBuilder.newClient();
            Response response = client.target(generateURL("/list;int=abcdef;int=abcdef")).request()
                    .header(HttpHeaderNames.ACCEPT, "application/int")
                    .get();
            Assert.assertEquals(HttpResponseCodes.SC_NOT_FOUND, response.getStatus());
            response.close();
            client.close();
        }
    }
}
