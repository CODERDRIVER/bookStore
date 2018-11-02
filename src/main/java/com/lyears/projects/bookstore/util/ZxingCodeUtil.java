package com.lyears.projects.bookstore.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import org.aspectj.util.FileUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description google 的 zxing  生成条形码 和解析条形码
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/24 11:38
 */
public class ZxingCodeUtil {

    final static String ROOT_BARCODE_IMAGE_PATH = "/Users/mac/bookStore/src/main/resources/static/assets/images/barcode/";
    final static String REMOTE_RELATIVE_PATH = "119.23.75.180/";

    /**
     *  生成条形码
     * @param contents
     * @param width
     * @param height
     */
    public static String createBarCode(String contents,int width,int height)
    {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String filePath = "";
        String relPath = "";
        String format = simpleDateFormat.format(date);
        try {
            File file = new File(ROOT_BARCODE_IMAGE_PATH+format); //+format
            if (!file.exists())
            {
                file.mkdirs();
            }
            String fileName = IDGenerator.getFileName();
             filePath= file.getAbsolutePath()+"/"+fileName+".png";
            relPath +=REMOTE_RELATIVE_PATH+format+"/"+fileName+".png"; //+format
            encode(contents,width,height,filePath,fileName+".png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return relPath;
    }
    /**
     *  条形码生成
     * @param contents
     * @param width
     * @param height
     * @param imgPath
     */
    public static void encode(String contents,int width,int height,String imgPath,String fileName)
    {
        int codeWidth = Math.max(70,width);
        int codeHeight = Math.max(25,height);
        try{
            // 使用EAN_13编码格式进行编码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.CODE_128,codeWidth,codeHeight,null);

            // 生成png 格式的图片保存到imgPath上
            MatrixToImageWriter.writeToStream(bitMatrix,"png",new FileOutputStream(imgPath));
            File file = new File(imgPath);
            if (file.exists())
            {
                FtpUtil.uploadFile(file,fileName);
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                System.out.println(bufferedReader.readLine());
            }

        }catch (Exception e)
        {
            System.out.println(e);
            throw new UserDefinedException(ResultEnum.BARCODE_GENERATE_FAILURE);
        }
    }

    /**
     *  对图片进行解析
     * @param imgPath
     * @return
     */
    public static String decode(String imgPath)
    {
        return decode(new File(imgPath));
    }

    /**
     *  重载方法
     * @param file
     * @return
     */
    public static String decode(File file)
    {
        BufferedImage image = null;
        Result result = null;
        try{
            image = ImageIO.read(file);

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
            result = new MultiFormatReader().decode(binaryBitmap,null);
            return result.getText();
        }catch (Exception e)
        {
            throw new UserDefinedException(ResultEnum.NO_IMAGE_FILE);
        }
    }
}
