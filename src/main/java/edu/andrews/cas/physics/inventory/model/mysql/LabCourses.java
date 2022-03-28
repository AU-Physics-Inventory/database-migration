package edu.andrews.cas.physics.inventory.model.mysql;

import lombok.NonNull;

public record LabCourses(int id, @NonNull String courseName, @NonNull String courseNumber) {
}
