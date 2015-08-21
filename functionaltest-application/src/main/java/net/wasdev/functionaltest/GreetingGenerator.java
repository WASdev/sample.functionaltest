package net.wasdev.functionaltest;

public class GreetingGenerator {

    private final String message;
    
    public GreetingGenerator(String name) {
        name = (name != null) ? name : "World";
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Hello, ").append(name).append("!");
        message = messageBuilder.toString();
    }

    public String getGreeting() {
        return message;
    }

}
