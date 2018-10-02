package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fzm
 * @date 2018/10/2
 **/
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
