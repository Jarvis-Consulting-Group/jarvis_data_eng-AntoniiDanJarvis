package com.jarvis_data_eng_antonii.tradeapp.persistent.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Immutable
@Table(name = "position")
@NoArgsConstructor
@AllArgsConstructor
@Subselect("select account_id, ticker, sum(size) as position FROM security_order where status = 'FILED' group by account_id, ticker")
public class Position implements Serializable {
    @Id
    @Column(name = "account_id", nullable = false)
    String accountId;
    @Column(nullable = false)
    String ticker;
    @Column(nullable = false)
    int position;
}