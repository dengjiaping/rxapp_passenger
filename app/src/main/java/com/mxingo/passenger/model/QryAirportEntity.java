package com.mxingo.passenger.model;

import java.util.List;

/**
 * Created by chendeqiang on 2017/11/30 10:19
 */

public class QryAirportEntity {

    /**
     * rspCode : 00
     * data : [{"code":"HGH","name":"萧山国际机场"}]
     * rspDesc : 成功
     */

    public String rspCode;
    public String rspDesc;
    public List<AirportEntity> data;

    @Override
    public String toString() {
        return "{" +
                "rspCode='" + rspCode + '\'' +
                ", rspDesc='" + rspDesc + '\'' +
                ", data=" + data +
                '}';
    }
}
