package edu.andrews.cas.physics.inventory.model.mongodb.lab.resource;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class LabResource {
    private final ResourceType type;
    private final int id;
    private final Quantities quantities;
    private final String notes;

    public LabResource(ResourceType type, int id, Quantities quantities) {
        this.type = type;
        this.id = id;
        this.quantities = quantities;
        this.notes = null;
    }

    public LabResource(ResourceType type, int id, Quantities quantities, String notes) {
        this.type = type;
        this.id = id;
        this.quantities = quantities;
        this.notes = notes;
    }

    public ResourceType getType() {
        return type;
    }

    public int getID() {
        return id;
    }

    public Quantities getQuantities() {
        return quantities;
    }

    public String getNotes() {
        return notes;
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
