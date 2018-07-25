package com.shenjing.dimension.dimension.me;

import android.os.Bundle;
import android.view.View;

import com.shenjing.dimension.dimension.base.activity.BaseActivity;

import butterknife.ButterKnife;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.util.ActivityUtil;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_setting);
        setTitleText("设置");
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {


        findViewById(R.id.view_self_message).setOnClickListener(this);
        findViewById(R.id.view_safe_secret).setOnClickListener(this);
        findViewById(R.id.view_about_dimension).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.view_self_message:  //个人信息
                ActivityUtil.gotoActivity(this, SelfMessageActivity.class);
                break;
            case R.id.view_safe_secret:  //安全与隐私
                ActivityUtil.gotoActivity(this, SafeAndSecretActivity.class);
                break;
            case R.id.view_about_dimension:  //关于次元局
                ActivityUtil.gotoActivity(this, AboutDimensionActivity.class);
                break;
        }
    }
}
