package com.shenjing.dimension.dimension.me;

import android.os.Bundle;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;

import butterknife.ButterKnife;

public class MyTaskActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_my_task);
        setStatusBarContainerVisibility(false);
        ButterKnife.bind(this);
    }
}
