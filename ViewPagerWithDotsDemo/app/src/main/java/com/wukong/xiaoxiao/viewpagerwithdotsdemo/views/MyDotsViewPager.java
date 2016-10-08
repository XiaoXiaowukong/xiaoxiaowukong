package com.wukong.xiaoxiao.viewpagerwithdotsdemo.views;/**
 * Created by jyt on 2016/9/23.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.wukong.xiaoxiao.viewpagerwithdotsdemo.R;
import com.wukong.xiaoxiao.viewpagerwithdotsdemo.adapters.FragmentViewPagerAdapter;

import java.util.List;

/**
 * created by neo on 2016/9/23 18:16
 * 自定义控件
 */
public class MyDotsViewPager extends LinearLayout {
    private LinearLayout ltDots;
    private MyViewPager viewPager;
    private Context context;
    private FragmentViewPagerAdapter fragmentViewPagerAdapter;
    private PagerListener pagerListener;

    public MyDotsViewPager(Context context) {
        super(context);
        this.context = context;
    }

    public MyDotsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public MyDotsViewPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(attrs);
    }

    public MyDotsViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setOrientation(VERTICAL);
        inflater.inflate(R.layout.view_mydots_viewpager, this, true);
        viewPager = (MyViewPager) findViewById(R.id.my_viewpager);
        ltDots = (LinearLayout) findViewById(R.id.lt_dots);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("gn", position + "第几个");
                reSaveDotsData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void reSaveDotsData(int position) {
        for (int i = 0; i < ltDots.getChildCount(); i++) {
            if (i == position) {
                ImageView imageView = (ImageView) ltDots.getChildAt(i);
                imageView.setImageResource(R.mipmap.ic_car_detail_selected);
            } else {
                ImageView imageView = (ImageView) ltDots.getChildAt(i);
                imageView.setImageResource(R.mipmap.ic_car_detail_unselect);
            }
        }
        if (pagerListener != null) {
            pagerListener.pagerPosition(position);
        }


    }

    public void saveData( FragmentManager fragmentManager, List<Fragment> fragments) {

        fragmentViewPagerAdapter = new FragmentViewPagerAdapter(fragmentManager, fragments);
        viewPager.setAdapter(fragmentViewPagerAdapter);
        ltDots.removeAllViews();
        for (int i = 0; i < fragments.size(); i++) {
            ImageView imageView = new ImageView(context);
            imageView.setPadding(10, 0, 0, 0);
            if (i == 0) {
                imageView.setImageResource(R.mipmap.ic_car_detail_selected);
            } else {
                imageView.setImageResource(R.mipmap.ic_car_detail_unselect);
            }
            ltDots.addView(imageView);
        }

    }

     public interface PagerListener {
        void pagerPosition(int page);
    }

    public void setPagerListener(PagerListener pagerListener) {
        this.pagerListener = pagerListener;
    }
}

