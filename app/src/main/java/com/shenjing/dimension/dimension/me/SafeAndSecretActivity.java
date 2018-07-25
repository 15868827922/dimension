package com.shenjing.dimension.dimension.me;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;
import com.shenjing.dimension.dimension.base.util.ActivityUtil;
import com.shenjing.dimension.dimension.base.util.string.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SafeAndSecretActivity extends BaseActivity {



    @Bind(R.id.tv_phone_tip)
    TextView mTvPhoneTip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_safe_and_secret);
        setTitleText("安全与隐私");

        ButterKnife.bind(this);

        mTvPhoneTip.setText("您当前绑定手机号：" + StringUtils.getPhoneNumHiddenText("15868827922"));
        findViewById(R.id.view_change_phone).setOnClickListener(this);
        findViewById(R.id.view_change_password).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.view_change_phone:  //更改手机号
                ActivityUtil.gotoActivity(this, ChangePhoneActivity.class);
                break;
            case R.id.view_change_password:  //更改密码
                ActivityUtil.gotoActivity(this, ChangePassWordActivity.class);
                break;
        }
    }
}
