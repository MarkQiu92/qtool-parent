package com.qw.coordinatetools.shape;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.io.FileUtil;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.data.shapefile.ShapefileDataStore;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;

import org.geotools.feature.FeatureFactory;
import org.geotools.geojson.feature.FeatureJSON;

import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.Geometry;

public class ShapeFormate {

    /**
     * shp转换为Geojson
     *
     * @param shpPath
     * @return
     */
    public Map shape2Geojson(String shpPath, String jsonPath) {
        Map map = new HashMap();
        try {
            File file = new File(shpPath);
            ShapefileDataStore shpDataStore = null;
            shpDataStore = new ShapefileDataStore(file.toURL());
            //设置编码
            Charset charset = Charset.forName("GBK");
            shpDataStore.setCharset(charset);
            String typeName = shpDataStore.getTypeNames()[0];
            SimpleFeatureSource featureSource = null;
            featureSource = shpDataStore.getFeatureSource(typeName);
            SimpleFeatureCollection result = featureSource.getFeatures();

            map.put("recode", result.size());
            SimpleFeatureIterator itertor = result.features();
            String front = "{\"type\":\"FeatureCollection\",\"features\":[ ";
            BufferedWriter writer = null;
            try {
                FeatureJSON fjson = new FeatureJSON();
                fjson.setEncodeNullValues(true);
                writer = FileUtil.getWriter(jsonPath, Charset.forName("GBK"), false);
                writer.write(front);
                String feature_f ="{\"type\":\"Feature\",\n" +
                        "        \"properties\":{},\n" +
                        "        \"geometry\":";
                String feature_e = " }";
                boolean first = true;
                while (itertor.hasNext()) {
                    if (!first) {
                        writer.write(" , ");
                    } else {
                        first = false;
                    }
                    SimpleFeature feature = itertor.next();
                    Object o = feature.getDefaultGeometry();
                    StringWriter writer2 = new StringWriter();
                    GeometryJSON g = new GeometryJSON();
                    g.write((org.locationtech.jts.geom.Geometry) o, writer2);

                    writer.write(feature_f+writer2.toString()+feature_e);
                    writer2.close();
                    writer.flush();
                }
                writer.write("]}");
                writer.flush();
            } finally {
                shpDataStore.dispose();
                itertor.close();
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Exception e) {

                    }
                }
            }
            map.put("status", "success");
        } catch (Exception e) {
            map.put("status", "failure");
            map.put("message", e.getMessage());
            e.printStackTrace();
        }
        return map;
    }



    /**
     * 工具类测试方法
     *
     * @param args
     */
    public static void main(String[] args) {
        ShapeFormate fileFormat = new ShapeFormate();
        long start = System.currentTimeMillis();
        String shpPath = "D:\\1\\1\\1\\cs\\cs\\Export_Output1.shp";
        String jsonPath = "D:\\1\\1\\1\\cs\\1.geojson";
        Map map = fileFormat.shape2Geojson(shpPath, jsonPath);
        System.out.println(jsonPath + ",共耗时" + (System.currentTimeMillis() - start) + "ms");
    }
}
