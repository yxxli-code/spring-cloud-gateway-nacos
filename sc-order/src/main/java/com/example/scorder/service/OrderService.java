package com.example.scorder.service;

import com.example.scorder.entity.Order;
import com.example.scorder.repository.OrderDAO;
import com.example.scorder.rpc.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author Create by Brian on 2019/12/4 19:19
 */
@Service
public class OrderService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private OrderDAO orderDAO;

    @Transactional(rollbackFor = Exception.class)
    public void create(String userId, String commodityCode, Integer count) {
        BigDecimal orderMoney = new BigDecimal(count).multiply(new BigDecimal(5));
        Order order = new Order();
        order.setUserId(userId);
        order.setCommodityCode(commodityCode);
        order.setCount(count);
        order.setMoney(orderMoney);
        orderDAO.save(order);

        userFeignClient.debit(userId, orderMoney);
    }
}
