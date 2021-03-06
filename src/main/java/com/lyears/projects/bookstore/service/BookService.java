package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.repository.BookRepository;
import com.lyears.projects.bookstore.util.IDGenerator;
import com.lyears.projects.bookstore.util.ZxingCodeUtil;
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

    public Page<Book> getAllBorrowBooks(int pageNo,int pageSize)
    {
        Pageable pageable = new PageRequest(pageNo - 1, pageSize, Sort.Direction.ASC, "bookId");
        return bookRepository.findBooksByStatus(0,pageable);
    }

    /**
     * 添加书籍信息
     * @param book
     */
    public void save(Book book){
        // 根据名称获得该书的库存量
        Integer inventory = bookRepository.findInventoryByBookName(book.getBookName());
        System.out.println(inventory);
        int counts = 0;
        if (inventory!=null)
        {
           counts =  inventory+1;
        }else {
            counts = 1;
        }
        book.setStatus(0);
        if (book.getBookId()!=null)
        {
            //补全书籍的bar_code 信息
            // 说明已经存在该书
            int bookId = book.getBookId();
            Book one = bookRepository.findOne(bookId);
            book.setBookUrl(one.getBookUrl());
//            book.setInventory(one.getInventory());
            bookRepository.save(book);
        }else{
            String barCode = IDGenerator.getUniqueId();
            book.setBarCode(barCode);
            /**
             *  根据barCode 生成 条形码图片
             */
            String barCodeUrl = ZxingCodeUtil.createBarCode(barCode, 70, 25);

            book.setBarCodeUrl(barCodeUrl);
            book.setInventory(inventory);
            bookRepository.save(book);
            bookRepository.updateInventoryByBookName(book.getBookName(),counts);
        }

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
    public boolean deleteBookById(int id)
    {
        //判断该书能否被删除
        System.out.println("###################");
        Book one = bookRepository.findOne(id);
        System.out.println("###################");
        if (one.getStatus()!=0)
        {
            return false;
        }
        bookRepository.delete(id);
        return true;
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

    /**
     *  根据名称查询书籍的剩余量
     */
    public int getInventoryByBookName(String bookName)
    {
        return bookRepository.findInventoryByBookName(bookName);
    }

    public Book findBookByBarCode(String barCode)
    {
        return bookRepository.findBookByBarCode(barCode);
    }
}
