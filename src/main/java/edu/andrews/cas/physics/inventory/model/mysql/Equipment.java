package edu.andrews.cas.physics.inventory.model.mysql;

import java.time.LocalDate;

public class Equipment {
    private int id;
    private String location;
    private String item_name;
    private String keywords;
    private String brand;
    private String model;
    private String part;
    private String serial_number;
    private String au_inventory;
    private String quantity;
    private LocalDate purchase_date;
    private String purchase_amount;
    private String missing;
    private String quantity_missing;
    private int record_locator;
    private LocalDate date_reported_missing;
    private String reported_missing_by;
    private String notes;
    private int soft_copy_available;
    private int hard_copy_available;
    private int receipt_available;
    private String unit_price;
    private String vendor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getAu_inventory() {
        return au_inventory;
    }

    public void setAu_inventory(String au_inventory) {
        this.au_inventory = au_inventory;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public LocalDate getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(LocalDate purchase_date) {
        this.purchase_date = purchase_date;
    }

    public String getPurchase_amount() {
        return purchase_amount;
    }

    public void setPurchase_amount(String purchase_amount) {
        this.purchase_amount = purchase_amount;
    }

    public String getMissing() {
        return missing;
    }

    public void setMissing(String missing) {
        this.missing = missing;
    }

    public String getQuantity_missing() {
        return quantity_missing;
    }

    public void setQuantity_missing(String quantity_missing) {
        this.quantity_missing = quantity_missing;
    }

    public int getRecord_locator() {
        return record_locator;
    }

    public void setRecord_locator(int record_locator) {
        this.record_locator = record_locator;
    }

    public LocalDate getDate_reported_missing() {
        return date_reported_missing;
    }

    public void setDate_reported_missing(LocalDate date_reported_missing) {
        this.date_reported_missing = date_reported_missing;
    }

    public String getReported_missing_by() {
        return reported_missing_by;
    }

    public void setReported_missing_by(String reported_missing_by) {
        this.reported_missing_by = reported_missing_by;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getSoft_copy_available() {
        return soft_copy_available;
    }

    public void setSoft_copy_available(int soft_copy_available) {
        this.soft_copy_available = soft_copy_available;
    }

    public int getHard_copy_available() {
        return hard_copy_available;
    }

    public void setHard_copy_available(int hard_copy_available) {
        this.hard_copy_available = hard_copy_available;
    }

    public int getReceipt_available() {
        return receipt_available;
    }

    public void setReceipt_available(int receipt_available) {
        this.receipt_available = receipt_available;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
}
