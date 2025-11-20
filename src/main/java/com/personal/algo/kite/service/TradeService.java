package com.personal.algo.kite.service;

import com.personal.algo.kite.dto.AnalyzeRequest;
import com.personal.algo.kite.dto.AnalyzeResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    private static final double THRESHOLD = 1491.0;
    private static final int PERIOD = 20;

    public AnalyzeResponse analyze(AnalyzeRequest req) {
        List<Double> prices = req.getPrices();
        if (prices == null || prices.size() < PERIOD) {
            throw new IllegalArgumentException("At least " + PERIOD + " prices required (oldest -> newest).");
        }

        double sma20 = computeSmaLastN(prices, PERIOD, 0); // current SMA over last 20
        Double prevSma20 = null;
        if (prices.size() >= PERIOD + 1) {
            prevSma20 = computeSmaLastN(prices, PERIOD, 1); // previous SMA over the 20 values before the last element
        }

        boolean buySignal = prevSma20 != null && prevSma20 <= THRESHOLD && sma20 > THRESHOLD;
        String action = buySignal ? "BUY" : "HOLD";

        return new AnalyzeResponse(sma20, prevSma20, THRESHOLD, buySignal, action);
    }

    /**
     * Compute SMA over PERIOD values, where offset = 0 uses the last PERIOD values,
     * offset = 1 uses the PERIOD values ending one element earlier, etc.
     */
    private double computeSmaLastN(List<Double> prices, int n, int offset) {
        int endIndex = prices.size() - 1 - offset;
        int startIndex = endIndex - n + 1;
        if (startIndex < 0) {
            throw new IllegalArgumentException("Not enough data for requested SMA offset");
        }
        double sum = 0.0;
        for (int i = startIndex; i <= endIndex; i++) {
            sum += prices.get(i);
        }
        return sum / n;
    }
}
