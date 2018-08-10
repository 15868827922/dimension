package com.shenjing.dimension.dimension.base.select_picture;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.bumptech.glide.Glide;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.device.DeviceInfo;

import java.util.ArrayList;
import java.util.List;

public class MultiImageSelectorAdapter extends BaseAdapter {
    
    ArrayList<String> resultList;
    LayoutInflater mInflater;
    Context mContext;
    SparseBooleanArray mSparseBooleanArray;
    int itemWid;
    CheckBoxViewChangeListener listener;
    private List<Image> mImages = new ArrayList<Image>();
    private List<Image> mSelectedImages = new ArrayList<Image>();
    int clickViewWidth;
    int mode;
    
    public MultiImageSelectorAdapter(Context context, ArrayList<String> resultList, int itemWid, int mode, CheckBoxViewChangeListener listener) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mSparseBooleanArray = new SparseBooleanArray();
        resultList = new ArrayList<String>();
        this.resultList = resultList;
        this.itemWid = itemWid;
        this.listener = listener;
        clickViewWidth = (DeviceInfo.getScreenWidth(mContext) - context.getResources().getDimensionPixelSize(R.dimen.dp_750_40)) / 8;
        this.mode = mode;
    }
    
    /**
     * 选择某个图片，改变选择状态
     * @param image
     */
    public void select(Image image) {
        if(mSelectedImages.contains(image)){
            mSelectedImages.remove(image);
        }else{
            mSelectedImages.add(image);
        }
        notifyDataSetChanged();
    }
    
    /**
     * 设置数据集
     * @param images
     */
    public void setData(List<Image> images) {
        mSelectedImages.clear();

        if(images != null && images.size()>0){
            mImages = images;
        }else{
            mImages.clear();
        }
        notifyDataSetChanged();
    }

    public ArrayList<String> getCheckedItems() {
        ArrayList<String> mTempArry = new ArrayList<String>();

        for(int i=0;i<resultList.size();i++) {
            if(mSparseBooleanArray.get(i)) {
                mTempArry.add(resultList.get(i));
            }
        }

        return mTempArry;
    }
    
    /**
     * 通过图片路径设置默认选择
     * @param resultList
     */
    public void setDefaultSelected(ArrayList<String> resultList){
        setDefaultSelected(resultList, true);
    }
    public void setDefaultSelected(ArrayList<String> resultList, boolean forceClean){
        if(forceClean){
            mSelectedImages.clear();
        }
        for(String path : resultList){
            Image image = getImageByPath(path);
            if(image != null && !mSelectedImages.contains(image)){
                mSelectedImages.add(image);
            }
        }
        if(forceClean || mSelectedImages.size() > 0){
            notifyDataSetChanged();
        }
    }
    
    private Image getImageByPath(String path){
        if(mImages != null && mImages.size()>0){
            for(Image image : mImages){
                if(image.path.equalsIgnoreCase(path)){
                    return image;
                }
            }
        }
        return null;
    }
    
    public int getCheckedSize(){
        int count = 0;
        for(int i=0;i<resultList.size();i++) {
            if(mSparseBooleanArray.get(i)) {
                count++;
            }
        }
        return count;
    }
    
    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Object getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.new_multiphoto_item, parent, false);
            convertView.setLayoutParams(new AbsListView.LayoutParams(itemWid, itemWid));
            vh = new ViewHolder();
            vh.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
//            vh.imageView.setDontLoadSameUrl(true);
            vh.checkBoxView = (ImageView) convertView.findViewById(R.id.checkBox1);
            vh.clickView = convertView.findViewById(R.id.view_click);
            LayoutParams params = (LayoutParams)vh.clickView.getLayoutParams();
            params.width = clickViewWidth;
            params.height = clickViewWidth;
            vh.clickView.setLayoutParams(params);
            vh.clickView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onCheckBoxChange((Integer)v.getTag());
                    }
                }
            });
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        vh.checkBoxView.setVisibility(mode == MultiImageSelectorFragment.MODE_SINGLE ? View.GONE : View.VISIBLE);
        vh.clickView.setVisibility(mode == MultiImageSelectorFragment.MODE_SINGLE ? View.GONE : View.VISIBLE);

        vh.clickView.setTag(position);
        String uri = "file://"+mImages.get(position).path;
        LayoutParams params = (LayoutParams) vh.imageView.getLayoutParams();
        params.width = itemWid;
        params.height = itemWid;
        vh.imageView.setLayoutParams(params);
//        vh.imageView.setImageUrl(uri);
        Glide.with(parent.getContext()).load(uri).asBitmap().placeholder(R.mipmap.bg_image_loading).into( vh.imageView);
        vh.checkBoxView.setImageResource(mSelectedImages.contains(mImages.get(position)) ? R.mipmap.icon_check_goods_select : R.mipmap.icon_check_goods_default);

        return convertView;
    }
    
    private class ViewHolder{
        ImageView imageView;
        ImageView checkBoxView;
        View clickView;
    }
    
    
    
    public interface CheckBoxViewChangeListener{
        public void onCheckBoxChange(int position);
    }
}