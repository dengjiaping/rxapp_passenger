package com.mxingo.passenger.model;

/**
 * Created by zhouwei on 2017/8/7.
 */

public class VCodeEntity {


    /**
     * rspCode : 00
     * usrId : 0
     * vcode : 5536
     * rspDesc : 成功
     */

    public String rspCode;
    public int usrId;
    public String vcode;
    public String rspDesc;

    @Override
    public String toString() {
        return "{" +
                "rspCode='" + rspCode + '\'' +
                ", usrId=" + usrId +
                ", vcode='" + vcode + '\'' +
                ", rspDesc='" + rspDesc + '\'' +
                '}';
    }
}
