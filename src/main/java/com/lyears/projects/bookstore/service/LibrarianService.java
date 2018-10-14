package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Librarian;
import com.lyears.projects.bookstore.repository.LibrarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fzm
 * @date 2018/10/3
 **/
@Service
public class LibrarianService {

    @Autowired
    private LibrarianRepository librarianRepository;

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    public Librarian findByEmail(String email) {
        return librarianRepository.getByEmail(email);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void save(Librarian librarian){
        librarianRepository.save(librarian);
    }

    /**
     * 根据图书馆管理员邮箱删除该用户
     */
    public void deleteByEmail(String email)
    {
        Librarian librarian = new Librarian();
        librarian.setEmail(email);
        librarianRepository.delete(librarian);
    }
}
