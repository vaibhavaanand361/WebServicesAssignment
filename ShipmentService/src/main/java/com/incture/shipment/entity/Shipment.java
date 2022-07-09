package com.incture.shipment.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shipment_table")
public class Shipment {

    @Id
    private String shipmentID;
    private String orderID;
    private String address;
    private String status;
    private String deliveryDate;

    public Shipment() {

    }

    public Shipment(String shipmentID, String orderID, String address, String status, String deliveryDate) {
        this.shipmentID = shipmentID;
        this.orderID = orderID;
        this.address = address;
        this.status = status;
        this.deliveryDate = deliveryDate;
    }

    public String getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(String shipmentID) {
        this.shipmentID = shipmentID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
