package com.wukong.xiaoxiao.viewpagerwithdotsdemo;/**
 * Created by gn on 2016/10/8.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * created by neo on 2016/10/8 10:38
 */
public class MyFragment extends Fragment {
    private View view;
    private ImageView imageView;
    private String pic;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pic = getArguments().getString("pic");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, null);
        initView();
        saveData(pic);
        return view;
    }

    void initView() {
        imageView = (ImageView) view.findViewById(R.id.image);
    }

    //设置数据
    public void saveData(String pic) {
        Glide.with(getActivity())
                .load(pic)
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageView);
    }
}
