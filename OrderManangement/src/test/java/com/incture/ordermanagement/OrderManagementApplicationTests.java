package com.incture.ordermanagement;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.Matchers.containsString;

@SpringBootTest
public class OrderManagementApplicationTests {

    @BeforeEach
    void setUp() throws Exception {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    @Order(1)
    public void getOrders() {
        RestAssured.get("/order/").then().assertThat().statusCode(200).body("size()", Matchers.is(1));
    }

    @Test
    @Order(2)
    public void getOrderById() {
        RestAssured.get("/order/O001").then().assertThat().statusCode(200).body("orderID", Matchers.equalTo("O001"));
    }

    @Test
    @Order(3)
    public void createOrder() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("orderID", "O001");
        obj.put("productName", "Pen");
        obj.put("price", 5.25);
        obj.put("quantity", 7);
        obj.put("address", "Bokaro");
        RestAssured.given().contentType("application/json").body(obj.toString()).when().post("order/placeOrder")
                .then().statusCode(200).body(containsString("Order has been successfully placed with orderID O001"));
    }

    @Test
    @Order(4)
    public void updateOrder() throws JSONException{
        JSONObject obj = new JSONObject();
        obj.put("quantity", "8");
        obj.put("address", "BBSR");
        RestAssured.given().pathParam("orderID","O001").contentType("application/json").body(obj.toString()).when().put("order/update/{orderID}")
                .then().statusCode(200).body(containsString("Order details updated successfully"));
    }

    @Test
    @Order(5)
    public void deleteOrder() throws JSONException{
        RestAssured.given().pathParam("orderId", "O002").when().delete("order/delete/{orderId}").then().statusCode(200).body(containsString("No such order exists"));
    }
}
