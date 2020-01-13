package com.qw.coordinatetools.wkt;

import com.esri.core.geometry.*;

/**
 * wkt 格式工具类
 *
 * @author qiuwei
 */
public class WktUtils {
    private OperatorExportToWkt operatorExportToWkt = OperatorExportToWkt.local();
    private OperatorImportFromWkt operatorImportFromWkt = OperatorImportFromWkt.local();

    /**
     * geometry转wkt
     *
     * @param geometry
     * @return
     */
    public String  geo2WktStr(Geometry geometry) {
        return operatorExportToWkt.execute(WktImportFlags.wktImportDefaults, geometry, null);
    }

    /**
     * geometry转wkt
     *
     * @param wkt_str
     * @return
     */
    public Geometry wktStr2Geo(String wkt_str) {
        return operatorImportFromWkt.execute(WktImportFlags.wktImportDefaults,Geometry.Type.Unknown,wkt_str,null);
    }

    public static void main(String[] args) {
        WktUtils wktUtils = new WktUtils();
        String wkt = "POLYGON((120.7 29.420833333,120.7125 29.420833333,120.7125 29.416666667,120.716666667 29.416666667,120.716666667 29.420833333,120.726111111 29.420833333,120.726111111 29.4125,120.7 29.4125,120.7 29.420833333))";
        Geometry geometry = wktUtils.wktStr2Geo(wkt);
        System.out.println(geometry.getType());
         OperatorExportToJson json =  OperatorExportToJson.local();
        System.out.println(json.execute(SpatialReference.create(4490),geometry));
        String wktN = wktUtils.geo2WktStr(geometry);
        System.out.println(wktN);
    }
}
