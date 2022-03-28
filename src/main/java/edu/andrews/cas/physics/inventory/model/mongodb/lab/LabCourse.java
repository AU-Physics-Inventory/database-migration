package edu.andrews.cas.physics.inventory.model.mongodb.lab;

import java.util.ArrayList;
import java.util.List;

public class LabCourse {
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
}
