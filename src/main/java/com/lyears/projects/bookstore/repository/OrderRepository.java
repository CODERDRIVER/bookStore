package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fzm
 * @date 2018/10/2
 **/
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> getAllByReturnDateBeforeAndOrderStatus(LocalDateTime localDateTime,int status);


    List<Order> findAllByOrderStatus(int orderStatus);
}
