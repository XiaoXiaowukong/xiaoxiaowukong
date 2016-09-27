package com.wukong.xiaoxiao.photoselectdemo.views;/**
 * Created by jyt on 2016/9/20.
 */

import android.app.Fragment;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import com.wukong.xiaoxiao.photoselectdemo.R;
import com.wukong.xiaoxiao.photoselectdemo.adapters.GdAdapter;
import com.wukong.xiaoxiao.photoselectdemo.beans.PhotoModel;
import com.wukong.xiaoxiao.photoselectdemo.utils.PictureUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * created by neo on 2016/9/20 15:39
 * 图片选择器
 */
public class PhotoFragment extends Fragment implements AdapterView.OnItemClickListener {
    private GridView gd;
    private GdAdapter adapter;
    public static int maxNumber = 6;
    public static final String POSITION = "position";
    // 选择图片

    public static List<PhotoModel> selected;

    public static final int SELECT_IMAGE_CODE = 1001;
    public static final int CAMERA_PHOTO = 10002;
    public static final int PHOTO_PHOTO = 1003;

    public static final int DATA_CHANGE_REQUST = 2000;
    public static final int DATA_CHANGE_RESULT = 2001;
    private String str_choosed_img = "";


    private MyPhotoDialog menuWindow;

    PictureUtil pictureUtil;
    private String imagename;
    private String path;
    private File dir;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo, null);
        initView(view);
        return view;
    }


    protected void initView(View view) {
        gd = (GridView) view.findViewById(R.id.gd);
        selected = new ArrayList<>();
        PhotoModel photoModel = new PhotoModel();
        photoModel.setOriginalPath("default");
        selected.add(photoModel);
        adapter = new GdAdapter(getActivity(), selected);
        gd.setAdapter(adapter);
        gd.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (selected.size() == maxNumber) {
            if (selected.get(maxNumber - 1).getOriginalPath().equals("default")) {//说明有五张图加一张加载图
                if (position == selected.size() - 1) {// 如果是最后一个
                    showChooseIMG_WAYDialog();
                } else {//浏览图片
                    Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
                    intent.putExtra(POSITION, position);
                    startActivityForResult(intent, DATA_CHANGE_REQUST);
                }
            } else {//说明有六张真实的图
                Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
                intent.putExtra(POSITION, position);
                startActivityForResult(intent, DATA_CHANGE_REQUST);
            }
        } else {
            if (position == selected.size() - 1) {// 如果是最后一个
                showChooseIMG_WAYDialog();
            } else {//浏览图片
                Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
                intent.putExtra(POSITION, position);
                startActivityForResult(intent, DATA_CHANGE_REQUST);
            }
        }

    }

    /*
  * 选择图片上传的方式
  */
    private void showChooseIMG_WAYDialog() {


        //显示窗口
        menuWindow = new MyPhotoDialog(getActivity());
        menuWindow.setClicklistener(new MyPhotoDialog.ClickListenerInterface() {
            @Override
            public void camera() {
                takeCamrea();
            }

            @Override
            public void cancel() {

            }

            @Override
            public void pic() {
                Intent intent = new Intent(getActivity(), GridImageActivity.class);
                startActivityForResult(intent, SELECT_IMAGE_CODE);
            }
        });
        menuWindow.show();
    }

    void takeCamrea() {
        if (selected.size() > 6) {
            Toast.makeText(getActivity(), "最多添加" + maxNumber + "张图", Toast.LENGTH_SHORT).show();
        } else {
            path = Environment.getExternalStorageDirectory() + "/" + "leTravel/photo/";
            dir = new File(path);
            if (!dir.exists()) dir.mkdirs();
            imagename = getTimeName(System.currentTimeMillis()) + ".png";
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(dir, imagename);//
                Uri u = Uri.fromFile(f);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                startActivityForResult(intent, CAMERA_PHOTO);
            } else {

                Toast.makeText(getActivity(), "没有SD卡", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 时间戳
     *
     * @param time
     * @return
     */
    public static String getTimeName(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(time);
        return formatter.format(date);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == PHOTO_PHOTO && requestCode == SELECT_IMAGE_CODE) {

            List<String> photos = (List<String>) data.getExtras()
                    .getSerializable("photos");
            if (selected.size() > 0) {// 如果原来有图片
                selected.remove(selected.size() - 1);
            }
            for (int i = 0; i < photos.size(); i++) {
                PhotoModel photoModel = new PhotoModel();
                photoModel.setOriginalPath(photos.get(i));
                selected.add(photoModel);
            }
            if (selected.size() < 6) {
                PhotoModel addModel = new PhotoModel();
                addModel.setOriginalPath("default");
                selected.add(addModel);
            } else if (selected.size() == maxNumber) {

            }
            adapter.notifyDataSetChanged();

        }
        if (requestCode == CAMERA_PHOTO) {
            PhotoModel cameraPhotoModel = new PhotoModel();
            cameraPhotoModel.setOriginalPath(path + imagename);
            if (selected.size() > 0) {// 如果原来有图片
                selected.remove(selected.size() - 1);
            }
            selected.add(cameraPhotoModel);
            if (selected.size() < 6) {
                PhotoModel addModel = new PhotoModel();
                addModel.setOriginalPath("default");
                selected.add(addModel);
            } else if (selected.size() == maxNumber) {

            }
            adapter.notifyDataSetChanged();
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{str_choosed_img}, null, null);
        }
        if (requestCode == DATA_CHANGE_REQUST && resultCode == DATA_CHANGE_RESULT) {
            if (PhotoFragment.selected.size() < PhotoFragment.maxNumber) {
                if (PhotoFragment.selected.get(PhotoFragment.selected.size() - 1).getOriginalPath().equals("default")) {//说明有五张图加一张加载图

                } else {//没有加载图
                    PhotoModel addModel = new PhotoModel();
                    addModel.setOriginalPath("default");
                    selected.add(addModel);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
