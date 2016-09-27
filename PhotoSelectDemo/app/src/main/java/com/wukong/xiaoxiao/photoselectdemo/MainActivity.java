package com.wukong.xiaoxiao.photoselectdemo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wukong.xiaoxiao.photoselectdemo.views.PhotoFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initImageLoader(this);
        savePhotoFragment();
    }

    public void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCacheSize(2 * 1024 * 1024) //缓存到内存的最大数据
                .memoryCacheSize(50 * 1024 * 1024) //设置内存缓存的大小
                .diskCacheFileCount(200)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
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
