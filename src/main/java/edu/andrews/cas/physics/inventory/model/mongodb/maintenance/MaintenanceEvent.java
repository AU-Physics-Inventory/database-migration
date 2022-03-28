package edu.andrews.cas.physics.inventory.model.mongodb.maintenance;

import java.time.LocalDate;

public record MaintenanceEvent(Status status, LocalDate effectiveDate) {}
