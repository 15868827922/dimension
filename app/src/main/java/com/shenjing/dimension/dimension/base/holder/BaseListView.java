package com.shenjing.dimension.dimension.base.holder;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by tiny  on 2016/10/29 15:12.
 * Desc:
 */

public interface BaseListView<T> {

    public void onLoadSuccess(int count, int updateCount);
    public void onLoadFailed(int code, String reason);

    public void onControllerCreated(ArrayList<T> list);

    public void notifyDataChanged();

    public void showToast(String toast);

    public Context getViewContext();
}
