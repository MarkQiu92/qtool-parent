package com.qw.coordinatetools.tools.bean;

/**
 * 出图的基本属性
 */
public class ExportMapBean {
    private double width = 400; //图片的宽 设个默认值
    private double height =400;//图片的高 设个默认值
    private double maxX; // 最大x
    private double maxY; // 最大y
    private double minX;// 最小x
    private double minY; // 最小y

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }
}
