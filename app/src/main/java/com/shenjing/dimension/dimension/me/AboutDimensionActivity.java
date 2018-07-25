package com.shenjing.dimension.dimension.me;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;
import com.shenjing.dimension.dimension.base.util.ActivityUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutDimensionActivity extends BaseActivity {

    @Bind(R.id.tv_version_code)
    TextView mTvVersionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_about_dimension);
        setTitleText("关于我们");
        ButterKnife.bind(this);
        mTvVersionCode.setText(ActivityUtil.getVersionName());
    }
}
