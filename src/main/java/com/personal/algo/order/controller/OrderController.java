package com.personal.algo.order.controller;

import com.personal.algo.order.service.KiteService;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final KiteService kiteService;

    public OrderController(KiteService kiteService) {
        this.kiteService = kiteService;
    }

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> body) {
        try {
            String tradingsymbol = (String) body.get("tradingsymbol");
            Integer quantity = (Integer) body.get("quantity");
            String side = (String) body.get("side");
            String product = (String) body.get("product");
            String exchange = (String) body.get("exchange");
            String orderType = (String) body.get("orderType");
            String variety = (String) body.get("variety");

            KiteConnect kite = kiteService.getAuthenticatedClient();

            OrderParams params = new OrderParams();
            params.product = product;
            params.exchange = exchange;
            params.orderType = orderType;
            params.quantity = quantity;
            params.tradingsymbol = tradingsymbol;
            params.transactionType = side;

            Order order = kite.placeOrder(params, variety);

            return ResponseEntity.ok(order);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        } catch (KiteException e) {
            throw new RuntimeException(e);
        }
    }
}
