package com.example.scaccount.repository;

import com.example.scaccount.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Create by Brian on 2019/12/4 17:59
 */
public interface AccountDAO extends JpaRepository<Account, Long> {

    Account findByUserId(String userId);

}
