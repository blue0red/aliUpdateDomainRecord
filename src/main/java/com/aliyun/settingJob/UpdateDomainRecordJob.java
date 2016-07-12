package com.aliyun.settingJob;

import com.aliyun.initConfInfo.AnalyzeConf;
import com.aliyun.initConfInfo.SignatureUrl;
import com.aliyun.model.Record;
import com.aliyun.model.UpdateResult;
import com.aliyun.utils.FileUtil;
import com.aliyun.utils.GetPublicIP;
import org.codehaus.jackson.map.ObjectMapper;
import org.quartz.*;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import static com.aliyun.initConfInfo.DescribeDomainRecords.getUrlResult;
import static com.aliyun.utils.UTCTimeUtil.getNow;

/**
 * Created by yangf on 2016/7/7.
 */
public class UpdateDomainRecordJob implements Job {
    public static JobKey jobKey = new JobKey("UpdateDomainRecordJob", "UpdateJob-group");
    public static TriggerKey
        triggerKey = new TriggerKey("UpdateDomainRecordTrigger", "UpdateTrigger-group");

    public static String WEB_IP = "";

    public static int count = 0;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        GetPublicIP getPublicIP = new GetPublicIP();
        String webIp = getPublicIP.getWebIp();
        //打印执行次数
//        insertDocument("[ exec  frequency ]:" + count++, Color.blue);
        if("".equals(webIp) || WEB_IP.equals(webIp)){
            return;
        }
        //首次更新ip
        if("".equals(WEB_IP)){
            FileUtil.writeToLog(null, Color.BLUE, "First update! ip same as old, Don't care.");
        }

        //IP变化时更新ip
        WEB_IP = webIp;
        List<Record> recordList = AnalyzeConf.loadUpdateRecord();
        ObjectMapper mapper = new ObjectMapper();
        for (Record record : recordList) {
            StringBuffer urlParameter = new StringBuffer();
            urlParameter.append("Action=UpdateDomainRecord");
            urlParameter.append("&RecordId=" + record.getRecordId());
            urlParameter.append("&RR=" + record.getRR());
            urlParameter.append("&Type=" + record.getType());
            urlParameter.append("&Value=" + record.getValue());
            urlParameter.append("&TTL=" + record.getTTL());
            urlParameter.append("&Line=" + record.getLine());
            String url = SignatureUrl.getSignUrl(urlParameter.toString());
            //获取请求返回的json字符串
            String result = getUrlResult(url);
            UpdateResult updateResult = new UpdateResult();
            try {
                updateResult = mapper.readValue(result, UpdateResult.class);
                if(updateResult.getMessage() != null){
                    FileUtil.writeErrToLog(null, getNow() + "[ record update fail ]:"
                        ,"HostID: "+ updateResult.getHostId()
                        ,"Code: " + updateResult.getCode()
                        ,"ErrorMessage: "+ updateResult.getMessage());
                }else {
                    FileUtil.writeSuccessToLog(getNow() + "[ record update success ]: Domain - " + record.getValue()+ " RecordId - " + record.getRecordId());
                }
            } catch (IOException e) {
                FileUtil.writeErrToLog(e, getNow() + "[ record update success ]:", record.toString(), e.getMessage());
            }
        }
    }
}
