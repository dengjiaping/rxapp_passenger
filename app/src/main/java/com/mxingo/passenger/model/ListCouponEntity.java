package com.mxingo.passenger.model;

import java.util.List;

/**
 * Created by zhouwei on 2017/8/8.
 */

public class ListCouponEntity {

    /**
     * rspCode : 00
     * coupons : [{"couponType":2,"createTime":1502089692000,"expireTime":1504681692000,"groupNo":"201708042211","minOrderAmount":10000,"mobile":"18767126193","no":"12901548299","startTime":1502089692000,"status":1,"title":"新用户体验券","topicNo":"20170804221102","usrId":1590,"value":1000},{"couponType":2,"createTime":1502089692000,"expireTime":1504540799000,"groupNo":"201708042211","minOrderAmount":10000,"mobile":"18767126193","no":"71562053587","startTime":1501811561000,"status":1,"title":"新用户体验券","topicNo":"20170804221101","usrId":1590,"value":2000}]
     * rspDesc : 成功
     */

    public String rspCode;
    public String rspDesc;
    public List<CouponEntity> coupons;

    @Override
    public String toString() {
        return "{" +
                "rspCode='" + rspCode + '\'' +
                ", rspDesc='" + rspDesc + '\'' +
                ", coupons=" + coupons +
                '}';
    }
}
