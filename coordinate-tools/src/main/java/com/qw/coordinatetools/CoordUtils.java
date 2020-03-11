package com.qw.coordinatetools;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MapGeometry;
import com.esri.core.geometry.SpatialReference;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.qw.coordinatetools.wkt.WktUtils;

/**
 * 坐标类工具
 * * @author qiuwei
 */

public class CoordUtils {
    /**
     * wkt格式工具类
     */
    private WktUtils wktUtils = new WktUtils();

    public WktUtils getWktUtils() {
        return wktUtils;
    }

    public String cover(String sourceType, String targetType, String coord) {
        return null;
    }
    //+++++++++++++++++++++++++++++++++++++++json to arcGis geo Json++++++++++++++++++++++++++++=
    public static boolean canParseInt(String str) {
        if (str == null) { //验证是否为空
            return false;
        }
        return str.matches("\\d+"); //使用正则表达式判断该字符串是否为数字，第一个\是转义符，\d+表示匹配1个或 //多个连续数字，"+"和"*"类似，"*"表示0个或多个
    }

    public static MapGeometry getMapGeometryByRings(String geometry, String wkid) {
        if (canParseInt(wkid)) {
            return getMapGeometryByRings(geometry, Integer.parseInt(wkid));
        }
        try {
            return getMapGeometryByRings(geometry, SpatialReference.create(wkid));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("检查arcgis geometry json 格式是否正确");
        }
    }

    public static MapGeometry getMapGeometryByRings(String geometry, int wkid) {

        try {
            return getMapGeometryByRings(geometry, SpatialReference.create(wkid));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("检查arcgis geometry json 格式是否正确");
        }
    }

    public static MapGeometry getMapGeometryByRings(String geometry, SpatialReference spatialReference) throws Exception {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = null;
        parser = factory.createJsonParser(geometry);
        MapGeometry mapGeometry = GeometryEngine.jsonToGeometry(parser);
        mapGeometry.setSpatialReference(spatialReference);
        return mapGeometry;
    }

    public static String getJsonFromGeometry(int wkid, Geometry g) {
        return GeometryEngine.geometryToJson(SpatialReference.create(wkid), g);
    }

    public static String getJsonFromGeometry(String wkid, Geometry g) {
        return GeometryEngine.geometryToJson(SpatialReference.create(wkid), g);
    }


}
