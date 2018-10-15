package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Administrator;
import com.lyears.projects.bookstore.entity.Librarian;
import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.repository.*;
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
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ConstantsRepository constantsRepository;

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

    /**
     * 更改所有的书籍罚金
     */
    public void updateBookFine(double fine)
    {
         constantsRepository.updateBookFine(fine);
    }

    /**
     * 更改所有书籍的归还期限
     */
    public void updateBookReturnDate(int days)
    {
         constantsRepository.updateBookReturnDate(days);
    }

    /**
     * 更爱所有读者创建账户时需要缴纳的保证金
     */
    public int updateReaderDeposit(double deposit)
    {
        return readerRepository.updateReaderDeposit(deposit);
    }

    /**
     * 根据邮箱更新密码
     * @param email
     * @param password
     * @return
     */
    public int updatePasswordByEmail(String email,String password)
    {
        return  adminRepository.updatePasswordByEmail(email, password);
    }
}
