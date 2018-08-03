package com.shenjing.dimension.dimension.me.fragment;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.shenjing.dimension.dimension.base.adapter.LPListViewAdapter;
import com.shenjing.dimension.dimension.base.adapter.SimpleListViewAdapter;
import com.shenjing.dimension.dimension.base.fragment.FragmentBase;
import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.request.RequestMap;
import com.shenjing.dimension.dimension.me.model.MessageInfo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageListFragment extends FragmentBase {

    @Bind(R.id.view_refresh)
    PullToRefreshLayout mRefresh;

    @Bind(R.id.view_list_view)
    ListView mListView;

    LPListViewAdapter<MessageInfo> mAdapter;

    private RequestMap mRequestMap;
    private ArrayList<MessageInfo> mList;
    @Override
    public int getContentLayout() {
        return R.layout.fragment_message_list;
    }

    @Override
    public void initView(@Nullable View view) {

        ButterKnife.bind(this,view);

        mRequestMap = RequestMap.newInstance();
        mList = new ArrayList<>();
        mList.add(new MessageInfo());
        mList.add(new MessageInfo());
        mList.add(new MessageInfo());
        mList.add(new MessageInfo());
        mList.add(new MessageInfo());

        mRefresh.setCanLoadMore(false);
        mRefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
//                reqMessageList(true);
            }

            @Override
            public void loadMore() {
//                reqList(false);
            }
        });


        mAdapter=new LPListViewAdapter<MessageInfo>(getActivity(),mList,mListView,R.layout.item_message_list) {

            @Override
            public void onConvert(LPViewHolder<MessageInfo> viewHolder, int position, MessageInfo mItem) {
                /*viewHolder.setText(R.id.tv_message_title,mItem.getTitle());
                viewHolder.setText(R.id.tv_message_content,mItem.getMessage());
                viewHolder.setVisible(R.id.tv_message_content, TextUtils.isEmpty(mItem.getMessage()) ? View.GONE : View.VISIBLE);
                viewHolder.setVisible(R.id.view_red_dot, "0".equals(mItem.getIs_read()) ? View.VISIBLE : View.INVISIBLE);
                viewHolder.setVisible(R.id.view_get_gift, (!TextUtils.isEmpty(mItem.getGift()))? View.VISIBLE : View.GONE);
                viewHolder.setText(R.id.view_get_gift, "0".equals(mItem.getGift_is_achieve()) ? "立即领取" : "已领取");*/

            }
        };
/*
        mAdapter.addClickEvent(R.id.view_get_gift,new SimpleListViewAdapter.OnClickEvent<MessageInfo>() {
            @Override
            public void onViewClick(View view, MessageInfo item, int position) {
                if ("1".equals(item.getGift_is_achieve())){
                    return;
                }else {
                    reqGetGift(item);
                }

            }
        });*/



        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

//        reqMessageList(true);
    }

    @Override
    public void initData() {

    }
}
