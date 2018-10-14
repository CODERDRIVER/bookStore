package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author fzm
 * @date 2018/9/27
 **/
public interface AdminRepository extends JpaRepository<Administrator, Integer> {
    Administrator findAdministratorByEmail(String email);

    //根据邮箱更新用户密码
    @Query(nativeQuery = true,value = "update administrator as a set password = :newPassword WHERE a.email = :email")
    int updatePasswordByEmail(@Param("email")String email, @Param("newPassword")String password);
}
