package com.qw.coordinatetools;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SHA256Util {

    public static final String SECRET_KEY = "Q4W8@JLw_.SAr";

    public static final Integer PARAM_NUM = 3;

    /**
     * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用SHA-256
     *
     * @param strSrc 要加密的字符串
     * @return
     */
    public static String Encrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    public static String getListFromInfo(Map<String, Object> stringObjectMap) {
        String[] resultToSort = new String[SHA256Util.PARAM_NUM];

        int i = 0;
        for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
            resultToSort[i] = entry.getKey() + "=" + entry.getValue();
            i++;
        }
        Arrays.sort(resultToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < resultToSort.length; j++) {
            sb.append(resultToSort[j]);
            if (j < resultToSort.length - 1) {
                sb.append("&");
            }
        }

        return String.valueOf(sb);
    }

    public static void main(String args[]) throws Exception {
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("moduleid", "hyyb");
        stringObjectMap.put("secretkey", SHA256Util.SECRET_KEY);
        long time = System.currentTimeMillis();
        stringObjectMap.put("time", time);
        String ss = getListFromInfo(stringObjectMap);
        //secret对应的值
        System.out.println(time);
        System.out.println(Encrypt(ss));
    }

}
