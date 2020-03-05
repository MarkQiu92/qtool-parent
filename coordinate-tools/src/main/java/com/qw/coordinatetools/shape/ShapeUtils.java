package com.qw.coordinatetools.shape;

import cn.hutool.core.io.IoUtil;
import com.qw.coordinatetools.gcs.CHzCoordDll;
import com.qw.coordinatetools.wkt.WktUtils;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


import java.io.*;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.*;

/**
 * wkt 格式工具类
 *
 * @author qiuwei
 */
public class ShapeUtils {


    public static void main(String[] args) throws IOException {
        ShapeUtils utils = new ShapeUtils();
       /* List<String> str = utils.simpleShape2WKtList("C:\\Users\\qiuwei\\Desktop\\1\\宗地2\\sde.st_zd.shp");
         System.out.println(str.size());*/
        // System.out.println(str);
        // utils.transShape("C:\\Users\\qiuwei\\Desktop\\1\\宗地\\sde.st_zd.shp","C:\\Users\\qiuwei\\Desktop\\1\\宗地2\\sde.st_zd.shp");
        //utils.readDBF();
        utils.transShape("C:\\Users\\qiuwei\\Desktop\\1\\宗地\\sde.st_zd.shp", "C:\\Users\\qiuwei\\Desktop\\1\\宗地2\\sde.st_zd.shp");
        List<String> str = utils.simpleShape2WKtList("C:\\Users\\qiuwei\\Desktop\\1\\宗地2\\sde.st_zd.shp");
        System.out.println(str.size());
        System.out.println(str.get(0));
    }

    public void readDBF() {
        try {
            FileChannel in = new FileInputStream("C:\\Users\\qiuwei\\Desktop\\1\\宗地\\sde.st_zd.dbf").getChannel();
            DbaseFileReader dbfReader = new DbaseFileReader(in, false, Charset.forName("GBK"));
            DbaseFileHeader header = dbfReader.getHeader();
            int fields = header.getNumFields();

            while (dbfReader.hasNext()) {
                DbaseFileReader.Row row = dbfReader.readRow();
//              System.out.println(row.toString());
                for (int i = 0; i < fields; i++) {
                    System.out.println(header.getFieldName(i) + " : " + row.read(i));
                }
            }
            dbfReader.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回shape的所有图形集合
     *
     * @param path 文件路径
     */
    public List<String> simpleShape2WKtList(String path) {
        FeatureCollection<SimpleFeatureType, SimpleFeature> result = shape2geo_geoTools(path);
        if (result == null) {
            return null;
        }
        FeatureIterator<SimpleFeature> iterator = result.features();
        List<String> wktList = new ArrayList<>();
        while (iterator.hasNext()) {
            SimpleFeature feature = iterator.next();
            Object o = feature.getDefaultGeometryProperty().getValue();
            if (o != null) {
                wktList.add(o.toString());
            } else {
                wktList.add("");
            }

        }
        return wktList;
    }

    public void transShape(String srcfilepath, String destfilepath) {
        try {
            //源shape文件
            ShapefileDataStore shapeDS = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(new File(srcfilepath).toURI().toURL());
            //创建目标shape文件对象
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
            params.put(ShapefileDataStoreFactory.URLP.key, new File(destfilepath).toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) factory.createNewDataStore(params);
            // 设置属性
            SimpleFeatureSource fs = shapeDS.getFeatureSource(shapeDS.getTypeNames()[0]);
            //下面这行还有其他写法，根据源shape文件的simpleFeatureType可以不用retype，而直接用fs.getSchema设置
            // ds.createSchema(SimpleFeatureTypeBuilder.retype(fs.getSchema(), DefaultGeographicCRS.WGS84));
            //ds.createSchema(fs.getSchema());
            CoordinateReferenceSystem crsTarget = CRS.decode("EPSG:4490");
            ds.createSchema(SimpleFeatureTypeBuilder.retype(fs.getSchema(), crsTarget));

            //设置writer
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
            WktUtils wktUtils = new WktUtils();
            //写记录
            SimpleFeatureIterator it = fs.getFeatures().features();
            CHzCoordDll cHzCoordDll = new CHzCoordDll();
            int i = 0;
            try {
                while (it.hasNext()) {
                    SimpleFeature f = it.next();
                    SimpleFeature fNew = writer.next();
                    fNew.setAttributes(f.getAttributes());
                    Object o = f.getDefaultGeometryProperty().getValue();
                    if (o != null) {
                        com.esri.core.geometry.Geometry geometry = wktUtils.wktStr2Geo(f.getDefaultGeometryProperty().getValue().toString());
                        com.esri.core.geometry.Geometry geometryCgcs2000 = cHzCoordDll.city2Gcs(geometry);
                        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
                        WKTReader reader = new WKTReader(geometryFactory);
                        String wkt = wktUtils.geo2WktStr(geometryCgcs2000);
                        org.locationtech.jts.geom.Geometry geometry2 = reader.read(wkt);
                        geometry2.setSRID(4490);
                       try {
                           Point point = geometry2.getInteriorPoint();
                           if (point.getX() > 121) {
                               System.out.println(wkt);
                               System.out.println(point);
                           }
                       }catch (Exception e){
                           System.out.println("报错的wkt"+wkt);
                          // e.printStackTrace();
                       }

                        fNew.setDefaultGeometry(wkt);
                    } else {
                        System.out.println("12312312");
                    }
                    i++;
                    writer.write();

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                it.close();
            }
            writer.close();
            ds.dispose();
            shapeDS.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
