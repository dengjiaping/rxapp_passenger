package com.mxingo.passenger.model;

/**
 * Created by zhouwei on 2017/7/25.
 */

public class CheckVersionEntity {


    /**
     * rspCode : 00
     * rspDesc : 成功
     */

    public String rspCode;
    public String rspDesc;



    public DataEntity data;

    public static class DataEntity {

        public long createTime;
        public String group;
        public int id;
        public String key;
        public String keyDisplay;
        public boolean status;
        public String value;
        public String valueDisplay;

        @Override
        public String toString() {
            return "{" +
                    "createTime=" + createTime +
                    ", group='" + group + '\'' +
                    ", id=" + id +
                    ", key='" + key + '\'' +
                    ", keyDisplay='" + keyDisplay + '\'' +
                    ", status=" + status +
                    ", value='" + value + '\'' +
                    ", valueDisplay='" + valueDisplay + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "{" +
                "rspCode='" + rspCode + '\'' +
                ", rspDesc='" + rspDesc + '\'' +
                ", data=" + data +
                '}';
    }
}
