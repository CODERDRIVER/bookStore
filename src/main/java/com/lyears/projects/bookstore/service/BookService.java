package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.repository.BookRepository;
import com.lyears.projects.bookstore.util.IDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        // 根据名称获得该书的库存量
        List<Book> byBookName = bookRepository.findByBookName(book.getBookName());
        int counts = 0;
        if (byBookName!=null&&byBookName.size()!=0)
        {
           counts =  byBookName.get(0).getInventory()+1;
        }else {
            counts = 1;
        }
        book.setStatus(0);
        bookRepository.save(book);

        bookRepository.updateInventoryByBookName(book.getBookName(),counts);
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


    /**
     *  根据id 删除书籍
     */
    public void deleteBookById(int id)
    {
        bookRepository.delete(id);
    }

    /**
     *  多条件查询 根据书名 作者 类型
     */
    public Page<Book> findBooksByBookNameOrAuthorOrBookType(int pageNo,int pageSize,String query)
    {
        Pageable pageable = new PageRequest(pageNo - 1, pageSize, Sort.Direction.ASC, "bookId");
        return bookRepository.findAllByAuthorContainingOrBookNameContainingOrBookTypeContaining(query,query,query,pageable);
    }

    /**
     *  根据bookID 设置book 的状态
     */
    public void updateBookStatusByBookId(int bookStatus,int bookId)
    {
        bookRepository.updateStatusByBookId(bookStatus,bookId);
    }

    /**
     *  根据bookName 更新 book 的库存量
     */
    public void updateInventoryByBookName(String bookName,int inventory)
    {
        bookRepository.updateInventoryByBookName(bookName,inventory);
    }
}
