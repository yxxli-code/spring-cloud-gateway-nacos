package com.example.scbusiness.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Create by Brian on 2019/12/4 18:31
 */
@FeignClient(name = "sc-order")
public interface OrderFeignClient {

    @GetMapping("/create")
    void create(@RequestParam(value = "userId") String userId,
                @RequestParam(value = "commodityCode") String commodityCode,
                @RequestParam(value = "count") Integer count);



}
