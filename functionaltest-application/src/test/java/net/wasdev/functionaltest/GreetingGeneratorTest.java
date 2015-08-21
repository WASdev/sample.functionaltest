package net.wasdev.functionaltest;

import net.wasdev.functionaltest.GreetingGenerator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GreetingGeneratorTest {

    @Test
    public void testDefaultMessage() {
        testGreeting(null, "Hello, World!");
    }
    
    @Test
    public void testHelloPerson() {
        testGreeting("Iain", "Hello, Iain!");
    }
    
    private void testGreeting(String input, String expectedMessage) {
        GreetingGenerator messageGenerator = new GreetingGenerator(input);
        assertEquals(expectedMessage, messageGenerator.getGreeting());
    }
    
}
