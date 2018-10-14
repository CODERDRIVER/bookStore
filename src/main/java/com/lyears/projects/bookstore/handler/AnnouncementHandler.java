package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Announcement;
import com.lyears.projects.bookstore.service.AnnouncementService;
import com.lyears.projects.bookstore.util.AuthUtil;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.jvm.hotspot.runtime.ResultTypeFinder;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/12 16:34
 */
@RestController
public class AnnouncementHandler {


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 增加一条公告记录
     * @param announcement
     * @param token
     * @return
     */
    @RequestMapping(value = "/announcement/add",method = RequestMethod.POST)
    public ResponseMessage addAnnouncement(Announcement announcement, @CookieValue("token")String token)
    {
        if (!AuthUtil.isLibrarian(token))
        {
            return ResultUtil.error(ResultEnum.NO_RIGHT,request.getRequestURL().toString());
        }
        announcementService.addAnnouncement(announcement);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     * 根据id修改
     * @param id
     * @param announcement
     * @param token
     * @return
     */
    @RequestMapping(value = "/announcement/{id}",method = RequestMethod.POST)
    public ResponseMessage updateAnnouncement(@PathVariable("id")int id, Announcement announcement, @CookieValue("token")String token)
    {
        // 根据id 查询 原来公告
        Announcement announcementById = announcementService.findAnnouncementById(id);
        if (announcement==null)
        {
            return ResultUtil.successNoData(request.getRequestURL().toString());
        }
        if (announcement.getTitle()!=null&&!"".equals(announcement.getTitle()))
        {
            announcementById.setTitle(announcement.getTitle());
        }
        if (announcement.getContent()!=null&&!"".equals(announcement.getContent()))
        {
            announcementById.setContent(announcement.getContent());
        }
        announcementService.updateAnnouncement(announcement);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    @RequestMapping(value = "/announcement/{id}",method = RequestMethod.DELETE)
    public ResponseMessage deleteAnnouncementById(@PathVariable("id")int id)
    {
        try{
            announcementService.deleteAnnouncementById(id);
            return ResultUtil.successNoData(request.getRequestURL().toString());
        }catch (Exception e)
        {
            return ResultUtil.error(ResultEnum.NO_ANNOUNCEMENT,request.getRequestURL().toString());
        }
    }


}
