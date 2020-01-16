package com.qw.coordinatetools.txt;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Polygon;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * txt 界址点解析类
 */
public interface TxtOperator {
    /**
     * 支持的界址点类型
     */
    enum Type {
        commonOperator,
        //勘测定界界址点坐标交换格式
        kcdj,
        stnzy
    }

    /**
     * 界址点文本格式转geometry
     *
     * @param txt
     * @return
     */
    List txt2Geo(String txt);

    /**
     * geometry 转界址点文本
     *
     * @param geometry
     * @return
     */
    String geo2txt(Geometry geometry);

    /**
     * 简单解析单个地块。
     * 根据杭州老一张图的自定义界址点文件功能实现
     * @param txt txt文件
     * @param fgf 分隔符
     * @param tghs 跳过多少行数进行进行（客户都是从1开始数哦）
     * @param xls x所在的列数（客户都是从1开始数哦）
     * @param yls y所在的列数（客户都是从1开始数哦）
     * @return Geometry 返回的图形
     */
    @SneakyThrows
    default Geometry commonSigleReader(String txt, String fgf, int tghs, int xls, int yls){
       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(txt.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        String lineStr = "";
        List<String> txtList = new ArrayList<>();
        while ((lineStr = bufferedReader.readLine()) != null) {
            if (!lineStr.trim().equals("")){
                txtList.add(lineStr);
            }
        }
        Polygon polygon = new Polygon();
        for (int i = tghs; i < txtList.size(); i++) {

            String[] sz = txtList.get(i).split(fgf);
            String x= sz[xls-1];
            String y= sz[yls-1];
            if (i==tghs){
                polygon.startPath(Double.valueOf(x),Double.valueOf(y));
            }else{
                polygon.lineTo(Double.valueOf(x),Double.valueOf(y));
            }
        }
        return polygon;
    }
}
