package edu.andrews.cas.physics.inventory.model.mongodb.accountability;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import edu.andrews.cas.physics.inventory.model.mongodb.asset.Asset;
import lombok.NonNull;
import edu.andrews.cas.physics.measurement.Quantity;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;

public record MissingReport(@BsonProperty("quantity") Quantity quantityMissing, @BsonProperty("date") LocalDate reportDate, String reportedBy) implements DocumentConversion {
    @Override
    public Document toDocument() {
        return new Document()
                .append("quantity", this.quantityMissing().toDocument())
                .append("date", this.reportDate())
                .append("reportedBy", this.reportedBy());
    }
}