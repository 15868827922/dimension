package com.shenjing.dimension.dimension.me;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.FundmentalActivity;
import com.shenjing.dimension.dimension.me.fragment.TaskListFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyTaskActivity extends FundmentalActivity implements View.OnClickListener{

    @Bind(R.id.view_my_task_all)
    View mViewTaskAll;

    @Bind(R.id.view_my_task_ing)
    View mViewTaskIng;

    @Bind(R.id.view_my_task_finish)
    View mViewTaskFinish;

    private TaskListFragment mTask1;
    private TaskListFragment mTask2;
    private TaskListFragment mTask3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_my_task);
        setStatusBarContainerVisibility(false);
        ButterKnife.bind(this);
        mViewTaskAll.setSelected(true);
        initView();
    }

    private void initView() {
        switchTabHost(1);
        mViewTaskAll.setOnClickListener(this);
        mViewTaskIng.setOnClickListener(this);
        mViewTaskFinish.setOnClickListener(this);
    }




    private void hideAllFragment(FragmentTransaction transaction){
        List<Fragment> fragments= getSupportFragmentManager().getFragments();
        if(fragments != null && !fragments.isEmpty()){
            for(int i=0;i<fragments.size();i++){
                Fragment fragment=fragments.get(i);
                if(fragment!=null){
                    transaction.hide(fragment);
                }
            }
        }

        mViewTaskAll.setSelected(false);
        mViewTaskFinish.setSelected(false);
        mViewTaskIng.setSelected(false);

    }

    public void switchTabHost(int tabPosition){

        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction transaction= fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        switch (tabPosition) {
            case 1:
                if(mTask1 == null){
                    mTask1 = new TaskListFragment();
                    transaction.add(R.id.content, mTask1, TaskListFragment.TAG_ALL);
                }else{
                    transaction.show(mTask1);
                }
                transaction.commitAllowingStateLoss();
                mViewTaskAll.setSelected(true);
                break;
            case 2:

                if(mTask2 == null){
                    mTask2 = new TaskListFragment();
                    transaction.add(R.id.content, mTask2, TaskListFragment.TAG_ING);
                }else{
                    transaction.show(mTask2);
                }
//                tabMeFragment.adjustScrollState();
                transaction.commitAllowingStateLoss();
                mViewTaskIng.setSelected(true);
                break;
            case 3:

                if(mTask3 == null){
                    mTask3 = new TaskListFragment();
                    transaction.add(R.id.content, mTask3, TaskListFragment.TAG_FINISH);
                }else{
                    transaction.show(mTask3);
                }
//                tabMeFragment.adjustScrollState();
                transaction.commitAllowingStateLoss();
                mViewTaskFinish.setSelected(true);
                break;

        }
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_my_task_all:
                switchTabHost(1);
                break;
            case R.id.view_my_task_ing:
                switchTabHost(2);
                break;
            case R.id.view_my_task_finish:
                switchTabHost(3);
                break;
        }
    }
}
