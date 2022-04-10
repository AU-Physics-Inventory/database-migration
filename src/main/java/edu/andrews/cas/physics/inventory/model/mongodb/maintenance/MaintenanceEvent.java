package edu.andrews.cas.physics.inventory.model.mongodb.maintenance;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.Document;

import java.time.LocalDate;

public record MaintenanceEvent(Status status, LocalDate effectiveDate) implements DocumentConversion {
    @Override
    public Document toDocument() {
        return new Document()
                .append("status", this.status().getCode())
                .append("effectiveDate", this.effectiveDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MaintenanceEvent that = (MaintenanceEvent) o;

        return new EqualsBuilder().append(status, that.status).append(effectiveDate, that.effectiveDate).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(status).append(effectiveDate).toHashCode();
    }
}
