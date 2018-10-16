package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Librarian;
import com.lyears.projects.bookstore.repository.LibrarianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
     *  根据图书管理员 id  查询该图书管理员
     */
    public Librarian findLibrarianById(int librarianId)
    {
        return librarianRepository.findOne(librarianId);
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

    /**
     *  查询所有的图书管理员列表
     */

    public List<Librarian> getAllLibralians()
    {
        return librarianRepository.findAll();
    }


    /**
     * 根据id 删除图书管理员
     */
    public void deleteById(String[] ids)
    {
        for (String id:ids)
        {
            librarianRepository.delete(Integer.parseInt(id));
        }
    }

}
