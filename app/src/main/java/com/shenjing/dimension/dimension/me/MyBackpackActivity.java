package com.shenjing.dimension.dimension.me;

import android.os.Bundle;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.FundmentalActivity;

import butterknife.ButterKnife;

public class MyBackpackActivity extends FundmentalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_my_backpack);
        setStatusBarContainerVisibility(false);
        ButterKnife.bind(this);
    }
}
