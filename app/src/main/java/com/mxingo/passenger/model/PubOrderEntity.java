package com.mxingo.passenger.model;

/**
 * Created by zhouwei on 2017/8/8.
 */

public class PubOrderEntity {

    /**
     * rspCode : 00
     * rspDesc : 成功
     */

    public String rspCode;
    public String rspDesc;
    public String orderNo;

    @Override
    public String toString() {
        return "{" +
                "rspCode='" + rspCode + '\'' +
                ", rspDesc='" + rspDesc + '\'' +
                ", orderNo='" + orderNo + '\'' +
                '}';
    }
}
