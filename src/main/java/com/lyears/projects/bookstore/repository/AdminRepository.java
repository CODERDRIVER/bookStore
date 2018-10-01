package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author fzm
 * @date 2018/9/27
 **/
public interface AdminRepository extends JpaRepository<Administrator, Integer> {
    Administrator findAdministratorByEmail(String email);
}
