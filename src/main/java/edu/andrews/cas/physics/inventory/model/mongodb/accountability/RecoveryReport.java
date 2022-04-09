package edu.andrews.cas.physics.inventory.model.mongodb.accountability;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import lombok.NonNull;
import edu.andrews.cas.physics.measurement.Quantity;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;

public record RecoveryReport(@NonNull @BsonProperty("quantity") Quantity quantityRecovered, @NonNull @BsonProperty("date") LocalDate dateRecovered,
                             @NonNull String reportedBy) implements DocumentConversion {
    @Override
    public Document toDocument() {
        return new Document()
                .append("quantity", this.quantityRecovered().toDocument())
                .append("date", this.dateRecovered())
                .append("reportedBy", this.reportedBy());
    }
}
