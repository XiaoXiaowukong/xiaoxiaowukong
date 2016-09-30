package com.huatai.gn.letravel.views;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;


import com.huatai.gn.letravel.R;
import com.huatai.gn.letravel.base.BaseActivity;
import com.huatai.gn.letravel.models.settings.ComplaintActivity;
import com.huatai.gn.letravel.adapters.PhotoViewPagerAdapter;
import com.huatai.gn.letravel.beans.PhotoModel;
import com.huatai.gn.letravel.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/6/7.
 */
public class PhotoViewActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager mPager;
    public List<PhotoModel> selectPic;
    private TitleBar titleBar;
    private PhotoViewPagerAdapter photoViewPagerAdapter;
    private int index;
    private int position;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_view_pager;
    }

    @Override
    protected void initView() {
        selectPic = new ArrayList<>();
        selectPic.addAll(PhotoFragment.factSelected);

        position = getIntent().getIntExtra(PhotoFragment.POSITION, 1);
        mPager = (ViewPager) findViewById(R.id.pager);
        titleBar = (TitleBar) findViewById(R.id.photo_view_title);
        titleBar.setRightText("删除");
        titleBar.setRightClickListener(this);
        titleBar.setLeftClickListener(this);
        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        photoViewPagerAdapter = new PhotoViewPagerAdapter(selectPic, this);
        mPager.setAdapter(photoViewPagerAdapter);
        mPager.setCurrentItem(position);
        mPager.setOnPageChangeListener(this);
        index = position;
    }

    @Override
    protected void loadData() {

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
