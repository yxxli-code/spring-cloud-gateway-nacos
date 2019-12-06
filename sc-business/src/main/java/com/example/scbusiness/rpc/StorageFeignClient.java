package com.example.scbusiness.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Create by Brian on 2019/12/4 18:33
 */
@FeignClient(name = "sc-storage")
public interface StorageFeignClient {

    @GetMapping("/deduct")
    Boolean deduct(@RequestParam("commodityCode") String commodityCode,
                   @RequestParam("count") Integer count);
}
