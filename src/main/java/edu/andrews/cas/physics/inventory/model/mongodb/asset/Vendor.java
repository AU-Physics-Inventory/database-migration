package edu.andrews.cas.physics.inventory.model.mongodb.asset;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import lombok.NonNull;
import org.bson.Document;

import java.net.URL;

public record Vendor(@NonNull String name, URL url) implements DocumentConversion {
    @Override
    public Document toDocument() {
        return new Document()
                .append("name", this.name())
                .append("url", this.url());
    }
}
