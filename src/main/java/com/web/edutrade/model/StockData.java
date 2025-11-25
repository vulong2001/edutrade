package com.web.edutrade.model;

import lombok.Data;
import java.util.Date;

@Data
public class StockData {
    private String symbol;
    private double price;
    private double change;
    private double changePercent;
    private long volume;
    private Date timestamp;
}