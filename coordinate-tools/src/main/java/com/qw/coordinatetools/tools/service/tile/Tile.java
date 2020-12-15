package com.qw.coordinatetools.tools.service.tile;

import com.qw.coordinatetools.tools.service.tile.bean.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 导出影像图——多线程版
 */

public class Tile {
	public static BufferedImage export(MapExtent extent, ExportSize size, TileInfo tileInfo) {
		TileExportParam param = calcExportParam(extent,size,tileInfo);
		List<TileImage> tiles = fetchTileImage(param,tileInfo.getTileUrl());
		return mergeImage(tiles,param,size);
	}
	private static TileExportParam calcExportParam(MapExtent extent, ExportSize size, TileInfo tileInfo) {
		int rows = tileInfo.getRows()==null ? 256:tileInfo.getRows();
		int cols = tileInfo.getCols()==null ? 256:tileInfo.getCols();
		double realResolution = (extent.getYmax()-extent.getYmin())/size.getHeight();
		Lod targetLod = null;
		List<Lod> lods = tileInfo.getLods();
		double diff = 1;
		for(Lod lod : lods) {
			double currentDiff = Math.abs(lod.getResolution()-realResolution)/lod.getResolution();
			if(currentDiff>diff) {
				break;
			}else {
				currentDiff = diff;
				targetLod = lod;
			}
		}
		int minc,maxc,minr,maxr;
		minc = getPosition(extent.getXmin(),tileInfo.getOrigin().getX(),cols,targetLod.getResolution());
		maxc = getPosition(extent.getXmax(),tileInfo.getOrigin().getX(),cols,targetLod.getResolution());
		minr = getPosition(extent.getYmin(),tileInfo.getOrigin().getY(),rows,targetLod.getResolution());
		maxr = getPosition(extent.getYmax(),tileInfo.getOrigin().getY(),rows,targetLod.getResolution());
		int cn = Math.abs(maxc-minc)+1;
		int rn = Math.abs(maxr-minr)+1;
		int[][][] imagePositions = new int[cn][rn][2];
		int xd = minc<maxc ? 1:-1;
		int yd = minr<maxr ? 1:-1;
		for(int i=0;i<cn;i++) {
			for(int j=0;j<rn;j++) {
				imagePositions[i][j][0]=minc+i*xd;
				imagePositions[i][j][1]=maxr-j*yd;
			}
		}
		TileExportParam param = new TileExportParam();
		param.setImagePositions(imagePositions);
		param.setImageCols(cn);
		param.setImageRows(rn);
		param.setLevel(targetLod.getLevel());
		param.setWidth(cn*cols);
		param.setHeight(rn*rows);
		int cutOriginx = getPositionDiff(extent.getXmin(),tileInfo.getOrigin().getX(),cols,targetLod.getResolution());
		int cutOriginy = getPositionDiff(extent.getYmax(),tileInfo.getOrigin().getY(),rows,targetLod.getResolution());
		param.setCutOriginx(cutOriginx);
		param.setCutOriginy(cutOriginy);
		return param;
	}
	private static int getPosition(double t, double o, Integer p, double r) {
		double position = Math.abs(t-o)/(p*r);
		return (int) Math.round(position-0.5);
	}
	private static int getPositionDiff(double t, double o, Integer p, double r) {
		double position = Math.abs(t-o)/(p*r);
		double diff = position-Math.round(position-0.5);
		return (int) Math.round(diff*p-0.5);
	}
	private static List<TileImage> fetchTileImage(TileExportParam param, String url) {
		List<TileImage> tileList = new ArrayList<TileImage>();
		int total = param.getImageCols()*param.getImageRows();
		ExecutorService es = Executors.newFixedThreadPool(total<13 ? total:12);
		for(int i=0;i<param.getImageCols();i++) {
			for(int j=0;j<param.getImageRows();j++) {
				String tileUrl = url;
				tileUrl = tileUrl.replace("{TileMatrix}",param.getLevel()+"");
				tileUrl = tileUrl.replace("{TileCol}",param.getImagePositions()[i][j][0]+"");
				tileUrl = tileUrl.replace("{TileRow}",param.getImagePositions()[i][j][1]+"");
				TileImage tileImage = new TileImage();
				tileImage.setX(i);
				tileImage.setY(j);
				tileList.add(tileImage);
				es.submit(new ImageService(tileImage, tileUrl));
			}
		}
		es.shutdown();
		try {
			es.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return tileList;
	}
	private static BufferedImage mergeImage(List<TileImage> tiles, TileExportParam param, ExportSize size) {
		int imageType = 0;
		int width=0,height=0;
		for(TileImage tile:tiles) {
			if(tile!=null&&tile.getImage()!=null) {
				BufferedImage image = tile.getImage();
				imageType = image.getType();
				width = image.getWidth();
				height = image.getHeight();
				break;
			}
		}
		BufferedImage full = new BufferedImage(param.getWidth(), param.getHeight(), imageType);
		Graphics2D g = full.createGraphics();
		for(TileImage tile:tiles) {
			if(tile!=null&&tile.getImage()!=null) {
				BufferedImage image = tile.getImage();
				g.drawImage(image, tile.getX()*width, tile.getY()*height, width, height, null);
			}
		}
		g.dispose();
		return full.getSubimage(param.getCutOriginx(), param.getCutOriginy(), (int) Math.round(size.getWidth()), (int) Math.round(size.getHeight()));
	}
}
