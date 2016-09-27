package com.wukong.xiaoxiao.photoselectdemo.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wukong.xiaoxiao.photoselectdemo.R;
import com.wukong.xiaoxiao.photoselectdemo.beans.PhotoModel;
import com.wukong.xiaoxiao.photoselectdemo.utils.Util;

import java.util.List;


/**
 * 
 * @ClassName: AppointmentsAdapter
 * @Description: TODO(发布商品的图片)
 * @author scene
 * @date 2015-4-17 上午11:36:50
 * 
 */
public class GdAdapter extends BaseAdapter {
	private Context mContext;
	private List<PhotoModel> mLists;

	public GdAdapter(Context mContext, List<PhotoModel> mLists) {
		this.mLists = mLists;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return mLists == null ? 0 : mLists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mLists == null ? null : mLists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View view, ViewGroup group) {
		Holder holder;
		if (view == null) {
			holder = new Holder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			view = inflater.inflate(
					R.layout.activity_slidingmenu_albums_item_item, null);
			holder.img = (ImageView) view.findViewById(R.id.img);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		PhotoModel info = mLists.get(position);

		if (info != null) {
			if (info.getOriginalPath().equals("default")) {
//				ImageLoader.getInstance().displayImage(
//						"drawable://" + R.mipmap.ic_add, holder.img);
				Glide.with(mContext)
						.load("drawable://" + R.mipmap.ic_add)
						.centerCrop()
						.thumbnail(0.1f)
						.placeholder(R.mipmap.ic_add)
						.error(R.mipmap.ic_add)
						.into(holder.img);
			} else {
//				ImageLoader.getInstance().displayImage(
//						"file://" + info.getOriginalPath(), holder.img);
				Glide.with(mContext)
						.load("file://" + info.getOriginalPath())
						.centerCrop()
						.thumbnail(0.1f)
						.placeholder(R.mipmap.ic_launcher)
						.error(R.mipmap.ic_launcher)
						.into(holder.img);
			}
			Util.setViewHeight2(
					holder.img,
					(Util.getScreen(mContext)[1] - Util.dip2px(mContext, 50)) / 4);

		}
		return view;
	}

	class Holder {
		ImageView img;
	}

}
