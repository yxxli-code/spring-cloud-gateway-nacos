package com.example.scorder.repository;

import com.example.scorder.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Create by Brian on 2019/12/4 19:18
 */
public interface OrderDAO extends JpaRepository<Order, Long> {
}
