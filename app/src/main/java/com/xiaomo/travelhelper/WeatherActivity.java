package com.xiaomo.travelhelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.xiaomo.travelhelper.http.HttpServiceFactory;
import com.xiaomo.travelhelper.http.WeatherService;
import com.xiaomo.travelhelper.model.found.CurrentAir;
import com.xiaomo.travelhelper.model.found.CurrentWeather;
import com.xiaomo.travelhelper.model.found.ForecastWeather;
import com.xiaomo.travelhelper.model.found.LifeStyle;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;


/**
 * 天气 activity 界面
 */

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.weather_back_iv) RelativeLayout mBackIv;
    @BindView(R.id.weather_city_tv) TextView mCityTv;
    @BindView(R.id.weather_more_city_iv) RelativeLayout mMoreCityIv;
    @BindView(R.id.weather_refresh_layout) JellyRefreshLayout mRefreshLayout;
    @BindView(R.id.weather_pic_iv) ImageView mPicIv;
    @BindView(R.id.weather_temp_tv) TextView mTempTv;
    @BindView(R.id.weather_max_temp_tv) TextView mMaxTempTv;
    @BindView(R.id.weather_min_temp_tv) TextView mMinTempTv;
    @BindView(R.id.weather_pm25_tv) TextView mPm25Tv;
    @BindView(R.id.weather_air_desc_tv) TextView mAirDescTv;

    @BindViews({R.id.weather_life_drsg_brf,R.id.weather_life_flu_brf,R.id.weather_life_sport_brf,
            R.id.weather_life_trav_brf,R.id.weather_life_uv_brf})
    List<TextView> mBrfTvList;

    @BindViews({R.id.weather_life_drsg_txt,R.id.weather_life_flu_txt,R.id.weather_life_sport_txt,
            R.id.weather_life_trav_txt,R.id.weather_life_uv_txt})
    List<TextView> mTxtTvList;

    @BindViews({R.id.weather_day01_date_tv,R.id.weather_day02_date_tv,R.id.weather_day03_date_tv})
    List<TextView> mDateTvList;

    @BindViews({R.id.weather_day01_temp_tv,R.id.weather_day02_temp_tv,R.id.weather_day03_temp_tv})
    List<TextView> mTempTvList;

    @BindViews({R.id.weather_day01_desc__tv,R.id.weather_day02_desc__tv,R.id.weather_day03_desc__tv})
    List<TextView> mDescTvList;

    private String mLngLat = "深圳";
    private WeatherService mWeatherService;
    private LocationClient mLocationClient;

    // 城市选择器
    private CityPickerView mCityPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        mWeatherService = HttpServiceFactory.buildWeatherService();

        mLngLat = getIntent().getStringExtra("mLngLat");
        initListener();
        initCityPicker();
        initRefreshLayout();
        requestCurrentWeather(mLngLat);

    }

    private void initCityPicker(){
        mCityPicker = new CityPickerView();
        CityConfig cityConfig = new CityConfig.Builder()
                .confirTextColor("#000000")
                .build();
        mCityPicker.setConfig(cityConfig);
        mCityPicker.init(WeatherActivity.this);
        mCityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                if (province != null) {
                    //城市
                    if (city != null && district == null) {
                        requestCurrentWeather(city.getName());
                    }else if (district != null) {
                        if("市辖区".equals(district.getName())){
                            requestCurrentWeather(city.getName());
                        }else{
                            requestCurrentWeather(district.getName());
                        }
                    }
                }
            }
        });

    }


    private void initListener(){
        mBackIv.setOnClickListener(this);
        mMoreCityIv.setOnClickListener(this);

    }

    private void initRefreshLayout(){

        TextView tv = new TextView(WeatherActivity.this);
        tv.setText("努力加载中...");
        tv.setTextColor(getResources().getColor(R.color.grey_2));
        tv.setTextSize(18);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        mRefreshLayout.setLoadingView(tv);
        mRefreshLayout.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                if(mLocationClient == null){
                    LocationClientOption option = new LocationClientOption();
                    mLocationClient = new LocationClient(getApplicationContext());
                    mLocationClient.setLocOption(option);
                    mLocationClient.registerLocationListener(new BDLocationListener() {
                        @Override
                        public void onReceiveLocation(BDLocation bdLocation) {
                            mLngLat = bdLocation.getLongitude() + "," + bdLocation.getLatitude();
                            requestCurrentWeather(mLngLat);
                            mLocationClient.stop();
                        }
                    });
                }
                mLocationClient.start();
            }
        });
    }

    public static void actionStart(AppCompatActivity context,String lngLat){

        Intent intent = new Intent(context,WeatherActivity.class);
        intent.putExtra("mLngLat",lngLat);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private void requestCurrentWeather(final String latLng){
        // 获取实况天气
        mWeatherService.getCurrentWeatherByLatLng(latLng)
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<CurrentWeather, Observable<ForecastWeather>>() {
            @Override
            public Observable<ForecastWeather> call(CurrentWeather currentWeather) {

                if(currentWeather != null && currentWeather.getHeWeather6() != null
                        && currentWeather.getHeWeather6().size() > 0){
                    final CurrentWeather.HeWeather6Entity entity = currentWeather.getHeWeather6().get(0);
                    if("ok".equals(entity.getStatus())){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    updateCurrentWeather(entity);
                                }catch (Exception e){

                                }
                            }
                        });
                        // 获取3天预报
                        return mWeatherService.getForecastWeatherByLatLng(latLng);
                    }
                }
                return null;
            }
        }).subscribeOn(Schedulers.newThread()).flatMap(new Func1<ForecastWeather, Observable<LifeStyle>>() {
            @Override
            public Observable<LifeStyle> call(ForecastWeather forecastWeather) {
                if(forecastWeather != null && forecastWeather.getHeWeather6() != null
                        && forecastWeather.getHeWeather6().size() > 0) {
                    final ForecastWeather.HeWeather6Entity entity = forecastWeather.getHeWeather6().get(0);
                    if ("ok".equals(entity.getStatus())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    updateForecast(entity);
                                }catch (Exception e){
                                }
                            }
                        });
                        // 获取生活指数
                        return mWeatherService.getLifeStyleByLatLng(latLng);
                    }
                }
                return null;
            }
        }).subscribeOn(Schedulers.newThread()).flatMap(new Func1<LifeStyle, Observable<CurrentAir>>() {
            @Override
            public Observable<CurrentAir> call(LifeStyle lifeStyle) {

                if(lifeStyle != null && lifeStyle.getHeWeather6() != null
                        && lifeStyle.getHeWeather6().size() > 0) {
                    final LifeStyle.HeWeather6Entity entity = lifeStyle.getHeWeather6().get(0);
                    if ("ok".equals(entity.getStatus())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    updateLifestyle(entity);
                                }catch (Exception e){

                                }
                            }
                        });
                        // 获取空气质量
                        return mWeatherService.getCurrentAirByLatLng(entity.getBasic().getParent_city());
                    }
                }
                return null;
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<CurrentAir>() {
            @Override
            public void onCompleted() {
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mRefreshLayout.isRefreshing()){
                            mRefreshLayout.setRefreshing(false);
                        }
                    }
                },500);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(WeatherActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(mRefreshLayout.isRefreshing()){
                            mRefreshLayout.setRefreshing(false);
                        }
                    }
                },500);
            }

            @Override
            public void onNext(CurrentAir currentAir) {
                if(currentAir != null && currentAir.getHeWeather6() != null
                        && currentAir.getHeWeather6().size() > 0) {
                    final CurrentAir.HeWeather6Entity entity = currentAir.getHeWeather6().get(0);
                    if ("ok".equals(entity.getStatus())) {
                        updateCurrentAir(entity);
                    }
                }
            }
        });

    }

    private void updateCurrentWeather(CurrentWeather.HeWeather6Entity entity){
        String location = entity.getBasic().getLocation();
        mCityTv.setText(TextUtils.isEmpty(location)? entity.getBasic().getParent_city() : location);
        mTempTv.setText(entity.getNow().getTmp() + "℃");
        String code = entity.getNow().getCond_code();
        switch (code){
            case "100":
                mPicIv.setImageResource(R.mipmap.sunny_icon);
                break;
            case "101":
                mPicIv.setImageResource(R.mipmap.cloudy_icon);
                break;
            case "103":
                mPicIv.setImageResource(R.mipmap.cloudytosunny_icon);
                break;
            case "300":
                mPicIv.setImageResource(R.mipmap.light_rain_icon);
                break;
            case "301":
                mPicIv.setImageResource(R.mipmap.heavy_rain_icon);
                break;
        }

    }

    private void updateCurrentAir(CurrentAir.HeWeather6Entity entity){
        mPm25Tv.setText(entity.getAir_now_city().getPm25());
        mAirDescTv.setText(entity.getAir_now_city().getQlty());
    }

    private void updateForecast(ForecastWeather.HeWeather6Entity entity){
        List<ForecastWeather.HeWeather6Entity.Daily_forecastEntity> entityList =  entity.getDaily_forecast();
        if(entityList != null && !entityList.isEmpty()){
            for(int i = 0;i<entityList.size();i++){
                ForecastWeather.HeWeather6Entity.Daily_forecastEntity dailyEntity= entityList.get(i);
                mDateTvList.get(i).setText(dailyEntity.getDate());
                mTempTvList.get(i).setText(dailyEntity.getTmp_min() + "℃-" + dailyEntity.getTmp_max() + "℃");
                mDescTvList.get(i).setText(dailyEntity.getCond_txt_n());
                if(i == 0){
                    mMaxTempTv.setText(dailyEntity.getTmp_max() + "℃");
                    mMinTempTv.setText(dailyEntity.getTmp_min() + "℃");
                }
            }
        }
    }

    private void updateLifestyle(LifeStyle.HeWeather6Entity entity){
        List<LifeStyle.HeWeather6Entity.LifestyleEntity> entityList = entity.getLifestyle();
        if(entityList != null && !entityList.isEmpty()){
            for(int i = 0;i<entityList.size() && i < 5;i++){
                LifeStyle.HeWeather6Entity.LifestyleEntity lifestyleEntity= entityList.get(i+1);
                mBrfTvList.get(i).setText(mBrfTvList.get(i).getText().toString().substring(0,4)
                        +"-"+lifestyleEntity.getBrf());
                mTxtTvList.get(i).setText(lifestyleEntity.getTxt());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.weather_back_iv:
                finish();
                break;
            case R.id.weather_more_city_iv:
                mCityPicker.showCityPicker();
                break;

        }
    }
}
