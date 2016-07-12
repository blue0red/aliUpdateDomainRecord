package com.aliyun.utils;

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.aliyun.initConfInfo.AnalyzeConf.URL_LIST;
import static com.aliyun.initConfInfo.DescribeDomainRecords.getUrlResult;

public class GetPublicIP {

    public String getWebIp() {
        //用来获取ip的网站
        String ip = "";
        for (int i = 0; i < URL_LIST.size(); i++) {
            String url = URL_LIST.get(i);
            System.out.println(url);
            String resultIp = getUrlResult(url, "ip");
            String regEx = "([0-9]{1,3}[.]{1}){3}[0-9]{1,3}";
            Pattern pat = Pattern.compile(regEx);
            Matcher mat = pat.matcher(resultIp);
            while (mat.find()) {
                ip = mat.group();
            }
            if (ip != null && !("".equals(ip))) {
                break;
            }
        }
        return ip;
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