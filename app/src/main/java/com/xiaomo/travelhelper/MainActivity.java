package com.xiaomo.travelhelper;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.xiaomo.travelhelper.dao.PrivateChatDao;
import com.xiaomo.travelhelper.http.ChatService;
import com.xiaomo.travelhelper.http.HttpServiceFactory;
import com.xiaomo.travelhelper.http.IChatService;
import com.xiaomo.travelhelper.model.UserConst;
import com.xiaomo.travelhelper.model.chat.PrivateChatModel;
import com.xiaomo.travelhelper.model.chat.PrivateChatResult;
import com.xiaomo.travelhelper.util.SharedPreferencesUtil;
import com.xiaomo.travelhelper.util.UIUtils;
import com.xiaomo.travelhelper.view.BottomBarItem;
import com.xiaomo.travelhelper.view.BottomBarLayout;
import com.xiaomo.travelhelper.view.CustomPopup;
import com.xiaomo.travelhelper.view.TopBarLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 主页面 activity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,CustomPopup.ItemClickCallback{

    @BindView(R.id.vp_content) ViewPager mVpContent;
    @BindView(R.id.bottom_bar_layout) BottomBarLayout mBottomBarLayout;
    @BindView(R.id.top_bar_layout) TopBarLayout mTopBarLayout;

    private List<Fragment> mFragmentList = new ArrayList<>();
    private CustomPopup mMorePopup;

    private PrivateChatDao mchatDao;
    private Timer mTimer;
    private IChatService mIChatService;

    /*百度地图*/
    private LocationClient mLocationClient;
    private String country;
    private String province;
    private String city;
    private String locationDesc;
    private String address;
    private double lat;
    private double lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();
        initListener();
        getLocationInfo();

        initData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        ChatService.disconnect();

    }

    private void initData(){
        mchatDao = new PrivateChatDao();
        mIChatService = HttpServiceFactory.buildChatService();
        // 每十秒扫描一次
        mTimer = new Timer();
        final String account = SharedPreferencesUtil.read(MainActivity.this, UserConst.ACCOUNT,"");
        final String token = SharedPreferencesUtil.read(MainActivity.this, UserConst.TOKEN,"");
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                final int result = mchatDao.countUnread(account);
                Log.i("ChatService","扫描未读信息总数-" + result);
                if(result >= 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBottomBarLayout.setUnread(2,result);//设置第二个页签的未读数
                        }
                    });
                }

                mIChatService.listMsg(account,token).subscribeOn(Schedulers.newThread()).subscribe(new Subscriber<PrivateChatResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(PrivateChatResult privateChatResult) {

                        Log.i("ChatService","扫描请求未读信息");

                        try{
                            if(privateChatResult != null && privateChatResult.isSuccess()){
                                List<PrivateChatResult.DataEntity> resultList = privateChatResult.getData();
                                Log.i("ChatService","扫描请求未读信息结果数-"+ resultList.size());
                                if(resultList!= null && !resultList.isEmpty()){
                                    for(PrivateChatResult.DataEntity dataEntity :resultList){

                                        PrivateChatModel model = new PrivateChatModel();
                                        model.setFromAccount(dataEntity.getFromAccount());
                                        model.setFromUsername(dataEntity.getFromUsername());
                                        model.setFromImg(dataEntity.getFromImg());
                                        model.setToImg(dataEntity.getToImg());
                                        model.setToAccount(dataEntity.getToAccount());
                                        model.setToUsername(dataEntity.getToUsername());
                                        model.setContent(dataEntity.getContent());
                                        model.setFlag(2);
                                        model.setTime(new Date(System.currentTimeMillis()));

                                        model.save();

                                        Log.i("ChatService","扫描请求未读信息结果数-"+ model);

                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        },1000,10*1000);
    }

    private void initMorePopupIfNeed() {
        if (mMorePopup == null) {
            mMorePopup = new CustomPopup(MainActivity.this, QMUIPopup.DIRECTION_NONE);
            mMorePopup.setContentView(R.layout.item_poup_more);
            mMorePopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_RIGHT);
            mMorePopup.setPreferredDirection(QMUIPopup.DIRECTION_BOTTOM);
            mMorePopup.setPopupLeftRightMinMargin(UIUtils.dip2Px(MainActivity.this,15));
            mMorePopup.setBackgroundDrawable(getResources().getDrawable(R.drawable.item_poup_bg));
            mMorePopup.setItemClickCallback(this);
        }
    }


    private void getLocationInfo(){

        LocationClientOption option = new LocationClientOption();

        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                city = bdLocation.getCity();
                country = bdLocation.getCountry();
                province = bdLocation.getProvince();
                address = bdLocation.getAddrStr();
                locationDesc = bdLocation.getLocationDescribe();
                lat = bdLocation.getLatitude();
                lng = bdLocation.getLongitude();
                Log.i("MemoEditActivity",lat + "-"+lng + city);
                ((DiscoverFragment)mFragmentList.get(0)).setLngLat(lng + "," + lat);
                mLocationClient.stop();
            }
        });

        mLocationClient.start();
    }

    private void initFragment() {

        DiscoverFragment discoverFragment = new DiscoverFragment();
        mFragmentList.add(discoverFragment);


        FriendFragment friendFragment = new FriendFragment();
        mFragmentList.add(friendFragment);

        CircleFragment circleFragment = new CircleFragment();
        mFragmentList.add(circleFragment);


        Fragment memoFragment = new MemoFragment();
        mFragmentList.add(memoFragment);

    }

    private void initListener() {
        mVpContent.setAdapter(new MyAdapter(getSupportFragmentManager()));
        mTopBarLayout.getMoreView().setOnClickListener(this);
        mBottomBarLayout.setViewPager(mVpContent);
        mBottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final BottomBarItem bottomBarItem, int position) {
                mBottomBarLayout.setUnread(2,0);//设置第二个页签的未读数
            }
        });
        /*mBottomBarLayout.setUnread(0,20);//设置第一个页签的未读数为20
        mBottomBarLayout.setUnread(1,1001);//设置第二个页签的未读数
        mBottomBarLayout.showNotify(2);//设置第三个页签显示提示的小红点
        mBottomBarLayout.setMsg(3,"NEW");//设置第四个页签显示NEW提示文字*/
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.top_bar_more_view:
                initMorePopupIfNeed();
                mMorePopup.show(view);
                break;
            default:
                break;


        }
    }

    @Override
    public void itemClick(View v) {
        switch (v.getId()){
            case R.id.popup_add_friend_layout:
                AddFriendActivity.actionStart(MainActivity.this);
                break;
            case R.id.popup_chat_layout:
                Toast.makeText(MainActivity.this,"发起群聊",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }



    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity,MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }









}
