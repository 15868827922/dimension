package com.shenjing.dimension.dimension.me.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.wheelview.adapter.AbstractWheelTextAdapter1;

import java.util.ArrayList;

public class WheelViewTextAdapter extends AbstractWheelTextAdapter1 {
		ArrayList<String> list;

		public WheelViewTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		public CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}