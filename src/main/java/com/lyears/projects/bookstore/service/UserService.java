package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author fzm
 * @date 2018/2/25
 **/
@Service
public class UserService {

    @Autowired
    private ReaderRepository employeeRepository;

    @Transactional
    public void delete(Integer id){
        employeeRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public Reader findOne(Integer id){
        return employeeRepository.findOne(id);
    }

    //@Transactional
    //public void saveEmployee(Reader user){
    //    if(user.getId() == null){
    //        user.setCreatedTime(LocalDate.now());
    //    }
    //    employeeRepository.saveAndFlush(user);
    //}

    @Transactional(readOnly = true)
    public Reader findByUserName(String userName){
        return employeeRepository.findByUserName(userName);
    }

    @Transactional(readOnly = true)
    public Page<Reader> getPage(int pageNo, int pageSize){
        PageRequest pageable = new PageRequest(pageNo-1,pageSize);
        return employeeRepository.findAll(pageable);
    }
}
