package com.qw.coordinatetools.tools;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.esri.core.geometry.*;
import com.esri.core.geometry.Point;
import com.qw.coordinatetools.CoordUtils;
import com.qw.coordinatetools.tools.bean.ExportMapBean;
import com.qw.coordinatetools.tools.service.eris.ArcgisUtils;
import com.qw.coordinatetools.tools.service.graphic.GraphicUtils;
import com.qw.coordinatetools.tools.service.tile.Tile;
import com.qw.coordinatetools.tools.service.tile.bean.ExportSize;
import com.qw.coordinatetools.tools.service.tile.bean.MapExtent;
import com.qw.coordinatetools.tools.service.tile.bean.TileInfo;
import com.qw.coordinatetools.wkt.WKTService;
import com.qw.coordinatetools.wkt.WktUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ImgTools {
    private static WktUtils wktUtils = new WktUtils();
    static String tileinfostring = "{\"lods\":[{\"level\":1,\"scale\":295829355.4545656,\"resolution\":0.703125},{\"level\":2,\"scale\":147914677.7272828,\"resolution\":0.3515625},{\"level\":3,\"scale\":73957338.8636414,\"resolution\":0.17578125},{\"level\":4,\"scale\":36978669.4318207,\"resolution\":0.087890625},{\"level\":5,\"scale\":18489334.71591035,\"resolution\":0.0439453125},{\"level\":6,\"scale\":9244667.357955175,\"resolution\":0.02197265625},{\"level\":7,\"scale\":4622333.678977588,\"resolution\":0.010986328125},{\"level\":8,\"scale\":2311166.839488794,\"resolution\":0.0054931640625},{\"level\":9,\"scale\":1155583.419744397,\"resolution\":0.00274658203125},{\"level\":10,\"scale\":577791.7098721985,\"resolution\":0.001373291015625},{\"level\":11,\"scale\":288895.85493609926,\"resolution\":0.0006866455078125},{\"level\":12,\"scale\":144447.92746804963,\"resolution\":0.00034332275390624995},{\"level\":13,\"scale\":72223.96373402482,\"resolution\":0.00017166137695312497},{\"level\":14,\"scale\":36111.98186701241,\"resolution\":0.00008583068847656249},{\"level\":15,\"scale\":18055.990933506204,\"resolution\":0.00004291534423828124},{\"level\":16,\"scale\":9027.995466753102,\"resolution\":0.00002145767211914062},{\"level\":17,\"scale\":4513.997733376551,\"resolution\":0.00001072883605957031},{\"level\":18,\"scale\":2256.998866688275,\"resolution\":0.0000053644180297851546}],\"tileUrl\":\"http://t0.tianditu.gov.cn/img_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={TileMatrix}&TILEROW={TileRow}&TILECOL={TileCol}&tk=0a023e52b9e8d3544156af0c674461d5\",\"origin\":{\"x\":-180,\"y\":90},\"compressionQuality\":0,\"rows\":256,\"spatialReference\":{\"wkid\":4490},\"cols\":256}";
    static TileInfo tileInfo = JSON.parseObject(tileinfostring, TileInfo.class); //切片信息

    /**
     * 出图
     *
     * @param wkt 需要画的图
     * @return
     */
    public static String imgLayer(String wkt, double width, double height) {
        Geometry geometry = wktUtils.wktStr2Geo(wkt);
        Geometry bufferGeometry = GeometryEngine.buffer(geometry, SpatialReference.create(4490), 0.0005);
        ExportMapBean bean = new ExportMapBean();
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
            BufferedImage graphic = GraphicUtils.draw(bean, points);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(graphic, "png", baos);
                java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
                base64String = encoder.encodeToString(baos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                baos.close();
            }
            return base64String;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
    /*    String wkt = "MULTIPOLYGON (((40 40, 20 45, 45 30, 40 40)),\n" +
                "((20 35, 10 30, 10 10, 30 5, 45 20, 20 35),\n" +
                "(30 20, 20 15, 20 25, 30 20)))";
        String base = ImgTools.imgLayer(wkt,200,200);
        System.out.println(base);*/
        String wkt = "MULTIPOLYGON (((119.88892006063949 28.411678361266034, 119.8890701434175 28.411563234701376, 119.88904649978879 28.411578296709145, 119.88883168273391 28.41171514685737, 119.88877332824126 28.41182161731209, 119.88863927044979 28.411965219220658, 119.88847820150777 28.41207626145928, 119.88844267310263 28.412088530894284, 119.88828144758646 28.412144171632328, 119.88811573020061 28.4122495340742, 119.88797257438739 28.412366265973247, 119.88766262762961 28.41261898323088, 119.88729302908997 28.412783079352362, 119.88709369457573 28.41284768319636, 119.886950113305 28.41297978907101, 119.88679094706097 28.413093158838464, 119.88664093496492 28.413110728423177, 119.88650829305092 28.41319521884058, 119.8862922032047 28.41337099868963, 119.88629008600115 28.41338487449818, 119.88628752690492 28.41340160124726, 119.88621957273325 28.413472493375377, 119.8861744019747 28.413519363281218, 119.88635218034295 28.413459872456954, 119.88635169255596 28.41332311746936, 119.88636138571776 28.41331665597222, 119.88642909786465 28.41327155167979, 119.88665043422696 28.413248005233477, 119.8868637931471 28.413182719815982, 119.88701018092955 28.41304141277072, 119.88733145055289 28.412835040171416, 119.88752448482707 28.41276280713151, 119.88811986294532 28.412394747653853, 119.88836555004244 28.412243495962546, 119.88867781855652 28.412002797797612, 119.8888148009465 28.41185363998891, 119.88892006063949 28.411678361266034)))";
        String url = "https://gtkj.zjzwfw.gov.cn:6443/zjgt51/397f87a23631c7f04980c8747ea66457/arcgis/rest/services/GCS330000_2002_TDLYGH_2020/GCS330000_2002_YJJBNTBHTB_YJJBNTTB_2020/MapServer";
        File f = ImgTools.imgLayerWithTDT(wkt, 400, 400, url, "0");
        System.out.println(f.getAbsolutePath());
    }
    public static File imgLayerWithTDT(String wkt, double width, double height, String arcGisserviceUrl, String layerIndex){
        Geometry geometry = wktUtils.wktStr2Geo(wkt);
        return imgLayerWithTDT(geometry,wkt,width,height,arcGisserviceUrl,layerIndex);
    }
    public static File imgLayerWithTDT(Geometry geometry,String wkt, double width, double height, String arcGisserviceUrl, String layerIndex) {

        Geometry bufferGeometry = GeometryEngine.buffer(geometry, SpatialReference.create(4490), 0.0001);
        ExportMapBean bean = new ExportMapBean();
        bean.setHeight(height);
        bean.setWidth(width);
        Envelope env = new Envelope();
        bufferGeometry.queryEnvelope(env);
        double area =env.calculateArea2D();
        while (area<1E-6){
            bufferGeometry = GeometryEngine.buffer(bufferGeometry, SpatialReference.create(4490), 0.0001);
            bufferGeometry.queryEnvelope(env);
            area =env.calculateArea2D();
            System.out.println(area);
        }

        bean.setMinX(env.getLowerLeft().getX());
        bean.setMinY(env.getLowerLeft().getY());
        bean.setMaxX(env.getUpperRight().getX());
        bean.setMaxY(env.getUpperRight().getY());
        //天地图影像图
        MapExtent extent = new MapExtent();
        extent.setXmax(bean.getMaxX());
        extent.setXmin(bean.getMinX());
        extent.setYmax(bean.getMaxY());
        extent.setYmin(bean.getMinY());
        ExportSize size = new ExportSize();
        size.setHeight(height);
        size.setWidth(width);
        //天地图影像图片layerUrl
        BufferedImage tdtImage = null;
        try {
            tdtImage =  Tile.export(extent, size, tileInfo);
        }catch (RasterFormatException e){
            System.out.println("出图尺寸不够，重新buffer");
            return  imgLayerWithTDT(bufferGeometry,wkt,width,height,arcGisserviceUrl,layerIndex);
        }catch (Exception e){
            System.out.println("天地圖生產失敗，檢查tk");
            e.printStackTrace();
        }
        LinkedList<LinkedList<LinkedList<Point>>> points = WKTService.getPointsFormWKT(wkt);
        String layerFile = ArcgisUtils.downLoadLayerImgByServerId(bean, arcGisserviceUrl, layerIndex);
        File lfile = new File(layerFile);
        File toggerImgFile = null;
        try {
            BufferedImage graphic = GraphicUtils.draw(bean, points);
            BufferedImage d = loadImageLocal(lfile);
            String tempUrl = System.getProperty("java.io.tmpdir") + File.separator+"aaaaaQTools" + File.separator;
            toggerImgFile = new File(tempUrl + IdUtil.fastSimpleUUID() + ".png");
            toggerImgFile.mkdirs();
            //影像+业务图层+画的图 三图叠加
            BufferedImage image = d;
            if (tdtImage != null) {
                if (d != null) {
                    image = modifyImagetogeter(d, tdtImage);
                } else {
                    image = tdtImage;
                }
                image= modifyImagetogeter(graphic, image);
            }else{
                if (d!=null){
                    image= modifyImagetogeter(graphic, d);
                }else{
                    image = graphic;
                }
            }
            ImageIO.write(image, "png", toggerImgFile);
            return toggerImgFile;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
           try {
               lfile.delete();
           }catch (Exception e){
               //eat exception
           }
        }
        return null;
    }

    /**
     * 导入本地图片到缓冲区
     */
    public static BufferedImage loadImageLocal(File imgName) {
        try {
            return ImageIO.read( imgName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static BufferedImage modifyImagetogeter(BufferedImage b, BufferedImage d) {
        Graphics2D g = null;
        try {
            int w = b.getWidth();
            int h = b.getHeight();
            g = d.createGraphics();
            g.drawImage(b, 0, 0, w, h, null);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return d;
    }
}
