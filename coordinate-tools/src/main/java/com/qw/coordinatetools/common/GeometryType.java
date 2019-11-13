package com.qw.coordinatetools.common;


import java.io.Serializable;

public enum GeometryType implements Serializable {
    UNKNOWN(0),
    //点
    POINT(33),
    //多点
    MULTIPOINT(550),
    //范围
    ENVELOPE(197),
    //线
    POLYLINE(1607),
    //线
    LINESTRING(1607),
    //多线
    MULTILINESTRING(1607),
    //面
    POLYGON(1736),
    //多面
    MULTIPOLYGON(1736),
    //图形集合
    GEOMETRYCOLLECTION(1);

    private static final long serialVersionUID = -7703091684572095232L;

    private int index;

    GeometryType(int index){
        this.index = index;
    }

    public static GeometryType of(String name){
        GeometryType[] types = values();
        for(int z=0;z<types.length;z++){
            if(types[z].name().toLowerCase().equals(name.toLowerCase())){
                return types[z];
            }
        }
        return null;
    }

    public static GeometryType valueOf(int i){
        GeometryType[] types = values();
        for(int z=0;z<types.length;z++){
            if(types[z].index == i){
                return types[z];
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

    public boolean equals(String type){
        return this.toString().equals(type);
    }

}
