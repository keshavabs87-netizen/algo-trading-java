package com.personal.algo.kite.dto;

import java.util.List;

public class AnalyzeRequest {
    // Prices ordered from oldest to newest (older first, most recent last)

    private List<Double> prices;

    public AnalyzeRequest() {}

    public AnalyzeRequest(List<Double> prices) {
        this.prices = prices;
    }

    public List<Double> getPrices() {
        return prices;
    }

    public void setPrices(List<Double> prices) {
        this.prices = prices;
    }
}
