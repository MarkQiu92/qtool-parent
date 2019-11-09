package com.qw.coordinatetools.txt.operatorImp;

import cn.hutool.core.util.ReUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.qw.coordinatetools.CoordUtils;
import com.qw.coordinatetools.txt.TxtOperator;
import com.qw.coordinatetools.wkt.WktUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * 勘测定界界址点坐标交换格式
 * 	文本格式
 * [属性描述]
 * 格式版本号=
 * 数据产生单位=
 * 数据产生日期=
 * 坐标系=80国家大地坐标系
 * 几度分带=
 * 投影类型=
 * 计量单位=米
 * 带号=
 * 精度=
 * 转换参数=X平移,Y平移,Z平移,X旋转,Y旋转,Z旋转,尺度参数
 * [地块坐标]
 * 界址点数,地块面积,地块编号,地块名称,记录图形属性(点、线、面),图幅号,地块用途,地类编码,@
 * {点号,地块圈号,X坐标,Y坐标
 * ...
 * ...
 * 点号,地块圈号,X坐标,Y坐标}
 * 界址点数,地块面积,地块编号,地块名称,记录图形属性(点、线、面),图幅号,地块用途,地类编码,@
 * {点号,地块圈号,X坐标,Y坐标
 * ...
 * ...
 * 点号,地块圈号,X坐标,Y坐标}
 */

public class TxtKcdjOperator implements TxtOperator {
    Log log = LogFactory.getCurrentLogFactory().getLog(TxtKcdjOperator.class);

    @Override
    public List<Geometry> txt2Geo(String txt) {
        log.debug("获取到的txt界址点：{}", txt);
        BufferedReader bufferedReader = null;
        List<Geometry> list = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(txt.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
            String lineStr;
            //读取头部信息
            while ((lineStr = bufferedReader.readLine()) != null) {
                if (lineStr.startsWith("坐标系=")) {
                    List<String> vList = ReUtil.findAll("(?<==).+", lineStr, 0);
                    System.out.println(vList.get(0));
                } else if (lineStr.startsWith("投影类型=")) {
                    List<String> vList = ReUtil.findAll("(?<==).+", lineStr, 0);
                    System.out.println(vList.get(0));
                } else if (lineStr.startsWith("带号=")) {
                    List<String> vList = ReUtil.findAll("(?<==).+", lineStr, 0);
                    System.out.println(vList.get(0));
                } else if (lineStr.startsWith("[地块坐标]")) {
                    log.debug("解析完属性信息");
                    break;
                }
            }
            List<Point> dkList = new ArrayList<>();
            while ((lineStr = bufferedReader.readLine()) != null) {
                String[] sxs = lineStr.split(",");
                int gs = Integer.valueOf(sxs[0]);
                log.debug("开始解析地块: {},地块为： {} 点位个数为{}", sxs[3], sxs[4],gs);
                String currentqh = null;//当前圈号
                Polygon polygon = new Polygon();
                for (int i = 0; i < gs; i++) {
                    String[] zbcs;
                    try {
                        zbcs = bufferedReader.readLine().split(",");
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.error(lineStr + "地块报错，检查是否点号个数错误");
                        throw new RuntimeException(lineStr + "地块报错，检查是否界址点数错误");
                    }
                    String dh = zbcs[0]; //点号
                    String qh = zbcs[1]; //地块圈号
                    //首条坐标设置初始化圈号
                    double x = Double.valueOf(zbcs[2]);
                    double y = Double.valueOf(zbcs[3]);
                    //第一个点
                    if (i == 0) {
                        currentqh = qh;
                        log.debug("画圈号为：{}", qh);
                        polygon.startPath(x, y);
                    } else {
                        if (currentqh.equals(qh)) {//同一个圈
                            polygon.lineTo(x, y);
                        } else {//初始化内圈
                            log.debug("画圈号为：{}", qh);
                            polygon.startPath(x, y);
                            currentqh = qh; //游标指向当前圈号
                        }
                    }
                }
                list.add(polygon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return list;
    }

    @Override
    public String geo2txt(Geometry geometry) {
        return null;
    }
}
