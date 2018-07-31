package com.shenjing.dimension.dimension.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.device.DeviceInfo;

public class MenuDialog {
    
    public static Dialog createDialog(Context context, CharSequence title, String[] items, final OnMenuItemClickListener listener){
        return createDialog(context, title, items, -1, listener);
    }
    
    public static Dialog createDialog(Context context, CharSequence title, String[] items, int checkedPosition, final OnMenuItemClickListener listener){
        final Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_menu);
        android.view.WindowManager.LayoutParams windowLp = dialog.getWindow().getAttributes();
        windowLp.width = (int)(DeviceInfo.getScreenWidth(context) * 0.8);
        dialog.getWindow().setAttributes(windowLp);
//        if(title != null){
//            dialog.findViewById(R.id.titleLayout).setVisibility(View.VISIBLE);
//            ((TextView)dialog.findViewById(R.id.textTitle)).setText(title);
//        }else{
//        }
        dialog.findViewById(R.id.titleLayout).setVisibility(View.GONE);
        
        ListView listView = (ListView)dialog.findViewById(R.id.listView);
        listView.setAdapter(new MenuListAdapter(context, items, checkedPosition));
        listView.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                dialog.dismiss();
                if(listener != null){
                    listener.onMenuItemClick(position);
                }
            }
        });
        
        return dialog;
    }

    static class MenuListAdapter extends BaseAdapter {
        private Context context;
        private String[] items;
        private int checkedPosition;
        
        public MenuListAdapter(Context context, String[] items, int checkedPosition) {
            this.context = context;
            this.items = items;
            this.checkedPosition = checkedPosition;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_dialog_menu, parent, false);
                vh = new ViewHolder();
                vh.textMenu = (TextView) convertView.findViewById(R.id.textMenu);
                vh.imgCheck = (ImageView)convertView.findViewById(R.id.imgCheck);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            if(getCount() == 1){
                convertView.setBackgroundResource(R.drawable.selector_item_with_small_coeners);
            }else {
                if(position == 0){
                    convertView.setBackgroundResource(R.drawable.selector_dialog_corner_top);
                }else if(position == getCount() -1){
                    convertView.setBackgroundResource(R.drawable.selector_dialog_corner_bottom);
                }else{
                    convertView.setBackgroundResource(R.drawable.selector_dialog_btn_middle);
                }
            }
            vh.textMenu.setText(items[position]);
            vh.imgCheck.setVisibility((checkedPosition == position)? View.VISIBLE : View.GONE);
            
            return convertView;
        }
        
        private class ViewHolder {
            TextView textMenu;
            ImageView imgCheck;
        }
        
    }
    
    public interface OnMenuItemClickListener{
        public void onMenuItemClick(int position);
    }

    public static <T>Dialog createDialog2(Context context, CharSequence title, final T[] items, final OnMenuItemClickListener2<T> listener) {
        return  createDialog2(context,title,items,-1,listener);
    }
        public static <T>Dialog createDialog2(Context context, CharSequence title, final T[] items, int checkedPosition, final OnMenuItemClickListener2<T> listener){
        final Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_menu);
        android.view.WindowManager.LayoutParams windowLp = dialog.getWindow().getAttributes();
        windowLp.width = (int)(DeviceInfo.getScreenWidth(context) * 0.8);
        dialog.getWindow().setAttributes(windowLp);
//        if(title != null){
//            dialog.findViewById(R.id.titleLayout).setVisibility(View.VISIBLE);
//            ((TextView)dialog.findViewById(R.id.textTitle)).setText(title);
//        }else{
//        }
        dialog.findViewById(R.id.titleLayout).setVisibility(View.GONE);

        ListView listView = (ListView)dialog.findViewById(R.id.listView);
        listView.setAdapter(new CommonListAdapter<T>(context, items, checkedPosition));
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                dialog.dismiss();
                if(listener != null){
                    listener.onMenuItemClick(position,items[position]);
                }
            }
        });

        return dialog;
    }

    public interface OnMenuItemClickListener2<T>{
        public void onMenuItemClick(int position, T item);
    }

    static class CommonListAdapter<T> extends BaseAdapter {
        private Context context;
        private T[] items;
        private int checkedPosition;

        public CommonListAdapter(Context context, T[] items, int checkedPosition) {
            this.context = context;
            this.items = items;
            this.checkedPosition = checkedPosition;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_dialog_menu, null);
                vh = new ViewHolder();
                vh.textMenu = (TextView) convertView.findViewById(R.id.textMenu);
                vh.imgCheck = (ImageView)convertView.findViewById(R.id.imgCheck);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            if(getCount() == 1){
                convertView.setBackgroundResource(R.drawable.selector_item_with_small_coeners);
            }else {
                if(position == 0){
                    convertView.setBackgroundResource(R.drawable.selector_dialog_corner_top);
                }else if(position == getCount() -1){
                    convertView.setBackgroundResource(R.drawable.selector_dialog_corner_bottom);
                }else{
                    convertView.setBackgroundResource(R.drawable.selector_dialog_btn_middle);
                }
            }
            vh.textMenu.setText(items[position].toString());
            vh.imgCheck.setVisibility((checkedPosition == position)? View.VISIBLE : View.GONE);

            return convertView;
        }

        private class ViewHolder {
            TextView textMenu;
            ImageView imgCheck;
        }

    }


}
