package com.mxingo.passenger.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.mxingo.driver.module.BaseActivity;
import com.mxingo.driver.utils.Constants;
import com.mxingo.passenger.MyApplication;
import com.mxingo.passenger.R;
import com.mxingo.passenger.module.base.http.ComponentHolder;
import com.mxingo.passenger.module.base.http.MyPresenter;
import com.mxingo.passenger.module.base.log.LogUtils;
import com.mxingo.passenger.module.order.OrderInfoActivity;
import com.mxingo.passenger.module.order.OrderStatus;
import com.mxingo.passenger.widget.ShowToast;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Inject
    MyPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        ComponentHolder.getAppComponent().inject(this);
        presenter.register(this);
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APPID, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtils.d("wx resp", resp.errCode + "," + resp.errStr + "," + resp.openId + "," + resp.transaction);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == -1) {
                ShowToast.showCenter(this, "支付失败");
                presenter.payBack(MyApplication.orderNo, OrderStatus.PAY_FAIL_TYPE);
                finish();
            } else if (resp.errCode == -2) {
                ShowToast.showCenter(this, "支付失败");
                presenter.payBack(MyApplication.orderNo, OrderStatus.PAY_FAIL_TYPE);
                finish();
            } else if (resp.errCode == 0) {
                ShowToast.showCenter(this, "支付成功");
                presenter.payBack(MyApplication.orderNo, OrderStatus.PAY_SUCC_TYPE);
                OrderInfoActivity.startOrderInfoActivity(this,MyApplication.orderNo);
                EventBus.getDefault().post("1");
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unregister(this);
    }
}
