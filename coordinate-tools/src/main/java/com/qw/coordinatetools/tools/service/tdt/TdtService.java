package com.qw.coordinatetools.tools.service.tdt;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.BufferUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.URLEncoder;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class TdtService {
    private static String url = "http://api.tianditu.gov.cn/staticimage?" +
            "center={center}&width={width}&height={height}&zoom={zoom}&layers=img_c,cva_c,cta_c&tk=c18a5fb397de0079e87610b4e230d673";

    public static Image exportTdt(ImgParams imgParams,String width ,String height)   {
      /*  String pixLocation = imgParams.getXmin() + "," + imgParams.getYmax() + "|"
                + imgParams.getXmax() + "," + imgParams.getYmax() + "|"
                + imgParams.getXmax() + "," + imgParams.getYmin() + "|"
                + imgParams.getXmin() + "," + imgParams.getYmin();*/
        String pixLocation = imgParams.getXmin() + "," + imgParams.getYmax() + "%7C"
                + imgParams.getXmax() + "," + imgParams.getYmin();
        String sourceUrl = url.replace("{center}", imgParams.getCenterX() + "," + imgParams.getCenterY())
                .replace("{width}",String.valueOf(Double.valueOf(width).intValue()))
                .replace("{height}",String.valueOf(Double.valueOf(height).intValue()));
        String pointstr="";
        imgParams.setZoom(19);
        do {
            imgParams.setZoom(imgParams.getZoom()-1);
            String calUrl = (sourceUrl+"&pixLocation={pixLocation}").replace("{pixLocation}", pixLocation).replace("{zoom}", String.valueOf(imgParams.getZoom()));
            pointstr = HttpUtil.get(calUrl);
            int i = pointstr.indexOf("-");
         } while (pointstr.indexOf("-") != -1);

        String[] point = pointstr.split("\\|\\|");
        Rectangle rectangle = new Rectangle();
        String[] p =point[0].split(",");
        String[] p2 =point[1].split(",");
        rectangle.setFrameFromDiagonal(Double.valueOf(p[0]), Double.valueOf(p[1]),Double.valueOf(p2[0]),Double.valueOf(p2[1]));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String tempUrl =System.getProperty("java.io.tmpdir");
        File sourceFile = FileUtil.file(tempUrl+ File.separator+ IdUtil.fastUUID()+".pnf");
        HttpUtil.downloadFile(sourceUrl.replace("{zoom}", String.valueOf(imgParams.getZoom())),sourceFile);
        try {
            BufferedImage srcImage =ImageIO.read(sourceFile);
            Image tartImage =ImgUtil.cut(srcImage,rectangle);
            tartImage = ImgUtil.scale(tartImage,Double.valueOf(width).intValue(),Double.valueOf(height).intValue());
            return tartImage;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            FileUtil.del(sourceFile);
        }
        return null;
    }
}
