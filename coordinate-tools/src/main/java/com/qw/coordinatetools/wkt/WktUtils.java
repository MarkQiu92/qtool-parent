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
        String wkt = "POLYGON((121.4268 28.6688,121.4263 28.6692,121.4269 28.6695,121.4272 28.6693,121.4272 28.6691,121.4268 28.6688))";
        Geometry geometry = wktUtils.wktStr2Geo(wkt);
        System.out.println(geometry.getType());
        String wktN = wktUtils.geo2WktStr(geometry);
        System.out.println(wktN);
    }
}
