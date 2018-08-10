package com.shenjing.dimension.dimension.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.device.DeviceInfo;

import java.util.ArrayList;

public class LPDialogFactory {
    public final static int BTN_LEFT = 0;
    public final static int BTN_RIGHT = 1;

    public static class Builder{
        private Context context;
        private CharSequence title;
        private CharSequence content;
        private View contentView;
        private CharSequence leftText;
        private CharSequence rightText = "确定";
        private OnDialogButtonClickListener listener;
//        private double widthScale = 0.773; //占比为 (750 - 85 * 2) / 750 = 0.773
        private double widthScale = 0.8;
        private int backgroundColor;
        private boolean isFloating = true;
        private boolean cancelable = true;
        private boolean canceledOnTouchOutside = true;
        private int titleMaxLines = 2;

        private Builder(){}

        public Builder(Context context){
            this.context = context;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setContent(CharSequence content) {
            this.content = content;
            return this;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setFloating(boolean floating) {
            isFloating = floating;
            return this;
        }

        public Builder setLeftText(CharSequence leftText) {
            this.leftText = leftText;
            return this;
        }

        public Builder setListener(OnDialogButtonClickListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setRightText(CharSequence rightText) {
            this.rightText = rightText;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setWidthScale(double widthScale) {
            this.widthScale = widthScale;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder setTitleMaxLines(int titleMaxLines) {
            this.titleMaxLines = titleMaxLines < 0 ? 0 : titleMaxLines;
            return this;
        }

        public Dialog build(){
            final Dialog dialog = new Dialog(context, isFloating? R.style.CustomDialog : R.style.CustomDialogNotFloating);
            dialog.setContentView(R.layout.dialog_lp);
            android.view.WindowManager.LayoutParams windowLp = dialog.getWindow().getAttributes();
            windowLp.width = (int)(DeviceInfo.getScreenWidth(context) * widthScale);
            dialog.getWindow().setAttributes(windowLp);
            if(title != null){
                TextView titleText = (TextView)dialog.findViewById(R.id.textTitle);
                titleText.setMaxLines(titleMaxLines);
                titleText.setText(title);
            }else{
                dialog.findViewById(R.id.textTitle).setVisibility(View.GONE);
                dialog.findViewById(R.id.conetentSpace).setVisibility(View.VISIBLE);
            }
            if(content != null){
                ((TextView)dialog.findViewById(R.id.textContent)).setText(content);
            }else if(contentView != null){
                dialog.findViewById(R.id.textContent).setVisibility(View.GONE);
                ((LinearLayout)dialog.findViewById(R.id.contentLayout)).addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            }

            Button btnLeft = (Button)dialog.findViewById(R.id.btnLeft);
            Button btnRight = (Button)dialog.findViewById(R.id.btnRight);
            if(leftText != null){
                btnLeft.setText(leftText);
            }
            if(rightText != null){
                btnRight.setText(rightText);
            }
            if(TextUtils.isEmpty(leftText) && TextUtils.isEmpty(rightText)){
                dialog.findViewById(R.id.btnLayout).setVisibility(View.GONE);
            }
            if(backgroundColor !=0){
                View dialog_lp = dialog.findViewById(R.id.dialpg_lp);
                dialog_lp.setBackgroundResource(backgroundColor);
            }
            btnLeft.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onDialogButtonClick(dialog, BTN_LEFT);
                    }else{
                        dialog.dismiss();
                    }
                }
            });
            btnRight.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onDialogButtonClick(dialog, BTN_RIGHT);
                    }else{
                        dialog.dismiss();
                    }
                }
            });

            //默认布局为两个按钮，一个按钮时
            if(leftText == null){
                btnLeft.setVisibility(View.GONE);
            }
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);

            return dialog;
        }
    }



   /* public static Dialog showSingleSelectedDialog(final Activity activity, CharSequence title, ArrayList<String> items, int checkedPosition, final OnLpDiaogListItemClickListener listener){
        View contentView = LayoutInflater.from(activity).inflate(R.layout.view_dialog_list_single_select, null);
        Builder builder = new Builder(activity);
        builder.setTitle(title).setRightText("").setContentView(contentView);
        final Dialog dialog = builder.build();
        LimitHeightListView itemList = (LimitHeightListView) contentView.findViewById(R.id.list_items);
        itemList.setMaxHeight(activity.getResources().getDimensionPixelSize(R.dimen.dp_750_510));
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if(listener != null){
                    listener.onLpDiaogListItemClick(position);
                }
            }
        });
        itemList.setAdapter(new ItemListAdapter(activity, items, checkedPosition));
        if(0<checkedPosition && checkedPosition < items.size()-1){
            itemList.setSelection(checkedPosition);
        }


        dialog.show();
        return dialog;
    }
*/
    public interface OnLpDiaogListItemClickListener{
        void onLpDiaogListItemClick(int position);
    }

  /*  private static class ItemListAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> items;
        private int checkedPosition;

        public ItemListAdapter(Context context, ArrayList<String> items, int checkedPosition) {
            this.context = context;
            this.items = items;
            this.checkedPosition = checkedPosition;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_lpdialog_list, parent, false);
                vh = new ViewHolder();
                vh.textMenu = (TextView) convertView.findViewById(R.id.textMenu);
                vh.imgCheck = (ImageView) convertView.findViewById(R.id.imgCheck);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            vh.textMenu.setText(items.get(position));
            vh.imgCheck.setVisibility((checkedPosition == position) ? View.VISIBLE : View.GONE);

            return convertView;
        }
        private class ViewHolder {
            TextView textMenu;
            ImageView imgCheck;
        }
    }*/

    public interface OnInputTextFinishListener{
        public void onInputTextFinish(Dialog dialog, String text);
    }
    
    public interface OnDialogButtonClickListener{
        public void onDialogButtonClick(Dialog dialog, int which);
    }

    // --------------------------------------------------------------------------- //
    public static Dialog buildBackDialog(Context context, OnDialogButtonClickListener onDialogButtonClickListener) {
        LPDialogFactory.Builder builder = new LPDialogFactory.Builder(context)
                .setContent("退出此次编辑？")
                .setLeftText("取消")
                .setRightText("退出")
                .setListener(onDialogButtonClickListener);
        return builder.build();
    }

    public static ActionSheetDialog buildChooseImageDialog(Context context, ActionSheetDialog.ActionSheetListener actionSheetListener) {
        ActionSheetDialog.Builder builder = new ActionSheetDialog.Builder(context, ActionSheetDialog.STYLE_LIST_VIEW);
        builder.setItems(new String[]{context.getString(R.string.take_photo), context.getString(R.string.album)})
                .setPositiveButton(R.string.btn_cancel)
                .setActionSheetListener(actionSheetListener);
        return builder.build();
    }

    public static Dialog buildGroupMemberRemovedDialog(Context context, OnDialogButtonClickListener onDialogButtonClickListener) {
            LPDialogFactory.Builder builder = new LPDialogFactory.Builder(context)
                    .setContent("确定将该用户删除吗？")
                    .setLeftText("取消")
                    .setRightText("确定")
                    .setListener(onDialogButtonClickListener);
            return builder.build();
        }

    public static Dialog buildGroupCancelManager(Context context, OnDialogButtonClickListener onDialogButtonClickListener) {
        LPDialogFactory.Builder builder = new LPDialogFactory.Builder(context)
                .setTitle("取消管理员")
                .setContent("确定取消该成员管理员权限吗？")
                .setLeftText("取消")
                .setRightText("确定")
                .setListener(onDialogButtonClickListener);
        return builder.build();
    }
}
