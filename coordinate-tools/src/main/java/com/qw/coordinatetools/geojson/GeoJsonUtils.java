package com.qw.coordinatetools.geojson;

import com.esri.core.geometry.*;

public class GeoJsonUtils {
    OperatorImportFromGeoJson importOperator = OperatorImportFromGeoJson.local();
    OperatorExportToGeoJson exportOperator = OperatorExportToGeoJson.local();

    public String geo2Json(Geometry geometry) {
        return exportOperator.execute(geometry);
    }

    public MapGeometry  json2geo(String json) {
        return importOperator.execute(GeoJsonImportFlags.geoJsonImportDefaults,Geometry.Type.Unknown,json,null);
    }

    public static void main(String[] args) {
        GeoJsonUtils utils = new GeoJsonUtils();
      MapGeometry mg=  utils.json2geo("{ \"type\": \"FeatureCollection\",\n" +
              "    \"features\": [\n" +
              "      { \"type\": \"Feature\",\n" +
              "        \"geometry\": {\"type\": \"Point\", \"coordinates\": [102.0, 0.5]},\n" +
              "        \"properties\": {\"prop0\": \"value0\"}\n" +
              "        },\n" +
              "      { \"type\": \"Feature\",\n" +
              "        \"geometry\": {\n" +
              "          \"type\": \"LineString\",\n" +
              "          \"coordinates\": [\n" +
              "            [102.0, 0.0], [103.0, 1.0], [104.0, 0.0], [105.0, 1.0]\n" +
              "            ]\n" +
              "          },\n" +
              "        \"properties\": {\n" +
              "          \"prop0\": \"value0\",\n" +
              "          \"prop1\": 0.0\n" +
              "          }\n" +
              "        },\n" +
              "      { \"type\": \"Feature\",\n" +
              "         \"geometry\": {\n" +
              "           \"type\": \"Polygon\",\n" +
              "           \"coordinates\": [\n" +
              "             [ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0],\n" +
              "               [100.0, 1.0], [100.0, 0.0] ]\n" +
              "             ]\n" +
              "         },\n" +
              "         \"properties\": {\n" +
              "           \"prop0\": \"value0\",\n" +
              "           \"prop1\": {\"this\": \"that\"}\n" +
              "           }\n" +
              "         }\n" +
              "       ]\n" +
              "     }");
        System.out.println(mg);
    }
}
