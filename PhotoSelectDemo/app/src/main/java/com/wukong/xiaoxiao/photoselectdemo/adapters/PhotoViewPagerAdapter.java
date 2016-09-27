package com.wukong.xiaoxiao.photoselectdemo.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wukong.xiaoxiao.photoselectdemo.R;
import com.wukong.xiaoxiao.photoselectdemo.beans.PhotoModel;

import java.util.List;


/**
 * Created by Administrator on 2016/6/7.
 */
public class PhotoViewPagerAdapter extends PagerAdapter {
    private List<PhotoModel> selected;
    private Context context;

    public PhotoViewPagerAdapter(List<PhotoModel> selected, Context context) {
        this.selected = selected;
        this.context = context;
    }

    @Override
    public int getCount() {
        return selected.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = new ImageView(context);
//        view.enable();
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Glide.with(context)
                .load("file://" + selected.get(position).getOriginalPath())
                .thumbnail(0.1f)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(view);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
