package com.xiaomo.travelhelper.view.nicelayout.picshower;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.xiaomo.travelhelper.R;
import com.xiaomo.travelhelper.model.memo.MemoConstants;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        mContext = this;

        Intent intent = getIntent();
        ArrayList imgList = intent.getStringArrayListExtra("IMG_LIST_KEY");
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new SamplePagerAdapter(imgList));
    }

    class SamplePagerAdapter extends PagerAdapter {


        private ArrayList<String> imgList;

        public SamplePagerAdapter(ArrayList<String> imgList) {
            this.imgList = imgList;
        }

        @Override
        public int getCount() {
            if(imgList == null){
                return 0;
            }
            return imgList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(mContext)
                    .load(imgList.get(position))
                    .into(photoView);

            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    public static void actionStart(AppCompatActivity context,ArrayList<String> imgList){

        Intent intent = new Intent(context,ViewPagerActivity.class);
        intent.putStringArrayListExtra("IMG_LIST_KEY",imgList);
        context.startActivity(intent);


    }
}
