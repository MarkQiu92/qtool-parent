package com.qw.coordinatetools.txt.operatorImp;

import cn.hutool.core.util.ReUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.qw.coordinatetools.pojo.GeometryDetail;
import com.qw.coordinatetools.txt.TxtOperator;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * 1,1,1,J1,3358689.935,40509409.89
 * 2,1,1,J2,3358689.935,40509422.555
 * 3,1,1,J3,3358694.657,40509600.424
 * 4,1,1,J4,3358562.709,40509604.142
 * 5,1,1,J5,3358562.414,40509597.358
 * 6,1,1,J6,3358551.654,40509598.523
 * 7,1,1,J7,3358552.024,40509603.827
 * 8,1,1,J8,3358545.714,40509604.604
 * 9,1,1,J9,3358527.757,40509602.458
 * 10,1,1,J10,3358511.99,40509602.931
 * 11,1,1,J11,3358508.084,40509541.286
 * 12,1,1,J12,3358512.483,40509539.586
 * 13,1,1,J13,3358515.134,40509538.375
 * 14,1,1,J14,3358517.08,40509537.486
 * 15,1,1,J15,3358526.179,40509535.937
 * 16,1,1,J16,3358530.699,40509532.221
 * 17,1,1,J17,3358525.249,40509484.81
 * 18,1,1,J18,3358528.779,40509484.692
 * 19,1,1,J19,3358529.43,40509461.394
 * 20,1,1,J20,3358562.178,40509459.644
 * 21,1,1,J21,3358562.254,40509460.216
 * 22,1,1,J22,3358563.534,40509470.006
 * 23,1,1,J23,3358564.775,40509479.492
 * 24,1,1,J24,3358576.524,40509479.492
 * 25,1,1,J25,3358583.959,40509479.461
 * 26,1,1,J26,3358589.114,40509456.915
 * 27,1,1,J27,3358595.474,40509457.844
 * 28,1,1,J28,3358597.223,40509441.396
 * 29,1,1,J29,3358603.924,40509414.249
 * 30,1,1,J30,3358605.574,40509407.349
 * 31,1,1,J31,3358610.524,40509397.5
 * 32,1,1,J32,3358613.149,40509395.934
 * 33,1,1,J33,3358615.471,40509394.551
 * 34,1,1,J34,3358625.319,40509395.05
 * 35,1,1,J35,3358635.119,40509397.45
 * 36,1,1,J36,3358669.215,40509405.899
 * 37,1,1,J37,3358676.165,40509407.2
 * 38,1,1,J1,3358689.935,40509409.89
 */

public class TxtNzyOperator implements TxtOperator {
    Log log = LogFactory.getCurrentLogFactory().getLog(TxtNzyOperator.class);

    @Override
    public List<GeometryDetail> txt2Geo(String txt) {
        log.debug("获取到的txt界址点：{}", txt);
        BufferedReader bufferedReader = null;
        List<GeometryDetail> list = new ArrayList<>();
        int wkid = 0; //
        int dkxh = 1;//地块号所在的列
        int qhxh = 2;//圈号所在的列
        int yxh = 4;
        int xxh = 5;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(txt.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));

            commonReader(bufferedReader, list, wkid, dkxh, qhxh, yxh, xxh);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return list;
    }

    /**
     * @param bufferedReader
     * @param list
     * @param wkid
     * @param dkxh
     * @param qhxh
     * @param yxh
     * @param xxh
     * @throws IOException
     */
    private void commonReader(BufferedReader bufferedReader, List<GeometryDetail> list, int wkid, int dkxh, int qhxh, int yxh, int xxh) throws IOException {
        List<String> txtList = new ArrayList<>();
        String lineStr;
        while ((lineStr = bufferedReader.readLine()) != null) {
            if (!lineStr.trim().equals("")){
                txtList.add(lineStr);
            }

        }
        double currentDkh = 0;
        double currentQh = 0;
        for (int i = 0; i < txtList.size(); i++) {
            String[] lineArr = txtList.get(i).split(",");
            double starty = Double.valueOf(lineArr[yxh]);
            double startx = Double.valueOf(lineArr[xxh]);
            currentDkh = Double.valueOf(lineArr[dkxh]);
            currentQh = Double.valueOf(lineArr[qhxh]);
            GeometryDetail geometryDetail = new GeometryDetail();
            Polygon polygon = new Polygon();
            geometryDetail.setGeometry(polygon);
            list.add(geometryDetail);
            polygon.startPath(startx, starty);
            for ( ; ; ) {
                if (++i==txtList.size()){
                    //结束了
                    break;
                }
                String[] nextLine = txtList.get(i).split(",");
                double y = Double.valueOf(nextLine[yxh]);
                double x = Double.valueOf(nextLine[xxh]);
                double nextDkh = Double.valueOf(nextLine[dkxh]);
                double nextQh = Double.valueOf(nextLine[qhxh]);
                if (nextDkh==currentDkh){
                    //同一个地块，同一个圈号
                    if (nextQh == currentQh){
                        polygon.lineTo(x,y);
                    }else{
                        //同一个地块的，另一个圈号
                        polygon.startPath(x,y);
                        //当前圈号指向新圈号
                        currentQh = nextQh;
                    }
                }else{
                    //第一个地块解析完成，开始下一个地块
                    break;
                }

            }

        }


        while ((lineStr = bufferedReader.readLine()) != null) {

//            String[] sxs = lineStr.split(",");
//            log.debug("开始解析地块: {},地块为： {}", sxs[dkxh], sxs[qhxh]);
//
//            Polygon polygon = new Polygon();
//            String[] zbcs = lineStr.split(",");
//            double starty = Double.valueOf(zbcs[yxh]);
//            double startx = Double.valueOf(zbcs[xxh]);
//            double currentDkh =  Double.valueOf(zbcs[dkxh]);
//            double currentQh =  Double.valueOf(zbcs[qhxh]);
//            //开始
//            polygon.startPath(startx, starty);
//            //继续往下读
//            while ((lineStr = bufferedReader.readLine())!=null){
//                String[] currentLine = lineStr.split(",");
//                //同一个地块
//                if (Double.valueOf(currentLine[dkxh])==currentDkh){
//                    //同一个圈号
//                    if (Double.valueOf(currentLine[qhxh])==currentQh){
//                        double y = Double.valueOf(currentLine[yxh]);
//                        double x = Double.valueOf(currentLine[xxh]);
//                        polygon.lineTo(x, y);
//                    }else {
//                        //内圈
//
//                    }
//                }else{
//                    //另一个圈号
//
//                }
//
//            }
//
//
//            GeometryDetail detail = new GeometryDetail();
//            detail.setGeometry(polygon);
//            detail.setWkid(wkid);
//            list.add(detail);
        }
    }

    @Override
    public String geo2txt(Geometry geometry) {
        return null;
    }
}
