package edu.andrews.cas.physics.inventory.model.mongodb.accountability;

import edu.andrews.cas.physics.inventory.model.mongodb.asset.Asset;
import lombok.NonNull;
import edu.andrews.cas.physics.measurement.Quantity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;

public record MissingReport(@NonNull @BsonProperty("quantity") Quantity quantityMissing, @NonNull @BsonProperty("date") LocalDate reportDate,
                            @NonNull String reportedBy) {}