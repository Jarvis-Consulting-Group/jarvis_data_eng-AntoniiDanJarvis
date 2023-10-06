package com.jarvis_data_eng_antonii.tradeapp.persistent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trader")
public class Trader {
    @Id
    @Column(nullable = false)
    String id;
    @Column(nullable = false)
    String firstName;
    @Column(nullable = false)
    String lastName;
    @Column(nullable = false)
    Date date;
    @Column(nullable = false)
    String country;
    @Column(nullable = false)
    String email;
}