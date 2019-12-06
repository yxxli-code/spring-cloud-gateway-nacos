package com.example.scorder.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @author Create by Brian on 2019/12/4 19:15
 */

@Entity
@Table(name = "order_tbl")
@DynamicUpdate
@DynamicInsert
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String commodityCode;

    private BigDecimal money;

    private Integer count;
}
