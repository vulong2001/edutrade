package com.web.edutrade.service;

import com.web.edutrade.model.StockData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class StockDataService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Lấy dữ liệu chứng khoán từ VNDirect API
     */
    public StockData getStockData(String symbol) {
        try {
            String apiUrl = "https://finfo-api.vndirect.com.vn/v4/stock_prices/" + symbol;
            String response = restTemplate.getForObject(apiUrl, String.class);

            JsonNode root = objectMapper.readTree(response);
            JsonNode data = root.get("data");

            StockData stockData = new StockData();
            stockData.setSymbol(symbol);
            stockData.setPrice(data.get("lastPrice").asDouble());
            stockData.setChange(data.get("change").asDouble());
            stockData.setChangePercent(data.get("changePct").asDouble());
            stockData.setVolume(data.get("totalVolume").asLong());
            stockData.setTimestamp(new Date());

            return stockData;
        } catch (Exception e) {
            System.err.println("Error fetching stock data for " + symbol + ": " + e.getMessage());
            throw new RuntimeException("Cannot fetch stock data for " + symbol, e);
        }
    }
}