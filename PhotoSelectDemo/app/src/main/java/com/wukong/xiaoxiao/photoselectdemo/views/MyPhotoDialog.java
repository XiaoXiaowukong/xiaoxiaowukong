package com.wukong.xiaoxiao.photoselectdemo.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wukong.xiaoxiao.photoselectdemo.R;


/**
 * Created by 郭宁 on 2015/5/28.
 */
public class MyPhotoDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private TextView btn_take_photo, btn_pick_photo, btn_cancel;
    private ClickListenerInterface clickListenerInterface;

    public MyPhotoDialog(Context context) {

        super(context, R.style.selectorDialog);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.select_pic_pop, null);
        setContentView(view);

        btn_take_photo = (TextView) findViewById(R.id.btn_take_photo);
        btn_pick_photo = (TextView) findViewById(R.id.btn_pick_photo);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        //设置按钮监听
        btn_pick_photo.setOnClickListener(this);
        btn_take_photo.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        Window dialogWindow = getWindow();

        dialogWindow.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        dialogWindow.setWindowAnimations(R.style.mystyle);  //添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 1);
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                clickListenerInterface.camera();
                break;
            case R.id.btn_pick_photo:
                clickListenerInterface.pic();
                break;
            case R.id.btn_cancel:
                clickListenerInterface.cancel();
                break;
        }
        MyPhotoDialog.this.cancel();
    }

    public interface ClickListenerInterface {
        void camera();

        void cancel();

        void pic();
    }

}
