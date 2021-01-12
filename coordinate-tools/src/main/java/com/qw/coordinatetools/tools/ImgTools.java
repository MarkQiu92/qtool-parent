package com.qw.coordinatetools.tools;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.esri.core.geometry.*;
import com.esri.core.geometry.Point;
import com.qw.coordinatetools.CoordUtils;
import com.qw.coordinatetools.geojson.GeoJsonUtils;
import com.qw.coordinatetools.tools.bean.ExportMapBean;
import com.qw.coordinatetools.tools.service.eris.ArcgisUtils;
import com.qw.coordinatetools.tools.service.graphic.GraphicUtils;
import com.qw.coordinatetools.tools.service.tdt.ImgParams;
import com.qw.coordinatetools.tools.service.tdt.TdtService;
import com.qw.coordinatetools.tools.service.tile.Tile;
import com.qw.coordinatetools.tools.service.tile.bean.ExportSize;
import com.qw.coordinatetools.tools.service.tile.bean.MapExtent;
import com.qw.coordinatetools.tools.service.tile.bean.TileInfo;
import com.qw.coordinatetools.wkt.WKTService;
import com.qw.coordinatetools.wkt.WktUtils;
import org.apache.commons.lang3.StringUtils;
import sun.awt.image.ToolkitImage;

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
        String geoJson = "{\"type\":\"Polygon\",\"coordinates\":[[[120.16956642299999,27.997572118999983],[120.16961163799999,27.997648116999983],[120.16964335,27.997740141000008],[120.16960741000003,27.997828246999973],[120.16953987099998,27.997896378000007],[120.16946779099999,27.997944501000006],[120.16935507100004,27.997956648000013],[120.16921534000005,27.998000848999993],[120.16912074899994,27.998073013999999],[120.16901710900004,27.998125182000024],[120.16892241799997,27.998133301999985],[120.16884121800001,27.998117390999994],[120.16876900099999,27.998077450999972],[120.16870577700001,27.998021488999996],[120.168651576,27.99796551899999],[120.16861538700005,27.997893512000019],[120.16855213199995,27.997817533999978],[120.16847988400002,27.99775758200002],[120.16835354399996,27.997713706000013],[120.16824978700004,27.997689815999991],[120.168177582,27.997657883999977],[120.168078283,27.997601965999991],[120.16798802300002,27.997558045000005],[120.16787983500001,27.997586196999976],[120.16778970999997,27.997630335999986],[120.16767257599997,27.997706532999985],[120.16758693199995,27.997730654000009],[120.167537251,27.997682680000025],[120.16751905800004,27.997582634000025],[120.16744681600005,27.997526682],[120.16733401900001,27.997490793999987],[120.167243816,27.997482897999987],[120.16714911300005,27.997483013000021],[120.16700934100004,27.997503198000004],[120.16698237799994,27.997563275000005],[120.16693737000003,27.997619367000027],[120.16685175400005,27.997663500999977],[120.16676610699994,27.997687621000011],[120.16670887199996,27.997702785999991],[120.16661826699999,27.997751187999995],[120.16644609399998,27.997831887000018],[120.16632833300002,27.997912518000021],[120.16612891,27.997961055000019],[120.16592031599998,27.997945209000022],[120.16578434200005,27.997977563000006],[120.16563931400003,27.998017985999979],[120.16558479399998,27.997945614000002],[120.16556642299997,27.997792703000016],[120.16557364499999,27.997726338000007],[120.16557020400001,27.997643944000004],[120.16557010500003,27.997579206000012],[120.16558653799996,27.997485019999999],[120.16565604499999,27.997408425999993],[120.16570241399995,27.997378942000012],[120.16578521500003,27.997325873000023],[120.16582818400002,27.997240482999985],[120.16590393000001,27.99714428499999],[120.16595352499996,27.997136215000012],[120.16607074399997,27.997116061999975],[120.16612027099995,27.99706396800002],[120.166160655,27.99693182499999],[120.16611540500003,27.996831810000003],[120.16600705999997,27.996755887999996],[120.16595282599997,27.996679901999983],[120.16594819099998,27.996599849999996],[120.16599321599995,27.996551762000024],[120.16605630699996,27.996523667000019],[120.16617801799998,27.996491497000022],[120.16628622899998,27.996479357999988],[120.16638542199996,27.996467229000018],[120.16645759999994,27.996483152999986],[120.16667863099997,27.996522912999978],[120.166741742,27.996506825999973],[120.16683644399996,27.996506710999995],[120.16702591299997,27.996550511999999],[120.16708010900004,27.996602483000004],[120.16713887000003,27.996690471000022],[120.16719308999996,27.996758456000009],[120.16729698899996,27.996874408999986],[120.16736027399998,27.996970400000009],[120.16742350699997,27.997034368000016],[120.16751380599999,27.997102304000009],[120.167631142,27.997158200999991],[120.16768074699996,27.99715814000001],[120.16777087699995,27.997118001999979],[120.16788356899997,27.997085841000001],[120.16803239199999,27.997089662000008],[120.16812259100004,27.997093554999992],[120.16820380199999,27.997117473000003],[120.16828056500003,27.997181424000019],[120.16833479900004,27.99725741200001],[120.16841156700002,27.997325364999995],[120.16851991099998,27.997397281000019],[120.16861468299999,27.997441198999979],[120.16871388599998,27.997437070999979],[120.16883108100001,27.997400901999981],[120.16894826400005,27.997356729999979],[120.16903390799996,27.997332606999976],[120.16913315800002,27.997356502999992],[120.16922341899999,27.997400420000019],[120.16931824200003,27.997476356999982],[120.16942201899997,27.997512255000004],[120.16948515399997,27.997512176999976],[120.16956642299999,27.997572118999983]]]}";
        GeoJsonUtils utils = new GeoJsonUtils();
        Geometry geometry = utils.json2geo(geoJson).getGeometry();
        //String wkt =wktUtils.geo2WktStr(geometry);
        String wkt = "MULTIPOLYGON ( ((120.98160742243219 30.070662344124862, 120.98171294279835 30.070649523133042, 120.98181122652336 30.070635872122732, 120.98191778011795 30.070619516336485, 120.98200397280736 30.07060389888317, 120.98208542834378 30.070588515209852, 120.98214975606851 30.070339436806204, 120.98215004575177 30.070338316132148, 120.98201768664732 30.07031651082102, 120.98148930423092 30.07022254224465, 120.98148089123048 30.07024857433158, 120.98132327240708 30.0706863577672, 120.98140376856786 30.070680931000883, 120.9814994339755 30.0706730817443, 120.98160742243219 30.070662344124862)))";
        String url = "https://gtkj.zjzwfw.gov.cn:6443/zjgt51/db93ab395acc1a69208c8d5b4423e6e4/arcgis/rest/services/GCS330000_2002_TDLYGH_2020/GCS330000_TDLYGH_GHYT_2020/MapServer";
        File f = ImgTools.imgLayerWithTDT(wkt, 1000, 1000, url, "0");
        System.out.println(f.getAbsolutePath());
    }

    public static File imgLayerWithTDT(String wkt, double width, double height, String arcGisserviceUrl, String layerIndex) {
        Geometry geometry = wktUtils.wktStr2Geo(wkt);
        return imgLayerWithTDT(geometry, wkt, width, height, arcGisserviceUrl, layerIndex);
    }

    public static File imgLayerWithTDT(Geometry geometry, String wkt, double width, double height, String arcGisserviceUrl, String layerIndex) {
        Geometry bufferGeometry = GeometryEngine.buffer(geometry, SpatialReference.create(4490), 0.0003);
        ExportMapBean bean = new ExportMapBean();
        bean.setHeight(height);
        bean.setWidth(width);
        Envelope env = new Envelope();
        bufferGeometry.queryEnvelope(env);
        double area = env.calculateArea2D();
        while (area < 1E-6) {
            bufferGeometry = GeometryEngine.buffer(bufferGeometry, SpatialReference.create(4490), 0.0001);
            bufferGeometry.queryEnvelope(env);
            area = env.calculateArea2D();
            System.out.println(area);
        }
        bean.setMinX(env.getLowerLeft().getX());
        bean.setMinY(env.getLowerLeft().getY());
        bean.setMaxX(env.getUpperRight().getX());
        bean.setMaxY(env.getUpperRight().getY());
        String layerFile = ArcgisUtils.downLoadLayerImgByServerId(bean, arcGisserviceUrl, layerIndex);
        //天地图影像图片layerUrl
        BufferedImage tdtImage = null;
        ImgParams params = new ImgParams();
        params.setCenterX(String.valueOf(env.getCenterX()));
        params.setCenterY(String.valueOf(env.getCenterY()));
        params.setXmax(bean.getMaxX());
        params.setXmin(bean.getMinX());
        params.setYmax(bean.getMaxY());
        params.setYmin(bean.getMinY());
        ToolkitImage toolkitImage = ((ToolkitImage) TdtService.exportTdt(params, String.valueOf(width), String.valueOf(height)));
        tdtImage = new BufferedImage(toolkitImage.getWidth(), toolkitImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = tdtImage.createGraphics();
        g2d.drawImage(toolkitImage, 0, 0, null);
        g2d.dispose();
        LinkedList<LinkedList<LinkedList<Point>>> points = WKTService.getPointsFormWKT(wkt);
        File lfile = new File(layerFile);
        File toggerImgFile = null;
        try {
            BufferedImage graphic = GraphicUtils.draw(bean, points);
            BufferedImage d = loadImageLocal(lfile);
            String tempUrl = System.getProperty("java.io.tmpdir") + File.separator + "aaaaaQTools" + File.separator;
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
                image = modifyImagetogeter(graphic, image);
            } else {
                if (d != null) {
                    image = modifyImagetogeter(graphic, d);
                } else {
                    image = graphic;
                }
            }
            ImageIO.write(image, "png", toggerImgFile);
            return toggerImgFile;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                lfile.delete();
            } catch (Exception e) {
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
            return ImageIO.read(imgName);
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
