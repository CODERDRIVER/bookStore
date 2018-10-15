package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Announcement;
import com.lyears.projects.bookstore.repository.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/12 16:38
 */
@Service
public class AnnouncementService {


    @Autowired
    private AnnouncementRepository announcementRepository;


    /**
     *  获得所有的公告列表
     */
    public List<Announcement> getAllAnnouncement()
    {
        return announcementRepository.findAll();
    }

    /**
     * 增加一条公告
     * @param announcement
     */
    public void addAnnouncement(Announcement announcement)
    {
        announcementRepository.save(announcement);
    }

    /**
     *  根据id 查询
     * @param id
     * @return
     */
    public Announcement findAnnouncementById(int id)
    {
        return announcementRepository.findOne(id);
    }

    /**
     * 更新 Announcement
     */
    public void updateAnnouncement(Announcement announcument)
    {
         announcementRepository.save(announcument);
    }

    /**
     * 根据id 删除记录
     */

    public void deleteAnnouncementById(int id)
    {
        announcementRepository.delete(id);
    }

}
