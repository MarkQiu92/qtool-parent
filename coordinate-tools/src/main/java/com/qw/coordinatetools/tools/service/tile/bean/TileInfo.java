package com.qw.coordinatetools.tools.service.tile.bean;

import java.util.List;

public class TileInfo {
	private String tileUrl;
	private Integer rows;
	private Integer cols;
	private Coordinate origin;
	private List<Lod> lods;
	public String getTileUrl() {
		return tileUrl;
	}
	public void setTileUrl(String tileUrl) {
		this.tileUrl = tileUrl;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getCols() {
		return cols;
	}
	public void setCols(Integer cols) {
		this.cols = cols;
	}
	public Coordinate getOrigin() {
		return origin;
	}
	public void setOrigin(Coordinate origin) {
		this.origin = origin;
	}
	public List<Lod> getLods() {
		return lods;
	}
	public void setLods(List<Lod> lods) {
		this.lods = lods;
	}
	
	
}
