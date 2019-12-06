package com.example.scorder.controller;

import com.example.scorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Create by Brian on 2019/12/4 19:23
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create")
    public Boolean create(String userId, String commodityCode, Integer count) {
        orderService.create(userId, commodityCode, count);
        return true;
    }

}
