package com.wukong.xiaoxiao.photoselectdemo.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


import com.wukong.xiaoxiao.photoselectdemo.R;
import com.wukong.xiaoxiao.photoselectdemo.adapters.PhotoViewPagerAdapter;
import com.wukong.xiaoxiao.photoselectdemo.beans.PhotoModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/6/7.
 */
public class PhotoViewActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager mPager;
    public List<PhotoModel> selectPic;
    private PhotoViewPagerAdapter photoViewPagerAdapter;
    private int index;
    private int position;
    private TextView left, right;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        initView();
    }

    protected void initView() {
        selectPic = new ArrayList<>();
        selectPic.addAll(PhotoFragment.factSelected);

        position = getIntent().getIntExtra(PhotoFragment.POSITION, 1);
        mPager = (ViewPager) findViewById(R.id.pager);
        left= (TextView) findViewById(R.id.layout_left);
        right= (TextView) findViewById(R.id.layout_right);

        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        photoViewPagerAdapter = new PhotoViewPagerAdapter(selectPic, this);
        mPager.setAdapter(photoViewPagerAdapter);
        mPager.setCurrentItem(position);
        mPager.setOnPageChangeListener(this);
        index = position;
        left.setOnClickListener(this);
        right.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_right:
                PhotoFragment.factSelected.remove(index);
                selectPic.remove(index);
                photoViewPagerAdapter.notifyDataSetChanged();
                if (PhotoFragment.factSelected.size() == 0) {
                    setResult(PhotoFragment.DATA_CHANGE_RESULT);
                    PhotoViewActivity.this.finish();
                }
                break;
            case R.id.layout_left:
                setResult(PhotoFragment.DATA_CHANGE_RESULT);
                PhotoViewActivity.this.finish();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        index = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(PhotoFragment.DATA_CHANGE_RESULT);
            PhotoViewActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
