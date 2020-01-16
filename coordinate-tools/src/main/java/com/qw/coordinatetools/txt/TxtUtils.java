package com.qw.coordinatetools.txt;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Transformation2D;
import com.qw.coordinatetools.gcs.GcsUtils;
import com.qw.coordinatetools.pojo.GeometryDetail;
import com.qw.coordinatetools.wkt.WktUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TxtUtils {
    private static TxtParserFactory txtParserFactory = TxtParserFactory.getInstance();


    public static  List<GeometryDetail>  txt2Geo(String txt,TxtOperator.Type type){
        List<GeometryDetail> list =   txtParserFactory.getOperator(type).txt2Geo(txt);
        return list;
    }

    public static  Geometry  sigletxt2Geo (String txt, String fgf, int tghs, int xls, int yls){
        return   txtParserFactory.getOperator(TxtOperator.Type.commonOperator).commonSigleReader(txt,fgf,tghs,xls,yls);

    }

    public static String getCode(String path) throws Exception {
        InputStream inputStream = new FileInputStream(path);
        byte[] head = new byte[3];
        inputStream.read(head);
        String code = "gb2312";  //或GBK
        if (head[0] == -1 && head[1] == -2 )
            code = "UTF-16";
        else if (head[0] == -2 && head[1] == -1 )
            code = "Unicode";
        else if(head[0]==-17 && head[1]==-69 && head[2] ==-65)
            code = "UTF-8";
        inputStream.close();
        return code;
    }

    /**
     * 根据txt的字符进行读取，解决乱码为题
     * @param file
     * @return
     */
    private static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), Charset.forName(getCode(file.getAbsolutePath())));
            BufferedReader br = new BufferedReader(isr);//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }


   /* public static void main(String[] args) {

        List<GeometryDetail> list = txtParserFactory.getOperator(TxtOperator.Type.kcdj).txt2Geo(txt2);
        List<Geometry> geometries = list.stream().map(geometryDetail -> geometryDetail.getGeometry()).collect(Collectors.toList());
        Geometry geometry =  GeometryEngine.union(geometries.toArray(new Geometry[geometries.size()]),null);
        WktUtils wktUtils = new WktUtils();
       // int tyWkid = (int) (4528 + Math.floor((x - 118.5) / 3));
        int txtWkid = list.get(0).getWkid();
        geometry = GcsUtils.convertSR(geometry,"EPSG:"+txtWkid,"EPSG:4490");

        String wkt = wktUtils.geo2WktStr(geometry);
        System.out.println(geometry.calculateArea2D());;
        System.out.println(wkt);

    }*/

   /* private  static String txt2 ="[属性描述]\n" +
            "格式版本号=1.01版本\n" +
            "数据产生单位=浙江华东建设工程有限公司\n" +
            "数据产生日期=2019-4-19\n" +
            "坐标系=2000国家大地坐标系\n" +
            "几度分带=3\n" +
            "投影类型=高斯克吕格\n" +
            "计量单位=米\n" +
            "带号=40\n" +
            "精度=3\n" +
            "转换参数=0,0,0,0,0,0,0\n" +
            "[地块坐标]\n" +
            "66,5.7089,1,彭埠单元B/R-14地块,面,,,,@\n" +
            "J01,1,3353189.181,40520986.477\n" +
            "J02,1,3353176.426,40520960.456\n" +
            "J03,1,3353159.066,40520925.043\n" +
            "J04,1,3353141.707,40520889.630\n" +
            "J05,1,3353124.347,40520854.217\n" +
            "J06,1,3353118.338,40520835.142\n" +
            "J07,1,3353096.330,40520790.246\n" +
            "J08,1,3353095.525,40520788.254\n" +
            "J09,1,3353095.012,40520786.168\n" +
            "J10,1,3353094.804,40520784.029\n" +
            "J11,1,3353094.903,40520781.883\n" +
            "J12,1,3353095.307,40520779.773\n" +
            "J13,1,3353096.008,40520777.742\n" +
            "J14,1,3353096.993,40520775.833\n" +
            "J15,1,3353098.240,40520774.083\n" +
            "J16,1,3353099.725,40520772.530\n" +
            "J17,1,3353101.416,40520771.205\n" +
            "J18,1,3353126.466,40520754.323\n" +
            "J19,1,3353151.516,40520737.443\n" +
            "J20,1,3353179.014,40520717.706\n" +
            "J21,1,3353207.910,40520698.233\n" +
            "J22,1,3353236.806,40520678.760\n" +
            "J23,1,3353238.604,40520677.722\n" +
            "J24,1,3353240.526,40520676.942\n" +
            "J25,1,3353242.539,40520676.435\n" +
            "J26,1,3353244.602,40520676.210\n" +
            "J27,1,3353246.677,40520676.274\n" +
            "J28,1,3353248.723,40520676.622\n" +
            "J29,1,3353250.701,40520677.249\n" +
            "J30,1,3353252.574,40520678.144\n" +
            "J31,1,3353254.306,40520679.287\n" +
            "J32,1,3353255.862,40520680.659\n" +
            "J33,1,3353257.214,40520682.234\n" +
            "J34,1,3353267.874,40520696.532\n" +
            "J35,1,3353291.631,40520728.400\n" +
            "J36,1,3353315.388,40520760.267\n" +
            "J37,1,3353339.145,40520792.134\n" +
            "J38,1,3353362.902,40520824.000\n" +
            "J39,1,3353372.315,40520841.647\n" +
            "J40,1,3353402.199,40520881.734\n" +
            "J41,1,3353404.228,40520885.459\n" +
            "J42,1,3353405.134,40520889.604\n" +
            "J43,1,3353404.842,40520893.836\n" +
            "J44,1,3353403.376,40520897.818\n" +
            "J45,1,3353400.854,40520901.230\n" +
            "J46,1,3353397.479,40520903.799\n" +
            "J47,1,3353360.389,40520924.482\n" +
            "J48,1,3353338.956,40520936.433\n" +
            "J49,1,3353330.146,40520941.346\n" +
            "J50,1,3353323.298,40520945.165\n" +
            "J51,1,3353300.573,40520957.838\n" +
            "J52,1,3353272.374,40520968.136\n" +
            "J53,1,3353263.305,40520972.436\n" +
            "J54,1,3353254.146,40520976.545\n" +
            "J55,1,3353244.905,40520980.462\n" +
            "J56,1,3353235.583,40520984.185\n" +
            "J57,1,3353226.186,40520987.712\n" +
            "J58,1,3353216.716,40520991.042\n" +
            "J59,1,3353207.180,40520994.174\n" +
            "J60,1,3353204.202,40520994.794\n" +
            "J61,1,3353201.162,40520994.800\n" +
            "J62,1,3353198.181,40520994.193\n" +
            "J63,1,3353195.384,40520992.996\n" +
            "J64,1,3353192.887,40520991.261\n" +
            "J65,1,3353190.790,40520989.058\n" +
            "J01,1,3353189.181,40520986.477";*/

}
