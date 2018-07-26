package com.shenjing.dimension.dimension.me;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;

import butterknife.ButterKnife;

public class MyWalletActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_my_wallet);
        ButterKnife.bind(this);
        setTitleText("我的钱包");
        showRightTextBtn("收支明细");
        setRightTextButtonClickListener(this);
        initView();
    }

    private void initView() {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case ID_RIGHT_TEXT:
                break;
        }
    }
}
