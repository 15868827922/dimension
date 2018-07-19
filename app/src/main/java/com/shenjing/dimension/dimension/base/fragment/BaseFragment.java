package com.shenjing.dimension.dimension.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.shenjing.dimension.dimension.base.activity.BaseActivity;
import com.shenjing.dimension.dimension.base.log.LPLog;


public class BaseFragment extends Fragment {

    private Toast toast;

    protected long mPageResumeTime;
    protected long mPagePauseTime;
    private boolean ishidden;

//    protected PageRequestManager prm; // 请求管理器

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LPLog.print(BaseFragment.class, this.toString() + " hidden: " + hidden);
        ishidden = hidden;
        if(hidden == false){
            if (getActivity() instanceof BaseActivity){
                ((BaseActivity)getActivity()).setTitleBarVisible(true);
            }
        }


    }


    public void toast(CharSequence text){
        if (toast == null){
            toast = Toast.makeText(getContext(), text, Toast.LENGTH_SHORT);
        }else{
            toast.setText(text);
        }
        toast.show();
    }

    protected void toast(int resstr){
        if (toast == null){
            toast = Toast.makeText(getContext(), resstr, Toast.LENGTH_SHORT);
        }else{
            toast.setText(resstr);
        }
        toast.show();
    }


    public String tag(){
        return String.valueOf(hashCode());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isResumed()){
        }
    }

    public long getPageResumeTime() {
        return mPageResumeTime;
    }

    public long getPagePauseTime() {
        return mPagePauseTime;
    }
}
