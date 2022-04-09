package edu.andrews.cas.physics.inventory.model.mongodb.maintenance;

import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import edu.andrews.cas.physics.inventory.model.mongodb.asset.Asset;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceRecord implements DocumentConversion {
    private final Asset asset;
    private MaintenanceEvent currentStatus;
    private final List<MaintenanceEvent> history;
    private final CalibrationDetails calibrationDetails;
    private String notes;

    public MaintenanceRecord() {
        this.asset = null;
        this.history = new ArrayList<>();
        this.calibrationDetails = new CalibrationDetails(null, null, null, null);
    }

    public MaintenanceRecord(Asset asset) {
        this.asset = asset;
        this.history = new ArrayList<>();
        this.calibrationDetails = new CalibrationDetails(null, null, null, null);
    }

    public MaintenanceRecord(MaintenanceEvent currentStatus, List<MaintenanceEvent> history, CalibrationDetails calibrationDetails, String notes) {
        this.asset = null;
        this.currentStatus = currentStatus;
        this.history = history;
        this.calibrationDetails = calibrationDetails;
        this.notes = notes;
    }

    public MaintenanceRecord(Asset asset, List<MaintenanceEvent> history, CalibrationDetails calibrationDetails) {
        this.asset = asset;
        this.history = history;
        this.calibrationDetails = calibrationDetails;
    }

    public MaintenanceEvent getCurrentStatus() {
        return currentStatus;
    }

    public List<MaintenanceEvent> getHistory() {
        return history;
    }

    public CalibrationDetails getCalibrationDetails() {
        return calibrationDetails;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void changeStatus(MaintenanceEvent newStatus) {
        if (this.currentStatus != null) history.add(this.currentStatus);
        this.currentStatus = newStatus;
    }

    public void addCalibrationEvent(LocalDate eventDate) {
        this.calibrationDetails.addEvent(eventDate);
    }

    public void addCalibrationEvent(LocalDate eventDate, LocalDate nextDate) {
        this.calibrationDetails.addEvent(eventDate, nextDate);
    }

    @Override
    public Document toDocument() {
        return new Document()
                .append("currentStatus", getCurrentStatus().toDocument())
                .append("history", getHistory().stream().map(MaintenanceEvent::toDocument).toList())
                .append("calibration", getCalibrationDetails().toDocument())
                .append("notes", getNotes());
    }
}
