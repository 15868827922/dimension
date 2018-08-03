package com.shenjing.dimension.dimension.me;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;
import com.shenjing.dimension.dimension.main.fragent.GameFragment;
import com.shenjing.dimension.dimension.main.fragent.MyFragment;
import com.shenjing.dimension.dimension.main.fragent.RestFragment;
import com.shenjing.dimension.dimension.main.fragent.SupplyFragment;
import com.shenjing.dimension.dimension.me.fragment.MessageListFragment;
import com.shenjing.dimension.dimension.me.fragment.OrderListFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyMessageActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    @Bind(R.id.radioGroupStatus)
    RadioGroup mRadioGroup;

    @Bind(R.id.radioLeft)
    RadioButton mRadioButtonLeft;

    @Bind(R.id.radioRight)
    RadioButton mRadioButtonRight;

    private MessageListFragment mMessage1;
    private MessageListFragment mMessage2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_my_message);
        setTitleText("我的消息");
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mRadioGroup.setOnCheckedChangeListener(this);
        switchTabHost(1);
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

    }

    public void switchTabHost(int tabPosition){

        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction transaction= fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        switch (tabPosition) {
            case 1:
                if(mMessage1 == null){
                    mMessage1 = new MessageListFragment();
                    transaction.add(R.id.content, mMessage1, RestFragment.TAG);
                }else{
                    transaction.show(mMessage1);
                }
                transaction.commitAllowingStateLoss();
                break;
            case 2:

                if(mMessage2 == null){
                    mMessage2 = new MessageListFragment();
                    transaction.add(R.id.content, mMessage2, RestFragment.TAG);
                }else{
                    transaction.show(mMessage2);
                }
//                tabMeFragment.adjustScrollState();
                transaction.commitAllowingStateLoss();
                break;

        }
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
       /* switch (checkedId){
            case mRadioButtonLeft.getId():
                break;
        }*/
        if (checkedId == mRadioButtonLeft.getId()) {
            switchTabHost(1);
        }else if (checkedId == mRadioButtonRight.getId()){
            switchTabHost(4);
        }

    }
}
