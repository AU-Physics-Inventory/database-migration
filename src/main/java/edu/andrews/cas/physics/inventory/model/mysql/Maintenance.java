package edu.andrews.cas.physics.inventory.model.mysql;

import java.time.LocalDate;

public class Maintenance implements Comparable<Maintenance> {

    private int id;
    private String repair_status;
    private LocalDate status_change_date;
    private String status_history;
    private LocalDate last_calibration_date;
    private LocalDate next_calibration_date;
    private String calibration_history;
    private String notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRepair_status() {
        return repair_status;
    }

    public void setRepair_status(String repair_status) {
        this.repair_status = repair_status;
    }

    public LocalDate getStatus_change_date() {
        return status_change_date;
    }

    public void setStatus_change_date(LocalDate status_change_date) {
        this.status_change_date = status_change_date;
    }

    public String getStatus_history() {
        return status_history;
    }

    public void setStatus_history(String status_history) {
        this.status_history = status_history;
    }

    public LocalDate getLast_calibration_date() {
        return last_calibration_date;
    }

    public void setLast_calibration_date(LocalDate last_calibration_date) {
        this.last_calibration_date = last_calibration_date;
    }

    public LocalDate getNext_calibration_date() {
        return next_calibration_date;
    }

    public void setNext_calibration_date(LocalDate next_calibration_date) {
        this.next_calibration_date = next_calibration_date;
    }

    public String getCalibration_history() {
        return calibration_history;
    }

    public void setCalibration_history(String calibration_history) {
        this.calibration_history = calibration_history;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int compareTo(Maintenance m) {
        return Integer.compare(this.id, m.getId());
    }
}
