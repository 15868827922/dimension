package com.shenjing.dimension.dimension.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChageNewPhoneActivity extends BaseActivity {

    @Bind({R.id.edt_input_phone})
    EditText mEdtInputPhone;

    @Bind({R.id.edt_input_code})
    EditText mEdtInputCode;

    @Bind(R.id.view_get_code)
    View mViewGetCode;

    @Bind(R.id.tv_get_code)
    TextView mTvGetCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_change_new_phone);
        setTitleText("变更手机号");

        ButterKnife.bind(this);

        findViewById(R.id.view_delete_phone).setOnClickListener(this);
        findViewById(R.id.btn_go_sure).setOnClickListener(this);
        mViewGetCode.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.view_get_code:
                startCountDownView();
                break;
            case R.id.view_delete_phone:  //删除输入的手机号
                mEdtInputPhone.setText("");
                break;
            case R.id.btn_go_sure:
                if (TextUtils.isEmpty(mEdtInputPhone.getText().toString().trim())){
                    toast("请输入手机号");
                    return;
                }

                if (TextUtils.isEmpty(mEdtInputCode.getText().toString().trim())){
                    toast("请输入验证码");
                    return;
                }
                break;
        }
    }


    private void startCountDownView(){
        mViewGetCode.setEnabled(false);
        remainSeconds = 61;
        mViewGetCode.post(task);
    }

    private void stopCountDown(){
        remainSeconds = -1;//确保停止验证码重新发送的倒计时
    }

    public void close(){
        stopCountDown();
        mViewGetCode.removeCallbacks(task);
    }


    int remainSeconds;
    Runnable task = new Runnable() {

        @Override
        public void run() {
            remainSeconds--;
            if(remainSeconds < 0){
                mViewGetCode.setEnabled(true);
                mTvGetCode.setText("获取验证码");
            }else{
                mTvGetCode.setText(remainSeconds + "秒后重试");
                mViewGetCode.postDelayed(task, 1000);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        close();
    }
}
