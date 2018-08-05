package com.creativity.datamanager.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "photos")
public class Photo {

    @Id
    private final String id;
    private Set<String> labels;

    public Photo(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id='" + id + '\'' +
                '}';
    }
}
