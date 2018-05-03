package com.xiaomo.travelhelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xiaomo.travelhelper.model.found.FoundContants;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.imallan.jellyrefresh.JellyRefreshLayout;
import uk.co.imallan.jellyrefresh.PullToRefreshLayout;


/**
 * 发现 Fragment 页面
 */

public class DiscoverFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private Fragment mFragment;
    private View mView;
    private String lngLat = "深圳";

    @BindView(R.id.discover_refresh_layout) JellyRefreshLayout mJellyRefreshLayout;
    @BindView(R.id.discover_location_layout) LinearLayout mLocationLayout;
    @BindView(R.id.discover_weather_layout) LinearLayout mWeatherLayout;
    @BindView(R.id.discover_round_layout) LinearLayout mRoundLayout;
    @BindView(R.id.discover_route_layout) LinearLayout mRouteLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;
        mFragment = this;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_discover_layout,container,false);
        ButterKnife.bind(this,mView);
        initRefreshLayout();
        initListener();
        return mView;
    }

    private void initRefreshLayout(){
        mJellyRefreshLayout.setPullToRefreshListener(new PullToRefreshLayout.PullToRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mJellyRefreshLayout.setRefreshing(false);
                    }
                }, 500);

            }
        });
    }
    private void initListener(){

        mRouteLayout.setOnClickListener(this);
        mRoundLayout.setOnClickListener(this);
        mLocationLayout.setOnClickListener(this);
        mWeatherLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.discover_location_layout:
                BaiduMapActivity.actionStart((AppCompatActivity)mContext, FoundContants.REQUEST_BASE);
                break;
            case R.id.discover_weather_layout:
                WeatherActivity.actionStart((AppCompatActivity)mContext,lngLat);
                break;
            case R.id.discover_round_layout:
                BaiduMapActivity.actionStart((AppCompatActivity)mContext, FoundContants.REQUEST_AROUND);
                break;
            case R.id.discover_route_layout:
                RoutePlanActivity.actionStart((AppCompatActivity)mContext);
                break;
        }
    }

    public void setLngLat(String lngLat) {
        this.lngLat = lngLat;
    }
}
