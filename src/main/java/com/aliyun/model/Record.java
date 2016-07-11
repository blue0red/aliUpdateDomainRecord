package com.aliyun.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/6.
 */
public class Record implements Serializable {
    private static final long serialVersionUID = -895599442916010326L;

    private String RR;

    private String Status;

    private String Value;

    private String RecordId;

    private String Type;

    private String DomainName;

    private String Locked;

    private String Line;

    private String TTL;

    private String Priority;

    @JsonProperty(value = "DomainName")
    public String getDomainName() {
        return DomainName;
    }

    @JsonProperty(value = "DomainName")
    public void setDomainName(String domainName) {
        DomainName = domainName;
    }

    @JsonProperty(value = "Line")
    public String getLine() {
        return Line;
    }

    @JsonProperty(value = "Line")
    public void setLine(String line) {
        Line = line;
    }

    @JsonProperty(value = "Locked")
    public String getLocked() {
        return Locked;
    }

    @JsonProperty(value = "Locked")
    public void setLocked(String locked) {
        Locked = locked;
    }

    @JsonProperty(value = "Priority")
    public String getPriority() {
        return Priority;
    }

    @JsonProperty(value = "Priority")
    public void setPriority(String priority) {
        Priority = priority;
    }

    @JsonProperty(value = "RecordId")
    public String getRecordId() {
        return RecordId;
    }

    @JsonProperty(value = "RecordId")
    public void setRecordId(String recordId) {
        RecordId = recordId;
    }

    @JsonProperty(value = "RR")
    public String getRR() {
        return RR;
    }

    @JsonProperty(value = "RR")
    public void setRR(String RR) {
        this.RR = RR;
    }

    @JsonProperty(value = "Status")
    public String getStatus() {
        return Status;
    }

    @JsonProperty(value = "Status")
    public void setStatus(String status) {
        Status = status;
    }

    @JsonProperty(value = "TTL")
    public String getTTL() {
        return TTL;
    }

    @JsonProperty(value = "TTL")
    public void setTTL(String TTL) {
        this.TTL = TTL;
    }

    @JsonProperty(value = "Type")
    public String getType() {
        return Type;
    }

    @JsonProperty(value = "Type")
    public void setType(String type) {
        Type = type;
    }

    @JsonProperty(value = "Value")
    public String getValue() {
        return Value;
    }

    @JsonProperty(value = "Value")
    public void setValue(String value) {
        Value = value;
    }

    @Override
    public String toString() {
        return "{" +
            "\"DomainName\":\"" + DomainName + "\"" +
            ", \"RR\":\"" + RR + "\"" +
            ", \"Status\":\"" + Status + "\"" +
            ", \"Value\":\"" + Value + "\"" +
            ", \"RecordId\":\"" + RecordId + "\"" +
            ", \"Type\":\"" + Type + "\"" +
            ", \"Locked\":\"" + Locked + "\"" +
            ", \"Line\":\"" + Line + "\"" +
            ", \"TTL\":\"" + TTL + "\"" +
            ", \"Priority\":\"" + Priority + "\"" +
            '}';
    }
}
