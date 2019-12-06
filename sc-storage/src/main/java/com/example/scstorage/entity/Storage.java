package com.example.scstorage.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Create by Brian on 2019/12/4 19:46
 */
@Entity
@Table(name = "storage_tbl")
@DynamicUpdate
@DynamicInsert
@Data
public class Storage {

    @Id
    private Long id;

    private String commodityCode;

    private Integer count;

}
