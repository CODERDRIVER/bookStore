package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fzm
 * @date 2018/9/30
 **/
public interface BorrowRepository extends JpaRepository<Borrow, Integer> {



}
