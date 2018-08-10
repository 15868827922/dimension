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
import com.shenjing.dimension.dimension.me.model.BackpackInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ApplySendActivity extends BaseActivity {

    @Bind(R.id.view_recycler_view)
    RecyclerView mRecyclerView;

    private List<BackpackInfo> mList;
    private SendListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_apply_send);
        setTitleText("申请发货");
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mList = new ArrayList<>();
        mList.add(new BackpackInfo());
        mList.add(new BackpackInfo());
        mList.add(new BackpackInfo());
        mList.add(new BackpackInfo());
        mList.add(new BackpackInfo());

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter=new SendListAdapter(R.layout.item_my_goods_for_send,mList);
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.view_chose_address).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.view_chose_address:
                ActivityUtil.gotoActivity(this, AddressManagerActivity.class);
                break;
        }
    }

    public class SendListAdapter extends BaseQuickAdapter<BackpackInfo, BaseViewHolder> {
        public SendListAdapter(int layoutResId, List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, BackpackInfo item) {
          /*  helper.setText(R.id.text, item.getTitle());
            helper.setImageResource(R.id.icon, item.getImageResource());
            // 加载网络图片
            Glide.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) helper.getView(R.id.iv));*/


        }
    }
}
