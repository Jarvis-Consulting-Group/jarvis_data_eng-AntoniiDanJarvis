package com.jarvis_data_eng_antonii.tradeapp.persistent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Entity
@Table(name = "security_order")
@NoArgsConstructor
@AllArgsConstructor
public class SecurityOrder {
    @Id
    @Column(nullable = false)
    String id;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "accountId", nullable = false)
    Account account;
    @Column(nullable = false)
    String status;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "ticker", nullable = false)
    Quote ticker;
    @Column(nullable = false)
    int size;
    @Column(nullable = false)
    double price;
    @Column(nullable = false)
    String notes;
}