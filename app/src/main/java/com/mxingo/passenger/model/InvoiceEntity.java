package com.mxingo.passenger.model;

/**
 * Created by zhouwei on 2017/8/10.
 */

public class InvoiceEntity {

    /**
     * addr : 西溪湿地
     * area : 浙江省杭州市西湖区
     * createTime : 1502359231000
     * invoiceAmount : 100
     * invoiceCode : 123456789
     * invoiceContent : 客运服务费
     * invoiceTitle : 杭州乐驰网络科技有限公司
     * invoiceType : 2
     * mobile : 18767126193
     * name : 魏州
     * status : 0
     */

    public String addr;
    public String area;
    public long createTime;
    public int invoiceAmount;
    public String invoiceCode;
    public String invoiceContent;
    public String invoiceTitle;
    public int invoiceType;
    public String mobile;
    public String name;
    public int status;
    public int visibile;

    @Override
    public String toString() {
        return "{" +
                "addr='" + addr + '\'' +
                ", area='" + area + '\'' +
                ", createTime=" + createTime +
                ", invoiceAmount=" + invoiceAmount +
                ", invoiceCode='" + invoiceCode + '\'' +
                ", invoiceContent='" + invoiceContent + '\'' +
                ", invoiceTitle='" + invoiceTitle + '\'' +
                ", invoiceType=" + invoiceType +
                ", mobile='" + mobile + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", visibile=" + visibile +
                '}';
    }
}
