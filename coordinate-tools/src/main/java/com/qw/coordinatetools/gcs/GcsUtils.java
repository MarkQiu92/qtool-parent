package com.qw.coordinatetools.gcs;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;

import com.qw.coordinatetools.common.ConvertGeometryType;
import com.qw.coordinatetools.common.GeometryType;
import org.geotools.geometry.jts.JTS;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * 坐标转换
 */
public class GcsUtils {

    public static Geometry convertSR(Geometry source, String sourceSR, String destSR) {
            String wkt = GeometryEngine.geometryToWkt(source, 0);
            return convertSR(wkt,sourceSR,destSR);
    }

    /**
     * wkt投影变换
     * @param wkt
     * @param sourceSR
     * @param destSR
     * @return
     */
    public static String wktConvertSR(String wkt, String sourceSR, String destSR){
        org.locationtech.jts.geom.Geometry geometry = wkt2GeoToolGeometry(wkt);
        geometry = conver(sourceSR, destSR, geometry);
        return geometry.toText();
    }
    public static Geometry convertSR(String wkt, String sourceSR, String destSR) {
        try {
            org.locationtech.jts.geom.Geometry geometry = wkt2GeoToolGeometry(wkt);
            geometry = conver(sourceSR, destSR, geometry);
            return GeometryEngine.geometryFromWkt(geometry.toText(), 0, ConvertGeometryType.convertToEsriType(GeometryType.of(geometry.getGeometryType())));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("坐标转换失败");
        }
    }

    private static org.locationtech.jts.geom.Geometry wkt2GeoToolGeometry(String wkt)  {

        try {
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
            WKTReader reader = new WKTReader(geometryFactory);
            return reader.read(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("wkt 格式无法转换成 geoTools Geometry  ");
        }
    }

    private static org.locationtech.jts.geom.Geometry conver(String sourceSR, String destSR, org.locationtech.jts.geom.Geometry geometry) {
        try {
            CoordinateReferenceSystem crsSource = CRS.decode(sourceSR);
            CoordinateReferenceSystem crsTarget = CRS.decode(destSR);
            MathTransform transform = CRS.findMathTransform(crsSource, crsTarget);
            geometry = JTS.transform(geometry, transform);
        } catch (FactoryException e) {
            e.printStackTrace();
            throw new RuntimeException(""+sourceSR+"  to  "+destSR+" 的转换失败");
        } catch (MismatchedDimensionException e) {
            e.printStackTrace();
            throw new RuntimeException("没有找到 "+sourceSR+"  to  "+destSR+" 的转换参数");
        } catch (TransformException e) {
            e.printStackTrace();
            throw new RuntimeException(""+sourceSR+"  to  "+destSR+" 的转换失败");
        }
        return geometry;
    }

    /**
     *  投影到对应带号
     * @param source
     * @return
     */
    public  static Geometry projection(Geometry source){
        Geometry tyGeo = null;
        return tyGeo;
    }


}
