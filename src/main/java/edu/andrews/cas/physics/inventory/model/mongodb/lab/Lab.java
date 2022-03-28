package edu.andrews.cas.physics.inventory.model.mongodb.lab;

import edu.andrews.cas.physics.inventory.model.mongodb.lab.resource.LabResource;

import java.util.ArrayList;
import java.util.List;

public class Lab {
    private final String name;
    private final List<LabResource> resources;

    public Lab(String name) {
        this.name = name;
        this.resources = new ArrayList<>();
    }

    public Lab(String name, List<LabResource> resources) {
        this.name = name;
        this.resources = resources;
    }

    public String getName() {
        return name;
    }

    public List<LabResource> getResources() {
        return resources;
    }

    public void addResource(LabResource resource) {
        if (!this.resources.contains(resource)) this.resources.add(resource);
    }
}
