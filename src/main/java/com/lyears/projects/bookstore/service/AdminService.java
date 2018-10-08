package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Administrator;
import com.lyears.projects.bookstore.entity.Librarian;
import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.repository.AdminRepository;
import com.lyears.projects.bookstore.repository.LibrarianRepository;
import com.lyears.projects.bookstore.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fzm
 * @date 2018/2/26
 **/
@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private LibrarianRepository librarianRepository;

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    public Administrator findByEmail(String email) {
        return adminRepository.findAdministratorByEmail(email);
    }

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    public Map<String, Object> findAllAccount() {
        Map<String, Object> accountMap = new HashMap<>(16);
        List<Reader> readers = readerRepository.findAll();
        List<Librarian> librarians = librarianRepository.findAll();
        readers.forEach(reader -> {
            reader.setOrders(null);
            reader.setBorrows(null);
            accountMap.put("reader", reader);
        });
        librarians.forEach(
                librarian -> accountMap.put("librarian", librarian)
        );
        return accountMap;
    }
}
