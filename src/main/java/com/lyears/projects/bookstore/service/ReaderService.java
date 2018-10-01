package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fzm
 * @date 2018/10/2
 **/
@Service
public class ReaderService {

    @Autowired
    private ReaderRepository readerRepository;

    public List<Reader> getAllReaders(){
        return readerRepository.findAll();
    }
}
