package com.mxingo.passenger.model;

import java.io.Serializable;

/**
 * Created by zhouwei on 2017/8/8.
 */

public class CouponEntity implements Serializable {
    /**
     * couponType : 2
     * createTime : 1502089692000
     * expireTime : 1504681692000
     * groupNo : 201708042211
     * minOrderAmount : 10000
     * mobile : 18767126193
     * no : 12901548299
     * startTime : 1502089692000
     * status : 1
     * title : 新用户体验券
     * topicNo : 20170804221102
     * usrId : 1590
     * value : 1000
     */

    public int couponType;
    public long createTime;
    public long expireTime;
    public String groupNo;
    public int minOrderAmount;
    public int maxDiscountAmout;

    public String mobile;
    public String no;
    public long startTime;
    public int status;
    public String title;
    public String topicNo;
    public int usrId;
    public int value;

    @Override
    public String toString() {
        return "{" +
                "couponType=" + couponType +
                ", createTime=" + createTime +
                ", expireTime=" + expireTime +
                ", groupNo='" + groupNo + '\'' +
                ", minOrderAmount=" + minOrderAmount +
                ", mobile='" + mobile + '\'' +
                ", no='" + no + '\'' +
                ", startTime=" + startTime +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", topicNo='" + topicNo + '\'' +
                ", usrId=" + usrId +
                ", value=" + value +
                '}';
    }

}
