package com.shenjing.dimension.dimension.base.select_picture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.time.TimeUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择Fragment
 * Created by Nereo on 2015/4/7.
 */
public class MultiImageSelectorFragment extends Fragment implements MultiImageSelectorAdapter.CheckBoxViewChangeListener {

    private static final String TAG = "MultiImageSelector";

    /** 最大图片选择数，int类型 */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /** 已选择图片数，int类 */
    public static final String EXTRA_SELECTED_PIC_NUM = "selected_pic_num";
    
    /** 图片选择模式，int类型 */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /** 默认选择的数据集 */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
    /** 单选 */
    public static final int MODE_SINGLE = 0;
    /** 多选 */
    public static final int MODE_MULTI = 1;
    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;

    /** 用于判断是从那个入口进入 */
    public static final String EXTRA_WHICH_ACT_FROM = "whichActivityFrom";  //用于判断从哪个入口进入选择图片  1.从聊天入口进入  2. 暂时包括除聊天之外的其他所有入口 3为表情选择
    // 图片选择模式
    int mode;

    // 结果数据
    private ArrayList<String> resultList = new ArrayList<String>();
    private ArrayList<Image> resultImgInfoList = new ArrayList<Image>();
    // 文件夹数据
    private ArrayList<Folder> mResultFolder = new ArrayList<Folder>();

    // 图片Grid
    private GridView mGridView;
    private Callback mCallback;

    private MultiImageSelectorAdapter mImageAdapter;
    private FolderAdapter mFolderAdapter;

    // 时间线
    private TextView mTimeLineText;
    // 类别
    private TextView mCategoryText;
    // 预览按钮
    private Button mPreviewBtn;

    private int mDesireImageCount;
    private int mImagePicNum;

    private boolean hasFolderGened = false;

    private File mTmpFile;
    private int whichActFrom;
    

    private static final int REQ_CODE_SELECT_SEND_PIC = 1;

