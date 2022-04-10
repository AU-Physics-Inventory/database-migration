package edu.andrews.cas.physics.inventory.model.mongodb.accountability;

import edu.andrews.cas.physics.exception.OperationOnQuantitiesException;
import edu.andrews.cas.physics.inventory.model.mongodb.DocumentConversion;
import edu.andrews.cas.physics.inventory.model.mongodb.asset.Asset;
import edu.andrews.cas.physics.measurement.Quantity;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.List;

public class AccountabilityReports implements DocumentConversion {
    private Asset asset;
    private final List<MissingReport> missingReports;
    private final List<RecoveryReport> recoveryReports;
    private Quantity quantityMissing;

    public AccountabilityReports() {
        this.asset = null;
        this.missingReports = new ArrayList<>();
        this.recoveryReports = new ArrayList<>();
        this.quantityMissing = null;
    }

    public AccountabilityReports(Asset asset) {
        this.asset = asset;
        this.missingReports = new ArrayList<>();
        this.recoveryReports = new ArrayList<>();
        this.quantityMissing = null;
    }

    public AccountabilityReports(List<MissingReport> missingReports, List<RecoveryReport> recoveryReports) {
        this.asset = null;
        this.missingReports = missingReports;
        this.recoveryReports = recoveryReports;
        this.quantityMissing = null;
    }

    public AccountabilityReports(Asset asset, List<MissingReport> missingReports, List<RecoveryReport> recoveryReports) {
        this.asset = asset;
        this.missingReports = missingReports;
        this.recoveryReports = recoveryReports;
        this.quantityMissing = null;
    }

    public List<MissingReport> getMissingReports() {
        return missingReports;
    }

    public List<RecoveryReport> getRecoveryReports() {
        return recoveryReports;
    }

    public Quantity getQuantityMissing() throws OperationOnQuantitiesException {
        //return calculateQuantityMissing(); TODO ENABLE THIS AFTER APP IS DEPLOYED IN PRODUCTION
        return this.quantityMissing;
    }

    public Quantity calculateQuantityMissing() throws OperationOnQuantitiesException {
        quantityMissing = new Quantity(0, asset.getTotalQuantity().getUnit());
        for (MissingReport report : missingReports) {
            quantityMissing = quantityMissing.add(report.quantityMissing());
        }
        for (RecoveryReport report : recoveryReports) {
            quantityMissing = quantityMissing.subtract(report.quantityRecovered());
        }
        return quantityMissing;
    }

    public void addMissingReport(MissingReport missingReport) {
        if (missingReport.quantityMissing().getValue() > 0) this.missingReports.add(missingReport);
    }

    public void addRecoveryReport(RecoveryReport recoveryReport) {
        this.recoveryReports.add(recoveryReport);
    }

    public void setQuantityMissing(Quantity quantityMissing) {
        this.quantityMissing = quantityMissing;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    @Override
    public Document toDocument() {
        Document d = new Document();
        try {
            d.put("missingReports", getMissingReports().stream().map(MissingReport::toDocument).toList());
            d.put("recoveryReports", getRecoveryReports().stream().map(RecoveryReport::toDocument).toList());
            d.put("quantity", getQuantityMissing().toDocument());
        } catch (OperationOnQuantitiesException e) {
            e.printStackTrace();
            d.put("quantity", null);
        }
        return d;
    }
}
