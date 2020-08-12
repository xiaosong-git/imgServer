package com.xdream.goldccm.util;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.UUID;

public class RetMsg implements Serializable{

    private static final long serialVersionUID = -1236045906097100833L;
    private String service_name;
    private String request_source;
    private String request_seq;
    private String response_time;
    private int result_code;
    private String result_desc;
    private String request_id;
    private Object content;

    public RetMsg() {

    }

    public RetMsg(Builder builder) {
        this.request_id = builder.request_id;
        this.result_code = builder.result_code;
        this.result_desc = builder.result_desc;
        this.response_time = builder.response_time;
        this.service_name = builder.service_name;
        this.request_source = builder.request_source;
        this.request_seq = builder.request_seq;
        this.content = builder.content;
    }

    public static class Builder{
        private int result_code;
        private String result_desc;
        private final String response_time;

        private String service_name;
        private String request_source;
        private String request_seq;

        private final String request_id;
        private Object content;

        public Builder() {
            this.response_time = String.valueOf(System.currentTimeMillis());
            this.request_id = UUID.randomUUID().toString().replaceAll("-","");
        }

        public Builder(int result_code, String result_desc) {
            this.result_code = result_code;
            this.result_desc = result_desc;
            this.response_time = String.valueOf(System.currentTimeMillis());
            this.request_id = UUID.randomUUID().toString().replaceAll("-","");
        }

        public Builder setResult_code(int result_code) {
            this.result_code = result_code;
            return this;
        }

        public Builder setResult_desc(String result_desc) {
            this.result_desc = result_desc;
            return this;
        }

        public Builder setService_name(String service_name) {
            this.service_name = service_name;
            return this;
        }

        public Builder setRequest_source(String request_source) {
            this.request_source = request_source;
            return this;
        }

        public Builder setRequest_seq(String request_seq) {
            this.request_seq = request_seq;
            return this;
        }

        public Builder setContent(Object content) {
            this.content = content;
            return this;
        }

        public RetMsg build() {
            return new RetMsg(this);
        }

    }

    public String getService_name() {
        return service_name;
    }

    public String getRequest_source() {
        return request_source;
    }

    public String getRequest_seq() {
        return request_seq;
    }

    public String getResponse_time() {
        return response_time;
    }

    public int getResult_code() {
        return result_code;
    }

    public String getResult_desc() {
        return result_desc;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public void setRequest_source(String request_source) {
        this.request_source = request_source;
    }

    public void setRequest_seq(String request_seq) {
        this.request_seq = request_seq;
    }

    public void setResponse_time(String response_time) {
        this.response_time = response_time;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public void setResult_desc(String result_desc) {
        this.result_desc = result_desc;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String toJSONString() {
        return JSON.toJSONString(this);
    }

    public Object getContent() {
        return content;
    }

}
