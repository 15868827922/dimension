package com.shenjing.dimension.dimension.me;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.activity.BaseActivity;
import com.shenjing.dimension.dimension.me.fragment.ScoreMallFragment;
import com.shenjing.dimension.dimension.me.model.CarefulChosenInfo;
import com.shenjing.dimension.dimension.me.view.TabScoreMallView;
import com.shenjing.dimension.dimension.view.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScoreMallActivity extends BaseActivity {



    @Bind(R.id.view_pull_to_refresh)
    PullToRefreshLayout mRefreshView;

    @Bind(R.id.view_scroll_view)
    ScrollView mScrollView;

    @Bind(R.id.view_recycler_view)
    RecyclerView mRecyclerView;


    @Bind(R.id.view_tab_score_mall)
    TabScoreMallView mTabScoreMallView;

    @Bind(R.id.view_float_tab_score_mall)
    TabScoreMallView mFloatTabScoreMallView;

    @Bind(R.id.content)
    View mViewContent;

    private CarefulChosenAdapter mAdapter;


    private ScoreMallFragment mScoreFragment1;
    private ScoreMallFragment mScoreFragment2;
    private ScoreMallFragment mScoreFragment3;
    private ScoreMallFragment mScoreFragment4;

    private List<CarefulChosenInfo> mCarefulChosenList;

    int mMadDyForFloatTab;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_score_mall);
        ButterKnife.bind(this);
        setTitleText("积分商城");

        initView();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        WindowManager wm = this.getWindowManager();
        int screenHeight = wm.getDefaultDisplay().getHeight();//获取屏幕高
        ViewGroup.LayoutParams layoutParams = mViewContent.getLayoutParams();
        layoutParams.height =screenHeight - (int)getResources().getDimension(R.dimen.dp_750_160);
        mViewContent.setLayoutParams(layoutParams);

        mRefreshView.setMinimumHeight(screenHeight + (int)getResources().getDimension(R.dimen.dp_750_656));
        mRefreshView.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {

            }

            @Override
            public void loadMore() {

            }
        });
        mCarefulChosenList = new ArrayList<>();
        mCarefulChosenList.add(new CarefulChosenInfo());
        mCarefulChosenList.add(new CarefulChosenInfo());
        mCarefulChosenList.add(new CarefulChosenInfo());
        mCarefulChosenList.add(new CarefulChosenInfo());

        //设置轮播图
        List<String> images = new ArrayList<>();
        images.add("http://img.zcool.cn/community/017c4955ee776932f875a1320b4f4d.jpg@1280w_1l_2o_100sh.jpg");
        images.add("http://img.zcool.cn/community/01ccf855448d430000019ae9643921.jpg@1280w_1l_2o_100sh.png");
        images.add("http://img.zcool.cn/community/017c4955ee776932f875a1320b4f4d.jpg@1280w_1l_2o_100sh.jpg");

        Banner banner = (Banner) findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);//设置banner样式
        banner.setImageLoader(new GlideImageLoader());//设置图片加载器
        banner.setImages(images);//设置图片集合
        banner.setBannerAnimation(Transformer.DepthPage);//设置banner动画效果
        banner.setDelayTime(2000);//设置轮播时间
        banner.setIndicatorGravity(BannerConfig.CENTER);  //设置指示器位置（当banner模式中有指示器时）
        banner.start(); //banner设置方法全部调用完毕时最后调用

        //设置精选
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter=new CarefulChosenAdapter(R.layout.item_careful_chosen,mCarefulChosenList);
        mRecyclerView.setAdapter(mAdapter);

        //这是顶部
        mMadDyForFloatTab=getResources().getDimensionPixelOffset(R.dimen.dp_750_656);
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (i1 > mMadDyForFloatTab){
                    mFloatTabScoreMallView.setVisibility(View.VISIBLE);
                }else {
                    mFloatTabScoreMallView.setVisibility(View.GONE);
                }
            }


        });

        //设置tab
        mTabScoreMallView.switchTab(TabScoreMallView.CONVERT_SCORE_POSITION);
        mFloatTabScoreMallView.switchTab(TabScoreMallView.CONVERT_SCORE_POSITION);
        switchTabHost(1);
        mFloatTabScoreMallView.setOnTabClickListener(new TabScoreMallView.OnTabClickListener() {
            @Override
            public void onTabClick(int position) {
                switchTab(position);
            }
        });

        mTabScoreMallView.setOnTabClickListener(new TabScoreMallView.OnTabClickListener() {
            @Override
            public void onTabClick(int position) {
                switchTab(position);
            }
        });



    }

    private void switchTab(int position) {
        switch (position){
            case TabScoreMallView.CONVERT_SCORE_POSITION:
                mTabScoreMallView.switchTab(TabScoreMallView.CONVERT_SCORE_POSITION);
                mFloatTabScoreMallView.switchTab(TabScoreMallView.CONVERT_SCORE_POSITION);
                switchTabHost(1);
                break;
            case TabScoreMallView.DOLL_POSITION:
                mTabScoreMallView.switchTab(TabScoreMallView.DOLL_POSITION);
                mFloatTabScoreMallView.switchTab(TabScoreMallView.DOLL_POSITION);
                switchTabHost(2);
                break;
            case TabScoreMallView.AROUND_MALL_POSITION:
                mTabScoreMallView.switchTab(TabScoreMallView.AROUND_MALL_POSITION);
                mFloatTabScoreMallView.switchTab(TabScoreMallView.AROUND_MALL_POSITION);
                switchTabHost(3);
                break;
            case TabScoreMallView.MILKY_TEA_POSITION:
                mTabScoreMallView.switchTab(TabScoreMallView.MILKY_TEA_POSITION);
                mFloatTabScoreMallView.switchTab(TabScoreMallView.MILKY_TEA_POSITION);
                switchTabHost(4);
                break;
        }
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
                if(mScoreFragment1 == null){
                    mScoreFragment1 = new ScoreMallFragment();
                    transaction.add(R.id.content, mScoreFragment1, ScoreMallFragment.TAG_CONVERT_SCORE);
                }else{
                    transaction.show(mScoreFragment1);
                }
                transaction.commitAllowingStateLoss();
                break;
            case 2:

                if(mScoreFragment2 == null){
                    mScoreFragment2 = new ScoreMallFragment();
                    transaction.add(R.id.content, mScoreFragment2, ScoreMallFragment.TAG_DOLL);
                }else{
                    transaction.show(mScoreFragment2);
                }
//                tabMeFragment.adjustScrollState();
                transaction.commitAllowingStateLoss();
                break;
            case 3:

                if(mScoreFragment3 == null){
                    mScoreFragment3 = new ScoreMallFragment();
                    transaction.add(R.id.content, mScoreFragment3, ScoreMallFragment.TAG_DOLL);
                }else{
                    transaction.show(mScoreFragment3);
                }
//                tabMeFragment.adjustScrollState();
                transaction.commitAllowingStateLoss();
                break;
            case 4:

                if(mScoreFragment4 == null){
                    mScoreFragment4 = new ScoreMallFragment();
                    transaction.add(R.id.content, mScoreFragment4, ScoreMallFragment.TAG_MILKY_TEA);
                }else{
                    transaction.show(mScoreFragment4);
                }
//                tabMeFragment.adjustScrollState();
                transaction.commitAllowingStateLoss();
                break;

        }
        getSupportFragmentManager().executePendingTransactions();
    }



    public class CarefulChosenAdapter extends BaseQuickAdapter<CarefulChosenInfo, BaseViewHolder> {
        public CarefulChosenAdapter(int layoutResId, List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, CarefulChosenInfo item) {
          /*  helper.setText(R.id.text, item.getTitle());
            helper.setImageResource(R.id.icon, item.getImageResource());
            // 加载网络图片
            Glide.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) helper.getView(R.id.iv));*/
        }
    }

}
