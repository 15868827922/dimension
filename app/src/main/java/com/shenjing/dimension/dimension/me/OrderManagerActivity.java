package com.shenjing.dimension.dimension.me;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;
import com.shenjing.dimension.dimension.main.fragent.GameFragment;
import com.shenjing.dimension.dimension.main.fragent.MyFragment;
import com.shenjing.dimension.dimension.main.fragent.RestFragment;
import com.shenjing.dimension.dimension.main.fragent.SupplyFragment;
import com.shenjing.dimension.dimension.me.fragment.OrderListFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderManagerActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    @Bind(R.id.radioGroupStatus)
    RadioGroup mRadioGroup;

    @Bind(R.id.radioLeft)
    RadioButton mRadioButtonLeft;

    @Bind(R.id.radioMiddle1)
    RadioButton mRadioButtonMiddle1;

    @Bind(R.id.radioMiddle2)
    RadioButton mRadioButtonMiddle2;

    @Bind(R.id.radioRight)
    RadioButton mRadioButtonRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_order_manager);
        setTitleText("收支明细");

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
                OrderListFragment orderListFragment= getFragmentByTag(OrderListFragment.TAG);
                if(orderListFragment == null){
                    orderListFragment = new OrderListFragment();
                    transaction.add(R.id.content, orderListFragment, RestFragment.TAG);
                }else{
                    transaction.show(orderListFragment);
                }
                transaction.commitAllowingStateLoss();
                break;
            case 2:

                GameFragment gameFragment= getFragmentByTag(GameFragment.TAG);
                if(gameFragment == null){
                    gameFragment = new GameFragment();
                    transaction.add(R.id.content, gameFragment, GameFragment.TAG);
                }else{
                    transaction.show(gameFragment);
                }
//                tabMeFragment.adjustScrollState();
                transaction.commitAllowingStateLoss();
                break;
            case 3:
                SupplyFragment supplyFragment=getFragmentByTag(SupplyFragment.TAG);
                if(supplyFragment == null){
                    supplyFragment = new SupplyFragment();
                    transaction.add(R.id.content, supplyFragment, SupplyFragment.TAG);
                }else{
                    transaction.show(supplyFragment);
                }
                transaction.commitAllowingStateLoss();
                break;
            case 4:

                MyFragment myFragment = getFragmentByTag(MyFragment.TAG);
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    transaction.add(R.id.content, myFragment, MyFragment.TAG);
                } else {
                    transaction.show(myFragment);
                }
                transaction.commitAllowingStateLoss();
                break;
            default:
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
       if (checkedId == mRadioButtonLeft.getId()){
           switchTabHost(1);
       }else if (checkedId == mRadioButtonMiddle1.getId()){
           switchTabHost(2);
       }else if (checkedId == mRadioButtonMiddle2.getId()){
           switchTabHost(3);
       }else if (checkedId == mRadioButtonRight.getId()){
           switchTabHost(4);
       }

    }
}
