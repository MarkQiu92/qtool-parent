package com.qw.coordinatetools.shape;

import cn.hutool.core.io.IoUtil;
import com.esri.core.geometry.*;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


import java.io.*;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;

/**
 * wkt 格式工具类
 *
 * @author qiuwei
 */
public class ShapeUtils {
    private OperatorImportFromESRIShape operatorImportFromESRIShape = OperatorImportFromESRIShape.local();
    private OperatorExportToESRIShape operatorExportToESRIShape = OperatorExportToESRIShape.local();

    /**
     * geometry转wkt
     *
     * @param geometry
     * @return
     */
    public String  geo2Shape(Geometry geometry) {
       // return operatorExportToESRIShape.execute(WktImportFlags.wktImportDefaults, geometry, null);
        return  null;
    }

    /**
     * geometry转wkt
     *
     * @param shapeByteBuffer
     * @return
     */
    public Geometry shape2Geo(ByteBuffer shapeByteBuffer) {
        return operatorImportFromESRIShape.execute(ShapeImportFlags.ShapeImportDefaults,Geometry.Type.Unknown,shapeByteBuffer);
    }

    public static void main(String[] args) throws IOException {
        ShapeUtils utils = new ShapeUtils();
         File shapeFile = new File("C:\\Users\\qiuwei\\Desktop\\长兴压覆SHP图形\\BMD.shp");
        FileInputStream in = new FileInputStream(shapeFile);
        ByteBuffer byteBuffer = ByteBuffer.wrap(toByteArray(in));
        //Geometry geometry = GeometryEngine.geometryFromEsriShape(toByteArray(in),Geometry.Type.Unknown);
        utils.shape2Geo("C:\\Users\\qiuwei\\Desktop\\长兴压覆SHP图形\\BMD.shp");
       // System.out.println(geometry.getType());

    }
    public void shape2Geo(String path){
        ShapefileDataStore shpDataStore = null;
        try{
            shpDataStore = new ShapefileDataStore(new File(path).toURI().toURL());
            shpDataStore.setCharset(Charset.forName("GBK"));
            String typeName = shpDataStore.getTypeNames()[0];
            FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = null;
            featureSource = (FeatureSource<SimpleFeatureType, SimpleFeature>)shpDataStore.getFeatureSource(typeName);
            FeatureCollection<SimpleFeatureType, SimpleFeature> result = featureSource.getFeatures();
            System.out.println(result.size());
            FeatureIterator<SimpleFeature> itertor = result.features();
            while(itertor.hasNext()){
                SimpleFeature feature = itertor.next();
                Collection<Property> p = feature.getProperties();
                Iterator<Property> it = p.iterator();
                while(it.hasNext()) {
                    Property pro = it.next();
                    if (pro.getValue() instanceof Point) {
                        System.out.println("PointX = " + ((Point)(pro.getValue())).getX());
                        System.out.println("PointY = " + ((Point)(pro.getValue())).getY());
                    } else {
                        System.out.println(pro.getName() + " = " + pro.getValue());
                    }
                }
            }
            itertor.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e) { e.printStackTrace(); }


}




    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024*4];
        int n=0;
        while ( (n=in.read(buffer)) !=-1) {
            out.write(buffer,0,n);
        }
        return out.toByteArray();
    }
}
