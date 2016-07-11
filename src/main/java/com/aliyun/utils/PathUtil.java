package com.aliyun.utils;

/**
 * Created by Administrator on 2016/7/6.
 */
public class PathUtil {
    public static final String CLASS_PATH = getClassPath();

    public static final String RELATIVE_PATH = getRelativePath();

    public static final String CONF_FILE = getRelativePath() + "conf.properties";

    public static final String RECORD_FILE = getRelativePath() + "record.properties";
    //查询返回的域名记录文件
    public static final String RESULT_RECORD_FILE = getRelativePath() + "resultRecord.properties";

    public static final String LOG_FILE = getRelativePath() + "domain.log";

    public static final String LOGO_ICO = getRelativePath() + "logo.png";

    public static  String getClassPath(){
        //打包后时候该路径
        String path = System.getProperty("java.class.path");
        //idea上测试使用该路径
//        String path = new File("aliUpdateDomainRecord").getAbsolutePath();
        return path;
    }

    public static String getRelativePath(){
        String path = CLASS_PATH.substring(0, CLASS_PATH.indexOf("aliUpdateDomainRecord"));
        return path;
    }

}
