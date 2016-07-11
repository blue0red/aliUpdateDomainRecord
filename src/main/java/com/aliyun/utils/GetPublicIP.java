package com.aliyun.utils;

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.aliyun.initConfInfo.AnalyzeConf.CONF_MAP;
import static com.aliyun.initConfInfo.DescribeDomainRecords.getUrlResult;

public class GetPublicIP {

    public String getWebIp() {
        //用来获取ip的网站
        String strUrl = CONF_MAP.get("getIpUrl");
        String ip = getUrlResult(strUrl);
        String regEx = "([0-9]{1,3}[.]{1}){3}[0-9]{1,3}";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(ip);
        while (mat.find()) {
            return mat.group();
        }
        return "";
    }

    public void getIp() {
        // TODO Auto-generated method stub
        InetAddress ia = null;
        try {
            ia = ia.getLocalHost();

            String localname = ia.getHostName();
            String localip = ia.getHostAddress();
            System.out.println("本机名称是：" + localname);
            System.out.println("本机的ip是 ：" + localip);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}