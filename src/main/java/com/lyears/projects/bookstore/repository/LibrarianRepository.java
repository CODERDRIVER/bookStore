package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fzm
 * @date 2018/10/3
 **/
public interface LibrarianRepository extends JpaRepository<Librarian, Integer> {

    Librarian getByEmail(String email);
}
