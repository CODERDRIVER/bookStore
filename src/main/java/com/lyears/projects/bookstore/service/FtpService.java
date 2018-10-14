package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.util.FtpUtil;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/11 21:48
 */
@Service
public class FtpService {

    @Value("${ftp.address}")
    private String ADDRESS;

    @Value("${ftp.port}")
    private int PORT;

    @Value("${ftp.username}")
    private String USERNAME;

    @Value("${ftp.password}")
    private String PASSWORD;

    @Value("${ftp.basepath}")
    private String BASEPATH;

    /**
     * 上传文件
     */
    public boolean uploadFile(MultipartFile multipartFile)
    {
        String originName = multipartFile.getOriginalFilename();
        System.out.println(originName);
        String ext = originName.split("\\.")[1];
        String prefix = UUID.randomUUID().toString();
        String name = prefix+"."+ext;
        return FtpUtil.uploadFile(multipartFile,ADDRESS,PORT,USERNAME,PASSWORD,BASEPATH, name,FTPClient.ASCII_FILE_TYPE);
    }

}
