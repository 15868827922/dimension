package com.shenjing.dimension.dimension.me.fragment;

import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.fragment.FragmentBase;
import com.shenjing.dimension.dimension.base.request.RequestMap;
import com.shenjing.dimension.dimension.me.model.ScoreMallInfo;
import com.shenjing.dimension.dimension.view.MediaGridInset;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ScoreMallFragment extends FragmentBase {


    public static final String TAG_CONVERT_SCORE="ScoreMallFragmentScoreMall";
    public static final String TAG_DOLL="ScoreMallFragmentDoll";
    public static final String TAG_AROUND_MALL="ScoreMallFragmentAroundMall";
    public static final String TAG_MILKY_TEA="ScoreMallFragmentMilkyTea";

    @Bind(R.id.view_recycler_view)
    RecyclerView mRecyclerView;


    private RequestMap mRequestMap;
    private ArrayList<ScoreMallInfo> mList;

    private ScoreMallAdapter mAdapter;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_score_mall;
    }

    @Override
    public void initView(@Nullable View view) {

        ButterKnife.bind(this,view);
        mRecyclerView.setOnScrollListener(null);

        mRequestMap = RequestMap.newInstance();
        mList = new ArrayList<>();
        mList.add(new ScoreMallInfo());
        mList.add(new ScoreMallInfo());
        mList.add(new ScoreMallInfo());
        mList.add(new ScoreMallInfo());
        mList.add(new ScoreMallInfo());
        mList.add(new ScoreMallInfo());

        int spacing = getResources().getDimensionPixelOffset(R.dimen.dp_750_20);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mRecyclerView.addItemDecoration(new MediaGridInset(3, spacing, true));
//        mRecyclerView.setLayoutManager(manager);
        mAdapter=new ScoreMallAdapter(R.layout.item_score_mall,mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {

    }

    public class ScoreMallAdapter extends BaseQuickAdapter<ScoreMallInfo, BaseViewHolder> {
        public ScoreMallAdapter(int layoutResId, List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ScoreMallInfo item) {
          /*  helper.setText(R.id.text, item.getTitle());
            helper.setImageResource(R.id.icon, item.getImageResource());
            // 加载网络图片
            Glide.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) helper.getView(R.id.iv));*/
        }
    }
}
