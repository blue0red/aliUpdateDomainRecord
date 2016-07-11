package com.aliyun.utils;

import java.util.Random;

/**
 * Created by Administrator on 2016/7/6.
 * 生成随机字符串
 */
public class RandomStr {

    public static String getRandomStr() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 24; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
            if ((i+1) % 4 == 0) {
                sb.append("-");
            }
        }
        String result = sb.toString();
        return result.substring(0,result.length()-1);
    }

}
