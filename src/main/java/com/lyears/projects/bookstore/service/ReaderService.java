package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.repository.ReaderRepository;
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
}
