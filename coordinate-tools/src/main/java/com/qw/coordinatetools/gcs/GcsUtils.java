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
import org.locationtech.jts.io.WKTReader;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * 坐标转换
 */
public class GcsUtils {

    public static Geometry convertSR(Geometry source, String sourceSR, String destSR) {
        try {
            CoordinateReferenceSystem crsSource = CRS.decode(sourceSR);
            CoordinateReferenceSystem crsTarget = CRS.decode(destSR);
            MathTransform transform = CRS.findMathTransform(crsSource, crsTarget);
            String wkt = GeometryEngine.geometryToWkt(source, 0);
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
            WKTReader reader = new WKTReader(geometryFactory);
            org.locationtech.jts.geom.Geometry geometry = reader.read(wkt);
            geometry = JTS.transform(geometry, transform);
            return GeometryEngine.geometryFromWkt(geometry.toText(), 0, ConvertGeometryType.convertToEsriType(GeometryType.of(geometry.getGeometryType())));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("坐标转换失败");
        }

    }

}
