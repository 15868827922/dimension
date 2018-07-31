package com.shenjing.dimension.dimension.base.debug;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shenjing.dimension.BuildConfig;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;
import com.shenjing.dimension.dimension.base.cathe.SharePreferenceUtil;
import com.shenjing.dimension.dimension.base.device.DeviceInfo;
import com.shenjing.dimension.dimension.base.request.PackerNg;
import com.shenjing.dimension.dimension.base.request.URLManager;
import com.shenjing.dimension.dimension.base.util.ActivityUtil;
import com.shenjing.dimension.dimension.base.util.string.StringUtils;
import com.shenjing.dimension.dimension.view.MenuDialog;
import com.zjlp.httpvolly.VolleyConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import com.shenjing.dimension.R;
import com.zjlp.httpvolly.log.LPLog;

/**
 * Created by ZZQ on 2017/7/24.
 * 把DebugDialog改成Activity
 */

public class DebugActivity extends BaseActivity {

    public static final String EXTRA_CONFIGURATION = "configuration";

    Configuration mCurrentConfiguration;
    EditText inputMainServer;
    EditText inputImageServer;
    EditText inputResourceServer;
    EditText inputOfflineMsgServer;
    EditText inputIMFriendServer;
    TextView textMoreInfo  ;
    TextView textServerSelector;
    View mSelectServerLayout;
    Button btnControlLog;
    Button btnSwitchHttpOrHttps;
    Button btnTrustAllSvrForHttps;
    boolean isShowLog;

    public static void startActivity(Context context, Configuration currentConfiguration) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CONFIGURATION, currentConfiguration);
        ActivityUtil.gotoActivity(context, DebugActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_debug);
        setTitleText("DEBUG");

        init();
        showRightTextBtn("确定环境");
        setRightTextColor(getResources().getColor(R.color.unit_color_main));
        setRightTextButtonClickListener(this);
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.mCurrentConfiguration = (Configuration) bundle.getSerializable(EXTRA_CONFIGURATION);
        }
        inputMainServer = (EditText) findViewById(R.id.inputMainServer);
        inputImageServer = (EditText) findViewById(R.id.inputImageServer);
        inputResourceServer = (EditText) findViewById(R.id.inputResourceServer);
        inputOfflineMsgServer = (EditText) findViewById(R.id.inputOfflineMsgServer);
        inputIMFriendServer = (EditText) findViewById(R.id.inputIMFriendServer);
        textMoreInfo = (TextView)findViewById(R.id.textMoreInfo);
        textServerSelector= (TextView)findViewById(R.id.textServerSelector);
        btnControlLog = (Button) findViewById(R.id.btnOpenLog);

        findViewById(R.id.layout_server_selector).setVisibility(BuildConfig.DEBUG ? View.VISIBLE : View.GONE);
        if (getResources().getBoolean(R.bool.is_edit_enable)){
            findViewById(R.id.layout_server_selector).setVisibility(View.VISIBLE);
        }

        btnSwitchHttpOrHttps = (Button) findViewById(R.id.btnSwitchHttpOrHttps);
        btnSwitchHttpOrHttps.setText("当前 "+ URLManager.PROTOCOL_DEFAULT+" 可切换为 "+URLManager.canSwitchTo());
        btnSwitchHttpOrHttps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchHttpOrHttps();
            }
        });
        btnTrustAllSvrForHttps = (Button)findViewById(R.id.btnTrustAllSvrForHttps);
        btnTrustAllSvrForHttps.setText("信任所有服务器证书："+ VolleyConfig.NeedTrustAllServers);
        btnTrustAllSvrForHttps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleyConfig.NeedTrustAllServers = !VolleyConfig.NeedTrustAllServers;
                btnTrustAllSvrForHttps.setText("信任所有服务器证书："+ VolleyConfig.NeedTrustAllServers);
            }
        });
        textServerSelector.setText(mCurrentConfiguration.toString());
        findViewById(R.id.btnApplyPatch).setOnClickListener(this);
        findViewById(R.id.btnCleanPatch).setOnClickListener(this);
        findViewById(R.id.btnShowMethod).setOnClickListener(this);
        findViewById(R.id.btnCopyDbToSD).setOnClickListener(this);
        findViewById(R.id.copyDbFromSD).setOnClickListener(this);
        btnControlLog.setOnClickListener(this);
        mSelectServerLayout=findViewById(R.id.layout_server_selector);
        mSelectServerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuDialog.createDialog(DebugActivity.this, null, Configuration.getServerNames(), new MenuDialog.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        mCurrentConfiguration= Configuration.getConfigurationByIndex(position);
                        textServerSelector.setText(mCurrentConfiguration.name);
                        updateViewMode(mCurrentConfiguration.id != 1);
                        updateConfigurationView(mCurrentConfiguration);
                    }
                }).show();
            }
        });
        TextView textChannel = (TextView) findViewById(R.id.textChannel);
        textChannel.setText(BuildConfig.DEBUG ? "Develop" : PackerNg.getMarket(this));
        updateConfigurationView(mCurrentConfiguration);

        DisplayMetrics metric = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240

        textMoreInfo.append("versionCode = " + ActivityUtil.getVersionCode() + "\n");
        textMoreInfo.append("versionName = " + ActivityUtil.getVersionName() + "\n\n");
        textMoreInfo.append("screenSize = " + width + "*" + height + "\n");
        textMoreInfo.append("density = " + density + "\n");
        textMoreInfo.append("densityDpi = " + densityDpi + "\n\n");
        textMoreInfo.append("Mobile = " + android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL + ", " + android.os.Build.VERSION.SDK + ", " + android.os.Build.VERSION.RELEASE + "\n\n");
        textMoreInfo.append("AndroidSupport = " + "孙亚杰\n\n");
