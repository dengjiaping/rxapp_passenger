package com.mxingo.passenger.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouwei on 2017/8/14.
 */

public class FlightEntity implements Serializable {


    /**
     * msg : 0
     * rspCode : 00
     * FlightNo : MU5121
     * DepDate : 20AUG
     * rspDesc : 成功
     * AvFltModel : [{"Arr":"北京首都国际机场/PEK","DepCityName":"上海虹桥","Ope":"","ArrTime":"20:20","ArrTerm":"T2","DepTerm":"T2","ArrCityName":"北京首都","Dep":"虹桥国际机场/SHA","DepTime":"18:00"}]
     */

    public String msg;
    public String rspCode;
    public String FlightNo;
    public String DepDate;
    public String rspDesc;
    public List<AvFltModelEntity> AvFltModel;

    public static class AvFltModelEntity implements Serializable{
        /**
         * Arr : 北京首都国际机场/PEK
         * DepCityName : 上海虹桥
         * Ope :
         * ArrTime : 20:20
         * ArrTerm : T2
         * DepTerm : T2
         * ArrCityName : 北京首都
         * Dep : 虹桥国际机场/SHA
         * DepTime : 18:00
         */

        public Airport Arr;
        public String DepCityName;
        public String Ope;
        public String ArrTime;
        public long arrTime;
        public String ArrTerm;
        public String DepTerm;
        public String ArrCityName;
        public Airport Dep;
        public String DepTime;
        public String FlightNo;

        @Override
        public String toString() {
            return "{" +
                    "Arr=" + Arr +
                    ", DepCityName='" + DepCityName + '\'' +
                    ", Ope='" + Ope + '\'' +
                    ", ArrTime='" + ArrTime + '\'' +
                    ", ArrTerm='" + ArrTerm + '\'' +
                    ", DepTerm='" + DepTerm + '\'' +
                    ", ArrCityName='" + ArrCityName + '\'' +
                    ", Dep=" + Dep +
                    ", DepTime='" + DepTime + '\'' +
                    ", FlightNo='" + FlightNo + '\'' +
                    '}';
        }
    }

    public static class Airport implements Serializable{
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

    @Override
    public String toString() {
        return "{" +
                "msg='" + msg + '\'' +
                ", rspCode='" + rspCode + '\'' +
                ", FlightNo='" + FlightNo + '\'' +
                ", DepDate='" + DepDate + '\'' +
                ", rspDesc='" + rspDesc + '\'' +
                ", AvFltModel=" + AvFltModel +
                '}';
    }
}
