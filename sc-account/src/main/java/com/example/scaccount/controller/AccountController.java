package com.example.scaccount.controller;

import com.example.scaccount.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author Create by Brian on 2019/12/4 17:55
 */
@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/debit")
    public Boolean debit(String userId, BigDecimal money) {
        accountService.debit(userId, money);
        return true;
    }

}
