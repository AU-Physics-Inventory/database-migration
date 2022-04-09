package edu.andrews.cas.physics.inventory.model.mongodb.lab;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class LabCourse implements DocumentConversion {
    private final String name;
    private final String number;
    private final String location;
    private final List<Lab> labs;

    public LabCourse(String name, String number, String location) {
        this.name = name;
        this.number = number;
        this.location = location;
        this.labs = new ArrayList<>();
    }

    public LabCourse(String name, String number, String location, List<Lab> labs) {
        this.name = name;
        this.number = number;
        this.location = location;
        this.labs = labs;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getLocation() {
        return location;
    }

    public List<Lab> getLabs() {
        return labs;
    }

    public void addLab(Lab lab) {
        if (!this.labs.contains(lab)) this.labs.add(lab);
    }

    @Override
    public Document toDocument() {
        return new Document()
                .append("name", getName())
                .append("number", getNumber())
                .append("location", getLocation())
                .append("labs", getLabs().stream().map(Lab::toDocument).toList());
    }
}
