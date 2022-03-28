package edu.andrews.cas.physics.inventory.model.mongodb.asset;

import lombok.NonNull;

import java.net.URL;

public record Vendor(@NonNull String name, URL url) {}
