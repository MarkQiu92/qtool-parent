package com.qw.coordinatetools.pojo;

import com.esri.core.geometry.Geometry;
import lombok.Data;

@Data
public class GeometryDetail {
    private Geometry geometry;
    private int wkid;
    private String wkt;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public int getWkid() {
        return wkid;
    }

    public void setWkid(int wkid) {
        this.wkid = wkid;
    }

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }
}
