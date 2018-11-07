package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/11/6 19:23
 */
@Repository
public interface LocationRepository extends JpaRepository<Location,Integer>{
}
