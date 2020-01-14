package com.qw.coordinatetools.shape;

import cn.hutool.core.io.IoUtil;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


import java.io.*;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * wkt 格式工具类
 *
 * @author qiuwei
 */
public class ShapeUtils {


    public static void main(String[] args) throws IOException {
        ShapeUtils utils = new ShapeUtils();
        List<String> str = utils.simpleShape2WKtList("d:\\tmp\\BMD.shp");
        System.out.println(str);

    }

    /**
     * 返回shape的所有图形集合
     *
     * @param path 文件路径
     */
    public List<String> simpleShape2WKtList(String path) {
        FeatureCollection<SimpleFeatureType, SimpleFeature> result =  shape2geo_geoTools(path);
        if (result==null){
            return null;
        }
        FeatureIterator<SimpleFeature> iterator = result.features();
        List<String> wktList = new ArrayList<>();
        while (iterator.hasNext()){
            SimpleFeature feature = iterator.next();
            Object o =feature.getDefaultGeometryProperty().getValue();
            wktList.add(o.toString());
        }
        return wktList;
    }

    private FeatureCollection<SimpleFeatureType, SimpleFeature> shape2geo_geoTools(String path) {
        ShapefileDataStore shpDataStore = null;
        List<Geometry> geometryList = new ArrayList<>();
        FeatureCollection<SimpleFeatureType, SimpleFeature> result = null;
        try {
            shpDataStore = new ShapefileDataStore(new File(path).toURI().toURL());
            shpDataStore.setCharset(Charset.forName("GBK"));
            String typeName = shpDataStore.getTypeNames()[0];
            FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = null;
            featureSource = (FeatureSource<SimpleFeatureType, SimpleFeature>) shpDataStore.getFeatureSource(typeName);
            result = featureSource.getFeatures();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
