package edu.andrews.cas.physics.inventory.model.mysql;

import lombok.NonNull;

public record Group(int id, @NonNull String name) {}
