package edu.andrews.cas.physics.inventory.model.mysql;

import lombok.NonNull;

public record LabCourses(@NonNull String courseName, @NonNull String courseNumber) {}
