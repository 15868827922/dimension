package com.shenjing.dimension.dimension.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.ghnor.imagecompressor.ImageCompressor;
import com.ghnor.imagecompressor.callback.Callback;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;
import com.shenjing.dimension.dimension.base.crop.Crop;
import com.shenjing.dimension.dimension.base.image.LPNetworkRoundedImageView;
import com.shenjing.dimension.dimension.base.request.HttpRequestCallback;
import com.shenjing.dimension.dimension.base.request.RequestMap;
import com.shenjing.dimension.dimension.base.request.URLManager;
import com.shenjing.dimension.dimension.base.select_picture.MultiImageSelectorActivity;
import com.shenjing.dimension.dimension.base.util.ActivityUtil;
import com.shenjing.dimension.dimension.base.util.Constants;
import com.shenjing.dimension.dimension.base.util.PermissionManager;
import com.shenjing.dimension.dimension.base.util.file.FileUtils;
import com.shenjing.dimension.dimension.base.util.image.ImageUtils;
import com.shenjing.dimension.dimension.main.LPApplicationLike;
import com.shenjing.dimension.dimension.me.view.ChangeAddressPopupWindow;
import com.shenjing.dimension.dimension.me.view.ChangeDatePopwindow;
import com.shenjing.dimension.dimension.me.view.ChangeSelectPopupWindow;
import com.shenjing.dimension.dimension.view.ActionSheetDialog;
import com.shenjing.dimension.dimension.view.LPItemArrowRightView;
import com.zjlp.httpvolly.HttpService;
import com.zjlp.httpvolly.RequestError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelfMessageActivity extends BaseActivity implements PermissionManager.OnPermissonRequestListner{

    private final String TEMP_PHOTO_NAME = "sj_temp.jpg";
    private String mPhotoUrl;
    private List<FeedBackActivity.PicItem> mSelectedPics;

    private static final String HEAD_IMAGE_DIR_NAME = "sj_head_images";
    private static final String HEAD_IMAGE_FILE_SUFFIX = "head_images_";

    private File uploadFile;

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

    @Bind(R.id.img_user_header)
    LPNetworkRoundedImageView mImgHeader;

    private RequestMap requestMap = RequestMap.newInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_self_message);
        setTitleText("个人信息");
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        mSelectedPics = new ArrayList<>();
        mViewUserNickName.setOnClickListener(this);
        mViewUserSign.setOnClickListener(this);
        mViewUserSex.setOnClickListener(this);
        mViewUserBirthday.setOnClickListener(this);
        mViewUserConstellation.setOnClickListener(this);
        mViewUserLocation.setOnClickListener(this);
        mViewUserIndustry.setOnClickListener(this);
        mViewAddressManager.setOnClickListener(this);
        mViewSetSecurity.setOnClickListener(this);

        findViewById(R.id.view_change_header).setOnClickListener(this);

        mViewUserId.setArrowVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.view_change_header:
                showChooseImageDialog();
                break;
            case R.id.view_user_nickname:  //我的昵称
                ModifyPersonalInfoActivity.gotoActivity(this,ModifyPersonalInfoActivity.INFO_TYPE_NICKNAME, Constants.REQ_CODE_SET_NICKNAME);
                break;
            case R.id.view_user_sign:  //我的签名
                ModifyPersonalInfoActivity.gotoActivity(this,ModifyPersonalInfoActivity.INFO_TYPE_SIGN, Constants.REQ_CODE_SET_SIGN);
                break;
            case R.id.view_user_sex:  //性别
                showChoseConstellation(ChangeSelectPopupWindow.TYPE_SEX,"男");
                break;
            case R.id.view_user_birthday:  //生日
                selectDate();
                break;
            case R.id.view_user_constellation: //星座
                showChoseConstellation(ChangeSelectPopupWindow.TYPE_CONSTELLATION,"金牛座");
                break;
            case R.id.view_user_location: //所在地
                showChoseAddress();
                break;
            case R.id.view_user_industry: //行业
                showChoseConstellation(ChangeSelectPopupWindow.TYPE_INDUSTRY,"金融");
                break;
            case R.id.view_address_manager: //地址管理
                ActivityUtil.gotoActivity(this,AddressManagerActivity.class);
                break;
            case R.id.view_set_security: //密保问题
                ActivityUtil.gotoActivity(this,SetSecurityActivity.class);
                break;
        }
    }

    private void showChoseAddress(){
        ChangeAddressPopupWindow popupWindow = new ChangeAddressPopupWindow(SelfMessageActivity.this);
        popupWindow.setAddress("浙江", "杭州", "西湖区");
        popupWindow.setView();
        popupWindow.showAtLocation(mViewUserLocation, Gravity.BOTTOM, 0, 0);
        popupWindow
                .setAddresskListener(new ChangeAddressPopupWindow.OnAddressCListener() {

                    @Override
                    public void onClick(String province, String city, String area) {
                        mViewUserLocation.setSecTitleText(province + city + area);
                    }
                });
    }

    private void selectDate() {
        final String[] str = new String[10];
        ChangeDatePopwindow mChangeBirthDialog = new ChangeDatePopwindow(
                this);
        mChangeBirthDialog.setDate("2016", "1", "1");
        mChangeBirthDialog.showAtLocation(mViewUserBirthday, Gravity.BOTTOM, 0, 0);
        mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow.OnBirthListener() {

            @Override
            public void onClick(String year, String month, String day) {
                // TODO Auto-generated method stub
                StringBuilder sb = new StringBuilder();
                sb.append(year.substring(0, year.length() - 1)).append("-").append(month.substring(0, day.length() - 1)).append("-").append(day);
                str[0] = year + "-" + month + "-" + day;
                str[1] = sb.toString();
                mViewUserBirthday.setSecTitleText(year + "-" + month + "-" + day);

            }
        });
    }

    private void showChoseConstellation(int type, String defaultStr){
        ChangeSelectPopupWindow selectPopupWindow = new ChangeSelectPopupWindow(SelfMessageActivity.this);
        selectPopupWindow.setCurrentStr(defaultStr);
        selectPopupWindow.setView(type);
        selectPopupWindow.showAtLocation(mViewUserLocation, Gravity.BOTTOM, 0, 0);
        selectPopupWindow
                .setChangeConstellationListener(new ChangeSelectPopupWindow.OnConstellationListener(){

                    @Override
                    public void onClick(String currentStr) {
                        switch (type){
                            case ChangeSelectPopupWindow.TYPE_CONSTELLATION:
                                mViewUserConstellation.setSecTitleText(currentStr);
                                break;
                            case ChangeSelectPopupWindow.TYPE_SEX:
                                mViewUserSex.setSecTitleText(currentStr);
                                break;
                            case ChangeSelectPopupWindow.TYPE_INDUSTRY:
                                mViewUserIndustry.setSecTitleText(currentStr);
                                break;
                        }
                    }
                });
    }



    /**
     * 显示拍照或从相册选择的对话框
     *
     */
    private void showChooseImageDialog() {

        ActionSheetDialog.Builder builder=new ActionSheetDialog.Builder(this,ActionSheetDialog.STYLE_LIST_VIEW);
        builder.setItems(new String[]{getString(R.string.take_photo),getString(R.string.album)})
                .setPositiveButton(R.string.btn_cancel).setActionSheetListener(new ActionSheetDialog.ActionSheetListener() {
            @Override
            public void onActionClick(int position) {
                if (position == 0) {
                    // 拍照
                    if (PermissionManager.getInstance().isPermissionGranted(mContext, PermissionManager.PERMISSION_CAMERA)){
                        takePhoto();
                    }else{
                        PermissionManager.getInstance().setOnPermissonRequestListner(SelfMessageActivity.this);
                        PermissionManager.getInstance().requestPermisison(SelfMessageActivity.this, PermissionManager.PERMISSION_CAMERA);
                    }
                } else if (position == 1) {
                    // 从相册选择
                    if (PermissionManager.getInstance().isPermissionGranted(mContext, PermissionManager.PERMISSION_READ_EXTERNAL_STORAGE)){
                        pickPhoto();
                    }else{
                        PermissionManager.getInstance().setOnPermissonRequestListner(SelfMessageActivity.this);
                        PermissionManager.getInstance().requestPermisison(SelfMessageActivity.this, PermissionManager.PERMISSION_READ_EXTERNAL_STORAGE);
                    }
                }
            }
            @Override
            public void onPositiveButtonClick() {

            }
        });
        builder.build().show();



    }


    private void pickPhoto() {

        MultiImageSelectorActivity.startActivityForSingle(SelfMessageActivity.this, Constants.REQ_CODE_PICK_PHOTO);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mPhotoUrl = FileUtils.getBestFaceChildDir(FileUtils.DIR_IMAGES).getPath() + System.currentTimeMillis() + TEMP_PHOTO_NAME;
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPhotoUrl)));
        startActivityForResult(intent, Constants.REQ_CODE_TAKE_PHOTO);

    }

    private File getFile(boolean create) {
        File dir = FileUtils.getBestFaceChildDir(FileUtils.DIR_IMAGES);
        if (create) {
            mPhotoUrl = UUID.randomUUID().toString() + TEMP_PHOTO_NAME;
        }
        File file = new File(dir, mPhotoUrl);
        if (file.exists() && create) {
            file.delete();
        }
        return file;
    }

    private void startPhotoZoom(Uri uri, int size, int requestCode) {

        uploadFile = new File(FileUtils.getCacheDir(getApplicationContext(), HEAD_IMAGE_DIR_NAME),
                HEAD_IMAGE_FILE_SUFFIX + System.currentTimeMillis() + FileUtils.JPG_SUFFIX);

        Crop.of(uri, Uri.fromFile(uploadFile)).withAspect(size, size).start(this);
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
                case Constants.REQ_CODE_PICK_PHOTO:

                    ArrayList<String> files = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_PERFORM_ADD_PIC_URLS);
                    if(files!=null&&!files.isEmpty()){
                        String path=files.get(0);
                        startPhotoZoom(Uri.fromFile(new File(path)), 640, requestCode);
                    }

                    break;
                case Constants.REQ_CODE_TAKE_PHOTO:
                    if(mPhotoUrl != null){
                        ImageUtils.notifyScanFile(this, mPhotoUrl);
                        File srcPhotoFile = new File(mPhotoUrl);
                        startPhotoZoom(Uri.fromFile(srcPhotoFile), 640, requestCode);
                    }else{
                        toast("拍摄图片失败，请稍后再试");
                    }
                    break;
                case Crop.REQUEST_CROP:
                    if(uploadFile == null){
                        toast("图片裁剪失败，请稍后再试");
                    }else{
                        ImageCompressor.with().load(uploadFile).compress(new Callback<String>() {
                            @Override
                            public void callback(String s) {
                                String urlImg = FileUtils.getFileUrl(s);
                                mImgHeader.setImageUrl(urlImg);
//                                uploadImage(imageItem);
                            }
                        });
                    }
                    break;
            }
        }

    }

    @Override
    public void onPermissonRequestSucceed(int permissionType) {
        if (permissionType == PermissionManager.PERMISSION_CAMERA){
            takePhoto();
        }else if (permissionType == PermissionManager.PERMISSION_READ_EXTERNAL_STORAGE){
            pickPhoto();
        }
    }

    @Override
    public void onPermissonRequestFailed(int permissionType) {

    }

    @Override
    public void onPermissonRequestTip(int permissionType) {
        PermissionManager.getInstance().toastPermission(mContext, permissionType);
    }

    private void reqEditUserInfo(int type){
        String url = URLManager.getRequestURL(URLManager.Method_Edit_User_Info);
        requestMap.cancel(url);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", LPApplicationLike.getUserId());
            jsonObject.put("token",LPApplicationLike.getUserToken());
            jsonObject.put("login_type","mobile");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRequestCallback callback = new HttpRequestCallback(this) {
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
