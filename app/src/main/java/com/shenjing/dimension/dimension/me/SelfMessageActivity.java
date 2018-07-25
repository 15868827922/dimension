package com.shenjing.dimension.dimension.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;
import com.shenjing.dimension.dimension.base.util.ActivityUtil;
import com.shenjing.dimension.dimension.base.util.Constants;
import com.shenjing.dimension.dimension.view.LPItemArrowRightView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelfMessageActivity extends BaseActivity {



    @Bind(R.id.view_user_id)
    LPItemArrowRightView mViewUserId;

    @Bind(R.id.view_user_nickname)
    LPItemArrowRightView mViewUserNickName;

    @Bind(R.id.view_user_sign)
    LPItemArrowRightView mViewUserSign;

    @Bind(R.id.view_user_sex)
    LPItemArrowRightView mViewUserSex;

    @Bind(R.id.view_user_birthday)
    LPItemArrowRightView mViewUserBirthday;

    @Bind(R.id.view_user_constellation)
    LPItemArrowRightView mViewUserConstellation;

    @Bind(R.id.view_user_location)
    LPItemArrowRightView mViewUserLocation;

    @Bind(R.id.view_user_industry)
    LPItemArrowRightView mViewUserIndustry;

    @Bind(R.id.view_address_manager)
    LPItemArrowRightView mViewAddressManager;

    @Bind(R.id.view_set_security)
    View mViewSetSecurity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_self_message);
        setTitleText("个人信息");
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mViewUserNickName.setOnClickListener(this);
        mViewUserSign.setOnClickListener(this);
        mViewUserSex.setOnClickListener(this);
        mViewUserBirthday.setOnClickListener(this);
        mViewUserConstellation.setOnClickListener(this);
        mViewUserLocation.setOnClickListener(this);
        mViewUserIndustry.setOnClickListener(this);
        mViewAddressManager.setOnClickListener(this);
        mViewSetSecurity.setOnClickListener(this);

        mViewUserId.setArrowVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.view_user_nickname:  //我的昵称
                ModifyPersonalInfoActivity.gotoActivity(this,ModifyPersonalInfoActivity.INFO_TYPE_NICKNAME, Constants.REQ_CODE_SET_NICKNAME);
                break;
            case R.id.view_user_sign:  //我的签名
                ModifyPersonalInfoActivity.gotoActivity(this,ModifyPersonalInfoActivity.INFO_TYPE_SIGN, Constants.REQ_CODE_SET_SIGN);
                break;
            case R.id.view_user_sex:  //性别
                break;
            case R.id.view_user_birthday:  //生日
                break;
            case R.id.view_user_constellation: //星座
                break;
            case R.id.view_user_location: //所在地
                break;
            case R.id.view_user_industry: //行业
                break;
            case R.id.view_address_manager: //地址管理
                break;
            case R.id.view_set_security: //密保问题
                ActivityUtil.gotoActivity(this,SetSecurityActivity.class);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case Constants.REQ_CODE_SET_NICKNAME:
                    String nickname = data.getStringExtra(Constants.EXTRA_GET_NICKNAME);
                    mViewUserNickName.setSecTitleText(nickname);
                    break;
                case Constants.REQ_CODE_SET_SIGN:
                    String sign = data.getStringExtra(Constants.EXTRA_GET_SIGN);
                    mViewUserSign.setSecTitleText(sign);
                        break;
            }
        }

    }
}
