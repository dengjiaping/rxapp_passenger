package com.mxingo.passenger.module.base.http;


import com.mxingo.passenger.module.MainActivity;
import com.mxingo.passenger.module.coupon.CouponsActivity;
import com.mxingo.passenger.module.coupon.UseCouponActivity;
import com.mxingo.passenger.module.invoice.InvoiceActivity;
import com.mxingo.passenger.module.invoice.MakeInvoiceActivity;
import com.mxingo.passenger.module.invoice.RecordInvoiceActivity;
import com.mxingo.passenger.module.login.LoginActivity;
import com.mxingo.passenger.module.order.AirportActivity;
import com.mxingo.passenger.module.order.CarLevelActivity;
import com.mxingo.passenger.module.order.MyTripsActivity;
import com.mxingo.passenger.module.order.OrderInfoActivity;
import com.mxingo.passenger.module.order.PayOrderActivity;
import com.mxingo.passenger.module.order.PubOrderActivity;
import com.mxingo.passenger.module.order.SearchFlightActivity;
import com.mxingo.passenger.module.setting.SettingActivity;
import com.mxingo.passenger.module.setting.SuggestionsActivity;
import com.mxingo.passenger.wxapi.WXPayEntryActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by zhouwei on 2017/6/22.
 */

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject(MainActivity activity);

    void inject(LoginActivity activity);

    void inject(PubOrderActivity activity);

    void inject(MyTripsActivity activity);

    void inject(CouponsActivity activity);

    void inject(CarLevelActivity activity);

    void inject(SettingActivity activity);

    void inject(OrderInfoActivity activity);

    void inject(UseCouponActivity activity);

    void inject(PayOrderActivity activity);

    void inject(WXPayEntryActivity activity);

    void inject(InvoiceActivity activity);

    void inject(RecordInvoiceActivity activity);

    void inject(MakeInvoiceActivity activity);

    void inject(SearchFlightActivity activity);

    void inject(SuggestionsActivity activity);

    void inject(AirportActivity activity);

}
