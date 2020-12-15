package com.qw.coordinatetools.tools.service.eris;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import com.qw.coordinatetools.tools.bean.ExportMapBean;

import java.io.File;

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
        String layerUrl = serviceUrl + "/export?bbox=" + bbox + "&size=" + size + "&dpi=" + dpi + "&format=" + format + "&transparent=" + transparent + "&layers=" + layers + "&f=image";
        String tempUrl =System.getProperty("java.io.tmpdir");
        String downFile = tempUrl  + "downloadTemp" + File.separator + IdUtil.fastSimpleUUID() + ".png";
        HttpUtil.downloadFile(layerUrl, downFile);
        return downFile;
    }

}
