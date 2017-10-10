package com.mxingo.passenger.module.base.map;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.mxingo.passenger.MyApplication;
import com.mxingo.passenger.R;
import com.mxingo.passenger.module.base.log.LogUtils;
import com.mxingo.passenger.util.DisplayUtil;
import com.mxingo.passenger.util.TextUtil;


/**
 * Created by zhouwei on 16/9/30.
 */

public class BaiduMapUtil {

    private LocationClient mLocClient;
    private BDLocationListener myListener;
    private LocationClientOption option;
    private ACache aCache;
    private BaiduMap baiduMap;
    private MyLocationData locData;
    private MapView mv;
    private MyLocationConfiguration config;

    private static BaiduMapUtil instance;

    private GeoCoder mSearch = null;
    private PoiSearch mPoiSearch;

    public static BaiduMapUtil getInstance() {
        if (instance == null) {
            instance = new BaiduMapUtil();
            SDKInitializer.initialize(MyApplication.application);
            SDKInitializer.setCoordType(CoordType.GCJ02);
        }
        return instance;
    }


    public BaiduMapUtil() {
        initBaidu();
    }

    public void setBaiduMap(MapView baiduMapView) {
        this.baiduMap = baiduMapView.getMap();
        mv = baiduMapView;
//        this.baiduMap.setOnMapClickListener(this);
        LatLng centPoint = new LatLng(30.281307, 120.170892);
        config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, BitmapDescriptorFactory
                .fromResource(R.drawable.ic_location));
        this.baiduMap.setMyLocationConfiguration(config);
        this.baiduMap.setMyLocationEnabled(true);

        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(centPoint)
                .zoom(15)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        this.baiduMap.setMapStatus(mapStatusUpdate);
        // 删除百度地图logo
        baiduMapView.removeViewAt(1);
        // 删除比例尺控件
        baiduMapView.removeViewAt(2);
        baiduMapView.showZoomControls(false);
    }

    private void initBaidu() {
        if (mLocClient == null) {
            mLocClient = new LocationClient(MyApplication.application);
        }
        if (aCache == null) {
            aCache = ACache.get(MyApplication.application);
        }
        if (myListener == null) {
            myListener = new BDLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    if (bdLocation == null) {
                        return;
                    }

                    if (bdLocation.getLatitude() != 0 && bdLocation.getLongitude() != 0) {
                        aCache.put("lon", bdLocation.getLongitude() + "");
                        aCache.put("lat", bdLocation.getLatitude() + "");
                        aCache.put("cityName", bdLocation.getCity() + "");

                    }
                    if (baiduMap != null) {
                        locData = new MyLocationData.Builder()
                                .accuracy(0)// 定位精度半径
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction(0).latitude(bdLocation.getLatitude())// 纬度
                                .longitude(bdLocation.getLongitude()).build();// 经度
                        baiduMap.setMyLocationData(locData);

                        LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                        MapStatusUpdate su = MapStatusUpdateFactory.newLatLng(ll);
                        baiduMap.animateMapStatus(su);

                        searchGeoCoder(new OnGetGeoCoderResultListener() {
                            @Override
                            public void onGetGeoCodeResult(GeoCodeResult result) {

                            }

                            @Override
                            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                                //创建InfoWindow展示的view
                                TextView addMarker = new TextView(MyApplication.application.getApplicationContext());
                                int padding = DisplayUtil.dip2px(mv.getContext(), 5);
                                addMarker.setPadding(padding * 2, padding, padding * 2, padding);
                                addMarker.setTextColor(ContextCompat.getColor(MyApplication.application, R.color.text_color_black));
                                addMarker.setBackgroundColor(ContextCompat.getColor(MyApplication.application, R.color.white));
                                if (result != null && result.getPoiList() != null && !result.getPoiList().isEmpty()) {
                                    PoiInfo poiInfo = result.getPoiList().get(0);
                                    addMarker.setText(poiInfo.name);
                                    //定义用于显示该InfoWindow的坐标点
                                    LatLng pt = result.getPoiList().get(0).location;
                                    //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                                    InfoWindow mInfoWindow = new InfoWindow(addMarker, pt, -135);
                                    //显示InfoWindow
                                    baiduMap.clear();
                                    baiduMap.showInfoWindow(mInfoWindow);
                                    unregisterLocationListener();
                                }
                            }
                        }, bdLocation.getLatitude(), bdLocation.getLongitude());

                    }
                }
            }

            ;
        }

        if (option == null) {
            option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            option.setCoorType(CoordType.GCJ02.name());//可选，默认gcj02，设置返回的定位结果坐标系 百度坐标系
            int span = 1000;
            option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
            option.setOpenGps(true);//可选，默认false,设置是否使用gps
            option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            //      option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            //      option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
            option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
            //      option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
            option.setNeedDeviceDirect(true);
            mLocClient.setLocOption(option);
        }
    }

    public void registerLocationListener() {
        if (myListener != null && mLocClient != null) {
            mLocClient.registerLocationListener(myListener);
            mLocClient.start();
        }
    }

    public void unregisterLocationListener() {
        if (myListener != null && mLocClient != null) {
            mLocClient.unRegisterLocationListener(myListener);
            mLocClient.stop();
        }
    }

    public double getLon() {
        if (aCache == null)
            return 0;
        String lonStr = aCache.getAsString("lon");
        if (!TextUtil.isEmpty(lonStr)) {
            return Double.valueOf(lonStr);
        } else {
            return 0;
        }
    }

    public double getLat() {
        if (aCache == null)
            return 0;
        String latStr = aCache.getAsString("lat");
        if (!TextUtil.isEmpty(latStr)) {
            return Double.valueOf(latStr);
        } else {
            return 0;
        }
    }

    public String getCityName() {
        if (aCache == null)
            return "北京";
        return aCache.getAsString("cityName");

    }

    public void searchGeoCoder(OnGetGeoCoderResultListener listener, double lat, double lon) {

        if (mSearch == null) {
            mSearch = GeoCoder.newInstance();
        }
        mSearch.setOnGetGeoCodeResultListener(listener);
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(lat, lon)));
    }

    public void getPoiByPoiSearch(String cityName, String keyName, int pageNum, OnGetPoiSearchResultListener listener) {
        try {
            if (mPoiSearch == null) {
                mPoiSearch = PoiSearch.newInstance();
            }
            LogUtils.d("参数", cityName + "," + keyName + "," + pageNum);
            mPoiSearch.setOnGetPoiSearchResultListener(listener);
            mPoiSearch.searchInCity((new PoiCitySearchOption()).city(cityName)
                    .keyword(keyName).pageNum(pageNum));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void poiSearchDestroy() {
        if (mPoiSearch != null) {
            mPoiSearch.destroy();
        }
    }

    public void geoCoderDestroy() {
        if (mSearch != null) {
            mSearch.destroy();
        }
    }
}
