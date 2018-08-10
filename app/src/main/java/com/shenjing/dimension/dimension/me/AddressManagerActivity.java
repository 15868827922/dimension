package com.shenjing.dimension.dimension.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;
import com.shenjing.dimension.dimension.base.util.ActivityUtil;
import com.shenjing.dimension.dimension.me.model.AddressInfo;
import com.shenjing.dimension.dimension.me.model.BackpackInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddressManagerActivity extends BaseActivity {


    @Bind(R.id.view_recycler_view)
    RecyclerView mRecyclerView;

    private List<AddressInfo> mList;
    private AddressListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_address_manager);
        ButterKnife.bind(this);
        setTitleText("地址管理");
        initView();
    }

    private void initView() {

        findViewById(R.id.view_add_address).setOnClickListener(this);

        mList = new ArrayList<>();
        mList.add(new AddressInfo());
        mList.add(new AddressInfo());
        mList.add(new AddressInfo());
        mList.add(new AddressInfo());
        mList.add(new AddressInfo());

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter=new AddressListAdapter(R.layout.item_address_manager,mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.view_add_address:
                ActivityUtil.gotoActivity(this, AddAddressActivity.class);
                break;
        }
    }
    public class AddressListAdapter extends BaseQuickAdapter<AddressInfo, BaseViewHolder> {
        public AddressListAdapter(int layoutResId, List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, AddressInfo item) {
          /*  helper.setText(R.id.text, item.getTitle());
            helper.setImageResource(R.id.icon, item.getImageResource());
            // 加载网络图片
            Glide.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) helper.getView(R.id.iv));*/


        }
    }

}
