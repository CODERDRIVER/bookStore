package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.repository.BookRepository;
import com.lyears.projects.bookstore.util.IDGenerator;
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

    /**
     * 添加书籍信息
     * @param book
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void save(Book book){
        //补全书籍的bar_code 信息
        book.setBarCode(IDGenerator.getUniqueId());
        bookRepository.save(book);
    }

    /**
     * 根据bookid 查询该书籍
     */
    public Book findBookById(int id)
    {
        return bookRepository.findOne(id);
    }

    /**
     * 根据bookid 修改该书籍的类别
     */
    public int updateBookCategoryById(int id,String category)
    {
        return bookRepository.updateBookCategoryById(id,category);
    }

    /**
     *  根据ID 编辑书籍的所在的位置
     */
    public int updateBookPosition(int id,String location)
    {
        return bookRepository.updateBookPositionById(id,location);
    }
}
