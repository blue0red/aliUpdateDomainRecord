package com.aliyun.initConfInfo;

import com.aliyun.model.Record;
import com.aliyun.utils.FileUtil;
import com.aliyun.utils.PathUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.type.TypeReference;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

import static com.aliyun.initConfInfo.AnalyzeConf.CONF_MAP;
import static com.aliyun.utils.UTCTimeUtil.getNow;

/**
 * Created by Administrator on 2016/7/4.
 */
public class DescribeDomainRecords {

    public boolean execute() {
        try {
            AnalyzeConf analyzeConf = new AnalyzeConf();
            //加载配置文件
            analyzeConf.loadConf();
            analyzeConf = null;
            List<String> recordList = new ArrayList<String>();
            Set<String> keySet = CONF_MAP.keySet();
            for (String key : keySet) {
                if (key.indexOf("DomainName") > -1) {
                    StringBuffer urlParameter = new StringBuffer();
                    //构造DescribeDomainRecordInfo （获取解析记录信息）需要的参数
                    urlParameter.append("Action=DescribeDomainRecords");
                    urlParameter.append("&DomainName=" + CONF_MAP.get(key));
                    String url = SignatureUrl.getSignUrl(urlParameter.toString());
                    //获取请求返回的json字符串
                    String result = getUrlResult(url);
                    List<String> records = getRecordJson(key, result);
                    recordList.addAll(records);
                }
            }
            //将record信息列表输出到文件
            Map<String, String> propMap = new HashMap<String, String>();
            FileUtil propertiesUtil = new FileUtil();
            FileUtil.writeToLog(null, Color.blue, "please option record by you want update! copy to [ record.properties ]\n");
            for (int i = 0; i < recordList.size(); i++) {
                FileUtil.writeSuccessToLog("Record" + i + "=" + recordList.get(i) + "\n");propMap.put("Record" + i, recordList.get(i));
            }
            propertiesUtil.WriteProperties(propMap, PathUtil.RESULT_RECORD_FILE);
            if (recordList == null || recordList.isEmpty()) {
                return false;
            }
            return true;
        } catch (IOException e) {
            FileUtil.writeErrToLog(e, getNow() + "[ load fail by" + PathUtil.CONF_FILE + " ]\n",
                e.getMessage());
            return false;
        }

    }

    //处理返回的json，获取包含单个记录字符串的数组
    public static List<String> getRecordJson(String key, String str) {
        ObjectMapper mapper = new ObjectMapper();
        mapper = mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper = mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector());
        HashMap map = null;
        List<String> recordJsonStr = new ArrayList<String>();
        List<Record> recordList = new ArrayList<Record>();
        try {
            map = mapper.readValue(str, HashMap.class);
            HashMap domainRecordsMap = (HashMap) map.get("DomainRecords");
            String recordsStr = mapper.writeValueAsString(domainRecordsMap.get("Record"));
            recordList = mapper.readValue(recordsStr, new TypeReference<List<Record>>() {
            });
            for (Record record : recordList) {
                recordJsonStr.add(record.toString());
            }
        } catch (IOException e) {
            FileUtil.writeErrToLog(e, getNow() + "[ "+key + ":" + CONF_MAP.get(key)+" ]Error Json--", str,
                "[ error message ]:" );
        } finally {
            return recordJsonStr;
        }
    }
    //请求经过处理的url拿到返回包含所有记录的json字符串
    public static String getUrlResult(String... httpUrl) {
        BufferedReader reader = null;
        String resultStr = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            URL url = new URL(httpUrl[0]);
            // 创建httpget.
            HttpGet httpget = new HttpGet(httpUrl[0]);
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // 打印响应内容
                resultStr = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            if(httpUrl.length <= 1)
            FileUtil.writeErrToLog(e, getNow() + "[ Site access exception, error message]:");
        } finally {
            return resultStr;
        }
    }
}
