package edu.andrews.cas.physics.inventory.model.mongodb.lab.resource;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.Document;
import org.bson.types.ObjectId;

public class LabResource implements DocumentConversion {
    private final ResourceType type;
    private final ObjectId id;
    private Quantities quantities;
    private final String notes;

    public LabResource(ResourceType type, ObjectId id, Quantities quantities) {
        this.type = type;
        this.id = id;
        this.quantities = quantities;
        this.notes = null;
    }

    public LabResource(ResourceType type, ObjectId id, Quantities quantities, String notes) {
        this.type = type;
        this.id = id;
        this.quantities = quantities;
        this.notes = notes;
    }

    public ResourceType getType() {
        return type;
    }

    public ObjectId getID() {
        return id;
    }

    public Quantities getQuantities() {
        return quantities;
    }

    public String getNotes() {
        return notes;
    }

    public void setQuantities(Quantities quantities) {
        this.quantities = quantities;
    }

    @Override
    public Document toDocument() {
        return new Document()
                .append("type", switch (getType()) {
                    case ASSET -> "asset";
                    case SET -> "set";
                    case GROUP -> "group";
                })
                .append("typeID", getID())
                .append("quantities", getQuantities().toDocument())
                .append("notes", getNotes());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LabResource that = (LabResource) o;

        return new EqualsBuilder().append(id, that.id).append(type, that.type).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(type).append(id).toHashCode();
    }
}
