package com.qw.coordinatetools.common;
import com.esri.core.geometry.Geometry;


public class ConvertGeometryType {
    /**
     * 转换成Rest的服务格式
     * @param geometryType
     * @return
     */
    public static String convertToEsriRestType(GeometryType geometryType){
        switch (geometryType){
            case POINT:
                return "esriGeometryPoint";
            case ENVELOPE:
                return "esriGeometryEnvelope";
            case POLYLINE:
            case LINESTRING:
            case MULTILINESTRING:
                return "esriGeometryPolyline";
            case MULTIPOINT:
                return "esriGeometryMultipoint";
            default:
                return "esriGeometryPolygon";
        }
    }

    public static Geometry.Type convertToEsriType(GeometryType geometryType){
        switch (geometryType){
            case POINT:
                return Geometry.Type.Point;
            case ENVELOPE:
                return Geometry.Type.Envelope;
            case POLYLINE:
            case LINESTRING:
            case MULTILINESTRING:
                return Geometry.Type.Polyline;
            case MULTIPOINT:
                return Geometry.Type.MultiPoint;
            default:
                return Geometry.Type.Polygon;
        }
    }

}
