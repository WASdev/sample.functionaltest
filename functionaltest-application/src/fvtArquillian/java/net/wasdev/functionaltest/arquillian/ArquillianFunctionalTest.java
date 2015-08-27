package net.wasdev.functionaltest.arquillian;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import net.wasdev.functionaltest.Greeting;
import net.wasdev.functionaltest.GreetingGenerator;

/**
 * This FVT uses a Arquillian to start and stop the server. The first half of this test is the extra setup code you need, the second the tests themself.
 */
@RunWith(Arquillian.class)
public class ArquillianFunctionalTest {

    @Deployment(testable=false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war").addClasses(Greeting.class, GreetingGenerator.class);
    }

    @Test
    public void testServletWithName(@ArquillianResource URL url) throws IOException {
        URL testUrl = new URL(url, "Hello?name=Iain");
        String response = getResponse(testUrl);
        assertEquals("Hello, Iain!", response);
    }

    @Test
    public void testServletWithDefaultMessage(@ArquillianResource URL url) throws IOException {
        URL testUrl = new URL(url, "Hello");
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
