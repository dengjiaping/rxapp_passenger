package com.mxingo.passenger.model;

import java.util.List;

/**
 * Created by zhouwei on 2017/8/10.
 */

public class QryInvoiceEnitity {

    /**
     * rspCode : 00
     * invoices : [{"addr":"西溪湿地","area":"浙江省杭州市西湖区","createTime":1502359231000,"invoiceAmount":100,"invoiceCode":"123456789","invoiceContent":"客运服务费","invoiceTitle":"杭州乐驰网络科技有限公司","invoiceType":2,"mobile":"18767126193","name":"魏州","status":0},{"addr":"杭州西湖区","area":"330682","createTime":1502347886000,"expressNo":"123456","invoiceAmount":100,"invoiceCode":"1234567","invoiceContent":"车费","invoiceTitle":"个人","invoiceType":1,"mobile":"13018806034","name":"魏先生","status":1}]
     * rspDesc : 成功
     */

    public String rspCode;
    public String rspDesc;
    public List<InvoiceEntity> invoices;

    @Override
    public String toString() {
        return "{" +
                "rspCode='" + rspCode + '\'' +
                ", rspDesc='" + rspDesc + '\'' +
                ", invoices=" + invoices +
                '}';
    }
}
