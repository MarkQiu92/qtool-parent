package com.qw.coordinatetools.gcs;


import com.alibaba.fastjson.JSON;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.qw.coordinatetools.CoordUtils;
import com.qw.coordinatetools.arcjson.ArcJsonUtils;
import com.qw.coordinatetools.wkt.WktUtils;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 杭州dll解析
 */

public class CHzCoordDll {
    static ArcJsonUtils coordUtils = new ArcJsonUtils();

    public interface CHzCoordDll2 extends Library {
        // DLL文件默认路径为项目根目录，若DLL文件存放在项目外，请使用绝对路径
        CHzCoordDll2 INSTANCE = (CHzCoordDll2) Native.loadLibrary("hzcitydll", CHzCoordDll2.class);// 加载动态库文件

        // 声明将要调用的DLL中的方法（可以是多个方法）
        void City2GCS(double xh, double yh, DoubleByReference B, DoubleByReference L);

        void GCS2City(double B, double L, DoubleByReference xh, DoubleByReference yh);
    }

    private static void city2GCS(List point) {
        DoubleByReference xRef = new DoubleByReference();
        DoubleByReference yRef = new DoubleByReference();
        try {
            double x = Double.valueOf(point.get(1).toString()).doubleValue();
            double y = Double.valueOf(point.get(0).toString()).doubleValue();
            CHzCoordDll2.INSTANCE.City2GCS(x, y, yRef, xRef);
            point.remove(0);
            point.remove(0);
            point.add(BigDecimal.valueOf(xRef.getValue()));
            point.add(BigDecimal.valueOf(yRef.getValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void gcs2City(List point) {
        DoubleByReference xRef = new DoubleByReference();
        DoubleByReference yRef = new DoubleByReference();
        CHzCoordDll2.INSTANCE.GCS2City(Double.valueOf(point.get(1).toString()).doubleValue(), Double.valueOf(point.get(0).toString()).doubleValue(), yRef, xRef);
        point.remove(0);
        point.remove(0);
        point.add(BigDecimal.valueOf(xRef.getValue()));
        point.add(BigDecimal.valueOf(yRef.getValue()));
    }

    /**
     * 杭州城市坐标转经纬度
     *
     * @param geometry
     * @return
     */
    public static Geometry city2Gcs(Geometry geometry) {
        return cover(geometry, true);
    }

    /**
     * 经纬度转杭州城市坐标
     *
     * @param geometry
     * @return
     */
    public static Geometry gcs2city(Geometry geometry) {
        return cover(geometry, false);
    }

    private static Geometry cover(Geometry geometry, boolean is2Gcs) {
        String json = coordUtils.geo2Json(SpatialReference.create(4490), geometry);
        Geometry.Type type = geometry.getType();
        Map jsonMap = JSON.parseObject(json, Map.class);
        //面
        if (type == Geometry.Type.Polygon) {
            List<List<List<BigDecimal>>> rings = (List<List<List<BigDecimal>>>) jsonMap.get("rings");
            for (int i = 0; i < rings.size(); i++) {
                List<List<BigDecimal>> rList = rings.get(i);
                for (int j = 0; j < rList.size(); j++) {
                    List<BigDecimal> point = rList.get(j);
                    if (is2Gcs) {
                        city2GCS(point);
                    } else {
                        gcs2City(point);
                    }
                }
            }
        /*    rings.stream().forEach(lists -> lists.forEach(point -> {
            }));*/

            jsonMap.put("rings", rings);
        } else if (type == Geometry.Type.Line) {
            //线
            List<List<BigDecimal>> path = (List<List<BigDecimal>>) jsonMap.get("path");
            path.parallelStream().forEach(lists -> {
                if (is2Gcs) {
                    city2GCS(lists);
                } else {
                    gcs2City(lists);
                }
            });
        } else if (type == Geometry.Type.Point) {
            List point = new ArrayList();
            point.add(jsonMap.get("x"));
            point.add(jsonMap.get("y"));
            if (is2Gcs) {
                city2GCS(point);
            } else {
                gcs2City(point);
            }

            jsonMap.put("x", point.get(0));
            jsonMap.put("y", point.get(1));
        }
        return coordUtils.json2Geo(JSON.toJSONString(jsonMap)).getGeometry();
    }

    public static void byteToFile(byte[] bytes, String path) {
        try {
            // 根据绝对路径初始化文件
            File localFile = new File(path);
            if (!localFile.exists()) {
                localFile.createNewFile();
            }
            // 输出流
            OutputStream os = new FileOutputStream(localFile);
            os.write(bytes);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
      /*   DoubleByReference xRef = new DoubleByReference();
        DoubleByReference yRef = new DoubleByReference();
        CHzCoordDll2.INSTANCE.City2GCS(83829, 81633.87, yRef, xRef);
        System.out.println(yRef.getValue()+""+xRef.getValue());
        */

        WktUtils utils = new WktUtils();
        String w =
                "MULTIPOLYGON (((81592.25 83807.22999999998, 81633.87 83829, 81644.32650000002 83809.1801, 81605.65000000002 83788.47999999998, 81600.54999999999 83786.31, 81571.42460000003 83772.58559999999, 81562.96999999997 83790.14000000001, 81593.63 83804.43, 81592.25 83807.22999999998)))";
        com.esri.core.geometry.Geometry geometry = utils.wktStr2Geo(w);
        CHzCoordDll.city2Gcs(geometry);
    }

}
