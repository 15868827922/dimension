package com.shenjing.dimension.dimension.me;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;

import butterknife.ButterKnife;

public class MyBackpackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_my_backpack);
        setStatusBarContainerVisibility(false);
        ButterKnife.bind(this);
    }
}
