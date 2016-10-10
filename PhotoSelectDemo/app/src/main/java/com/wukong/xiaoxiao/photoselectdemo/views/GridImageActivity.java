package com.wukong.xiaoxiao.photoselectdemo.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.wukong.xiaoxiao.photoselectdemo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.wukong.xiaoxiao.photoselectdemo.views.PhotoFragment.maxNumber;


/**
 * Created by Hanks on 2015/9/14.
 */
public class GridImageActivity extends Activity implements View.OnClickListener {

    private View viewLayer;
    private TextView tvTitle, tvCancle, tvOk;
    private RecyclerView recyclerImage;
    private RecyclerView recyclerDir;
    private ImageAdapter imageAdapter;


    private ArrayList<Floder> mDirPaths = new ArrayList<Floder>();

    /**
     * 已选择的图片
     */
    public ArrayList<String> selectedPicture = new ArrayList<String>();
    private String cameraPath = null;
    private Floder imageAll, currentImageFolder;
    private FolderAdapter dirAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_picture);
        initView();
    }



    protected void initView() {
        bindViews();
        initViews();
        bindListeners();
    }

    public void back(View view) {
        onBackPressed();
    }

    private void bindViews() {
        viewLayer = findViewById(R.id.view_layer);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        recyclerImage = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerDir = (RecyclerView) findViewById(R.id.recycler_dir);
        viewLayer.setVisibility(View.GONE);
        tvCancle = (TextView) findViewById(R.id.tv_cancle);
        tvOk = (TextView) findViewById(R.id.tv_ok);
        tvCancle.setOnClickListener(this);
        tvOk.setOnClickListener(this);
    }

    private void initViews() {
        selectedPicture.clear();//每次进来清除数据

        imageAll = new Floder();
        imageAll.setDir("/所有图片");
        currentImageFolder = imageAll;
        mDirPaths.add(imageAll);
        getThumbnail();
    }

    private void bindListeners() {
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDirlistPop();
            }
        });

        //图片列表
        GridLayoutManager mgr = new GridLayoutManager(this, 3);
        recyclerImage.setLayoutManager(mgr);
        recyclerImage.addItemDecoration(new GridSpacingItemDecoration(3, dp2px(5), false));
        imageAdapter = new ImageAdapter();
        recyclerImage.setAdapter(imageAdapter);

        //目录列表
        recyclerDir.setLayoutManager(new LinearLayoutManager(this));
        dirAdapter = new FolderAdapter();
        recyclerDir.setAdapter(dirAdapter);

        //黑色图层
        viewLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideDirList();
            }
        });
    }

    boolean isDirShowing = false;

    private void toggleDirlistPop() {
        if (isDirShowing) {
            hideDirList();
        } else {
            showDirlist();
        }
    }

    private void hideDirList() {
        viewLayer.animate().alpha(0).setDuration(300).start();

        recyclerDir.animate().translationY(-dp2px(310)).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isDirShowing = false;
                viewLayer.setVisibility(View.GONE);
            }
        }).start();
    }

    private void showDirlist() {
        viewLayer.setVisibility(View.VISIBLE);
        viewLayer.animate().alpha(1).setDuration(300).start();
        recyclerDir.animate().translationY(0).setDuration(300).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isDirShowing = true;
            }
        }).start();
    }


    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        /**
         * 临时的辅助类，用于防止同一个文件夹的多次扫描
         */
        HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();

        Cursor mCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns.DATA}, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        Log.e("TAG", mCursor.getCount() + "");
        if (mCursor.moveToFirst()) {
            do {
                // 获取图片的路径
                String path = mCursor.getString(0);
                Log.e("TAG", path);
                imageAll.images.add(new ImageItem(path));
                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                Floder imageFloder = null;
                String dirPath = parentFile.getAbsolutePath();
                if (!tmpDir.containsKey(dirPath)) {
                    // 初始化imageFloder
                    imageFloder = new Floder();
                    imageFloder.setDir(dirPath);
                    imageFloder.setFirstImagePath(path);
                    mDirPaths.add(imageFloder);
                    Log.d("zyh", dirPath + "," + path);
                    tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
                } else {
                    imageFloder = mDirPaths.get(tmpDir.get(dirPath));
                }
                imageFloder.images.add(new ImageItem(path));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        for (int i = 0; i < mDirPaths.size(); i++) {
            Floder f = mDirPaths.get(i);
            Log.d("zyh", i + "-----" + f.getName() + "---" + f.images.size());
        }
        tmpDir = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                GridImageActivity.this.finish();
                break;
            case R.id.tv_ok:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("photos", selectedPicture);
                intent.putExtras(bundle);
                setResult(1003, intent);
                GridImageActivity.this.finish();
                break;
        }
    }

    class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ImageViewHolder holder, int position) {


            final ImageItem item = currentImageFolder.images.get(position);
            Glide.with(GridImageActivity.this).load("file://" + item.path).into(holder.imageView);
            //如果需要标记已经选择的图片得话，可以打开这段代码
//            if (selectedPicture.contains(item.path)) {
//                holder.tv_click.setChecked(true);
//            } else {
//                holder.tv_click.setChecked(false);
//            }
            holder.tv_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPicture.size() + PhotoFragment.selected.size() > maxNumber) {
                        Toast.makeText(GridImageActivity.this, "最多添加" + maxNumber + "张图", Toast.LENGTH_SHORT).show();
                        ((CheckBox) v).setChecked(false);
                    } else {
                        if (((CheckBox) v).isChecked()) {
                            selectPicture(item.path);
                        } else {
                            removePicture(item.path);
                        }
                    }
                }
            });
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.tv_click.isChecked()) {
                        holder.tv_click.setChecked(false);
                        removePicture(item.path);
                    } else {
                        if (selectedPicture.size() + PhotoFragment.selected.size() > maxNumber) {
                            Toast.makeText(GridImageActivity.this, "最多添加" + maxNumber + "张图", Toast.LENGTH_SHORT).show();
                        } else {
                            holder.tv_click.setChecked(true);
                            selectPicture(item.path);
                        }
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return currentImageFolder.images.size();
        }
    }


    class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public CheckBox tv_click;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv);
            tv_click = (CheckBox) itemView.findViewById(R.id.tv_click);
        }

    }

    /**
     * 点击选中
     *
     * @param picPath
     */
    void selectPicture(String picPath) {
        selectedPicture.add(picPath);
    }

    /**
     * 点击取消选中
     *
     * @param picPath
     */
    void removePicture(String picPath) {
        if (selectedPicture.contains(picPath)) {
            selectedPicture.remove(picPath);
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//            Intent intent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("photos", selectedPicture);
//            intent.putExtras(bundle);
//            setResult(1003, intent);
//            GridImageActivity.this.finish();
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    class FolderAdapter extends RecyclerView.Adapter<FolderViewHolder> {
        @Override
        public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dir, parent, false);
            return new FolderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FolderViewHolder holder, int position) {
            final Floder item = mDirPaths.get(position);
            Glide.with(GridImageActivity.this).load("file://" + item.getFirstImagePath()).into(holder.iv_dir);
            holder.tv_dirname.setText(item.name + " (" + item.images.size() + "张) ");
            holder.ll_root.setSelected(currentImageFolder == item);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetDirList(item);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDirPaths.size();
        }
    }

    private void resetDirList(Floder selectFolder) {
        currentImageFolder = selectFolder;
        dirAdapter.notifyDataSetChanged();
        imageAdapter.notifyDataSetChanged();
        hideDirList();
    }

    class FolderViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_dir;
        public TextView tv_dirname;
        public View ll_root;

        public FolderViewHolder(View itemView) {
            super(itemView);
            ll_root = itemView.findViewById(R.id.ll_root);
            iv_dir = (ImageView) itemView.findViewById(R.id.iv_dir);
            tv_dirname = (TextView) itemView.findViewById(R.id.tv_dirname);
        }
    }


    class Floder {


        /**
         * 图片的文件夹路径
         */
        private String dir;
        /**
         * 第一张图片的路径
         */
        private String firstImagePath;

        /**
         * 文件夹的名称
         */
        private String name;
        public List<ImageItem> images = new ArrayList<ImageItem>();

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
            int lastIndexOf = this.dir.lastIndexOf("/");
            this.name = this.dir.substring(lastIndexOf);
        }

        public String getFirstImagePath() {
            return firstImagePath;
        }

        public void setFirstImagePath(String firstImagePath) {
            this.firstImagePath = firstImagePath;
        }

        public String getName() {
            return name;
        }

    }

    class ImageItem {

        String path;

        public ImageItem(String p) {
            this.path = p;
        }

    }

    class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }


    }

    public int dp2px(int dp) {
        float scale = getResources().getDisplayMetrics().densityDpi;
        return (int) (dp * scale / 160 + 0.5f);
    }

    private void showToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }
}
