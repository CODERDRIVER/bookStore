package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fzm
 * @date 2018/2/25
 **/
public interface ReaderRepository extends JpaRepository<Reader,Integer> {

    Reader findByUserName(String lastName);

}
