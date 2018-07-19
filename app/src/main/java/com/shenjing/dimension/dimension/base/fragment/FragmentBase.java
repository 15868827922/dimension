package com.shenjing.dimension.dimension.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class FragmentBase extends BaseFragment{
	
	
	public abstract int getContentLayout() ;		
	
	public abstract void initView(@Nullable View view);
		
	public abstract void initData();
	
	

	public View findViewById(int id){
		return getView().findViewById(id);
	}
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(getContentLayout(), null);
	}
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
		initData();
	}
	
}
