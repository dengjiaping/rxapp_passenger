package com.mxingo.passenger.model;

/**
 * Created by zhouwei on 2017/8/10.
 */

public class QryInvoiceLimitEntity {

    /**
     * rspCode : 00
     * invoiceAmount : 7
     * rspDesc : 成功
     */

    public String rspCode;
    public int invoiceAmount;
    public String rspDesc;

    @Override
    public String toString() {
        return "{" +
                "rspCode='" + rspCode + '\'' +
                ", invoiceAmount=" + invoiceAmount +
                ", rspDesc='" + rspDesc + '\'' +
                '}';
    }
}
