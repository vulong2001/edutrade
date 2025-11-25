package com.web.edutrade.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Data
public class Slide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;
    private String des;
    private String count;


    @Column(name = "stock_symbol")
    private String stockSymbol;

    @Column(name = "auto_update")
    private Boolean autoUpdate = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated")
    private Date lastUpdated;
}