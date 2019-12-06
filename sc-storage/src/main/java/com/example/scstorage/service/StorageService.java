package com.example.scstorage.service;

import com.example.scstorage.entity.Storage;
import com.example.scstorage.repository.StorageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Create by Brian on 2019/12/4 19:49
 */
@Service
public class StorageService {

    @Autowired
    private StorageDAO storageDAO;

    @Transactional(rollbackFor = Exception.class)
    public void deduct(String commodityCode, int count) {
        Storage storage = storageDAO.findByCommodityCode(commodityCode);
        storage.setCount(storage.getCount() - count);
        storageDAO.save(storage);
    }

}
