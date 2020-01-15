package com.qw.coordinatetools.txt;

import com.esri.core.geometry.Geometry;

import java.util.List;

/**
 * txt 界址点解析类
 */
public interface TxtOperator {
    /**
     * 支持的界址点类型
     */
    enum Type {
        //勘测定界界址点坐标交换格式
        kcdj,
        stnzy
    }

    /**
     * 界址点文本格式转geometry
     *
     * @param txt
     * @return
     */
    List txt2Geo(String txt);

    /**
     * geometry 转界址点文本
     *
     * @param geometry
     * @return
     */
    String geo2txt(Geometry geometry);
}
