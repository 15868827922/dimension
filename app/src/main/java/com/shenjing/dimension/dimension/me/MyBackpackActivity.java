package com.shenjing.dimension.dimension.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.FundmentalActivity;
import com.shenjing.dimension.dimension.base.util.ActivityUtil;
import com.shenjing.dimension.dimension.me.model.BackpackInfo;
import com.shenjing.dimension.dimension.me.view.ConvertScoreTipDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyBackpackActivity extends FundmentalActivity implements View.OnClickListener{

    @Bind(R.id.view_recycler_view)
    RecyclerView mRecyclerView;

    private List<BackpackInfo> mList;
    private BackpackAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_my_backpack);
        setStatusBarContainerVisibility(false);
        ButterKnife.bind(this);

        initView();


    }

    private void initView() {
        findViewById(R.id.img_back_for_backpack).setOnClickListener(this);
        findViewById(R.id.view_convert_score).setOnClickListener(this);
        findViewById(R.id.view_score_mall).setOnClickListener(this);
        findViewById(R.id.view_apply_send).setOnClickListener(this);

        mList = new ArrayList<>();
        mList.add(new BackpackInfo());
        mList.add(new BackpackInfo());
        mList.add(new BackpackInfo());
        mList.add(new BackpackInfo());
        mList.add(new BackpackInfo());
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter=new BackpackAdapter(R.layout.item_my_backpack,mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back_for_backpack:
                finish();
                break;
            case R.id.view_convert_score: //兑换积分
                showDialog();
                break;
            case R.id.view_score_mall: //积分商城
                break;
            case R.id.view_apply_send: //申请发货
                ActivityUtil.gotoActivity(this,ApplySendActivity.class);
                break;
        }

    }

    private void showDialog(){
        ConvertScoreTipDialog tipDialog = new ConvertScoreTipDialog(this);
        tipDialog.show();
    }


    public class BackpackAdapter extends BaseQuickAdapter<BackpackInfo, BaseViewHolder> {
        public BackpackAdapter(int layoutResId, List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, BackpackInfo item) {
          /*  helper.setText(R.id.text, item.getTitle());
            helper.setImageResource(R.id.icon, item.getImageResource());
            // 加载网络图片
            Glide.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) helper.getView(R.id.iv));*/


          helper.setOnClickListener(R.id.view_select_goods, new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if (view.isSelected()){
                      view.setSelected(false);
                  }else {
                      view.setSelected(true);
                  }
              }


          });
        }
    }
}
