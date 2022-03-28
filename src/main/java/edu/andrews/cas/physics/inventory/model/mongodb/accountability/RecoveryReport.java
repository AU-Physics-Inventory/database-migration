package edu.andrews.cas.physics.inventory.model.mongodb.accountability;

import lombok.NonNull;
import edu.andrews.cas.physics.measurement.Quantity;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;

public record RecoveryReport(@NonNull @BsonProperty("quantity") Quantity quantityRecovered, @NonNull @BsonProperty("date") LocalDate dateRecovered,
                             @NonNull String reportedBy) {}
