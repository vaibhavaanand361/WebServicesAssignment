package com.incture.shipment.controller;

import com.incture.shipment.entity.Shipment;
import com.incture.shipment.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipment")
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @GetMapping("/")
    public List<Shipment> getShipment() {
        return shipmentService.getShipment();
    }

    @GetMapping("/{shipmentID}")
    public Shipment getShipmentByID(@PathVariable String shipmentID) {
        return shipmentService.getShipmentByID(shipmentID);
    }

    @GetMapping("/order/{orderID}")
    public Shipment getShipmentByOrderID(@PathVariable String orderID) {
        return shipmentService.getShipmentByOrderID(orderID);
    }

    @PostMapping("/")
    public String processShipping(@RequestBody Shipment shipment)
    {
        return shipmentService.processShipping(shipment);
    }

    @PutMapping("/update/{shipmentID}")
    public String updateShipping(@RequestBody Shipment shipment, @PathVariable String shipmentID) {
        return shipmentService.updateShipment(shipment, shipmentID);
    }

    @DeleteMapping("/delete/{shipmentID}")
    public String deleteShipment(@PathVariable String shipmentID) {
        return shipmentService.deleteShipment(shipmentID);
    }
}
