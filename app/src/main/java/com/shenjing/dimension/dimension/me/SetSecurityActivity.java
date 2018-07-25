package com.shenjing.dimension.dimension.me;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;
import com.shenjing.dimension.dimension.view.LimitHeightListView;
import com.shenjing.dimension.dimension.view.spiner.SpinerAdapter;
import com.shenjing.dimension.dimension.view.spiner.SpinerPopWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SetSecurityActivity extends BaseActivity {

    @Bind(R.id.view_select_request_1)
    RelativeLayout mViewRequest1;


    @Bind(R.id.tv_set_request_1)
    TextView mTvRequest1;


    private List<String> mListType = new ArrayList<String>();  //类型列表
    private TextView mTView;
    private SpinerAdapter mAdapter;
    private SpinerPopWindow mSpinerPopWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_set_security);
        ButterKnife.bind(this);
        mViewRequest1.setOnClickListener(this);
        initView();
    }

    private void initView() {


        //初始化数据
        mListType.add("大家好！");
        mListType.add("老司机开车啦");
        mListType.add("快上车");
        mListType.add("快下班了");
        mListType.add("下班回家做好吃的");
        mListType.add("啦啦啦啦···");

        mAdapter = new SpinerAdapter(this, mListType);
        mAdapter.refreshData(mListType, 0);


        //初始化PopWindow
        mSpinerPopWindow = new SpinerPopWindow(this);
        mSpinerPopWindow.setAdatper(mAdapter);
        mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
            @Override
            public void onItemClick(int pos) {
                mTvRequest1.setText(mListType.get(pos));
            }
        });

        mSpinerPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.view_select_request_1:
//                showRightOrderMenu();
                showSpinWindow();
                break;
        }
    }


    //设置PopWindow
    private void showSpinWindow() {
        //设置mSpinerPopWindow显示的宽度
        mSpinerPopWindow.setWidth(mViewRequest1.getWidth());
        //设置显示的位置在哪个控件的下方
        mSpinerPopWindow.showAsDropDown(mViewRequest1);
    }

    //点击右边按钮时弹出选择框
    private void showRightOrderMenu() {
        View popupView = LayoutInflater.from(SetSecurityActivity.this).inflate(R.layout.view_popup_select_shop, null);
        LimitHeightListView popupListView = (LimitHeightListView) popupView.findViewById(R.id.listView);
        final PopupWindow popupWindow = new PopupWindow(popupView);
        popupListView.setMaxHeight((int) (popupWindow.getMaxAvailableHeight(mViewRequest1) * 0.9));

        popupListView.setAdapter(new OrderAdapter());
        popupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();

                /*switch (position) {
                    case 0:
                        orderBy = 4;
                        customFilter.setRightText("默认最新");
                        break;
                    case 1:
                        orderBy = 0;
                        customFilter.setRightText("成交订单");
                        break;
                    case 2:
                        orderBy = 2;
                        customFilter.setRightText("交易金额");
                        break;
                }*/

//                customerListView.disableLoadMore();
//                request(0, ONE_PAGE_LENGTH, orderBy, true, true);
            }
        });

        popupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
//                customFilter.updateTitleState();
            }
        });
        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(mViewRequest1, 0, (int)getResources().getDimension(R.dimen.dp_750_300));
    }

    class OrderAdapter extends BaseAdapter {

        String[] strs = {"默认最新", "成交订单", "交易金额"};
        String[] infos = {"", "从多到少", "从高到低"};

        @Override
        public int getCount() {
            return strs.length;
        }

        @Override
        public String getItem(int arg0) {
            return strs[arg0];
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(SetSecurityActivity.this).inflate(R.layout.item_customer_manager, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textOrderName = (TextView) convertView.findViewById(R.id.textShopName);
              /*  viewHolder.imgSelect = convertView.findViewById(R.id.imgSelect);
                viewHolder.textInfo = (TextView) convertView.findViewById(R.id.textShopInfo);*/
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textOrderName.setText(strs[position]);
//            viewHolder.textInfo.setText(infos[position]);

            int count = 0;
           /* if (orderBy == 4) {
                count = 0;
            } else if (orderBy == 0) {
                count = 1;
            } else {
                count = 2;
            }*/

//            viewHolder.imgSelect.setVisibility(count == position ? View.VISIBLE : View.GONE);

            return convertView;
        }

        class ViewHolder {
            TextView textOrderName;
           /* TextView textInfo;
            View imgSelect;*/
        }

    }
}
