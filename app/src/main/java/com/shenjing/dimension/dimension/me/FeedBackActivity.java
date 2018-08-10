package com.shenjing.dimension.dimension.me;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.ghnor.imagecompressor.ImageCompressor;
import com.ghnor.imagecompressor.callback.Callback;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;
import com.shenjing.dimension.dimension.base.device.DeviceInfo;
import com.shenjing.dimension.dimension.base.image.LPNetworkImageView;
import com.shenjing.dimension.dimension.base.select_picture.MultiImageSelectorActivity;
import com.shenjing.dimension.dimension.base.util.Constants;
import com.shenjing.dimension.dimension.base.util.PermissionManager;
import com.shenjing.dimension.dimension.base.util.file.FileUtils;
import com.shenjing.dimension.dimension.base.util.image.ImageUtils;
import com.shenjing.dimension.dimension.view.ActionSheetDialog;
import com.zjlp.httpvolly.CustomProgress;
import com.zjlp.httpvolly.ProgressCancelNotifer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.shenjing.dimension.dimension.base.util.FieldUtil;

public class FeedBackActivity extends BaseActivity implements AdapterView.OnItemClickListener,PermissionManager.OnPermissonRequestListner,TextWatcher{
    private final String TEMP_PHOTO_NAME = "lp_temp.jpg";
    private String mPhotoUrl;
    private List<PicItem> mSelectedPics;
    public final int MAX_PIC_SIZE = 3;
    private final String FEEDBACK_IMAGE_DIR_NAME = "lp_feedback_images";
    private final String FEEDBACK_IMAGE_FILE_SUFFIX = "feedback_images_";
    public final int IMAGE_QUALITY = 80;
    public final int IMAGE_MAX_SIDE = 800;
    private PicGvAdapter mPicAdapter;
    private Request request;
    private List<String> uploadUrls = null;

    @Bind(R.id.picture_gv)
    GridView gvPicture;

