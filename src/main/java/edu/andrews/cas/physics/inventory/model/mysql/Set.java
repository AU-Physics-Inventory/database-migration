package edu.andrews.cas.physics.inventory.model.mysql;

import lombok.NonNull;

public record Set(int id, @NonNull String name) {}
