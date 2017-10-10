package com.mxingo.passenger.module.base.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.mxingo.passenger.MyApplication;


/**
 * Created by zhouwei on 16/1/7.
 */
public class UserInfoPreferences {
    private static UserInfoPreferences preference = null;
    private SharedPreferences sharedPreference;
    private String packageName = "";

    private final String MOBILE = "mobile"; //手机号
    private final String DEVTOKEN = "devToken";//cid
    private final String USER_ID = "usr_id";
    private final String LECHTOKEN = "token";
    private final String ICODE = "icode";

    private static final String ADDRESS = "address"; //手机号


    public static UserInfoPreferences getInstance() {
        if (preference == null) {
            synchronized (UserInfoPreferences.class) {
                preference = new UserInfoPreferences(MyApplication.application);
            }
        }
        return preference;
    }


    public UserInfoPreferences(Context context) {
        packageName = context.getPackageName() + "";
        sharedPreference = context.getSharedPreferences(packageName, context.MODE_MULTI_PROCESS);
    }

    public int getUserId() {
        return sharedPreference.getInt(USER_ID,0);
    }


    public void setUserId(int userId) {
        SharedPreferences.Editor edit = sharedPreference.edit();
        edit.putInt(USER_ID, userId);
        edit.apply();
    }

    public String getMobile() {
        return sharedPreference.getString(MOBILE, "");
    }


    public void setMobile(String mobile) {
        SharedPreferences.Editor edit = sharedPreference.edit();
        edit.putString(MOBILE, mobile + "");
        edit.apply();
    }

    public String getDevToken() {
        return sharedPreference.getString(DEVTOKEN, "");
    }

    public void setDevtoken(String devToken) {
        SharedPreferences.Editor edit = sharedPreference.edit();
        edit.putString(DEVTOKEN, devToken);
        edit.apply();
    }

    public String getToken() {
        return sharedPreference.getString(LECHTOKEN, "");
    }

    public void setToken(String lechtoken) {
        SharedPreferences.Editor edit = sharedPreference.edit();
        edit.putString(LECHTOKEN, lechtoken + "");
        edit.apply();
    }

    public void setIcode(String icode) {
        SharedPreferences.Editor edit = sharedPreference.edit();
        edit.putString(ICODE, icode + "");
        edit.apply();
    }

    public String getIcode() {
        return sharedPreference.getString(ICODE, "");
    }

    public void setAddress(String address) {
        SharedPreferences.Editor edit = sharedPreference.edit();
        edit.putString(ADDRESS, address + "");
        edit.apply();
    }

    public void clear() {
//        setAddress("");
        setUserId(0);
        setToken("");
        setMobile("");
    }

    public String getAddress() {
        return sharedPreference.getString(ADDRESS, "");
    }

}
