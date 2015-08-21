package net.wasdev.functionaltest.rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import com.ibm.wsspi.kernel.embeddable.Server;
import com.ibm.wsspi.kernel.embeddable.Server.Result;
import com.ibm.wsspi.kernel.embeddable.ServerBuilder;

/**
 * This FVT uses a JUnit rule to start and stop the server. The first half of this test is the extra setup code you need, the second the tests themself.
 */
public class RuleFunctionalTest {

    /**
     * The ExternalRule will start and stop the server for us
     */
    @ClassRule
    public static ExternalResource serverResource = new ServerResource();

    /**
     * <p>This class will construct a {@link Server} instance based on two system properties being set:</p>
     * <ul>
     * <li>liberty.usr.dir: The user directory containing the server to start</li> 
     * <li>liberty.server.name: The name of the server to start</li> 
     * </ul>
     * @see http://www-01.ibm.com/support/knowledgecenter/SSAW57_8.5.5/com.ibm.websphere.wlp.nd.multiplatform.doc/ae/twlp_extend_embed.html?lang=en
     */
    public static class ServerResource extends ExternalResource {

        private final Server server;

        public ServerResource() {
            String usrDir = System.getProperty("liberty.usr.dir");
            String serverName = System.getProperty("liberty.server.name");
            ServerBuilder sb = new ServerBuilder();
            server = sb.setName(serverName).setUserDir(new File(usrDir)).build();
        }
        
        @Override
        protected void before() throws Throwable {
            Future<Result> startReturnCode = server.start();
            Result result = startReturnCode.get();
            assertEquals(true, result.successful());
        }
        
        @Override
        protected void after() {
            Future<Result> stopReturnCode = server.stop();
            try {
                Result result = stopReturnCode.get();
                assertEquals(true, result.successful());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                fail("Caught exception stopping server" + e.getMessage());
            }
            
        }

    }

    @Test
    public void testServletWithName() throws IOException {
        URL testUrl = new URL("http://localhost:9080/functionaltest-application/Hello?name=Iain");
        String response = getResponse(testUrl);
        assertEquals("Hello, Iain!", response);
    }

    @Test
    public void testServletWithDefaultMessage() throws IOException {
        URL testUrl = new URL("http://localhost:9080/functionaltest-application/Hello");
        String response = getResponse(testUrl);
        assertEquals("Hello, World!", response);
    }

    private String getResponse(URL testUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) testUrl.openConnection();
        assertEquals(200, connection.getResponseCode());
        String response = inputStreamToString(connection.getInputStream());
        return response;
    }

    private String inputStreamToString(InputStream inputStream) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(inputStream)) {
            char[] chars = new char[1024];
            StringBuilder responseBuilder = new StringBuilder();

            int read;
            while ((read = isr.read(chars)) != -1) {
                responseBuilder.append(chars, 0, read);
            }
            return responseBuilder.toString();
        }
    }

}
