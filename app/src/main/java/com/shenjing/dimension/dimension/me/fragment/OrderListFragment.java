package com.shenjing.dimension.dimension.me.fragment;

import android.support.annotation.Nullable;
import android.view.View;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.fragment.FragmentBase;

public class OrderListFragment extends FragmentBase {

    public static final String TAG_ALL="OrderListFragmentAll";
    public static final String TAG_RECHARGE="OrderListFragmentRecharge";
    public static final String TAG_PAY="OrderListFragmentPay";
    public static final String TAG_RECORD="OrderListFragmentRecord";
    @Override
    public int getContentLayout() {
        return R.layout.fragment_rest;
    }

    @Override
    public void initView(@Nullable View view) {

    }

    @Override
    public void initData() {

    }
}
