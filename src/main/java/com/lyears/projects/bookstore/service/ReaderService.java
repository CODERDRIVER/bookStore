package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.repository.ConstantsRepository;
import com.lyears.projects.bookstore.repository.ReaderRepository;
import com.lyears.projects.bookstore.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author fzm
 * @date 2018/2/25
 **/
@Service
public class ReaderService {

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private ConstantsRepository constantsRepository;

    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(Integer id) {
        readerRepository.delete(id);
    }

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    public Reader findOne(Integer id) {
        return readerRepository.findOne(id);
    }

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    public List<Reader> getAllReaders() {
        return readerRepository.findAll();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void save(Reader reader) {
        readerRepository.save(reader);
    }

    @Transactional(readOnly = true,rollbackFor = RuntimeException.class)
    public Reader findByUserName(String userName) {
        return readerRepository.findByUserName(userName);
    }
    @Transactional(readOnly = true,rollbackFor = RuntimeException.class)
    public Reader findByEmail(String email) {
        return readerRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Page<Reader> getPage(int pageNo, int pageSize) {
        PageRequest pageable = new PageRequest(pageNo - 1, pageSize);
        return readerRepository.findAll(pageable);
    }

    /**
     * 根据id 删除读者账号
     */
    public ResponseMessage deleteReaderById(int id)
    {
        //根据id 拿到该用户信息
        Reader reader = readerRepository.findOne(id);
        ResponseMessage responseMessage = new ResponseMessage();
        if (reader==null)
        {
            //进行错误处理
            // 返回用户不存在信息
            responseMessage.setCode(-1);
            responseMessage.setMessage("该用户不存在");
            return responseMessage;
        }
        //首先查询该用户的罚金有没有被缴纳

        Double totalFine = reader.getUnPaidFine();
        if (totalFine>0)
        {
            //读者罚金没有缴纳，不能注销
            responseMessage.setCode(-1);
            responseMessage.setMessage("读者罚金未缴纳完全，不能注销");
        }

        //查询该用户是否有没有归还的书籍
        int borrowNum = reader.getBorrowNum();
        if (borrowNum!=0)
        {
            //读者还有 没有归还的书籍
            responseMessage.setCode(-1);
            responseMessage.setMessage("注销失败，该读者有没有归还的书籍");

        }

        // 删除该用户
        readerRepository.delete(id);
        responseMessage.setCode(0);
        responseMessage.setMessage("删除成功");
        return responseMessage;
    }
}
