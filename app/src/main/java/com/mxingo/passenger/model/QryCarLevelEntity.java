package com.mxingo.passenger.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouwei on 2017/8/7.
 */

public class QryCarLevelEntity implements Serializable {

    /**
     * rspCode : 00
     * Message :
     * QueryResultList : [{"AddServices":[{"VendorAddServiceCode":"PC001","ServicePrice":"0"},{"VendorAddServiceCode":"CS002","ServicePrice":"38"},{"VendorAddServiceCode":"CS003","ServicePrice":"38"}],"Price":727,"VehicleType":"21"},{"AddServices":[{"VendorAddServiceCode":"PC001","ServicePrice":"0"},{"VendorAddServiceCode":"CS002","ServicePrice":"38"},{"VendorAddServiceCode":"CS003","ServicePrice":"38"}],"Price":251,"VehicleType":"1"},{"AddServices":[{"VendorAddServiceCode":"PC001","ServicePrice":"0"},{"VendorAddServiceCode":"CS002","ServicePrice":"38"},{"VendorAddServiceCode":"CS003","ServicePrice":"38"}],"Price":271,"VehicleType":"2"},{"AddServices":[{"VendorAddServiceCode":"PC001","ServicePrice":"0"},{"VendorAddServiceCode":"CS002","ServicePrice":"38"},{"VendorAddServiceCode":"CS003","ServicePrice":"38"}],"Price":387,"VehicleType":"5"},{"AddServices":[{"VendorAddServiceCode":"PC001","ServicePrice":"0"},{"VendorAddServiceCode":"CS002","ServicePrice":"38"},{"VendorAddServiceCode":"CS003","ServicePrice":"38"}],"Price":397,"VehicleType":"3"},{"AddServices":[{"VendorAddServiceCode":"PC001","ServicePrice":"0"},{"VendorAddServiceCode":"CS002","ServicePrice":"38"},{"VendorAddServiceCode":"CS003","ServicePrice":"38"}],"Price":615,"VehicleType":"20"},{"AddServices":[{"VendorAddServiceCode":"PC001","ServicePrice":"0"},{"VendorAddServiceCode":"CS002","ServicePrice":"38"},{"VendorAddServiceCode":"CS003","ServicePrice":"38"}],"Price":615,"VehicleType":"14"},{"AddServices":[{"VendorAddServiceCode":"PC001","ServicePrice":"0"},{"VendorAddServiceCode":"CS002","ServicePrice":"38"},{"VendorAddServiceCode":"CS003","ServicePrice":"38"}],"Price":777,"VehicleType":"15"}]
     * PriceMark : 102269
     * MsgCode : OK
     * rspDesc : 成功
     */

    public String rspCode;
    public String Message;
    public String PriceMark;
    public String MsgCode;
    public String rspDesc;
    public List<QueryResultListEntity> QueryResultList;

    public static class QueryResultListEntity implements Serializable {
        /**
         * AddServices : [{"VendorAddServiceCode":"PC001","ServicePrice":"0"},{"VendorAddServiceCode":"CS002","ServicePrice":"38"},{"VendorAddServiceCode":"CS003","ServicePrice":"38"}]
         * Price : 727
         * VehicleType : 21
         */

        public double Price;
        public String VehicleType;
        public List<AddServicesEntity> AddServices;
        /**
         * OverMileagePrice : 2
         * OverTimePrice : 30
         * Times : 8
         * Mileage : 100
         * ProductId : fe7a9f1b7cb3442c9d
         */

        public int OverMileagePrice;
        public int OverTimePrice;
        public int Times;
        public int Mileage;
        public String ProductId;


        public static class AddServicesEntity implements Serializable {
            /**
             * VendorAddServiceCode : PC001
             * ServicePrice : 0
             */

            public String VendorAddServiceCode;
            public String ServicePrice;

            @Override
            public String toString() {
                return "{" +
                        "VendorAddServiceCode='" + VendorAddServiceCode + '\'' +
                        ", ServicePrice='" + ServicePrice + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "{" +
                    "Price=" + Price +
                    ", VehicleType='" + VehicleType + '\'' +
                    ", AddServices=" + AddServices +
                    ", OverMileagePrice=" + OverMileagePrice +
                    ", OverTimePrice=" + OverTimePrice +
                    ", Times=" + Times +
                    ", Mileage=" + Mileage +
                    ", ProductId='" + ProductId + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "{" +
                "rspCode='" + rspCode + '\'' +
                ", Message='" + Message + '\'' +
                ", PriceMark='" + PriceMark + '\'' +
                ", MsgCode='" + MsgCode + '\'' +
                ", rspDesc='" + rspDesc + '\'' +
                ", QueryResultList=" + QueryResultList +
                '}';
    }
}
