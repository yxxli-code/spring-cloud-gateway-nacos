package com.example.scorder.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author Create by Brian on 2019/12/4 19:17
 */
@FeignClient(name = "sc-account")
public interface UserFeignClient {

    @GetMapping("/debit")
    Boolean debit(@RequestParam(value = "userId") String userId, @RequestParam(value = "money") BigDecimal money);

}
