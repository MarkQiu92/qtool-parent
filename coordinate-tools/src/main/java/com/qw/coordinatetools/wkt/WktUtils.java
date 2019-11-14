package com.qw.coordinatetools.wkt;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.OperatorExportToWkt;
import com.esri.core.geometry.OperatorImportFromWkt;
import com.esri.core.geometry.WktImportFlags;

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
        String wkt = "POLYGON((120.428 29.0596,120.4297 29.0563, 120.4242 29.0546,120.428 29.0596))";
        Geometry geometry = wktUtils.wktStr2Geo(wkt);
        System.out.println(geometry.getType());
        String wktN = wktUtils.geo2WktStr(geometry);
        System.out.println(wktN);
    }
}