    DecimalFormat decimalFormat=new DecimalFormat("##0");
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (Callback) activity;
        }catch (ClassCastException e){
            throw new ClassCastException("The Activity must implement MultiImageSelectorFragment.Callback interface...");
        }
    }

    int itemWid;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemWid = (int)getResources().getDimension(R.dimen.dp_750_177);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multi_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 选择图片数量
        mDesireImageCount = getArguments().getInt(EXTRA_SELECT_COUNT);
        mImagePicNum = getArguments().getInt(EXTRA_SELECTED_PIC_NUM);
        if(mDesireImageCount - mImagePicNum >= 0){
            mDesireImageCount -= mImagePicNum;
        }
        // 图片选择模式
        mode = getArguments().getInt(EXTRA_SELECT_MODE);
        whichActFrom = getArguments().getInt(EXTRA_WHICH_ACT_FROM, 2);

        // 默认选择
        if(mode == MODE_MULTI) {
            ArrayList<String> tmp = getArguments().getStringArrayList(EXTRA_DEFAULT_SELECTED_LIST);
            if(tmp != null && tmp.size()>0) {
                resultList = tmp;
            }
        }

        mImageAdapter = new MultiImageSelectorAdapter(getActivity(), resultList, itemWid, mode, this);

        mTimeLineText = (TextView) view.findViewById(R.id.timeline_area);
        // 初始化，先隐藏当前timeline
        mTimeLineText.setVisibility(View.GONE);

        mCategoryText = (TextView) view.findViewById(R.id.category_btn);
        // 初始化，加载所有图片
        mCategoryText.setText("所有图片");
        mCategoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFolderListDialog();
            }
        });

        mPreviewBtn = (Button) view.findViewById(R.id.preview);
        // 初始化，按钮状态初始化
        if(resultList == null || resultList.size()<=0){
            mPreviewBtn.setText("预览");
            mPreviewBtn.setEnabled(false);
        }
        mPreviewBtn.setVisibility(mode == MODE_SINGLE ? View.GONE : View.VISIBLE);
        mPreviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviewSelectImageActivity.startActivtyForResult(MultiImageSelectorFragment.this, resultImgInfoList,  0, mDesireImageCount,true,REQ_CODE_SELECT_SEND_PIC, false, whichActFrom);
            }
        });

        mGridView = (GridView) view.findViewById(R.id.grid);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {

                if(state == SCROLL_STATE_IDLE){
                    // 停止滑动，日期指示器消失
                    mTimeLineText.setVisibility(View.GONE);
                }else if(state == SCROLL_STATE_FLING){
                    mTimeLineText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(mTimeLineText.getVisibility() == View.VISIBLE) {
                    int index = firstVisibleItem + 1 == view.getAdapter().getCount() ? view.getAdapter().getCount() - 1 : firstVisibleItem + 1;
                    Image image = (Image) view.getAdapter().getItem(index);
                    if (image != null) {
                        mTimeLineText.setText(TimeUtils.formatPhotoDate(image.path));
                    }
                }
            }
        });
        mGridView.setAdapter(mImageAdapter);

        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mode == MODE_MULTI){
                    int picCount = parent.getCount();
                    String[] paths = new String[picCount];
                    for (int i = 0 ; i < picCount; i ++){
                        paths[i] = MultiImageSelectorActivity.getImages().get(i).path;
                    }
                    if(whichActFrom == 1){
                        if(resultImgInfoList == null || resultImgInfoList.isEmpty()){
                            PreviewSelectImageActivity.startActivtyForResult(MultiImageSelectorFragment.this, position, mDesireImageCount,REQ_CODE_SELECT_SEND_PIC, true, whichActFrom);
                        }else{
                            PreviewSelectImageActivity.startActivtyForResult(MultiImageSelectorFragment.this, resultImgInfoList, position, mDesireImageCount,false,REQ_CODE_SELECT_SEND_PIC, true, whichActFrom);
                        }
                    }else{
                        if(resultImgInfoList == null || resultImgInfoList.isEmpty()){
                            PreviewSelectImageActivity.startActivtyForResult(MultiImageSelectorFragment.this, position, mDesireImageCount,REQ_CODE_SELECT_SEND_PIC, false, whichActFrom);
                        }else{
                            PreviewSelectImageActivity.startActivtyForResult(MultiImageSelectorFragment.this, resultImgInfoList, position, mDesireImageCount,false,REQ_CODE_SELECT_SEND_PIC, false, whichActFrom);
                        }
                    }
                }else {
                    selectImageFromGrid(MultiImageSelectorActivity.getImages().get(position), mode, position);
                }
            }
        });
        mFolderAdapter = new FolderAdapter(getActivity(), itemWid);
    }

    private Dialog folderListDialog;
    private void showFolderListDialog() {
        
        folderListDialog = Utils.showFolderListDialog(getActivity(), mFolderAdapter, new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mFolderAdapter.setSelectIndex(position);

                final int index = position;
                final AdapterView v = parent;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        folderListDialog.dismiss();

                        if (index == 0) {
                            getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            mCategoryText.setText("所有图片");
                            
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            if (null != folder) {
                                MultiImageSelectorActivity.setImages(folder.images);
                                mImageAdapter.setData(folder.images);
                                mCategoryText.setText(folder.name);
                                // 设定默认选择
                                if (resultList != null && resultList.size() > 0) {
                                    mImageAdapter.setDefaultSelected(resultList);
                                }
                            }
                           
                        }

                        // 滑动到最初始位置
                        mGridView.smoothScrollToPosition(0);
                    }
                }, 100);

            }
        }, mCategoryText.getText());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 首次加载所有图片
        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    /**
     * 选择图片操作
     * @param image
     */
    @SuppressLint("StringFormatMatches")
    private void selectImageFromGrid(Image image, int mode, int position) {
        if(image != null) {
            // 多选模式
            if(mode == MODE_MULTI) {
                if (resultList.contains(image.path)) {
                    resultList.remove(image.path);
                    if(resultList.size() != 0) {
                        mPreviewBtn.setEnabled(true);
                        mPreviewBtn.setTextColor(getResources().getColor(R.color.white));
                        mPreviewBtn.setText("预览(" + resultList.size() + ")");
                    }else{
                        mPreviewBtn.setEnabled(false);
                        mPreviewBtn.setTextColor(getResources().getColor(R.color.white));
                        mPreviewBtn.setText("预览(");
                    }
                    if (mCallback != null) {
                        mCallback.onImageUnselected(image.path);
                    }
//                    if(isFromchat){
                        for(Image image2: resultImgInfoList){
                            if(image2.path.equals(image.path)){
                                resultImgInfoList.remove(image2);
                                break;
                            }
                        }
//                    }
                } else {
                    // 判断选择数量问题
                    if(mDesireImageCount == resultList.size()){
                        String text = getString(R.string.msg_amount_limit);
                        showPromptDialog(String.format(text, resultList.size()));
                        return;
                    }

                    resultList.add(image.path);
                    mPreviewBtn.setEnabled(true);
                    mPreviewBtn.setTextColor(getResources().getColor(R.color.white));
                    mPreviewBtn.setText(getResources().getString(R.string.preview) + "(" + resultList.size() + ")");
                    if (mCallback != null) {
                        mCallback.onImageSelected(image.path);
                    }
//                    if(isFromchat){
                    resultImgInfoList.add(image);
//                    }
                }
                mImageAdapter.select(image);
            }else if(mode == MODE_SINGLE){
                // 单选模式
                if(mCallback != null){
                    mCallback.onSingleImageSelected(image.path);
                }
            }
        }
    }

    private void showPromptDialog(String info) {
       /* LPDialogFactory.Builder builder = new LPDialogFactory.Builder(getContext())
                .setContent(info)
                .setRightText("知道了")
                .setListener(new LPDialogFactory.OnDialogButtonClickListener() {
                    @Override
                    public void onDialogButtonClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.build().show();*/
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if(id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }else if(id == LOADER_CATEGORY){
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0]+" like '%"+args.getString("path")+"%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                MultiImageSelectorActivity.setImages(new ArrayList<Image>());
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do{
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        File imageFile = new File(path);
                        Image image = new Image(path, name, dateTime, decimalFormat.format((double)imageFile.length()));
                        MultiImageSelectorActivity.getImages().add(image);
                        if( !hasFolderGened ) {
                            // 获取文件夹名称
//                            if(isFromchat){
//                            }
                            File folderFile = imageFile.getParentFile();
                            if(folderFile!=null){
                                Folder folder = new Folder();
                                folder.name = folderFile.getName();
                                folder.path = folderFile.getAbsolutePath();
                                folder.cover = image;
                                if (!mResultFolder.contains(folder)) {
                                    List<Image> imageList = new ArrayList<Image>();
                                    imageList.add(image);
                                    folder.images = imageList;
                                    mResultFolder.add(folder);
                                } else {
                                    // 更新
                                    Folder f = mResultFolder.get(mResultFolder.indexOf(folder));
                                    f.images.add(image);
                                }
                            }
                        }

                    }while(data.moveToNext());
                    
                    mImageAdapter.setData(MultiImageSelectorActivity.getImages());

                    // 设定默认选择
                    if(resultList != null && resultList.size()>0){
                        mImageAdapter.setDefaultSelected(resultList);
                    }

                    mFolderAdapter.setData(mResultFolder);
                    hasFolderGened = true;

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    /**
     * 回调接口
     */
    public interface Callback{
        void onSingleImageSelected(String path);
        void onImageSelected(String path);
        void onImageUnselected(String path);
        void onCameraShot(File imageFile);
        void onRemoveAllSelect();
    }

    
    @Override
    public void onCheckBoxChange(int position) {
        selectImageFromGrid(MultiImageSelectorActivity.getImages().get(position), mode, position);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            resultImgInfoList = (ArrayList<Image>) data.getSerializableExtra(PreviewSelectImageActivity.EXTRA_SELECT_PICINFO_LIST);
            resultList.clear();
            mCallback.onRemoveAllSelect();
            for(Image image : resultImgInfoList){
                resultList.add(image.path);
                if (mCallback != null) {
                    mCallback.onImageSelected(image.path);
                }
            }
            mImageAdapter.setDefaultSelected(resultList, true);
        }
        if(resultList.size() != 0) {
            mPreviewBtn.setEnabled(true);
            mPreviewBtn.setTextColor(getResources().getColor(R.color.white));
            mPreviewBtn.setText(getResources().getString(R.string.preview) + "(" + resultList.size() + ")");
        }else{
            mPreviewBtn.setEnabled(false);
            mPreviewBtn.setTextColor(getResources().getColor(R.color.unit_text_tv_title_2));
            mPreviewBtn.setText(R.string.preview);
        }
        
        if(resultCode == getActivity().RESULT_OK){
            Intent intent = new Intent();
            intent.putExtras(data);
            getActivity().setResult(getActivity().RESULT_OK, data);
            getActivity().finish();
        }
    }
}
