package com.jarvis_data_eng_antonii.tradeapp.persistent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    @Column(nullable = false)
    String id;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "trader_Id", nullable = false)
    Trader trader;
    @Column(nullable = false)
    double amount;
}
