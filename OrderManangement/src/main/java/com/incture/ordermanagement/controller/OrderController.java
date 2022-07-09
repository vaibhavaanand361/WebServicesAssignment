package com.incture.ordermanagement.controller;

import com.incture.ordermanagement.entity.Order;
import com.incture.ordermanagement.entity.Shipment;
import com.incture.ordermanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public List<Order> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/{orderID}")
    public Order getOrderByID(@PathVariable String orderID) {
        return orderService.getOrderByID(orderID);
    }

    @PostMapping("/placeOrder")
    public String createOrder(@RequestBody Order order) {
        orderService.createOrder(order);
        return "Order has been successfully placed with orderID " + order.getOrderID() ;
    }

    @PutMapping("/update/{orderID}")
    public String updateOrder(@RequestBody Order order, @PathVariable String orderID) {
        return orderService.updateOrder(order, orderID);
    }

    @DeleteMapping("/delete/{orderID}")
    public String deleteOrder(@PathVariable String orderID) {
        return orderService.deleteOrder(orderID);
    }

    @PutMapping("/shipment/update/")
    public String updateShipmentDetails(@RequestBody Shipment shipment) {
        return orderService.updateShipmentDetails(shipment);
    }
}
