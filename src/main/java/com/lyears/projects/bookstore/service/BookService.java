package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fzm
 * @date 2018/10/4
 **/
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    public Page<Book> getAllBooks(int pageNo, int pageSize) {
        Pageable pageable = new PageRequest(pageNo - 1, pageSize, Sort.Direction.ASC, "bookId");
        return bookRepository.findAll(pageable);
    }

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    public Page<Book> getAllBooks(int pageNo, int pageSize, String keyStr) {
        Pageable pageable = new PageRequest(pageNo - 1, pageSize, Sort.Direction.ASC, "bookId");
        return bookRepository
                .findAllByAuthorContainingOrBookNameContainingOrBookTypeContaining(keyStr, keyStr, keyStr, pageable);
    }

}
