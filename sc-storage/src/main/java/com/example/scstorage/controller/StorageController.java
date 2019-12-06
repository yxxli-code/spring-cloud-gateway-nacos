package com.example.scstorage.controller;

import com.example.scstorage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Create by Brian on 2019/12/4 19:51
 */
@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    @GetMapping(path = "/deduct")
    public Boolean deduct(String commodityCode, Integer count) {
        storageService.deduct(commodityCode, count);
        return true;
    }

}
