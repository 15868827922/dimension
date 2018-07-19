package com.shenjing.dimension;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.shenjing.dimension.dimension.base.activity.FundmentalActivity;
import com.shenjing.dimension.dimension.main.fragent.GameFragment;
import com.shenjing.dimension.dimension.main.fragent.MyFragment;
import com.shenjing.dimension.dimension.main.fragent.RestFragment;
import com.shenjing.dimension.dimension.main.fragent.SupplyFragment;
import com.shenjing.dimension.dimension.me.TabHostNewView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FundmentalActivity implements TabHostNewView.OnTabClickListener{

    @Bind(R.id.tabHost)
    TabHostNewView tabHostNewView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarContainerVisibility(false);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);


        switchTabHost(TabHostNewView.GAME_POSITION);
        tabHostNewView.switchTab(TabHostNewView.GAME_POSITION);

        tabHostNewView.setOnTabClickListener(this);
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
        if(tabHostNewView != null && tabHostNewView.getCurrPosition() == tabPosition/* && saved_tag_fragment_position == -1*/){
            return;
        }

        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction transaction= fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        switch (tabPosition) {
            case TabHostNewView.REST_POSITION:
                RestFragment restFragment= getFragmentByTag(RestFragment.TAG);
                if(restFragment == null){
                    restFragment = new RestFragment();
                    transaction.add(R.id.content, restFragment, RestFragment.TAG);
                }else{
                    transaction.show(restFragment);
                }
                transaction.commitAllowingStateLoss();
                break;
            case TabHostNewView.GAME_POSITION:

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
            case TabHostNewView.SUPPLY_POSITION:
                SupplyFragment supplyFragment=getFragmentByTag(SupplyFragment.TAG);
                if(supplyFragment == null){
                    supplyFragment = new SupplyFragment();
                    transaction.add(R.id.content, supplyFragment, SupplyFragment.TAG);
                }else{
                    transaction.show(supplyFragment);
                }
                transaction.commitAllowingStateLoss();
                break;
            case TabHostNewView.ME_POSITION:

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

    public  <T extends Fragment> T getFragmentByTag(String tag){
        return (T) getSupportFragmentManager().findFragmentByTag(tag);
    }


    @Override
    public void onTabClick(int position) {  //底部tab切换
        switchTabHost(position);
    }
}