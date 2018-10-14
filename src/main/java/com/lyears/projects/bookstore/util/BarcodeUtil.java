package com.lyears.projects.bookstore.util;

import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.thymeleaf.util.StringUtils;

import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @Description 条形码生成工具包
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/13 22:00
 */
public class BarcodeUtil {

    public static File generateFile(String msg,String path){
        File file = new File(path);
        try{
            generate(msg,new FileOutputStream(file));
        }catch (FileNotFoundException e)
        {
            //异常处理
            throw  new RuntimeException();
        }
        return file;
    }

    public static  byte[] generate(String msg)
    {
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        generate(msg,ous);
        return ous.toByteArray();
    }

    public static  void generate(String msg, OutputStream os)
    {
        if (StringUtils.isEmpty(msg) || os == null)
        {
            return ;
        }
        Code39Bean code39Bean = new Code39Bean();
        // 精细度
        final int dpi  = 150;

        // module 宽度
        final double moduleWidth = UnitConv.in2mm(1.0f/dpi);


        //配置对象
        code39Bean.setModuleWidth(moduleWidth);
        code39Bean.setWideFactor(3);
        code39Bean.doQuietZone(false);


        String format = "image/png";
        try{
            //输出到流
            BitmapCanvasProvider canvasProvider =  new BitmapCanvasProvider(os,format,dpi, BufferedImage.TYPE_BYTE_BINARY,false,0);

            //生成条形码
            code39Bean.generateBarcode(canvasProvider,msg);

            //结束绘制
            canvasProvider.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
