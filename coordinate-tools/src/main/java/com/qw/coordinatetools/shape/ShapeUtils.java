package com.qw.coordinatetools.shape;

import com.esri.core.geometry.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * wkt 格式工具类
 *
 * @author qiuwei
 */
public class ShapeUtils {
    private OperatorImportFromESRIShape operatorImportFromESRIShape = OperatorImportFromESRIShape.local();
    private OperatorExportToESRIShape operatorExportToESRIShape = OperatorExportToESRIShape.local();

    /**
     * geometry转wkt
     *
     * @param geometry
     * @return
     */
    public String  geo2Shape(Geometry geometry) {
       // return operatorExportToESRIShape.execute(WktImportFlags.wktImportDefaults, geometry, null);
        return  null;
    }

    /**
     * geometry转wkt
     *
     * @param shapeByteBuffer
     * @return
     */
    public Geometry shape2Geo(ByteBuffer shapeByteBuffer) {
        return operatorImportFromESRIShape.execute(ShapeImportFlags.ShapeImportDefaults,Geometry.Type.Unknown,shapeByteBuffer);
    }

    public static void main(String[] args) throws IOException {
        ShapeUtils utils = new ShapeUtils();
        ByteBuffer shapeByteBuffer = null ;
        File shapeFile = new File("C:\\Users\\qiuwei\\Desktop\\长兴压覆SHP图形\\KQ.dbf");
        FileInputStream in = new FileInputStream(shapeFile);
        FileChannel channel = in.getChannel();
        shapeByteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

        Geometry geometry = utils.shape2Geo(shapeByteBuffer);
        System.out.println(geometry.getType());

    }
}
