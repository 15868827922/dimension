package com.shenjing.dimension.dimension.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.shenjing.dimension.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tiny on 2016/5/19.
 */
public class ActionSheetDialog implements AdapterView.OnItemClickListener,View.OnClickListener {


    private Context mContext;
    private BottomSheetDialog dialog;
    private View mContentView;
    private TextView mTitleView;
    private Button mPositiveButton;
    private AbsListView mAdapterView;
    private BaseAdapter mAdapter;
    private int style;
    private View layoutFixedTitle;
    private TextView tvFixedTitle;

    public static final int STYLE_LIST_VIEW = 1;
    public static final int STYLE_GRID_VIEW = 2;


    ActionSheetListener mActionSheetListener;


    public ActionSheetDialog(Context context, int style) {
        this.mContext = context;
        dialog = new BottomSheetDialog(context);
        mContentView = View.inflate(context, R.layout.bottom_action_sheet_list, null);
        mTitleView = (TextView) mContentView.findViewById(R.id.bottom_sheet_title);
        layoutFixedTitle = mContentView.findViewById(R.id.view_fixed_height_title);
        tvFixedTitle = (TextView) mContentView.findViewById(R.id.text_fixed_height_title);
        ViewStub stub= (ViewStub) mContentView.findViewById(R.id.bottom_sheet_stub);
        this.style = style;
        switch (style) {
            case STYLE_LIST_VIEW:
                stub.setLayoutResource(R.layout.bottom_sheet_list_view);
                break;
            case STYLE_GRID_VIEW:
                stub.setLayoutResource(R.layout.bottom_sheet_grid_view);
                break;

        }
        mAdapterView= (AbsListView) stub.inflate();
        if(mAdapterView instanceof GridView){
            ((GridView) mAdapterView).setNumColumns(4);
        }
        mAdapterView.setOnItemClickListener(this);
        mPositiveButton = (Button) mContentView.findViewById(R.id.bottom_sheet_positive);
        mPositiveButton.setOnClickListener(this);
        dialog.setContentView(mContentView);
    }

    public void setGridColumnCount(int column) {
        if (style == STYLE_GRID_VIEW) {
            ((GridView)mAdapterView).setNumColumns(column);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mActionSheetListener!=null){
            mActionSheetListener.onActionClick(position);
        }
        dialog.dismiss();
    }

    public void show() {
        dialog.show();
    }

    public void setTitle(String title) {
        mTitleView.setText(title);
        mTitleView.setVisibility(!TextUtils.isEmpty(title) ? View.VISIBLE : View.GONE);
    }

    public void setTitle(int title) {
        mTitleView.setText(title);
        mTitleView.setVisibility(title != 0 ? View.VISIBLE : View.GONE);
    }

    public void setFixedHeightTitle(String title){
        if(TextUtils.isEmpty(title)){
            return;
        }
        layoutFixedTitle.setVisibility(View.VISIBLE);
        tvFixedTitle.setText(title);
    }

    public void setActionListAdapter(BaseAdapter adapter) {
        this.mAdapter = adapter;
        this.mAdapterView.setAdapter(mAdapter);
    }

    public void setPositiveButton(String text) {
        mPositiveButton.setText(text);

    }

    public void setPositiveButton(int text) {
        mPositiveButton.setText(text);
    }

    public void setActionSheetListener(ActionSheetListener listener) {
        this.mActionSheetListener = listener;
    }

    @Override
    public void onClick(View v) {
        dialog.dismiss();
        if (mActionSheetListener != null) {
            mActionSheetListener.onPositiveButtonClick();
        }
    }

    public interface ActionSheetListener {
        void onActionClick(int position);

        void onPositiveButtonClick();
    }

    public static class Builder {

        Context mContext;
        int style = STYLE_LIST_VIEW;
        BaseAdapter mAdapter;
        String mTitle;
        String mPositiveButton;
        List mItems;
        ActionSheetListener listener;
        int columnCount;
        HashMap<Integer,Integer> mSetColorList; //用于字体的颜色值的修改


        public Builder(Context context, int style) {
            this.mContext = context;
            this.style = style;
        }

        public <T> Builder setItems(ArrayList<T> items) {
            this.mItems = items;
            return this;
        }
        public <T> Builder setItems(T[] items) {
            this.mItems = Arrays.asList(items);
            return this;
        }

        public <T> Builder setTextColorItems(HashMap<Integer,Integer> items){
            this.mSetColorList = items;
            return this;
        }

        public Builder setAdapter(BaseAdapter adapter) {
            this.mAdapter = adapter;
            return this;

        }

        public Builder setActionSheetListener(ActionSheetListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setPositiveButton(String text) {
            this.mPositiveButton = text;
            return this;
        }
        public Builder setPositiveButton(int text) {
            this.mPositiveButton = mContext.getString(text);
            return this;
        }
        public Builder setTitle(String text) {
            this.mTitle = text;
            return this;
        }

        public Builder setTitle(int res) {
            this.mTitle = mContext.getString(res);
            return this;
        }
        public Builder setColumnCount(int count){
            this.columnCount=count;
            return this;
        }

        public ActionSheetDialog build() {
            ActionSheetDialog dialog = new ActionSheetDialog(mContext, style);
            dialog.setTitle(mTitle);
            dialog.setPositiveButton(mPositiveButton);
            if (mItems != null && mAdapter == null) {
                mAdapter = new SimpleListAdapter(mContext,mItems,mSetColorList);
            }
            dialog.setActionListAdapter(mAdapter);
            dialog.setActionSheetListener(listener);
            dialog.setGridColumnCount(columnCount);
            return dialog;
        }

    }

    private static class SimpleListAdapter extends BaseAdapter {
        List mItems;
        Context mContext;
        HashMap<Integer,Integer> mSetColorList;

        public SimpleListAdapter(Context context, List mItems, HashMap<Integer,Integer> mSetColorList) {
            this.mItems = mItems;
            this.mSetColorList = mSetColorList;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mItems.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView mTextView = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet_item, parent, false);
            }
            mTextView= (TextView) convertView.findViewById(R.id.bottom_sheet_text_item);
            if (mSetColorList != null && mSetColorList.containsKey(position)){
                int colorRes = mSetColorList.get(position);
                if (colorRes != 0){
                    mTextView.setTextColor(colorRes);
                }else {
                    mTextView.setTextColor(mContext.getResources().getColor(R.color.unit_text_tv_title_1));
                }
            }else {
                mTextView.setTextColor(mContext.getResources().getColor(R.color.unit_text_tv_title_1));
            }
            mTextView.setText(mItems.get(position).toString());
            return convertView;
        }
    }
}
