package org.example.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Task{
    private int id;
    private String name;
    private String description;


    public Task(@JsonProperty("id") int id,
                @JsonProperty("name") String name,
                @JsonProperty("description") String description) {
        this.name = String.valueOf(name);
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
