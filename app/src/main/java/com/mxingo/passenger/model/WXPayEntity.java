package com.mxingo.passenger.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhouwei on 2017/8/7.
 */

public class WXPayEntity {


    /**
     * rspCode : 00
     * package : Sign=WXPay
     * appid : wxf195161977c016dd
     * partnerid : 1463052802
     * prepayid : wx20170808180325f593f9dcbe0902611815
     * noncestr : fc76150735dde1d2d860aeb77ee2009e
     * rspDesc : 成功
     * timestamp : 1502186605
     */

    public String rspCode;
    @SerializedName("package")
    public String packageValue;
    public String appid;
    public String partnerid;
    public String prepayid;
    public String noncestr;
    public String rspDesc;
    public String timestamp;
    /**
     * sign : 30284053A99F54A791E2E9E2C02DCDD4
     */

    public String sign;

    @Override
    public String toString() {
        return "{" +
                "appId='" + appid + '\'' +
                ", partnerId='" + partnerid + '\'' +
                ", prepayId='" + prepayid + '\'' +
                ", packageValue='" + packageValue + '\'' +
                ", nonceStr='" + noncestr + '\'' +
                ", timeStamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }


}
