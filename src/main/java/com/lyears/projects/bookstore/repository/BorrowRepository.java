package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author fzm
 * @date 2018/9/30
 **/
public interface BorrowRepository extends JpaRepository<Borrow, Integer> {

    List<Borrow> getAllByBorrowDate(LocalDate date);

}