//        textMoreInfo.append("打包信息 = " + getResources().getString(R.string.build_host) + " AT " + getResources().getString(R.string.build_time) + "\n");
        /*textMoreInfo.append("热补丁相关信息 = "
                + "当前补丁版本：" +  LPApplicationLike.getInstance().getApatchInfo().getApatchVersionCode()
                + "当前下载的补丁版本：" +  LPApplicationLike.getInstance().getApatchInfo().getDownVersionCode()
                + "线上补丁下载次数：" +  LPApplicationLike.getInstance().getApatchInfo().getDownTimes());*/
        isShowLog = SharePreferenceUtil.getIsShowLog(this);
        updateLogBtn();
        updateViewMode(mCurrentConfiguration.id != 1);
    }

    private void switchHttpOrHttps() {
        URLManager.switchProtocol();
        Configuration.loadConfigurations();
        btnSwitchHttpOrHttps.setText("当前 "+URLManager.PROTOCOL_DEFAULT+" 可切换为 "+URLManager.canSwitchTo());
        mCurrentConfiguration = Configuration.getConfigurationById(mCurrentConfiguration.id);
        DebugUtils.init(mCurrentConfiguration);
        updateConfigurationView(mCurrentConfiguration);
        Toast.makeText(this,"已切换为"+URLManager.PROTOCOL_DEFAULT, Toast.LENGTH_SHORT);

        SharePreferenceUtil.saveHostIsHttp(mContext, URLManager.HTTP_STR.equals(URLManager.PROTOCOL_DEFAULT) ? true : false);
    }

    private void updateConfigurationView(Configuration currentConfiguration) {
        inputMainServer.setText(currentConfiguration.serverHostUrl);
        inputImageServer.setText(currentConfiguration.serverImageUrl);
        inputResourceServer.setText(currentConfiguration.serverResourceUrl);
        inputOfflineMsgServer.setText(currentConfiguration.serverOfflineMsgUrl);
        inputIMFriendServer.setText(currentConfiguration.serverIMFriendUrl);
    }

    /**
     *
     * @param isSemiAuto 是否半自动模式
     */
    private void updateViewMode(boolean isSemiAuto){
        int color=getResources().getColor(!isSemiAuto?R.color.text_black:R.color.text_light_gray);
        inputMainServer.setEnabled(!isSemiAuto);
        inputMainServer.setTextColor(color);
        inputImageServer.setEnabled(!isSemiAuto);
        inputImageServer.setTextColor(color);
        inputResourceServer.setEnabled(!isSemiAuto);
        inputResourceServer.setTextColor(color);
        inputOfflineMsgServer.setEnabled(!isSemiAuto);
        inputOfflineMsgServer.setTextColor(color);
        inputIMFriendServer.setEnabled(!isSemiAuto);
        inputIMFriendServer.setTextColor(color);
    }

    private void updateLogBtn(){
        if(isShowLog){
            btnControlLog.setText("关闭日志");
        }else{
            btnControlLog.setText("开启日志");
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == ID_RIGHT_TEXT){
            StringUtils.copyToClipBoard(this, textMoreInfo.getText().toString());
            if (mCurrentConfiguration.id == 0){
                // 生产环境上 也可以进行  http  https  切换
                URLManager.SelectedServerType=URLManager.ServerType_Production;
                URLManager.ServerURL_Production= mCurrentConfiguration.serverHostUrl;
                URLManager.ServerURL_Images_Production = mCurrentConfiguration.serverImageUrl;
                URLManager.ServerURL_Resource_Production = mCurrentConfiguration.serverResourceUrl;
                URLManager.ServerURL_Offline_Msg_Production = mCurrentConfiguration.serverOfflineMsgUrl;
                //URLManager.ServerURL_IM_FRIEND_REQUEST_Production = mCurrentConfiguration.serverIMFriendUrl; //好友这里目前不支持http访问
            }
            /** edit by Tiny on 2016/8/19 11:09 */
            if(mCurrentConfiguration.id == 1){
                mCurrentConfiguration.serverHostUrl=inputMainServer.getText().toString().trim();
                mCurrentConfiguration.serverImageUrl=inputImageServer.getText().toString().trim();
                mCurrentConfiguration.serverResourceUrl=inputResourceServer.getText().toString().trim();
                mCurrentConfiguration.serverOfflineMsgUrl=inputOfflineMsgServer.getText().toString().trim();
                mCurrentConfiguration.serverIMFriendUrl=inputIMFriendServer.getText().toString().trim();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("serverHostUrl", mCurrentConfiguration.serverHostUrl);
                    jsonObject.put("serverResourceUrl", mCurrentConfiguration.serverResourceUrl);
                    jsonObject.put("serverImageUrl", mCurrentConfiguration.serverImageUrl);
                    jsonObject.put("serverOfflineMsgUrl", mCurrentConfiguration.serverOfflineMsgUrl);
                    jsonObject.put("serverIMFriendUrl", mCurrentConfiguration.serverIMFriendUrl);
                    SharePreferenceUtil.setKeyHandServer(this, jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            int type = 1;
            if(mCurrentConfiguration.id!=0){
                type=0;
                VolleyConfig.getVolleyConfig().setReleaseEnvironment(false);
            }else {
                VolleyConfig.getVolleyConfig().setReleaseEnvironment(true);
            }
            URLManager.SelectedServerType=type;
            URLManager.ServerURL_QA = mCurrentConfiguration.serverHostUrl;
            URLManager.ServerURL_Images_QA = mCurrentConfiguration.serverImageUrl;
            URLManager.ServerURL_Resource_QA = mCurrentConfiguration.serverResourceUrl;
            URLManager.ServerURL_Offline_Msg_QA = mCurrentConfiguration.serverOfflineMsgUrl;
            URLManager.ServerURL_IM_FRIEND_REQUEST_QA = mCurrentConfiguration.serverIMFriendUrl;
//            IMServiceClient.configOfflineUrl(this,type,inputOfflineMsgServer.getText().toString());
            DebugUtils.setSelectServer(mCurrentConfiguration);
            SharePreferenceUtil.setKeyChoosedServer(this, mCurrentConfiguration.id);
            //清除上个环境的缓存
            String userName = SharePreferenceUtil.getSavedUserName(this);
            if (!TextUtils.isEmpty(userName)) {
                SharePreferenceUtil.saveServiceMenuList(this, null);
            }
        }/*else if(v.getId() == R.id.btnShowMethod){
            checkIsHaveData();
        }*//*else if(v.getId() == R.id.btnCopyDbToSD){
            LPLog.print(getClass(), "设备deviceId：" + DeviceInfo.getOriginalDeviceId(this) + "数据库密码：" + SecurityUtils.getEncryptPasswordText(this, DeviceInfo.getOriginalDeviceId(this)));
            FileUtils.fileChannelCopy(new File("/data/data/com.zjlp.bestface/databases/chatRecord_cipher"), new File(Environment.getExternalStorageDirectory() + File.separator + "chatRecord_cipher"));
        }else if(v.getId() == R.id.copyDbFromSD){
            FileUtils.fileChannelCopy(new File(Environment.getExternalStorageDirectory() + File.separator + "chatRecord_cipher"), new File("/data/data/com.zjlp.bestface/databases/chatRecord_cipher"));
            FileUtils.fileChannelCopy(new File(Environment.getExternalStorageDirectory() + File.separator + "lp_cipher.db"), new File("/data/data/com.zjlp.bestface/databases/lp_cipher.db"));
        }*/else if(v.getId() == R.id.btnOpenLog){
            if(isShowLog){
                isShowLog = false;
            }else{
                isShowLog = true;
            }
            updateLogBtn();
            LPLog.initLogConfig(isShowLog, isShowLog);
            SharePreferenceUtil.saveIsShowLog(this, isShowLog);
        }/*else if(v.getId() == R.id.btnCleanPatch){
            ApatchUtils.removeAllPatch();
            toast("清除tinker补丁成功，请锁屏并稍等几s");
        }else if(v.getId() == R.id.btnApplyPatch){
            ApatchUtils.detectLocalPatch(this);
            toast("应用tinker补丁成功，请锁屏并稍等几s");
        }*/

        finish();
    }
/*
    private void checkIsHaveData() {
        QueryBuilder qb = new QueryBuilder(BlockCanaryInfo.class);
        ArrayList<BlockCanaryInfo> infoList = LPApplicationLike.getDBConnection().query(qb);

        if(infoList != null && infoList.size() > 0){
            Intent intent = new Intent(LPApplicationLike.getContext(), BlockCanaryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(BlockCanaryActivity.EXTRA_INFO_LIST, infoList);
            LPApplicationLike.getContext().startActivityForResult(intent);
        }else {
            Toast.makeText(LPApplicationLike.getContext(), "数据库中暂无耗时方法记录", Toast.LENGTH_SHORT).show();
        }
    }*/

}
