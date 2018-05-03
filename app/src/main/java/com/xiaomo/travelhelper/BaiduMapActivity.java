package com.xiaomo.travelhelper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.mapapi.walknavi.adapter.IWEngineInitListener;
import com.baidu.mapapi.walknavi.adapter.IWRoutePlanListener;
import com.baidu.mapapi.walknavi.model.WalkRoutePlanError;
import com.baidu.mapapi.walknavi.params.WalkNaviLaunchParam;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.xiaomo.travelhelper.baidu.WNaviGuideActivity;
import com.xiaomo.travelhelper.baidu.WalkingRouteOverlay;
import com.xiaomo.travelhelper.model.found.FoundContants;
import com.xiaomo.travelhelper.view.BottomPopupViewHelper;
import com.xiaomo.travelhelper.view.PromotedActionsLibrary;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 定位与导航 activity 页面
 */
public class BaiduMapActivity extends AppCompatActivity implements View.OnClickListener
        ,OnGetPoiSearchResultListener{

    private int mRequestFlag;
    private boolean isMoved = false;
    @BindView(R.id.baidu_map_view) MapView mMapView;
    @BindView(R.id.baidu_map_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.baidu_map_discover_layout) LinearLayout mDiscoverLayout;
    @BindView(R.id.container) FrameLayout mFrameLayout;

    // 侧边栏
    @BindView(R.id.baidu_map_close_layout) RelativeLayout mCloseDrawerLayout;
    @BindView(R.id.baidu_map_location_et) EditText mLocationEt;
    @BindView(R.id.baidu_map_keyword_et) EditText mKeywordEt;
    @BindView(R.id.baidu_map_poi_sum_tv) TextView mSumPoiTv;
    @BindView(R.id.baidu_map_search_btn) QMUIRoundButton mSearchBtn;
    @BindView(R.id.baidu_map_poi_list_view) ListView mListView;


    /*百度地图*/
    private LocationClient mLocationClient;
    private BaiduMap mBaiduMap;
    private PoiSearch mPoiSearch;
    private LatLng mCurrentLatLng;
    private LatLng mTargetLatLng;
    private GeoCoder mGeoCoder;
    private RoutePlanSearch mRoutePlanSearch;

    private BottomPopupViewHelper mLocationPopupViewHelper;
    private BottomPopupViewHelper mRoundPopupViewHelper;
    private View mLocationPopupView;
    private View mRoundPopupView;

    private WalkNavigateHelper mWalkNavigateHelper;
    private boolean mInitNaviSuccess = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_map);
        ButterKnife.bind(this);
        initData();
        initDataWithNewThread();


    }


    private void initDataWithNewThread(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                initBaiduMap(mRequestFlag);
            }
        }).start();

    }

    private void initData(){
        Intent intent = getIntent();
        if(intent != null){
            mRequestFlag = intent.getIntExtra(FoundContants.REQUEST_KEY,0);
        }
        // 根据请求类别初始化 view 与监听器
        initView();
        initListener();
    }

    private void initView(){

        // 禁止侧边滑动
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        if(mRequestFlag == FoundContants.REQUEST_BASE){
            // 我的位置
            initFloatBtn();
            mDiscoverLayout.setVisibility(View.GONE);
        }else if(mRequestFlag == FoundContants.REQUEST_AROUND){
            // 周边
            mDiscoverLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initListener(){

        if(mRequestFlag == FoundContants.REQUEST_BASE){
            // 我的位置
        }else if(mRequestFlag == FoundContants.REQUEST_AROUND){
            // 周边
            mDiscoverLayout.setOnClickListener(this);
            mSearchBtn.setOnClickListener(this);
            mCloseDrawerLayout.setOnClickListener(this);
            mPoiSearch = PoiSearch.newInstance();
            mPoiSearch.setOnGetPoiSearchResultListener(this);
        }

    }

    /**
     * 初始化百度地图相关数据
     */
    private void initBaiduMap(final int mRequestFlag){
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.map_route);
        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfiguration(config);

        BaiduMap.OnMapLongClickListener longClickListener = new BaiduMap.OnMapLongClickListener() {
            /**
             * 地图长按事件监听回调函数
             * @param point 长按的地理坐标
             */
            public void onMapLongClick(LatLng point){
                initGeoCoder();
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(point));
            }

        };
        BaiduMap.OnMapClickListener clickListener = new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                initLocationPopupWin();
                mLocationPopupViewHelper.dismiss();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        };
        mBaiduMap.setOnMapLongClickListener(longClickListener);
        mBaiduMap.setOnMapClickListener(clickListener);

        LocationClientOption option = new LocationClientOption();
        //可选，设置定位模式，默认高精度
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(3000);


        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                double latitude = bdLocation.getLatitude();     //获取维度信息
                double longitude = bdLocation.getLongitude();    //获取经度信息
                // 构造定位数据
                MyLocationData locData = new MyLocationData.Builder()
                        //.accuracy(radius)
                        .direction(100).latitude(latitude)
                        .longitude(longitude)
                        .build();
                // 设置定位数据
                mCurrentLatLng = new LatLng(latitude,longitude);
                mBaiduMap.setMyLocationData(locData);
                if(!isMoved){
                    moveToCurrentLocation(latitude,longitude);
                    isMoved = true;
                }
            }
        });
        mLocationClient.start();
    }

    /**
     * 初始化路线搜索相关数据
     */
    private void initRoutePlanSearch(){
        if(mRoutePlanSearch == null){
            mRoutePlanSearch =  RoutePlanSearch.newInstance();
            OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {

                @Override
                public void onGetWalkingRouteResult(WalkingRouteResult result) {
                    //获取步行线路规划结果
                    List<WalkingRouteLine> lineList = result.getRouteLines();
                    if(lineList != null && !lineList.isEmpty()){
                        for(WalkingRouteLine line : lineList){
                            drawLineByWalkRouteOverlay(line);
                        }
                    }
                }

                @Override
                public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

                }

                @Override
                public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

                }

                @Override
                public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

                }

                @Override
                public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

                }

                @Override
                public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

                }

            };
            mRoutePlanSearch.setOnGetRoutePlanResultListener(listener);
        }



    }

    /**
     * 初始化编码与逆向编码相关数据
     */
    private void initGeoCoder(){

        if(mGeoCoder == null){
            mGeoCoder= GeoCoder.newInstance();
            OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {

                public void onGetGeoCodeResult(GeoCodeResult result) {

                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        //没有检索到结果
                    }
                    //获取地理编码结果
                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                    if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                        Toast.makeText(BaiduMapActivity.this,"没有检索结果",Toast.LENGTH_SHORT).show();
                    }else{
                        //获取反向地理编码结果
                        String address = result.getAddress();
                        String desc = result.getSematicDescription();
                        mTargetLatLng = result.getLocation();
                        mBaiduMap.clear();
                        addMarker(mTargetLatLng);
                        if(mRequestFlag == FoundContants.REQUEST_BASE){
                            requestWalkRoute(mCurrentLatLng,mTargetLatLng);
                            initLocationPopupWin();
                            mLocationPopupViewHelper.show(BaiduMapActivity.this, mLocationPopupView,144);
                            ((TextView) mLocationPopupView.findViewById(R.id.location_popup_address_tv))
                                            .setText(address);
                            ((TextView) mLocationPopupView.findViewById(R.id.location_popup_desc_tv))
                                            .setText(desc);
                        }else if(mRequestFlag == FoundContants.REQUEST_AROUND){
                            initRoundPopupWin();
                            mRoundPopupViewHelper.show(BaiduMapActivity.this, mRoundPopupView,144);
                            mLocationEt.setText(address);
                            ((TextView) mRoundPopupView.findViewById(R.id.round_popup_address_tv))
                                    .setText(address);
                            ((TextView) mRoundPopupView.findViewById(R.id.round_popup_desc_tv))
                                    .setText(desc);
                        }
                    }
                }
            };
            mGeoCoder.setOnGetGeoCodeResultListener(listener);
        }
    }

    /**
     * 请求路线规划
     * @param startLatLng
     * @param endLatLng
     */
    private void requestWalkRoute(LatLng startLatLng,LatLng endLatLng){

        initRoutePlanSearch();
        PlanNode startNode = PlanNode.withLocation(startLatLng);
        PlanNode endNode = PlanNode.withLocation(endLatLng);
        mRoutePlanSearch.walkingSearch(new WalkingRoutePlanOption()
                .from(startNode)
                .to(endNode));
    }


    private void initLocationPopupWin(){
        if(mLocationPopupView == null){
            mLocationPopupView = LayoutInflater.from(BaiduMapActivity.this).inflate(R.layout.location_popup_window,null);
            mLocationPopupViewHelper = new BottomPopupViewHelper();
            mLocationPopupView.findViewById(R.id.location_popup_navi_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initNaviHelper();
                    mLocationPopupViewHelper.dismiss();
                }
            });
        }
    }

    private void initRoundPopupWin(){

        if(mRoundPopupView == null){
            mRoundPopupView = LayoutInflater.from(BaiduMapActivity.this).inflate(R.layout.round_popup_window,null);
            mRoundPopupViewHelper = new BottomPopupViewHelper();
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.round_popup_navi_layout:
                            initNaviHelper();
                            break;
                        case R.id.round_popup_search_layout:
                            if(!mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                                mDrawerLayout.openDrawer(GravityCompat.START);
                            }
                            break;
                    }
                    mRoundPopupViewHelper.dismiss();
                }
            };
            mRoundPopupView.findViewById(R.id.round_popup_search_layout).setOnClickListener(listener);
            mRoundPopupView.findViewById(R.id.round_popup_navi_layout).setOnClickListener(listener);
        }
    }

    private void addMarker(LatLng point){

        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.map_location_icon);

        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    private void addMarker(List<LatLng> points){

        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.map_location_icon);
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        for(LatLng point : points){
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            options.add(option);
        }
        mBaiduMap.addOverlays(options);
    }

    private void drawLineByWalkRouteOverlay (WalkingRouteLine line){

        WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
        overlay.setData(line);
        overlay.addToMap();

    }

    private void initNaviHelper(){
        if(mWalkNavigateHelper == null) {
            mWalkNavigateHelper = WalkNavigateHelper.getInstance();
        }
        mWalkNavigateHelper.initNaviEngine(BaiduMapActivity.this, new IWEngineInitListener() {
                @Override
                public void engineInitSuccess() {
                    Log.d("MemoEditActivity", "引擎初始化成功");
                    routePlanWithParam(mCurrentLatLng,mTargetLatLng);
                }

                @Override
                public void engineInitFail() {
                    Log.d("MemoEditActivity", "引擎初始化失败");
                    mInitNaviSuccess = false;
                }
            });
    }

    /**
     * 开始算路
     */
    private void routePlanWithParam(LatLng  startPt,LatLng  endPt) {
        WalkNaviLaunchParam param = new WalkNaviLaunchParam().stPt(startPt).endPt(endPt);
        mWalkNavigateHelper.routePlanWithParams(param, new IWRoutePlanListener() {
            @Override
            public void onRoutePlanStart() {
                Log.d("MemoEditActivity", "开始算路");
            }

            @Override
            public void onRoutePlanSuccess() {
                Log.d("MemoEditActivity", "算路成功,跳转至诱导页面");
                Intent intent = new Intent();
                intent.setClass(BaiduMapActivity.this, WNaviGuideActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFail(WalkRoutePlanError error) {
                Log.d("MemoEditActivity", "算路失败");
            }

        });
    }


    private void moveToCurrentLocation(double latitude,double longitude){

        //设定中心点坐标
        LatLng cenpt =  new LatLng(latitude,longitude);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                //要移动的点
                .target(cenpt)
                //放大地图到16倍
                .zoom(16)
                .build();

        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    public static void actionStart(AppCompatActivity context,int requestCode){
        Intent intent = new Intent(context,BaiduMapActivity.class);
        intent.putExtra(FoundContants.REQUEST_KEY,requestCode);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private void poiSearchInCity(String city,String keyword,int pageNum,int pageSize){
        mPoiSearch.searchInCity(new PoiCitySearchOption()
            .city(city)
            .keyword(keyword)
            .pageNum(pageNum)
            .pageCapacity(pageSize));

    }

    private void poiSearchByLatLng(String keyword,LatLng latLng,int radius,int pageNum,int pageSize){

        mPoiSearch.searchNearby(new PoiNearbySearchOption()
                .keyword(keyword)
                .sortType(PoiSortType.distance_from_near_to_far)
                .location(latLng)
                .radius(radius)
                .pageNum(pageNum)
                .pageCapacity(pageSize));
    }

    private void initFloatBtn(){



        final PromotedActionsLibrary promotedActionsLibrary = new PromotedActionsLibrary();
        promotedActionsLibrary.setup(getApplicationContext(), mFrameLayout);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch ((String)view.getTag()){
                    case "普通":
                        mBaiduMap.setTrafficEnabled(false);
                        mBaiduMap.setBaiduHeatMapEnabled(false);
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                        break;
                    case "实况":
                        mBaiduMap.setBaiduHeatMapEnabled(false);
                        mBaiduMap.setTrafficEnabled(true);
                        break;
                    case "卫星":
                        mBaiduMap.setTrafficEnabled(false);
                        mBaiduMap.setBaiduHeatMapEnabled(false);
                        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                        break;
                    case "热力":
                        mBaiduMap.setTrafficEnabled(false);
                        mBaiduMap.setBaiduHeatMapEnabled(true);
                        break;

                }

                if (promotedActionsLibrary.isMenuOpened()) {
                    promotedActionsLibrary.closePromotedActions().start();
                    promotedActionsLibrary.setMenuOpened(false);
                } else {
                    promotedActionsLibrary.openPromotedActions().start();
                    promotedActionsLibrary.setMenuOpened(true);
                }
            }
        };

        promotedActionsLibrary.addItem("普通", onClickListener);
        promotedActionsLibrary.addItem("实况", onClickListener);
        promotedActionsLibrary.addItem("卫星", onClickListener);
        promotedActionsLibrary.addItem("热力", onClickListener);
        promotedActionsLibrary.addMainItem(getResources().getDrawable(android.R.drawable.ic_input_add));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.baidu_map_discover_layout:
                if(!mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }

                break;
            case R.id.baidu_map_search_btn:
                mSearchBtn.setText("搜索中...");
                mSearchBtn.setTextColor(getResources().getColor(R.color.grey_2));
                String keyword = mKeywordEt.getText().toString();
                String location = mLocationEt.getText().toString();
                if(TextUtils.isEmpty(keyword) || "关键词".equals(keyword.trim())){
                    Toast.makeText(BaiduMapActivity.this,"请输入关键词",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(location) || "当前位置".equals(location.trim())){
                    poiSearchByLatLng(keyword, mCurrentLatLng,5000,1,30);
                }else{

                    poiSearchByLatLng(keyword, mTargetLatLng,5000,1,30);
                }
                closeInputKeyBoard();
                break;
            case R.id.baidu_map_close_layout:
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                break;

        }
    }

    private void closeInputKeyBoard(){
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
        }

    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        int sum = poiResult.getTotalPoiNum();
        mSumPoiTv.setText("发现 " + sum + " 家");
        List<PoiInfo> poiInfoList = poiResult.getAllPoi();
        if(poiInfoList != null && !poiInfoList.isEmpty()){
            mListView.setAdapter(new MyAdapter(poiInfoList));
        }else{

        }
        mSearchBtn.setText("搜索周边");
        mSearchBtn.setTextColor(getResources().getColor(R.color.blue));
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        if(mPoiSearch != null){
            mPoiSearch.destroy();
        }
        if(mGeoCoder != null){
            mGeoCoder.destroy();
        }
        if(mRoutePlanSearch != null){
            mRoutePlanSearch.destroy();
        }
    }

    class MyAdapter extends BaseAdapter{

        private List<PoiInfo> mPoiInfoList;

        MyAdapter(List<PoiInfo> poiInfoList){
            mPoiInfoList = poiInfoList;
        }

        @Override
        public int getCount() {
            if(mPoiInfoList == null){
                return 0;
            }
            return mPoiInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mPoiInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            ViewHolder viewHolder;
            if(view == null){
                view = LayoutInflater.from(BaiduMapActivity.this).inflate(R.layout.item_poi_list,null);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }
            final PoiInfo poiInfo = mPoiInfoList.get(position);
            viewHolder.mAddressTv.setText(poiInfo.address);
            viewHolder.mNameTv.setText(poiInfo.name);
            viewHolder.mRotoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTargetLatLng = poiInfo.location;
                    mBaiduMap.clear();
                    addMarker(mTargetLatLng);
                    requestWalkRoute(mCurrentLatLng,mTargetLatLng);

                    if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }

                    initRoundPopupWin();
                    mRoundPopupViewHelper.show(BaiduMapActivity.this,mRoundPopupView,144);
                    ((TextView) mRoundPopupView.findViewById(R.id.round_popup_address_tv))
                            .setText(poiInfo.address);
                    ((TextView) mRoundPopupView.findViewById(R.id.round_popup_desc_tv))
                            .setText(poiInfo.name);
                }
            });
            return view;
        }


        class ViewHolder{

            @BindView(R.id.poi_goto_layout) RelativeLayout mRotoLayout;
            @BindView(R.id.poi_address_tv) TextView mAddressTv;
            @BindView(R.id.poi_name_tv) TextView mNameTv;

            ViewHolder(View view){
                ButterKnife.bind(this,view);
            }
        }
    }

}
