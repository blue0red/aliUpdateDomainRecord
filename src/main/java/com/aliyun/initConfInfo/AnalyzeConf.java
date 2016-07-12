package com.aliyun.initConfInfo;

import com.aliyun.model.Record;
import com.aliyun.utils.FileUtil;
import com.aliyun.utils.PathUtil;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.aliyun.settingJob.UpdateDomainRecordJob.WEB_IP;
import static com.aliyun.utils.FileUtil.getAllProperties;
import static com.aliyun.utils.UTCTimeUtil.getNow;

/**
 * Created by Administrator on 2016/7/6.
 */
//关于Properties类常用的操作
public class AnalyzeConf {
    public static ConcurrentMap<String, String> CONF_MAP = new ConcurrentHashMap<String, String>();

    public static List<String> URL_LIST = new ArrayList<String>();

    //读取Properties的全部信息
    public static void loadConf() throws IOException {
        Map<String, String> confMap = getAllProperties(PathUtil.CONF_FILE);
        CONF_MAP.putAll(confMap);
        Set<String> keySet = CONF_MAP.keySet();
        for(String key:keySet){
            if(key.indexOf("getIpUrl") > -1){
                URL_LIST.add(CONF_MAP.get(key));
            }
        }
    }

    public static List<Record> loadUpdateRecord(){

        List<Record> updateRecord = new ArrayList<Record>();
        Map<String, String> recordMap = new HashMap<String, String>();
        try {
            recordMap = getAllProperties(PathUtil.RECORD_FILE);
        } catch (IOException e) {
            FileUtil.writeErrToLog(e, getNow() + "[ load failure by ]:" + PathUtil.RECORD_FILE + "\n[ error message ]:");
        }
        ObjectMapper mapper = new ObjectMapper();
        Set<String> keySet = recordMap.keySet();
        for(String key:keySet){
            Record  record = null;
            try {
                record = mapper.readValue(recordMap.get(key), Record.class);
                //如果自己给的Value参数是ip则动态获取本机公网ip设置
                if("ip".equals(record.getValue().toLowerCase())){
                    record.setValue(WEB_IP);
                }
                updateRecord.add(record);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return updateRecord;
    }
}