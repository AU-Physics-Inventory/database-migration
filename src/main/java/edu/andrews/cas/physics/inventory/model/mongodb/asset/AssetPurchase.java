package edu.andrews.cas.physics.inventory.model.mongodb.asset;

import edu.andrews.cas.physics.measurement.Quantity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.URL;
import java.time.LocalDate;

public class AssetPurchase {
    private final Vendor vendor;

    private final LocalDate date;

    private final double cost;

    private final double unitPrice;

    private final Quantity quantity;

    private final URL url;

    private final String receipt;

    public AssetPurchase(Vendor vendor, LocalDate date, double cost, double unitPrice, Quantity quantity) {
        this.vendor = vendor;
        this.date = date;
        this.cost = cost;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.url = null;
        this.receipt = null;
    }

    public AssetPurchase(Vendor vendor, LocalDate date, double cost, double unitPrice, Quantity quantity, URL url, String receipt) {
        this.vendor = vendor;
        this.date = date;
        this.cost = cost;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.url = url;
        this.receipt = receipt;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getCost() {
        return cost;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public URL getUrl() {
        return url;
    }

    public String getReceipt() {
        return receipt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AssetPurchase that = (AssetPurchase) o;

        return new EqualsBuilder().append(cost, that.cost).append(unitPrice, that.unitPrice).append(vendor, that.vendor).append(date, that.date).append(quantity, that.quantity).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(vendor).append(date).append(cost).append(unitPrice).append(quantity).toHashCode();
    }
}
