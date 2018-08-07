package com.shenjing.dimension.dimension.main.fragent;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.fragment.FragmentBase;
import com.shenjing.dimension.dimension.base.image.LPNetworkRoundedImageView;
import com.shenjing.dimension.dimension.base.request.HttpRequestCallback;
import com.shenjing.dimension.dimension.base.request.RequestMap;
import com.shenjing.dimension.dimension.base.util.ActivityUtil;
import com.shenjing.dimension.dimension.me.FeedBackActivity;
import com.shenjing.dimension.dimension.me.MyBackpackActivity;
import com.shenjing.dimension.dimension.me.MyMessageActivity;
import com.shenjing.dimension.dimension.me.MyTaskActivity;
import com.shenjing.dimension.dimension.me.MyWalletActivity;
import com.shenjing.dimension.dimension.me.ScoreMallActivity;
import com.shenjing.dimension.dimension.me.SettingActivity;
import com.shenjing.dimension.dimension.me.view.SignDialog;
import com.zjlp.httpvolly.HttpService;
import com.zjlp.httpvolly.RequestError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyFragment extends FragmentBase implements View.OnClickListener{

    public static final String TAG="MyFragment";
    String mString = "hello everyone!";
    byte[] mBytes = null;
    @Bind(R.id.img_user_header)
    LPNetworkRoundedImageView mImgUserHeader; //用户头像

    @Bind(R.id.tv_user_name)
    TextView mTvUserName;  //用户名字

    @Bind(R.id.tv_id)
    TextView mTvId;  //用户id

    private RequestMap requestMap = RequestMap.newInstance();
    @Override
    public int getContentLayout() {
        return R.layout.fragment_my;
    }

    @Override
    public void initView(@Nullable View view) {
        ButterKnife.bind(this,view);



        findViewById(R.id.img_is_sign).setOnClickListener(this);
        findViewById(R.id.view_my_task).setOnClickListener(this);
        findViewById(R.id.view_my_wallet).setOnClickListener(this);
        findViewById(R.id.view_my_backpack).setOnClickListener(this);
        findViewById(R.id.view_score_mall).setOnClickListener(this);

        findViewById(R.id.view_my_message).setOnClickListener(this);
        findViewById(R.id.view_my_order).setOnClickListener(this);
        findViewById(R.id.view_address_manager).setOnClickListener(this);
        findViewById(R.id.view_my_props).setOnClickListener(this);
        findViewById(R.id.view_give_mark).setOnClickListener(this);
        findViewById(R.id.view_give_feedback).setOnClickListener(this);
        findViewById(R.id.img_set_me).setOnClickListener(this);
    }

    @Override
    public void initData() {
        reqAddressList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_set_me:  //设置
                ActivityUtil.gotoActivity(getActivity(), SettingActivity.class);
                break;
            case R.id.view_my_task:  //我的任务
                ActivityUtil.gotoActivity(getActivity(), MyTaskActivity.class);
                /*// 解密
                String decrypted = null;
                try {
                    decrypted = AesUtils.decrypt("B+gR8WqBGJ9kUE48YSYrSA==");
                    mTvUserName.setText("解密：" + decrypted);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

               /* try {
                    AES mAes = new AES();
                 *//*   mBytes = mString.getBytes("UTF8");
                    String enString = mAes.encrypt(mBytes);
                    mTvId.setText("加密后：" + enString);*//*
                    String deString = mAes.decrypt("MS4yMzQ1Njc4MTIzNDU3RSsxNQ==");
                    mTvUserName.setText("解密后：" + deString);
                } catch (Exception e) {
                    Log.i("qing", "MainActivity----catch");
                }*/
                break;
            case R.id.view_my_wallet:  //我的钱包
                ActivityUtil.gotoActivity(getActivity(), MyWalletActivity.class);
                break;
            case R.id.view_my_backpack:  //我的背包
                ActivityUtil.gotoActivity(getActivity(), MyBackpackActivity.class);
                break;
            case R.id.view_score_mall:  //积分商城
                ActivityUtil.gotoActivity(getActivity(), ScoreMallActivity.class);

                break;
            case R.id.view_my_message:  //我的消息
                ActivityUtil.gotoActivity(getActivity(), MyMessageActivity.class);
                break;
            case R.id.view_my_order:  //我的订单
                break;
            case R.id.view_address_manager:  //地址管理
                break;

            case R.id.view_my_props:  //我的道具
                break;
            case R.id.view_give_mark:  //评分
                break;
            case R.id.view_give_feedback:  //意见反馈
                ActivityUtil.gotoActivity(getActivity(), FeedBackActivity.class);
                break;
            case R.id.img_is_sign:  //签到
                showSignDialog();
                break;
        }

    }

    SignDialog signDialog;
    private void showSignDialog(){
        signDialog = new SignDialog(getActivity());
        signDialog.show();
    }


    private void reqAddressList(){
        String url = /*URLManager.getRequestURL(URLManager.Method_Address_list)*/"http://192.168.0.157/register";
        requestMap.cancel(url);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_name", "15868827922");
            jsonObject.put("user_password","123456");
            jsonObject.put("login_type","mobile");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRequestCallback callback = new HttpRequestCallback(getActivity()) {
            @Override
            public void onFinished(JSONObject jsonObject) {
                super.onFinished(jsonObject);
                try {
                    jsonObject = new JSONObject(jsonObject.toString().replace(":null", ":\"\""));
                    JSONArray array  = jsonObject.optJSONArray("data");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(RequestError error) {
                super.onFailed(error);
                toast(error.getErrorReason());

            }
            @Override
            public void onQuitApp(String message) {
                super.onQuitApp(message);

                Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
            }

        };
        Request request = HttpService.doPost(url, jsonObject, callback, true, true, true);
        requestMap.add(url, request);
    }

}
