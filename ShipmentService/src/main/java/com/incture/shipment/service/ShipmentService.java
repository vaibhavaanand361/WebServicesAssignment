package com.incture.shipment.service;

import com.incture.shipment.entity.Order;
import com.incture.shipment.entity.Shipment;
import com.incture.shipment.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Shipment> getShipment() {
        List<Shipment> shipmentList = new ArrayList<>();
        for (Shipment shipment : shipmentRepository.findAll())
        {
            shipmentList.add(shipment);
        }
        return shipmentList;
    }

    public Shipment getShipmentByID(String shipmentID) {
        if(!shipmentRepository.existsById(shipmentID))
        {
            return new Shipment();
        }
        return shipmentRepository.findById(shipmentID).get();
    }


    public Shipment getShipmentByOrderID(String orderID) {
        for(Shipment shipment : shipmentRepository.findAll())
        {
            if(shipment.getOrderID().equals(orderID))
            {
                return shipment;
            }
        }
        return new Shipment();
    }

    public String processShipping(Shipment shipment) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String getUrl = "http://localhost:8080/order/" + shipment.getOrderID();
        Order responseOrder = restTemplate.getForObject(getUrl, Order.class);

        String putUrl = "http://localhost:8080/order/shipment/update/";
        HttpEntity<Shipment> httpEntity = new HttpEntity<Shipment>(shipment,headers);
        restTemplate.put(putUrl, httpEntity, Shipment.class);

        shipment.setAddress(responseOrder.getAddress());
        shipmentRepository.save(shipment);
        return "Your order with orderID " + shipment.getOrderID() + " has been shipped with shipmentID " + shipment.getShipmentID() + " and will reach by " + shipment.getDeliveryDate();
    }

    public String updateShipment(Shipment shipment, String shipmentID) {
        if(shipmentRepository.existsById(shipmentID))
        {
            Shipment shipmentDB = shipmentRepository.findById(shipmentID).get();
            if(shipment.getStatus().equals("Address change"))
            {
                shipmentDB.setAddress(shipment.getAddress());
            }
            else
            {
                if(!shipment.getStatus().isEmpty())
                {
                    shipmentDB.setStatus(shipment.getStatus());

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    String putUrl = "http://localhost:8080/order/shipment/update/";
                    shipment.setShipmentID(shipmentID);
                    shipment.setOrderID(shipmentDB.getOrderID());
                    HttpEntity<Shipment> httpEntity = new HttpEntity<Shipment>(shipment,headers);
                    restTemplate.put(putUrl, httpEntity, Shipment.class);
                }
                if(!shipment.getDeliveryDate().isEmpty())
                {
                    shipmentDB.setDeliveryDate(shipment.getDeliveryDate());
                }
            }
            shipmentRepository.save(shipmentDB);
            return "Shipment details updated successfully";
        }
        else
        {
            return "No such shipment details found";
        }
    }

    public String deleteShipment(String shipmentID) {
        if(shipmentRepository.existsById(shipmentID))
        {
            String orderId = shipmentRepository.findById(shipmentID).get().getOrderID();
            shipmentRepository.deleteById(shipmentID);
            return "Shipment with shipmentID " + shipmentID + " has been deleted";
        }
        return "No such shipment found";
    }
}
