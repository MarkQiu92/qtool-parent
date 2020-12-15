package com.qiuwei.eqb;



import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author baizhen
 * @date 2020/04/02 15:00
 */
public class HmacSHA256Utils {

    public static String hmacSha256(String message, String secret) {
        String hash = null;

        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secretKeySpec);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes("UTF-8"));
            hash = bytesToHexString(bytes);
        } catch (Exception var6) {
         }

        return hash;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src != null && src.length > 0) {
            for(int i = 0; i < src.length; ++i) {
                int v = src[i] & 255;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }

                stringBuilder.append(hv);
            }

            return stringBuilder.toString();
        } else {
            return null;
        }
    }
}
