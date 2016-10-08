package com.wukong.xiaoxiao.viewpagerwithdotsdemo;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.wukong.xiaoxiao.viewpagerwithdotsdemo.adapters.FragmentViewPagerAdapter;
import com.wukong.xiaoxiao.viewpagerwithdotsdemo.views.MyDotsViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MyDotsViewPager myDotsViewPager;
    private FragmentViewPagerAdapter fragmentViewPagerAdapter;
    private List<Fragment> list;
    private String[] pic = {"http://img3.imgtn.bdimg.com/it/u=4194240388,2262875649&fm=11&gp=0.jpg", "http://img0.imgtn.bdimg.com/it/u=379906380,1244319672&fm=11&gp=0.jpg", "http://img2.imgtn.bdimg.com/it/u=2135995201,188575537&fm=11&gp=0.jpg", "http://img0.imgtn.bdimg.com/it/u=379906380,1244319672&fm=11&gp=0.jpg", "http://img2.imgtn.bdimg.com/it/u=2135995201,188575537&fm=11&gp=0.jpg"};
    private int FragmentSize = 5;//包裹内容（Fragment）数量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        myDotsViewPager = (MyDotsViewPager) findViewById(R.id.viewpager);

        list = new ArrayList<>();
        fragmentViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), list);
        saveData();


    }

    void saveData() {
        for (int i = 0; i < FragmentSize; i++) {
            MyFragment myFragment = new MyFragment();
            Bundle bundle = new Bundle();
            bundle.putString("pic", pic[i]);
            myFragment.setArguments(bundle);
            list.add(myFragment);
        }
        myDotsViewPager.saveData(getSupportFragmentManager(), list);

        fragmentViewPagerAdapter.notifyDataSetChanged();
    }
}
