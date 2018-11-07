package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/11/6 19:22
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer>{
}
