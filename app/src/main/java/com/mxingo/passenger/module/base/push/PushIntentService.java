package com.mxingo.passenger.module.base.push;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.mxingo.passenger.module.base.data.UserInfoPreferences;
import com.mxingo.passenger.module.base.log.LogUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by zhouwei on 2017/6/23.
 */

public class PushIntentService extends GTIntentService {

    public PushIntentService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        if (msg != null && msg.getPayload() != null) {
            String data = new String(msg.getPayload());
            LogUtils.d("onReceiveMessageData:", data);
        }

    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.d(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        UserInfoPreferences.getInstance().setDevtoken(clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Log.d(TAG, "onReceiveOnlineState -> " + "online = " + online);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null && msg.obj != null) {
                Intent intent = (Intent) msg.obj;
                EventBus.getDefault().post(intent);
            }
        }
    };
}

