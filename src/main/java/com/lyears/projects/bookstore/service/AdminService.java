package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Administrator;
import com.lyears.projects.bookstore.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fzm
 * @date 2018/2/26
 **/
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Transactional(readOnly = true)
    public Administrator findByEmail(String email) {
        return adminRepository.findAdministratorByEmail(email);
    }
}
