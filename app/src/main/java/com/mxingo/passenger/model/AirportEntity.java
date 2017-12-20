package com.mxingo.passenger.model;

import java.io.Serializable;

/**
 * Created by chendeqiang on 2017/11/30 14:06
 */

public class AirportEntity implements Serializable {
    /**
     * code : HGH
     * name : 萧山国际机场
     */

    public String code;
    public String name;

    @Override
    public String toString() {
        return "{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
