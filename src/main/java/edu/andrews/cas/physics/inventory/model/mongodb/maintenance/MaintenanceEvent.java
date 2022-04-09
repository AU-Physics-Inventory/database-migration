package edu.andrews.cas.physics.inventory.model.mongodb.maintenance;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import org.bson.Document;

import java.time.LocalDate;

public record MaintenanceEvent(Status status, LocalDate effectiveDate) implements DocumentConversion {
    @Override
    public Document toDocument() {
        return new Document()
                .append("status", this.status().getCode())
                .append("effectiveDate", this.effectiveDate());
    }
}
