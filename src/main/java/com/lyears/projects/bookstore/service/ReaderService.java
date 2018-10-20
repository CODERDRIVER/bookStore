package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.model.ReaderData;
import com.lyears.projects.bookstore.repository.ConstantsRepository;
import com.lyears.projects.bookstore.repository.ReaderRepository;
import com.lyears.projects.bookstore.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    /**
     *  根据手机号查询读者信息
     * @param phoneNumber
     */
    public Reader findByPhoneNumber(String phoneNumber)
    {
        return readerRepository.findByPhoneNumber(phoneNumber);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(Integer id) {
        readerRepository.delete(id);
    }

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    public Reader findOne(Integer id) {
        return readerRepository.findOne(id);
    }

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    public List<ReaderData> getAllReaders() {
        List<Reader> all = readerRepository.findAll();
        List<ReaderData> readerDatas = new ArrayList<>();
        all.forEach(reader -> {
            ReaderData readerData = new ReaderData();
            readerData.setBorrowNum(reader.getBorrowNum());
            readerData.setEmail(reader.getEmail());
            readerData.setPaidFine(reader.getPaidFine());
            readerData.setPassword(reader.getPassword());
            readerData.setPhoneNumber(reader.getPhoneNumber());
            readerData.setReaderId(reader.getReaderId());
            readerData.setUnPaidFine(reader.getUnPaidFine());
            readerData.setUserName(reader.getUserName());
            readerDatas.add(readerData);
        });
        return readerDatas;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void save(Reader reader) {
        if (reader.getReaderId()==null)
        {
            readerRepository.save(reader);
            return ;
        }
        int readerId = reader.getReaderId();
        Reader one = readerRepository.findOne(readerId);
        if (reader.getPassword()==null)
        {
            reader.setPassword(one.getPassword());
        }
        if (reader.getBorrowNum()==null)
        {
            reader.setBorrowNum(one.getBorrowNum());
        }
        reader.setUnPaidFine(one.getUnPaidFine());
        reader.setPaidFine(one.getPaidFine());
        readerRepository.save(reader);
    }

    @Transactional(readOnly = true,rollbackFor = RuntimeException.class)
    public Reader findByUserName(String userName) {
        return readerRepository.findByUserName(userName);
    }

    /**
     *
     * @param email
     * @return
     */
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
     *  删除多个id
     */
    public List<ResponseMessage> deleteReaderByIds(String[] ids)
    {
        List<ResponseMessage> list = new ArrayList<>();
        for (String i: ids)
        {
            ResponseMessage responseMessage = deleteReaderById(Integer.parseInt(i));
            if (responseMessage.getCode()!=0)
            {
                list.add(responseMessage);
            }
        }
        return list;
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
            responseMessage.setData(reader.getReaderId());
            return responseMessage;
        }
        //首先查询该用户的罚金有没有被缴纳

        Double totalFine = reader.getUnPaidFine();
        if (totalFine!=null&&totalFine>0)
        {
            //读者罚金没有缴纳，不能注销
            responseMessage.setCode(-1);
            responseMessage.setMessage("读者罚金未缴纳完全，不能注销");
            responseMessage.setData(reader.getReaderId());
        }

        //查询该用户是否有没有归还的书籍
        int borrowNum = reader.getBorrowNum();
        if (borrowNum!=0)
        {
            //读者还有 没有归还的书籍
            responseMessage.setCode(-1);
            responseMessage.setMessage("注销失败，该读者有没有归还的书籍");
            responseMessage.setData(reader.getReaderId());
        }

        // 删除该用户
        readerRepository.delete(id);
        responseMessage.setCode(0);
        responseMessage.setMessage("删除成功");
        return responseMessage;
    }
}
