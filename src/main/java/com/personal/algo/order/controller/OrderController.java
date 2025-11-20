package com.personal.algo.order.controller;

import com.personal.algo.order.dto.AnalyzeRequest;
import com.personal.algo.order.dto.AnalyzeResponse;
import com.personal.algo.order.service.KiteService;
import com.personal.algo.order.service.TradeService;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.Order;
import com.zerodhatech.models.OrderParams;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final KiteService kiteService;
    private final TradeService tradeService;

    public OrderController(KiteService kiteService, TradeService tradeService) {
        this.kiteService = kiteService;
        this.tradeService = tradeService;
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

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(@RequestBody Map<String, Object> body) {
        try {
            KiteConnect kite = kiteService.getAuthenticatedClient();
            String tradingsymbol = (String) body.get("tradingsymbol");
            String exchange = (String) body.get("exchange");

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -20); // 20 days ago
            Date fromDate = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, -1); // 19 days ago for 'to' date
            Date toDate = calendar.getTime();

            List<Instrument> instruments = kite.getInstruments(exchange);
            long instrumentToken = -1;
            for (Instrument instrument : instruments) {
                if (instrument.tradingsymbol.equalsIgnoreCase(tradingsymbol)) {
                    instrumentToken = instrument.instrument_token;
                    break;
                }
            }

            if (instrumentToken == -1) {
                System.out.println("Instrument token not found for the given symbol.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Instrument token not found for the given symbol.");
            }

            // Fetch historical data (OHLC) for the last 20 days
            List<HistoricalData> historicalData = Collections.singletonList(kite.getHistoricalData(fromDate, toDate,
                    String.valueOf(instrumentToken), "day", false, false));

            System.out.println("Stock Price Data for " + tradingsymbol + " over the last 20 days:");
            for (HistoricalData data : historicalData) {
                System.out.println("Date: " + data.timeStamp + " | Open: " + data.open + " | High: " + data.high + " | Low: "
                        + data.low + " | Close: " + data.close);
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        } catch (KiteException e) {
            throw new RuntimeException(e);
        }

        //AnalyzeResponse response = tradeService.analyze(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Generated Historical Data");
    }
}
