package com.aliyun.utils;

import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2016/7/6.
 */
public class HmacUtil {
    /**
     * HmacMD5消息摘要
     *
     * @param data 待做摘要处理的数据
     * @param key  密钥
     * @return byte[] 消息摘要
     */
    //使用SHA1算法加密
    private static byte[] encodeHmac(byte[] data, byte[] key) throws Exception {
        //还原密钥，因为密钥是以byte形式为消息传递算法所拥有
        SecretKey secretKey = new SecretKeySpec(key, "HmacSHA1");
        //实例化Mac
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        //初始化Mac
        mac.init(secretKey);
        //执行消息摘要处理
        return mac.doFinal(data);
    }
    //用base64编码
    public static String base64(String key, String stringToSign){
        byte[] data = null;
        try {
            data = HmacUtil.encodeHmac(stringToSign.getBytes(), key.getBytes());
            return  (new BASE64Encoder()).encode(data);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
