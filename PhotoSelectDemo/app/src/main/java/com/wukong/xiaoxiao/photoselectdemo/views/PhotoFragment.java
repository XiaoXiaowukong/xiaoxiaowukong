package com.huatai.gn.letravel.views;/**
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.huatai.gn.letravel.R;
import com.huatai.gn.letravel.adapters.GdAdapter;
import com.huatai.gn.letravel.beans.PhotoModel;
import com.huatai.gn.letravel.utils.LogUtils;
import com.huatai.gn.letravel.utils.PictureUtil;

import java.io.File;
import java.io.Serializable;
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

    public static List<PhotoModel> selected;//展示的照片地址
    public static List<PhotoModel> factSelected;//实际真实的照片地址

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

        PhotoModel photoModel = new PhotoModel();
        photoModel.setOriginalPath("default");


        selected = new ArrayList<>();
        factSelected = new ArrayList<>();
        adapter = new GdAdapter(getActivity(), selected);

        selected.add(photoModel);
        gd.setAdapter(adapter);
        gd.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtils.e("gn", selected.size() + "数量" + position);

        if (factSelected.size() == 0) {//实际没有一张图片
            showChooseIMG_WAYDialog();
        } else {//有图片的
            if (selected.get(position).getOriginalPath().equals("default")) {
                showChooseIMG_WAYDialog();
            } else {
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
            for (int i = 0; i < photos.size(); i++) {
                PhotoModel photoModel = new PhotoModel();
                photoModel.setOriginalPath(photos.get(i));
                factSelected.add(photoModel);
            }
            selected.clear();
            if (factSelected.size() < maxNumber) {
                PhotoModel addModel = new PhotoModel();
                addModel.setOriginalPath("default");
                selected.addAll(factSelected);
                selected.add(addModel);
            } else if (factSelected.size() == maxNumber) {
                selected.addAll(factSelected);
            }
            adapter.notifyDataSetChanged();

        }
        if (requestCode == CAMERA_PHOTO) {
            PhotoModel cameraPhotoModel = new PhotoModel();
            cameraPhotoModel.setOriginalPath(path + imagename);
            factSelected.add(cameraPhotoModel);
            selected.clear();
            if (factSelected.size() < 6) {
                PhotoModel addModel = new PhotoModel();
                addModel.setOriginalPath("default");
                selected.addAll(factSelected);
                selected.add(addModel);
            } else {
                selected.addAll(factSelected);
            }
            adapter.notifyDataSetChanged();
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{str_choosed_img}, null, null);
        }
        if (requestCode == DATA_CHANGE_REQUST && resultCode == DATA_CHANGE_RESULT) {
            if (PhotoFragment.factSelected.size() < PhotoFragment.maxNumber) {
                selected.clear();
                PhotoModel addModel = new PhotoModel();
                addModel.setOriginalPath("default");
                selected.addAll(factSelected);
                selected.add(addModel);
            } else {
                selected.addAll(factSelected);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
