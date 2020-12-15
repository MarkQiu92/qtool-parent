package com.qw.coordinatetools.tools.service.tile;

import com.qw.coordinatetools.tools.service.tile.bean.TileImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ImageService implements Runnable{
	private TileImage image;
	private String tileUrl;
	
	public ImageService(TileImage image, String tileUrl) {
		super();
		this.image = image;
		this.tileUrl = tileUrl;
	}

	@Override
	public void run() {
		try {
			URL url = new URL(this.tileUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.connect();
			if(conn.getResponseCode()==HttpURLConnection.HTTP_OK) {
				InputStream input = conn.getInputStream();
				BufferedImage img = ImageIO.read(input);
				image.setImage(img);
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
