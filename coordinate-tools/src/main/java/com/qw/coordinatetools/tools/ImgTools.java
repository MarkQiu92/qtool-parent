package com.qw.coordinatetools.tools;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.esri.core.geometry.*;
import com.esri.core.geometry.Point;
import com.qw.coordinatetools.CoordUtils;
import com.qw.coordinatetools.tools.bean.ExportMapBean;
import com.qw.coordinatetools.wkt.WKTService;
import com.qw.coordinatetools.wkt.WktUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ImgTools {
private static WktUtils wktUtils = new WktUtils();
    public void wkt2image(String wkt){

    }
    /**
     *  出图
     * @param wkt 需要画的图
     * @return
     */
    public static String  imgLayer(String wkt, double width, double height){
        Geometry geometry = wktUtils.wktStr2Geo(wkt);
        Geometry bufferGeometry =GeometryEngine.buffer(geometry,SpatialReference.create(4490),0.002);
        ExportMapBean bean  = new ExportMapBean();
        bean.setHeight(height);
        bean.setWidth(width);
        LinkedList<LinkedList<LinkedList<Point>>> points = WKTService.getPointsFormWKT(wkt);
        Envelope env = new Envelope();
        bufferGeometry.queryEnvelope(env);
        bean.setMinX(env.getLowerLeft().getX());
        bean.setMinY(env.getLowerLeft().getY());
        bean.setMaxX(env.getUpperRight().getX());
        bean.setMaxY(env.getUpperRight().getY());
        String base64String = "";
        try {
            BufferedImage graphic = draw(bean, points);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(graphic, "png", baos);
                java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
                base64String =  encoder.encodeToString(baos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                baos.close();
            }

            return  base64String;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String wkt = "POLYGON((120.7 29.420833333,120.7125 29.420833333,120.7125 29.416666667,120.716666667 29.416666667,120.716666667 29.420833333,120.726111111 29.420833333,120.726111111 29.4125,120.7 29.4125,120.7 29.420833333))";
        String base = ImgTools.imgLayer(wkt,200,200);
        System.out.println(base);
    }
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
        double maxX = bean.getMaxX(); // 最大x
        double maxY = bean.getMaxY(); // 最大y
        double minX = bean.getMinX();// 最小x
        double minY = bean.getMinY(); // 最小y
        double diffL = maxX - minX;// 经度差
        double diffB = maxY - minY;// 纬度差
        double xRate, yRate;
        yRate = height / diffB; //单位纬度坐标的有多少个px值。
        xRate = width / diffL;//单位经度坐标有多少个px值
        BufferedImage image = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics2D graphics = image.createGraphics();
        graphics.setBackground(Color.WHITE);
        //透明背景
        image = graphics.getDeviceConfiguration().createCompatibleImage((int)width,(int) height, Transparency.TRANSLUCENT);
        graphics.dispose();
        graphics = image.createGraphics();
        // 消除线条锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        LinkedList<LinkedList<LinkedList<Point>>> geoList = new LinkedList<>();
        Polygon polygon = new Polygon();
        for (LinkedList<LinkedList<Point>> opoint : points) {
            LinkedList<LinkedList<Point>> rings = new LinkedList<>();
            for (LinkedList<Point> mpoint : opoint) {
                LinkedList<Point> ppoints = new LinkedList<Point>();
                for (int l = 0; l < mpoint.size(); l++) {
                    Point point = mpoint.get(l);
                    double xpoint, ypoint;
                    xpoint = (point.getX() - minX) * xRate;
                    ypoint = height - (point.getY() - minY) * yRate;
                    polygon.addPoint((int)xpoint,(int)ypoint);
                }
            }
        }
        graphics.setColor(new Color(0, 0, 255,50));
        graphics.fillPolygon(polygon);
        graphics.setColor(new Color(0, 0, 255));
        graphics.drawPolygon(polygon);
        graphics.dispose();// 释放此图形的上下文并释放它所使用的所有系统资源
        return image;
    }
}
