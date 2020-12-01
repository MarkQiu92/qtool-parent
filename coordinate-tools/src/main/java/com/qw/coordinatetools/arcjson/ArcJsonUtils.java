package com.qw.coordinatetools.arcjson;

import com.esri.core.geometry.*;
import com.qw.coordinatetools.wkt.WktUtils;

/**
 * arcJson操作类
 */
public class ArcJsonUtils {
    private OperatorExportToJson operatorExportToJson = OperatorExportToJson.local();
    private OperatorImportFromJson operatorImportFromJson = OperatorImportFromJson.local();

    public  MapGeometry json2Geo(String json){
        return operatorImportFromJson.execute(Geometry.Type.Unknown,json);
    }

    public String geo2Json(SpatialReference spatialReference,Geometry geometry){
        return operatorExportToJson.execute(spatialReference,geometry);
    }

    public static void main(String[] args) {
       /* WktUtils wktUtils = new WktUtils();
        String wkt = "POLYGON((120.7 29.420833333,120.7125 29.420833333,120.7125 29.416666667,120.716666667 29.416666667,120.716666667 29.420833333,120.726111111 29.420833333,120.726111111 29.4125,120.7 29.4125,120.7 29.420833333))";
        Geometry geometry = wktUtils.wktStr2Geo(wkt);
        ArcJsonUtils jsonUtils = new ArcJsonUtils();
        SpatialReference spatialReference = SpatialRefereddnce.create(4490);
        String json = jsonUtils.geo2Json( spatialReference,geometry);
        System.out.println(json);*/
        String json = "{\"rings\":[[[121.4275679640633,28.669742285125515],[121.4275679640633,28.669515786985993],[121.42788982978882,28.669515786985993],[121.42788982978882,28.669742285125515]]]}";
        ArcJsonUtils jsonUtils = new ArcJsonUtils();
        Geometry geometry =  jsonUtils.json2Geo(json).getGeometry();
        WktUtils wktUtils = new WktUtils();
        System.out.println(wktUtils.geo2WktStr(geometry));
    }
}
