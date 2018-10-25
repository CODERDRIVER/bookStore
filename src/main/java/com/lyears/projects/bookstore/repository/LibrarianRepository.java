package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author fzm
 * @date 2018/10/3
 **/
public interface LibrarianRepository extends JpaRepository<Librarian, Integer> {

    Librarian getByEmail(String email);
    List<Librarian> findAllByEmailContainingOrUserNameContaining(String email,String username);
}
