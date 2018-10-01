package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.entity.Borrow;
import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.repository.BookRepository;
import com.lyears.projects.bookstore.repository.BorrowRepository;
import com.lyears.projects.bookstore.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fzm
 * @date 2018/2/26
 **/
@Service
public class BorrowService {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void saveBorrowInfoWithBookAndReader(String bookName, String readerName, Borrow borrow) {
        Book book = bookRepository.findByBookName(bookName);
        Reader reader = readerRepository.findByUserName(readerName);
        borrow.setBorrowId(null);

        book.getBorrows().add(borrow);
        reader.getBorrows().add(borrow);

        borrow.setBook(book);
        borrow.setReader(reader);
        borrowRepository.save(borrow);

    }
}
