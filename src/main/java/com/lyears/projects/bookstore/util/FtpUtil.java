package com.lyears.projects.bookstore.util;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 文件上传工具类
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/11 21:26
 */
public class FtpUtil {

    public static boolean uploadFile(MultipartFile multipartFile,String address,int port,
                                     String username,String password,String basePath,String name,int fileType) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(address, port);
            ftpClient.login(username,password);

            //更改图片保存路径
            ftpClient.changeWorkingDirectory(basePath);

            /**
             * 按照年月日的文件夹目录进行保存
             */
            Date now = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String[] paths = simpleDateFormat.format(now).split("-");

            for (String s:paths)
            {
                ftpClient.makeDirectory(s);
                ftpClient.changeWorkingDirectory(s);
            }
            ftpClient.setFileType(fileType);
            return ftpClient.storeFile(name,multipartFile.getInputStream());
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
