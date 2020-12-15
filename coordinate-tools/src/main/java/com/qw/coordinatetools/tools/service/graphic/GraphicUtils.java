package com.qw.coordinatetools.tools.service.graphic;

import com.esri.core.geometry.Point;
import com.qw.coordinatetools.tools.bean.ExportMapBean;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

public class GraphicUtils {
    /**
     * 画图
     *
     * @param bean
     * @param points
     * @return
     * @throws IOException
     */
    public static BufferedImage draw(ExportMapBean bean, LinkedList<LinkedList<LinkedList<Point>>> points) throws IOException {
        double width = bean.getWidth();
        double height = bean.getHeight();
        // 最大x
        double maxX = bean.getMaxX();
        // 最大y
        double maxY = bean.getMaxY();
        // 最小x
        double minX = bean.getMinX();
        // 最小y
        double minY = bean.getMinY();
        // 经度差
        double diffL = maxX - minX;
        // 纬度差
        double diffB = maxY - minY;
        double xRate, yRate;
        //单位纬度坐标的有多少个px值。
        yRate = height / diffB;
        //单位经度坐标有多少个px值
        xRate = width / diffL;
        BufferedImage image = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics2D graphics = image.createGraphics();
        graphics.setBackground(Color.WHITE);
        //透明背景
        image = graphics.getDeviceConfiguration().createCompatibleImage((int) width, (int) height, Transparency.TRANSLUCENT);
        graphics.dispose();
        graphics = image.createGraphics();
        // 消除线条锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (LinkedList<LinkedList<Point>> opoint : points) {
            //地块
            Area polyArea =null;
            for (int i = 0; i < opoint.size(); i++) {
                LinkedList<Point> mpoint = opoint.get(i);
                Polygon polygon = new Polygon();
                for (int l = 0; l < mpoint.size(); l++) {
                    Point point = mpoint.get(l);
                    double xpoint, ypoint;
                    xpoint = (point.getX() - minX) * xRate;
                    ypoint = height - (point.getY() - minY) * yRate;
                    polygon.addPoint((int) xpoint, (int) ypoint);
                }
                //外圈
                if (i==0){
                    polyArea = new Area(polygon);
                }
                //有内圈
                if (i>0){
                    polyArea.subtract(new Area(polygon));
                }
            }
            graphics.setColor(new Color(0, 55, 255, 186));
            graphics.fill(polyArea);
            graphics.setColor(new Color(255, 0, 0, 255));
            graphics.draw(polyArea);
        }
        graphics.dispose();// 释放此图形的上下文并释放它所使用的所有系统资源
        return image;
    }
}
