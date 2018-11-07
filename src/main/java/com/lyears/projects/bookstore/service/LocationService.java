package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Location;
import com.lyears.projects.bookstore.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/11/6 19:25
 */
@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;
    public List<Location> findAllLocations()
    {
        return locationRepository.findAll();
    }

    /**
     *  添加一条位置信息
     * @param location
     */
    public void addLocation(Location location)
    {
         locationRepository.save(location);
    }

    public void editLocation(Location location,int id)
    {
        Location one = locationRepository.findOne(id);
        one.setName(location.getName());
        locationRepository.save(one);
    }
}
