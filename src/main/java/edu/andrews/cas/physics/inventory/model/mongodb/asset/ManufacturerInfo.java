package edu.andrews.cas.physics.inventory.model.mongodb.asset;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import org.bson.Document;

public record ManufacturerInfo(String brand, String model, String partNo, String serialNo) implements DocumentConversion {
    @Override
    public Document toDocument() {
        return new Document()
                .append("brand", this.brand())
                .append("model", this.model())
                .append("serialNo", this.serialNo());
    }
}
