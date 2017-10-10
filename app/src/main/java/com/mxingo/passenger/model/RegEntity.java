package com.mxingo.passenger.model;

/**
 * Created by zhouwei on 2017/8/7.
 */

public class RegEntity extends  CommEntity {


    /**
     * rxToken : 05911589
     */

    public String rxToken;

    @Override
    public String toString() {
        return "{" +
                "rspCode='" + rspCode + '\'' +
                ", rspDesc='" + rspDesc + '\'' +
                ", rxToken='" + rxToken + '\'' +
                '}';
    }
}
