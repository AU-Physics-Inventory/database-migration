package edu.andrews.cas.physics.inventory.model.mongodb.maintenance;

import lombok.NonNull;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;
import java.util.List;

public class CalibrationDetails {
    @BsonProperty("next")
    private LocalDate nextDate;

    @BsonProperty("last")
    private LocalDate lastDate;

    private Long interval;
    private final List<LocalDate> history;

    public CalibrationDetails(LocalDate nextDate, LocalDate lastDate, Long interval, List<LocalDate> history) {
       this.nextDate = nextDate;
       this.lastDate = lastDate;
       this.history = history;
       this.setInterval(interval);
    }

    public void addEvent(LocalDate eventDate) {
        this.history.add(this.lastDate);
        this.lastDate = eventDate;
        if (this.interval != null) this.nextDate = this.nextDate.plusDays(interval);
    }

    public void addEvent(LocalDate eventDate, LocalDate nextDate) {
        this.history.add(this.lastDate);
        this.lastDate = eventDate;
        this.nextDate = nextDate;
    }

    public LocalDate getNextDate() {
        return nextDate;
    }

    public LocalDate getLastDate() {
        return lastDate;
    }

    public List<LocalDate> getHistory() {
        return history;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        if (interval != null && interval < 0)
            throw new IllegalArgumentException("Calibration interval may not be a negative value");
        else this.interval = interval;
    }
}