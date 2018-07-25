package com.shenjing.dimension.dimension.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;
import com.shenjing.dimension.dimension.base.util.ActivityUtil;
import com.shenjing.dimension.dimension.base.util.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ModifyPersonalInfoActivity extends BaseActivity {

    public static final String EXTRA_INFO_TYPE = "infoType";
//    public static final String EXTRA_TEXT_HINT = "hint";

    public static final int INFO_TYPE_NICKNAME = 1;  //设置昵称
    public static final int INFO_TYPE_SIGN = 2; //设置签名


    private int infoType;

    @Bind(R.id.tv_text_num_tip)
    TextView mTvTextNumTip;

    @Bind(R.id.edt_nickname)
    EditText mEdtNickName;

    @Bind(R.id.edt_input_code)
    EditText mEdtSign;

    public static void gotoActivity(Activity activity, int infoType, int requestCode) {
        Bundle extras = new Bundle();
        extras.putInt(EXTRA_INFO_TYPE, infoType);
        ActivityUtil.gotoActivityForResult(activity, ModifyPersonalInfoActivity.class, extras, requestCode);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_set_nick_name);
        showRightTextBtn(R.string.save);
        setRightTextButtonClickListener(this);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            infoType = extras.getInt(EXTRA_INFO_TYPE);
        }
    }


    private void initView() {
        if (infoType == INFO_TYPE_NICKNAME){
            setTitleText("设定昵称");
            findViewById(R.id.view_input_nickname).setVisibility(View.VISIBLE);
            mTvTextNumTip.setText("( 0/6  最多输入6个字)");
        }else if (infoType == INFO_TYPE_SIGN){
            setTitleText("设定签名");
            findViewById(R.id.view_input_code).setVisibility(View.VISIBLE);
            mTvTextNumTip.setText("( 0/35  最多输入35个字)");
        }

        findViewById(R.id.view_delete_nickname).setOnClickListener(this);
        findViewById(R.id.view_delete_sign).setOnClickListener(this);

        mEdtNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String editable = mEdtNickName.getText().toString();
                String str = stringFilter(editable.toString());
                if(!editable.equals(str)){
                    mEdtNickName.setText(str);
                    //设置新的光标所在位置
                    mEdtNickName.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTvTextNumTip.setText("( " + editable.length() + "/6  最多输入6个字)");
            }
        });


        mEdtSign.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String editable = mEdtSign.getText().toString();
                String str = stringFilter(editable.toString());
                if(!editable.equals(str)){
                    mEdtSign.setText(str);
                    //设置新的光标所在位置
                    mEdtSign.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTvTextNumTip.setText("( " + editable.length() + "/35  最多输入35个字)");
            }
        });
    }
    public static String stringFilter(String str)throws PatternSyntaxException {
// 只允许字母、数字和汉字
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";//正则表达式
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case ID_RIGHT_TEXT:  //保存
                if (infoType == INFO_TYPE_NICKNAME){
                   if (TextUtils.isEmpty(mEdtNickName.getText().toString().trim())){
                       toast("请输入昵称");
                       return;
                   }

                    setResult(RESULT_OK, new Intent().putExtra(Constants.EXTRA_GET_NICKNAME, mEdtNickName.getText().toString().trim()));
                    finish();
                }else if (infoType == INFO_TYPE_SIGN){
                    if (TextUtils.isEmpty(mEdtSign.getText().toString().trim())){
                        toast("请输入签名");
                        return;
                    }
                    setResult(RESULT_OK, new Intent().putExtra(Constants.EXTRA_GET_SIGN, mEdtSign.getText().toString().trim()));
                    finish();
                }
                break;
            case R.id.view_delete_nickname:  //删除输入的昵称
                mEdtNickName.setText("");
                break;
            case R.id.view_delete_sign:  //删除输入的签名
                mEdtSign.setText("");
                break;
        }
    }

}
