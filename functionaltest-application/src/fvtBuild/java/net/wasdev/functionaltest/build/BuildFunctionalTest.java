package net.wasdev.functionaltest.build;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

/**
 * This FVT uses the build system to start and stop the server.  Therefore there isn't much in here other than the tests themselves.
 */
public class BuildFunctionalTest {

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
