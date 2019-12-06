package com.example.scbusiness.service;

import com.example.scbusiness.rpc.OrderFeignClient;
import com.example.scbusiness.rpc.StorageFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Create by Brian on 2019/12/4 18:29
 */
@Service
public class BusinessService {

    @Autowired
    private StorageFeignClient storageFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    /**
     * 减库存，下订单
     *
     * @param userId
     * @param commodityCode
     * @param orderCount
     */
    @GlobalTransactional(name="sc-business-fescar-service-group")
    public void purchase(String userId, String commodityCode, int orderCount) {
        storageFeignClient.deduct(commodityCode, orderCount);
        orderFeignClient.create(userId, commodityCode, orderCount);
    }
}
