package com.incture.ordermanagement.service;

import com.incture.ordermanagement.entity.Order;
import com.incture.ordermanagement.entity.Shipment;
import com.incture.ordermanagement.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Order> getOrders() {
        List<Order> orderList = new ArrayList<>();
        for (Order order : orderRepository.findAll())
        {
            orderList.add(order);
        }
        return orderList;
    }

    public Order getOrderByID(String orderID) {
        if(!orderRepository.existsById(orderID))
        {
            return new Order();
        }
        return orderRepository.findById(orderID).get();
    }

    public void createOrder(Order order) {
        order.setTotalPrice(order.getPrice() * order.getQuantity());
        orderRepository.save(order);
    }

    public String updateOrder(Order order, String orderID) {
        if(orderRepository.existsById(orderID))
        {
            Order orderDB = orderRepository.findById(orderID).get();
            if(order.getQuantity() > 0)
            {
                orderDB.setQuantity(order.getQuantity());
                orderDB.setTotalPrice(order.getQuantity() * orderDB.getPrice());
            }
            if(!order.getAddress().isEmpty())
            {
                orderDB.setAddress(order.getAddress());
                if(orderDB.getShipmentId() != null)
                {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    String putUrl = "http://localhost:8081/shipment/update/" + orderDB.getShipmentId();
                    Shipment shipment = new Shipment();
                    shipment.setAddress(order.getAddress());
                    shipment.setStatus("Address change");
                    HttpEntity<Shipment> httpEntity = new HttpEntity<Shipment>(shipment,headers);
                    restTemplate.put(putUrl, httpEntity, Shipment.class);
                }
            }
            orderRepository.save(orderDB);
            return "Order details updated successfully";
        }
        else
        {
            return "No such order exists";
        }

    }

    public String deleteOrder(String orderID) {
        if(orderRepository.existsById(orderID))
        {
            Order orderDB = orderRepository.findById(orderID).get();
            if(orderDB.getShipmentId() != null)
            {
                String shipmentID = orderDB.getShipmentId();
                HttpHeaders headers = new HttpHeaders();
                String url = "http://localhost:8081/shipment/delete/" + shipmentID;
                restTemplate.delete(url);
            }
            orderRepository.deleteById(orderID);
            return "Order deleted successfully with orderID " + orderID;
        }
        else
        {
            return "No such order exists";
        }
    }

    public String updateShipmentDetails(Shipment shipment) {
        if(orderRepository.existsById(shipment.getOrderID()))
        {
            Order order = orderRepository.findById(shipment.getOrderID()).get();
            order.setShipmentId(shipment.getShipmentID());
            order.setStatus(shipment.getStatus());
            orderRepository.save(order);
            return "Shipment details successfully updated";
        }
        return "No such order found";
    }
}
