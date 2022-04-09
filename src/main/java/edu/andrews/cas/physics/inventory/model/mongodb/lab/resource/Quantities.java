package edu.andrews.cas.physics.inventory.model.mongodb.lab.resource;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import edu.andrews.cas.physics.measurement.Quantity;
import org.bson.Document;

public record Quantities(Quantity frontTable, Quantity perStation) implements DocumentConversion {
    @Override
    public Document toDocument() {
        return new Document()
                .append("frontTable", this.frontTable().toDocument())
                .append("perStation", this.perStation().toDocument());
    }
}
