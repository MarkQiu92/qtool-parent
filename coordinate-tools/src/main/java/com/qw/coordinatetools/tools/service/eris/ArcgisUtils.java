package com.qw.coordinatetools.tools.service.eris;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.qw.coordinatetools.tools.bean.ExportMapBean;

import java.io.File;
import java.math.BigDecimal;
import java.util.Map;

public class ArcgisUtils {
    /**
     * arcgis restServer 出图
     * @param bean
     * @param serviceUrl
     * @param layerIndex
     * @return
     */
    public static String downLoadLayerImgByServerId(ExportMapBean bean, String serviceUrl,String layerIndex) {
        String bbox = bean.getMinX() + "," + bean.getMinY() + "," + bean.getMaxX() + "," + bean.getMaxY();
        String size = bean.getWidth() + "," + bean.getHeight();
        String dpi = "96";
        String format = "png32";
        String transparent = "true";
        String layers = "show:" + layerIndex;
        String layerUrl = serviceUrl + "/export?bbox=" + bbox + "&size=" + size + "&dpi=" + dpi + "&format=" + format + "&transparent=" + transparent + "&layers=" + layers + "&f=pjson";
        String json = HttpUtil.get(layerUrl);
        Map resutlMap = JSON.parseObject(json, Map.class);
        Map extentMap = (Map)resutlMap.get("extent");
        bean.setMaxX(((BigDecimal)extentMap.get("xmax")).doubleValue());
        bean.setMaxY(((BigDecimal)extentMap.get("ymax")).doubleValue());
        bean.setMinX(((BigDecimal)extentMap.get("xmin")).doubleValue());
        bean.setMinY(((BigDecimal)extentMap.get("ymin")).doubleValue());
        String tempUrl =System.getProperty("java.io.tmpdir");
        String downFile = tempUrl  + "downloadTemp" + File.separator + IdUtil.fastSimpleUUID() + ".png";
        String url = resutlMap.get("href").toString();
       try{
           HttpUtil.downloadFile(url, new File(downFile),3000);
       }catch (Exception e){
           System.out.println("url:"+url+"  异常重新出图");
           layerUrl = serviceUrl + "/export?bbox=" + bbox + "&size=" + size + "&dpi=" + dpi + "&format=" + format + "&transparent=" + transparent + "&layers=" + layers + "&f=image";
           HttpUtil.downloadFile(layerUrl, downFile);
       }


        return downFile;
    }

}
