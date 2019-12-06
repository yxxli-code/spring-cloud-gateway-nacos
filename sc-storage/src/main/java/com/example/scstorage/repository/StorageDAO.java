package com.example.scstorage.repository;

import com.example.scstorage.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Create by Brian on 2019/12/4 19:48
 */
public interface StorageDAO extends JpaRepository<Storage, String> {

    Storage findByCommodityCode(String commodityCode);

}