    @Bind(R.id.edt_input_reason)
    EditText mEdtInputReason;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_feed_back);
        setTitleText("意见反馈");
        showRightTextBtn("提交");
        setRightTextButtonClickListener(this);
        ButterKnife.bind(this);
        initData();
    }


    private void initData() {
        if (mSelectedPics != null) {
            mSelectedPics.clear();
        } else {
            mSelectedPics = new ArrayList<PicItem>();
        }
        mSelectedPics.add(PicItem.createAddItem());
        mPicAdapter = new PicGvAdapter();
        gvPicture.setOnItemClickListener(this);
        gvPicture.setAdapter(mPicAdapter);
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
                        PermissionManager.getInstance().setOnPermissonRequestListner(FeedBackActivity.this);
                        PermissionManager.getInstance().requestPermisison(FeedBackActivity.this, PermissionManager.PERMISSION_CAMERA);
                    }
                } else if (position == 1) {
                    // 从相册选择
                    if (PermissionManager.getInstance().isPermissionGranted(mContext, PermissionManager.PERMISSION_READ_EXTERNAL_STORAGE)){
                        pickPhoto();
                    }else{
                        PermissionManager.getInstance().setOnPermissonRequestListner(FeedBackActivity.this);
                        PermissionManager.getInstance().requestPermisison(FeedBackActivity.this, PermissionManager.PERMISSION_READ_EXTERNAL_STORAGE);
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
       /* MultiImageSelectorActivity.startActivityForMulti(FeedBackActivity.this, mSelectedPics.size() - 1, MAX_PIC_SIZE,
                2, MultiImageSelectorActivity.REQ_CODE_MULTI_PHOTO_SELECT);*/
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getFile(true)));
        startActivityForResult(intent, Constants.REQ_CODE_TAKE_PHOTO);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == ID_RIGHT_TEXT) {
            uploadImgs();  //提交意见反馈信息
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == Constants.REQ_CODE_MULTI_PHOTO_SELECT) {
                    ArrayList<String> mAddPics = data
                            .getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_PERFORM_ADD_PIC_URLS);
                    ImageCompressor.with().load(mAddPics).compress(new Callback<List<String>>() {
                        @Override
                        public void callback(List<String> strings) {
                            for (String path : strings) {
                                mSelectedPics.add(mSelectedPics.size() - 1, PicItem.createQRCodeItem(path));
                            }
                            mPicAdapter.notifyDataSetChanged();
                        }
                    });
                }
                else if (requestCode == Constants.REQ_CODE_TAKE_PHOTO) {
                    String path = getFile(false).getPath();

                    ///** edit  by liht on 2017/1/20 11:43 保存图片到系统 */
                    ImageUtils.notifyScanFile(this, path);
                    ImageCompressor.with().load(path).compress(new Callback<String>() {
                        @Override
                        public void callback(String s) {
                            PicItem pi = new PicItem();
                            pi.isSelected = true;
                            pi.picPath = s;
                            mSelectedPics.add(mSelectedPics.size() - 1, pi);
                            mPicAdapter.notifyDataSetChanged();
                        }
                    });
                }
                break;
        }
    }
    private void uploadImgs(){
        boolean needUploadPic = false;
        List<String> pathList = new ArrayList<String>();
        if(mSelectedPics != null && mSelectedPics.size() != 0){
            int size = mSelectedPics.size();
            for(int i = 0; i < size; i++){
                String path = mSelectedPics.get(i).picPath;
                if(!TextUtils.isEmpty(path)){
                    needUploadPic = true;
                    pathList.add(path);
                }
            }

        }

       /* if(!needUploadPic){
            if (!TextUtils.isEmpty(editFeedBack.getText().toString().trim()))
                submitFeedBackInfo();
            else {
                toast("内容不能为空", Toast.LENGTH_SHORT);
            }
        }*/else{
//            startUploadThread(pathList);
        }
    }

    /*private void startUploadThread(List<String> images){
        CustomProgress.showLoadingDialog(this, null, new ProgressCancelNotifer() {
            @Override
            public void notifyProgressCancel() {
                if(uploadTask != null){
                    uploadTask.cancel();
                    uploadTask = null;
                }
            }
        });
        doUploadImages(images);
    }*/

   /* private void doUploadImages(final List<String> imgs){
        uploadUrls = new ArrayList<String>();

        uploadTask = QNFileUploadUtils.with(this);
        for (int i = 0; i < imgs.size(); i++) {
            String path = FileUtils.getFilePath(imgs.get(i));
            String compressedPath = null;
            try {
                compressedPath = ImageCompressor.with().load(path).compressSync();
            } catch (Exception e) {
                e.printStackTrace();
            }
            uploadTask.add(compressedPath);
        }
        uploadTask.post(new QNFileUploadUtils.CallBack() {
            @Override
            public void onProgress(QNFileUploadUtils.Result result, int progress, int count) {

            }

            @Override
            public void onFinish(ArrayList <QNFileUploadUtils.Result> results) {

                if(results != null && results.size() > 0){
                    for (int i = 0; i < results.size(); i++) {
                        uploadUrls.add(results.get(i).url);
                    }
                }
                submitFeedBackInfo();
            }

            @Override
            public void onCancel() {
                uploadTask=null;
                CustomProgress.dismissLoadingDialog();
            }

            @Override
            public void onFailed() {
                CustomProgress.dismissLoadingDialog();
                toast(R.string.publish_fail);
            }
        });
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(request != null && !request.isCanceled()){
            request.cancel();
        }
       /* if(uploadTask !=null){
            uploadTask.cancel();
            uploadTask = null;
        }*/
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        /*if (s.length() > 0) {
            btnSubmit.setEnabled(true);
        } else {
            btnSubmit.setEnabled(false);
        }*/
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

    class PicGvAdapter extends BaseAdapter {

        AbsListView.LayoutParams vl;

        public PicGvAdapter() {
            int itemWid = getResources().getDimensionPixelSize(R.dimen.common_sw320dp_of_56);
            vl = new AbsListView.LayoutParams(itemWid, itemWid);
        }

        @Override
        public int getCount() {
            int count = mSelectedPics != null ? mSelectedPics.size() : 0;
            return count;
        }

        @Override
        public PicItem getItem(int position) {
            return mSelectedPics.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            viewHolder holder;
            if (convertView == null) {
                convertView = new RelativeLayout(FeedBackActivity.this);
                RelativeLayout layout = (RelativeLayout) convertView;
                holder = new viewHolder();
                holder.img = new LPNetworkImageView(FeedBackActivity.this);
                holder.img.setLayoutParams(vl);
                holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                layout.addView(holder.img);
                convertView.setTag(holder);
            } else {
                holder = (viewHolder) convertView.getTag();
            }
            PicItem pi = getItem(position);
            if (pi.isAddItem) {
                holder.img.setImageUrl("file:///android_asset/res/icon_feed_back_add_pic.png");
            } else {
                holder.img.setImageUrl("file://" + pi.picPath);
            }
            if (getCount() == MAX_PIC_SIZE + 1 && position == MAX_PIC_SIZE) {
                holder.img.setVisibility(View.GONE);
            }
            return convertView;

        }

        class viewHolder {
            LPNetworkImageView img;
        }

    }

    public static final class PicItem {
        public String picPath;
        public String bucketName;
        boolean isSelected;
        boolean isAddItem;
        boolean isTakePhotoItem;

        static PicItem createAddItem() {
            PicItem picItem = new PicItem();
            picItem.isAddItem = true;
            return picItem;
        }

        static PicItem createQRCodeItem(String path) {
            PicItem picItem = new PicItem();
            picItem.picPath = path;
            return picItem;
        }

        static PicItem createTakePhotoItem() {
            PicItem picItem = new PicItem();
            picItem.isTakePhotoItem = true;
            return picItem;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        PicItem item = (PicItem) parent.getItemAtPosition(position);
        if (item.isAddItem) {
            if (mSelectedPics.size() > MAX_PIC_SIZE) {
                toast(R.string.over_the_max_size);
            } else {
                showChooseImageDialog();
            }
        } else {
            final Dialog dialog = new Dialog(FeedBackActivity.this,R.style.Activity_Show);
            dialog.setContentView(R.layout.dialog_feed_back_picture);
            android.view.WindowManager.LayoutParams windowLp = dialog.getWindow().getAttributes();
            windowLp.width = (DeviceInfo.getScreenWidth(mContext));
            windowLp.height = DeviceInfo.getScreenHeight(mContext);
            dialog.getWindow().setAttributes(windowLp);
            dialog.show();
            final String uri = mSelectedPics.get(position).picPath;
            ImageView bigPicture=(ImageView) dialog.findViewById(R.id.bigPicture_iv);
            //点击查看大图
//            showBigPicture(bigPicture,position,uri);
            bigPicture.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            //关闭Dialog
            ImageView imgDelete = (ImageView) dialog.findViewById(R.id.delete_iv);
            imgDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mSelectedPics.size() - 1; i++) {
                        if (mSelectedPics.get(i).picPath.equals(uri)) {
                            mSelectedPics.remove(i);
                            mPicAdapter.notifyDataSetChanged();
                        }
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    //点击之后显示大图（dialo）
    /*private void showBigPicture(final ImageView photoView ,int position,String uri) {
        if(URLManager.isSlOkWebUrl(uri)){
            Glide.with(this).load(URLManager.getFixedShowImageUrl(uri)).into(photoView);
        }else{
            if(!uri.startsWith("file:"))
                uri = "file://" + uri;
            Glide.with(this).load(uri).asBitmap().placeholder(R.mipmap.icon_app).into(photoView);
        }
    }*/
}
