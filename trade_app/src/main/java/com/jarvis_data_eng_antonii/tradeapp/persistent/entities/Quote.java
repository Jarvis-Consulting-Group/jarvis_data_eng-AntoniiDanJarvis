package com.jarvis_data_eng_antonii.tradeapp.persistent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "quote")
@AllArgsConstructor
@NoArgsConstructor
public class Quote {
    @Id
    @Column(nullable = false)
    String ticker;
    @Column(nullable = false)
    double lastPrice;
    @Column(nullable = false)
    double bidPrice;
    @Column(nullable = false)
    long bidSize;
    @Column(nullable = false)
    long askPrice;
    @Column(nullable = false)
    long askSize;
}