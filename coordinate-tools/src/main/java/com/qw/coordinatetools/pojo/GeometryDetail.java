package com.qw.coordinatetools.pojo;

import com.esri.core.geometry.Geometry;
import lombok.Data;

@Data
public class GeometryDetail {
    private Geometry geometry;
    private int wkid;
    private String wkt;
}
