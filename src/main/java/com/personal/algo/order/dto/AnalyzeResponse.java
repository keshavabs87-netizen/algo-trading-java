package com.personal.algo.order.dto;

public class AnalyzeResponse {
    private double sma20;
    private Double previousSma20;
    private double threshold;
    private boolean buySignal;
    private String action;

    public AnalyzeResponse() {}

    public AnalyzeResponse(double sma20, Double previousSma20, double threshold, boolean buySignal, String action) {
        this.sma20 = sma20;
        this.previousSma20 = previousSma20;
        this.threshold = threshold;
        this.buySignal = buySignal;
        this.action = action;
    }

    public double getSma20() { return sma20; }
    public void setSma20(double sma20) { this.sma20 = sma20; }
    public Double getPreviousSma20() { return previousSma20; }
    public void setPreviousSma20(Double previousSma20) { this.previousSma20 = previousSma20; }
    public double getThreshold() { return threshold; }
    public void setThreshold(double threshold) { this.threshold = threshold; }
    public boolean isBuySignal() { return buySignal; }
    public void setBuySignal(boolean buySignal) { this.buySignal = buySignal; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}
