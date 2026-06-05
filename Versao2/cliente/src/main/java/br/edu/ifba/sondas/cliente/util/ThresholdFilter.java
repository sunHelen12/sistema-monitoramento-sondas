package br.edu.ifba.sondas.cliente.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThresholdFilter {
    private final double threshold;
    private final Map<String, Double> lastSent = new ConcurrentHashMap<>();
    public ThresholdFilter(double threshold) { this.threshold = threshold; }
    public boolean shouldSend(String sondaId, double value) {
        Double prev = lastSent.get(sondaId);
        if (prev == null || Math.abs(prev - value) >= threshold) {
            lastSent.put(sondaId, value);
            return true;
        }
        return false;
    }
}