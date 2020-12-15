package com.qw.coordinatetools.tools.service.tile;

import com.alibaba.fastjson.JSON;
import com.qw.coordinatetools.tools.service.tile.bean.*;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Test {
	public static void main(String[] args) {
		String tileinfostring = "{\"lods\":[{\"level\":1,\"scale\":295829355.4545656,\"resolution\":0.703125},{\"level\":2,\"scale\":147914677.7272828,\"resolution\":0.3515625},{\"level\":3,\"scale\":73957338.8636414,\"resolution\":0.17578125},{\"level\":4,\"scale\":36978669.4318207,\"resolution\":0.087890625},{\"level\":5,\"scale\":18489334.71591035,\"resolution\":0.0439453125},{\"level\":6,\"scale\":9244667.357955175,\"resolution\":0.02197265625},{\"level\":7,\"scale\":4622333.678977588,\"resolution\":0.010986328125},{\"level\":8,\"scale\":2311166.839488794,\"resolution\":0.0054931640625},{\"level\":9,\"scale\":1155583.419744397,\"resolution\":0.00274658203125},{\"level\":10,\"scale\":577791.7098721985,\"resolution\":0.001373291015625},{\"level\":11,\"scale\":288895.85493609926,\"resolution\":0.0006866455078125},{\"level\":12,\"scale\":144447.92746804963,\"resolution\":0.00034332275390624995},{\"level\":13,\"scale\":72223.96373402482,\"resolution\":0.00017166137695312497},{\"level\":14,\"scale\":36111.98186701241,\"resolution\":0.00008583068847656249},{\"level\":15,\"scale\":18055.990933506204,\"resolution\":0.00004291534423828124},{\"level\":16,\"scale\":9027.995466753102,\"resolution\":0.00002145767211914062},{\"level\":17,\"scale\":4513.997733376551,\"resolution\":0.00001072883605957031},{\"level\":18,\"scale\":2256.998866688275,\"resolution\":0.0000053644180297851546}],\"tileUrl\":\"http://t0.tianditu.gov.cn/img_c/wmts?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=c&FORMAT=tiles&TILEMATRIX={TileMatrix}&TILEROW={TileRow}&TILECOL={TileCol}&tk=399d4e839c75c16e69be766f7d1c8d48\",\"origin\":{\"x\":-180,\"y\":90},\"compressionQuality\":0,\"rows\":256,\"spatialReference\":{\"wkid\":4490},\"cols\":256}";
		TileInfo tileInfo = JSON.parseObject(tileinfostring, TileInfo.class); //切片信息
		MapExtent extent = new MapExtent();	//地图范围
		extent.setXmax(120.28330330803652);
		extent.setXmin(120.26270390160335);
		extent.setYmax(36.19274257154802);
		extent.setYmin(36.1822390200386);

		ExportSize size = new ExportSize(); //图片大小
		size.setHeight(979);
		size.setWidth(1920);
		BufferedImage image = Tile.export(extent , size , tileInfo);//生成全图
		try {
			ImageIO.write(image, "JPEG", new File("e:/map.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
