package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Location;
import com.lyears.projects.bookstore.service.LocationService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.plugin2.main.server.ResultID;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/11/6 19:49
 */
@Controller
public class LocationHandler {

    @Autowired
    private LocationService locationService;

    @Autowired
    private HttpServletRequest request;


    @ResponseBody
    @RequestMapping(value = "/locations",method = RequestMethod.GET)
    public ResponseMessage getAllLocation()
    {
        return ResultUtil.success(locationService.findAllLocations(),request.getRequestURL().toString());
    }

    @RequestMapping(value = "/add/location",method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage addLocation(@RequestBody Location location)
    {
        locationService.addLocation(location);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    @RequestMapping(value = "/location/{id}",method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage editLocation(@PathVariable("id")String id,@RequestBody Location location)
    {
        locationService.editLocation(location,Integer.parseInt(id));
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }
}
