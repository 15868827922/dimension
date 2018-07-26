package com.shenjing.dimension.dimension.main.fragent;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.fragment.FragmentBase;
import com.shenjing.dimension.dimension.base.image.LPNetworkRoundedImageView;
import com.shenjing.dimension.dimension.base.util.ActivityUtil;
import com.shenjing.dimension.dimension.me.MyWalletActivity;
import com.shenjing.dimension.dimension.me.SettingActivity;
import com.shenjing.dimension.dimension.me.view.SignDialog;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyFragment extends FragmentBase implements View.OnClickListener{

    public static final String TAG="MyFragment";

    @Bind(R.id.img_user_header)
    LPNetworkRoundedImageView mImgUserHeader; //用户头像

    @Bind(R.id.tv_user_name)
    TextView mTvUserName;  //用户名字

    @Bind(R.id.tv_id)
    TextView mTvId;  //用户id
    @Override
    public int getContentLayout() {
        return R.layout.fragment_my;
    }

    @Override
    public void initView(@Nullable View view) {
        ButterKnife.bind(this,view);

        findViewById(R.id.img_is_sign).setOnClickListener(this);
        findViewById(R.id.view_my_task).setOnClickListener(this);
        findViewById(R.id.view_my_wallet).setOnClickListener(this);
        findViewById(R.id.view_my_backpack).setOnClickListener(this);
        findViewById(R.id.view_score_mall).setOnClickListener(this);

        findViewById(R.id.view_my_message).setOnClickListener(this);
        findViewById(R.id.view_my_order).setOnClickListener(this);
        findViewById(R.id.view_address_manager).setOnClickListener(this);
        findViewById(R.id.view_my_props).setOnClickListener(this);
        findViewById(R.id.view_give_mark).setOnClickListener(this);
        findViewById(R.id.view_give_feedback).setOnClickListener(this);
        findViewById(R.id.img_set_me).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_set_me:  //设置
                ActivityUtil.gotoActivity(getActivity(), SettingActivity.class);
                break;
            case R.id.view_my_task:  //我的任务
                break;
            case R.id.view_my_wallet:  //我的钱包
                ActivityUtil.gotoActivity(getActivity(), MyWalletActivity.class);
                break;
            case R.id.view_my_backpack:  //我的背包
                break;
            case R.id.view_score_mall:  //我的任务
                break;
            case R.id.view_my_message:  //我的消息
                break;
            case R.id.view_my_order:  //我的订单
                break;
            case R.id.view_address_manager:  //地址管理
                break;

            case R.id.view_my_props:  //我的道具
                break;
            case R.id.view_give_mark:  //评分
                break;
            case R.id.view_give_feedback:  //意见反馈
                break;
            case R.id.img_is_sign:  //签到
                showSignDialog();
                break;
        }

    }

    SignDialog signDialog;
    private void showSignDialog(){
        signDialog = new SignDialog(getActivity());
        signDialog.show();
    }
}
