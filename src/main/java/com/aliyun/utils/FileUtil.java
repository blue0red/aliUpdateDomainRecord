package com.aliyun.utils;

import java.awt.*;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.aliyun.settingJob.UpdateDomainRecordJob.count;
import static com.aliyun.start.MyFrame.insertDocument;
import static com.aliyun.start.MyFrame.textPane;
import static com.aliyun.utils.PathUtil.CONF_FILE;
import static com.aliyun.utils.PathUtil.LOG_FILE;

/**
 * Created by yangf on 2016/7/7.
 */
public class FileUtil {

    //根据Key读取Value
    public synchronized String getValue(String key) {
        Properties pps = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(CONF_FILE));
            pps.load(in);
            String value = pps.getProperty(key);
            return value;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //写入Properties单条信息
    public void WritePropertiesOne(String key, String value, String fileName) throws IOException {
        Map<String, String> propMap = new HashMap<String, String>();
        propMap.put(key, value);
        WriteProperties(propMap, fileName);
    }

    //写入Properties信息
    public void WriteProperties(Map<String, String> propMap, String fileName) throws IOException {
        Properties pps = new Properties();
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        InputStream fis = new FileInputStream(fileName);
        //从输入流中读取属性列表（键和元素对）
        pps.load(fis);
        //调用 Hashtable 的方法 put使用 getProperty 方法提供并行性。
        //强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
        OutputStream os = new FileOutputStream(fileName);
        pps.putAll(propMap);
        //以适合使用 load 方法加载到 Properties 表中的格式，
        //将此 Properties 表中的属性列表（键和元素对）写入输出流
        pps.store(os, "ddd");
    }

    //读取Properties的全部信息
    public static Map<String, String> getAllProperties(String fileName) throws IOException {
        Properties pps = new Properties();
        InputStream in = new FileInputStream(fileName);
        pps.load(in);
        Enumeration en = pps.propertyNames(); //得到配置文件的名字
        Map<String, String> propMap = new HashMap<String, String>();
        while (en.hasMoreElements()) {
            String strKey = (String) en.nextElement();
            String strValue = pps.getProperty(strKey);
            propMap.put(strKey, strValue);
        }
        return propMap;
    }

    //错误日志
    public static void writeErrToLog(Throwable e, String... logInfo) {
        writeToLog(e, Color.red, logInfo);
    }

    //成功信息
    public static void writeSuccessToLog(String... logInfo) {
        Throwable e = null;
        writeToLog(e, Color.green, logInfo);
    }


    //写入到log文件
    public synchronized static void writeToLog(Throwable e, Color color, String... logInfo) {
        //每写入20次清理一次界面
        count++;
        if (count == 20) {
            textPane.setText("");
            count = 0;
        }
        ByteArrayOutputStream buf = null;
        String expMessage = "";
        if (e != null) {
            buf = new java.io.ByteArrayOutputStream();
            e.printStackTrace(new java.io.PrintWriter(buf, true));
            expMessage = buf.toString();
        }
        File file = new File(LOG_FILE);
        FileWriter fw = null;
        PrintWriter pw = null;
        for (String log : logInfo) {
            insertDocument(log, color);
        }
        if (!"".equals(expMessage))
            insertDocument(expMessage, color);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fw = new FileWriter(file, true);
            pw = new PrintWriter(fw);
            for (String log : logInfo) {
                pw.println(log);
            }
            if (!"".equals(expMessage))
                pw.println(expMessage);
        } catch (IOException e1) {
            e.printStackTrace();
        } finally {
            try {
                fw.flush();
                if (fw != null)
                    fw.close();
                pw.flush();
                if (pw != null)
                    pw.close();
                if (buf != null)
                    buf.close();
            } catch (IOException e1) {
                e.printStackTrace();
            }
        }
    }

}
