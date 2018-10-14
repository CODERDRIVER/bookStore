package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/12 16:38
 */
@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement,Integer>{


}
