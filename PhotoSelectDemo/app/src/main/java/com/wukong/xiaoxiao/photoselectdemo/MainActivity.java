package com.wukong.xiaoxiao.photoselectdemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wukong.xiaoxiao.photoselectdemo.views.PhotoFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        savePhotoFragment();
    }

    void savePhotoFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.
                beginTransaction();
        PhotoFragment fragment = new PhotoFragment();
        //加到Activity中
        fragmentTransaction.add(R.id.lt_fragment_content, fragment);
        //加到后台堆栈中，有下一句代码的话，点击返回按钮是退到Activity界面，没有的话，直接退出Activity
        //后面的参数是此Fragment的Tag。相当于id
        fragmentTransaction.addToBackStack("fragment1");
        //记住提交
        fragmentTransaction.commit();
    }
}
