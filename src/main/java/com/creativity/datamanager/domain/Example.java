package com.creativity.datamanager.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Document(collection = "examples")
public class Example {

    @Id
    private final String id = UUID.randomUUID().toString();

    private final Set<String> input;
    private final Set<String> output;

    public Example(Set<String> input, Set<String> output) {
        this.input = input;
        this.output = output;
    }

    public Set<String> getInput() {
        return input;
    }

    public Set<String> getOutput() {
        return output;
    }

    @Override
    public String toString() {
        return "Example{" +
                "id='" + id + '\'' +
                ", input=" + input +
                ", output=" + output +
                '}';
    }
}
