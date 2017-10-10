package com.mxingo.passenger.model;

import java.util.List;

/**
 * Created by zhouwei on 2017/7/10.
 */

public class ListOrderEntity {


    public List<OrderEntity> orders;

    /**
     * rspCode : 00
     * rspDesc : 成功
     * order : []
     * waitPayOrders : [{"bookDays":0,"bookTime":1502167800000,"carType":21,"createTime":1502160610000,"orderAmount":62500,"orderNo":"L2017080810501055072","orderStatus":0,"orderType":1},{"bookDays":0,"bookTime":1502167800000,"carType":21,"createTime":1502160649000,"orderAmount":62500,"orderNo":"L2017080810504951372","orderStatus":0,"orderType":1}]
     */

    public String rspCode;
    public String rspDesc;

    public List<OrderEntity> waitPayOrders;

    @Override
    public String toString() {
        return "{" +
                "orders=" + orders +
                ", rspCode='" + rspCode + '\'' +
                ", rspDesc='" + rspDesc + '\'' +
                ", waitPayOrders=" + waitPayOrders +
                '}';
    }
}
