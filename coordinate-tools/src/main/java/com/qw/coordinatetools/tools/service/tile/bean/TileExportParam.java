package com.qw.coordinatetools.tools.service.tile.bean;

public class TileExportParam {
	private int[][][] imagePositions;
	private int imageCols;
	private int imageRows;
	private int level;
	private int width;
	private int height;
	private int cutOriginx;
	private int cutOriginy;
	public int getImageCols() {
		return imageCols;
	}
	public void setImageCols(int imageCols) {
		this.imageCols = imageCols;
	}
	public int getImageRows() {
		return imageRows;
	}
	public void setImageRows(int imageRows) {
		this.imageRows = imageRows;
	}
	public int[][][] getImagePositions() {
		return imagePositions;
	}
	public void setImagePositions(int[][][] imagePositions) {
		this.imagePositions = imagePositions;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getCutOriginx() {
		return cutOriginx;
	}
	public void setCutOriginx(int cutOriginx) {
		this.cutOriginx = cutOriginx;
	}
	public int getCutOriginy() {
		return cutOriginy;
	}
	public void setCutOriginy(int cutOriginy) {
		this.cutOriginy = cutOriginy;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
