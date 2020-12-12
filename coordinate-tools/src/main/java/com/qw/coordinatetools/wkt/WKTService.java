package com.qw.coordinatetools.wkt;

import com.esri.core.geometry.Point;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.commons.lang3.StringUtils;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class WKTService {

    private NumberFormat nf = new DecimalFormat("##############.#########");

    public static LinkedList<LinkedList<LinkedList<Point>>> getPointsFormWKT(String wkt) {
        WKTReader readerWkt = new WKTReader(new GeometryFactory());
        com.vividsolutions.jts.geom.Geometry geometry = null;
        try {
            geometry = readerWkt.read(wkt);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
        return getPointsFormPolygon(geometry);
    }

    public static LinkedList<LinkedList<LinkedList<Point>>> getPointsFormPolygon(com.vividsolutions.jts.geom.Geometry geometry) {
        LinkedList<LinkedList<LinkedList<Point>>> geoList = new LinkedList<>();
        if (geometry.getGeometryType().toLowerCase().equals(Const.POLYGON)) {
            Polygon polygon = (Polygon) geometry;
            LinkedList<LinkedList<Point>> rings = new LinkedList<>();
            com.vividsolutions.jts.geom.Geometry boundary = polygon.getBoundary();
            int bN = boundary.getNumGeometries();
            int i = 0;
            while (bN > i) {
                com.vividsolutions.jts.geom.Geometry path = boundary.getGeometryN(i);
                addRing(rings, path.getCoordinates());
                i++;
            }
            geoList.add(rings);
        } else if (geometry.getGeometryType().toLowerCase().equals(Const.WKT_MULTIPOLYGON.toLowerCase())) {
            MultiPolygon polygon = (MultiPolygon) geometry;
            int num = polygon.getNumGeometries();
            int i = 0;
            while (num > i) {
                com.vividsolutions.jts.geom.Geometry son = polygon.getGeometryN(i);
                int bN = son.getBoundary().getNumGeometries();
                LinkedList<LinkedList<Point>> rings = new LinkedList<>();
                int z = 0;
                while (bN > z) {
                    com.vividsolutions.jts.geom.Geometry path = son.getBoundary().getGeometryN(z);
                    addRing(rings, path.getCoordinates());
                    z++;
                }
                geoList.add(rings);
                i++;
            }
        } else if (geometry.getGeometryType().toLowerCase().equals(Const.WKT_POINT.toLowerCase())) {
            com.vividsolutions.jts.geom.Point point = (com.vividsolutions.jts.geom.Point) geometry;
            LinkedList<LinkedList<Point>> rings = new LinkedList<>();
            addRing(rings, point.getCoordinates());
            geoList.add(rings);
        }
        return geoList;
    }

    private static void addRing(LinkedList<LinkedList<Point>> rings, Coordinate[] pc) {
        LinkedList<Point> ppoints = new LinkedList<Point>();
        for (Coordinate coo : pc) {
            Point point = new Point();
            point.setXY(coo.x, coo.y);
            ppoints.add(point);
        }
        rings.add(ppoints);
    }


    private void appendToWKT(StringBuffer wkt, List<Point> points) {
        wkt.append("(");
        List<String> a = pointToString(points);
        String c = StringUtils.join(a, ",");
        wkt.append(c).append(")");
    }

    private List<String> pointToString(List<Point> points) {
        List<String> strings = new ArrayList<>();
        for (int j = 0; j < points.size(); j++) {
            Point point = points.get(j);
            strings.add(nf.format(point.getX()) + " " + nf.format(point.getY()));
        }
        return strings;
    }
}