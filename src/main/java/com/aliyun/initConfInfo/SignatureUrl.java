package com.aliyun.initConfInfo;

import com.aliyun.utils.FileUtil;
import com.aliyun.utils.HmacUtil;
import com.aliyun.utils.RandomStr;
import com.aliyun.utils.UTCTimeUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

import static com.aliyun.initConfInfo.AnalyzeConf.CONF_MAP;
import static com.aliyun.utils.UTCTimeUtil.getNow;

/**
 * Created by Administrator on 2016/7/5.
 *
 */
public class SignatureUrl {
    /**
     *
     * @param urlParameter
     * @return
     */
    public static String getSignUrl(String urlParameter){
        StringBuffer strBuff = new StringBuffer();
        //拼接请求DescribeDomainRecords（获取解析记录列表）方法的url
        strBuff.append("http://alidns.aliyuncs.com/?");
        strBuff.append(urlParameter);
        strBuff.append("&Format=JSON");
        strBuff.append("&AccessKeyId=" + CONF_MAP.get("AccessID"));
        strBuff.append("&Version=2015-01-09");
        strBuff.append("&SignatureMethod=HMAC-SHA1");
        strBuff.append("&SignatureNonce=" + RandomStr.getRandomStr());
        strBuff.append("&SignatureVersion=1.0");
        strBuff.append("&Timestamp=" + UTCTimeUtil.getUTCTimeStr());
        String signBeforeUrl = strBuff.toString();
        //签名加密（SHA1）用的key
        String key = CONF_MAP.get("Accesskey") + "&";
        //构造用于计算签名的字符串
        String stringToSign = stringToSign(signBeforeUrl);
        //计算签名的值
        String signature = HmacUtil.base64(key, stringToSign);
        String signAfterUrl = "";
        try {
            //获得含有签名的字符串
            signAfterUrl =  signBeforeUrl + "&Signature=" +  URLEncoder.encode(signature,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            FileUtil.writeErrToLog(e, getNow() + "[ Signature code failure ]:" + signature + "\n[ error message ]:");
        }finally {
            return signAfterUrl;
        }
    }

    private static String stringToSign(String url) {
        String param = url.substring(url.indexOf("?") + 1);
        String[] paramArr = param.split("&");
        Arrays.sort(paramArr);
        return "GET&%2F&" + join(paramArr, "%26").replace("=", "%3D").replace(":", "%253A");
    }

    private static String join(String[] strArr, String spader) {
        StringBuffer strBuff = new StringBuffer();
        for (String str : strArr) {
            strBuff.append(str);
            strBuff.append(spader);
        }
        String str = strBuff.toString();
        return str.substring(0, str.lastIndexOf("%26"));
    }
}
