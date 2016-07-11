package com.aliyun.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by yangf on 2016/7/8.
 */
public class UpdateResult {
    private String RequestId;
    private String HostId;
    private String Code;
    private String Message;
    private String RecordId;

    @JsonProperty(value = "Code")
    public String getCode() {
        return Code;
    }

    @JsonProperty(value = "Code")
    public void setCode(String code) {
        Code = code;
    }

    @JsonProperty(value = "HostId")
    public String getHostId() {
        return HostId;
    }

    @JsonProperty(value = "HostId")
    public void setHostId(String hostId) {
        HostId = hostId;
    }

    @JsonProperty(value = "Message")
    public String getMessage() {
        return Message;
    }

    @JsonProperty(value = "Message")
    public void setMessage(String message) {
        Message = message;
    }

    @JsonProperty(value = "RecordId")
    public String getRecordId() {
        return RecordId;
    }

    @JsonProperty(value = "RecordId")
    public void setRecordId(String recordId) {
        RecordId = recordId;
    }

    @JsonProperty(value = "RequestId")
    public String getRequestId() {
        return RequestId;
    }

    @JsonProperty(value = "RequestId")
    public void setRequestId(String requestId) {
        RequestId = requestId;
    }
}
