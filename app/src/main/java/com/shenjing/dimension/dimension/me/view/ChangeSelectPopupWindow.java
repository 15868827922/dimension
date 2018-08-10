package com.shenjing.dimension.dimension.me.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.wheelview.OnWheelChangedListener;
import com.shenjing.dimension.dimension.base.wheelview.WheelView;
import com.shenjing.dimension.dimension.me.adapter.WheelViewTextAdapter;

import java.util.ArrayList;

public class ChangeSelectPopupWindow extends PopupWindow implements View.OnClickListener {

	public static final int TYPE_CONSTELLATION = 1;
	public static final int TYPE_SEX = 2;
	public static final int TYPE_INDUSTRY = 3;



	private WheelView wvProvince;

	private ArrayList<String> mConstellationList = new ArrayList<String>();
	private ArrayList<String> mSexList = new ArrayList<String>();
	private ArrayList<String> mIndustryList = new ArrayList<String>();

	private String mCurrentStr;

	private OnConstellationListener onConstellationListener;
	private View lyChangeConstellationChild;
	private TextView btnSure;
	private TextView btnCancel;

	private int maxsize = 19;
	private int minsize = 14;
	private Context context;

	private WheelViewTextAdapter mConstellationAdapter;
	private ArrayList<String> mList;

	private int type;

	public ChangeSelectPopupWindow(final Context context) {
		super(context);
		this.context = context;
		mConstellationList.add("白羊座");
		mConstellationList.add("金牛座");
		mConstellationList.add("双子座");
		mConstellationList.add("巨蟹座");
		mConstellationList.add("狮子座");
		mConstellationList.add("处女座");
		mConstellationList.add("天秤座");
		mConstellationList.add("天蝎座");
		mConstellationList.add("射手座");
		mConstellationList.add("魔羯座 ");
		mConstellationList.add("水瓶座");
		mConstellationList.add("双鱼座");

		mSexList.add("男");
		mSexList.add("女");

		mIndustryList.add("互联网/IT/通信");
		mIndustryList.add("广告/传媒/文化/体育");
		mIndustryList.add("金融");
		mIndustryList.add("教育培训");
		mIndustryList.add("制药/医疗");
		mIndustryList.add("交通/物流/贸易/零售");
		mIndustryList.add("专业服务");
		mIndustryList.add("房地产");
		mIndustryList.add("建筑");
		mIndustryList.add("汽车");
		mIndustryList.add("机械制造");
		mIndustryList.add("消费品");
		mIndustryList.add("服务业");
		mIndustryList.add("能源/化工/环保");
		mIndustryList.add("政府/非盈利机构/其他");
		mIndustryList.add("学生");


	}

	public void setView(int type) {
		View view= View.inflate(context, R.layout.pop_select_constellation,null);

		wvProvince = (WheelView) view.findViewById(R.id.wv_select_left);
		lyChangeConstellationChild = view.findViewById(R.id.view_chose_address);
		btnSure = (TextView) view.findViewById(R.id.btn_sure);
		btnCancel = (TextView)view. findViewById(R.id.btn_cancel);

		//设置SelectPicPopupWindow的View
		this.setContentView(view);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.Animation_BottomSheetDialogAni);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

		lyChangeConstellationChild.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		int currentIndex = 0;
		switch (type){
			case TYPE_CONSTELLATION:
				mList = mConstellationList;
				currentIndex = getConstellationItem(mCurrentStr);
				break;
			case TYPE_SEX:
				mList = mSexList;
				currentIndex = getSexItem(mCurrentStr);
				break;
			case TYPE_INDUSTRY:
				currentIndex = getIndustryItem(mCurrentStr);
				mList = mIndustryList;
				break;
		}


		mConstellationAdapter = new WheelViewTextAdapter(context, mList, currentIndex, maxsize, minsize);
		wvProvince.setVisibleItems(5);
		wvProvince.setViewAdapter(mConstellationAdapter);
		wvProvince.setCurrentItem(currentIndex);



		wvProvince.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mConstellationAdapter.getItemText(wheel.getCurrentItem());
				mCurrentStr = currentText;
				setTextviewSize(currentText, mConstellationAdapter);


			}
		});

	}

	/**
	 * 初始化地点
	 *
	 * @param currentStr
	 */
	public void setCurrentStr(String currentStr) {
		if (currentStr != null && currentStr.length() > 0) {
			this.mCurrentStr = currentStr;
		}

	}


	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText, WheelViewTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxsize);
			} else {
				textvew.setTextSize(minsize);
			}
		}
	}

	public void setChangeConstellationListener(OnConstellationListener onConstellationListener) {
		this.onConstellationListener = onConstellationListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSure) {
			if (onConstellationListener != null) {
				onConstellationListener.onClick(mCurrentStr);
			}
		} else if (v == btnCancel) {

		} else if (v == lyChangeConstellationChild) {
			return;
		} else {
//			dismiss();
		}
		dismiss();
	}

	/**
	 * 回调接口
	 * 
	 * @author Administrator
	 *
	 */
	public interface OnConstellationListener {
		public void onClick(String province);
	}


	/**
	 * 返回省会索引，没有就返回默认“广东”
	 * 
	 * @param province
	 * @return
	 */
	public int getConstellationItem(String province) {
		int size = mConstellationList.size();
		int provinceIndex = 0;
		boolean noprovince = true;
		for (int i = 0; i < size; i++) {
			if (province.equals(mConstellationList.get(i))) {
				noprovince = false;
				return provinceIndex;
			} else {
				provinceIndex++;
			}
		}
		if (!noprovince) {
			mCurrentStr = "双子";
			return 2;
		}
		return provinceIndex;
	}

	/**
	 *
	 * @param sexStr
	 * @return
	 */
	public int getSexItem(String sexStr) {
		int size = mSexList.size();
		int index = 0;
		boolean noprovince = true;
		for (int i = 0; i < size; i++) {
			if (sexStr.equals(mSexList.get(i))) {
				noprovince = false;
				return index;
			} else {
				index++;
			}
		}
		if (!noprovince) {
			mCurrentStr = "男";
			return 0;
		}
		return index;
	}

	/**
	 *
	 * @param industryStr
	 * @return
	 */
	public int getIndustryItem(String industryStr) {
		int size = mIndustryList.size();
		int index = 0;
		boolean noIndustry = true;
		for (int i = 0; i < size; i++) {
			if (industryStr.equals(mIndustryList.get(i))) {
				noIndustry = false;
				return index;
			} else {
				index++;
			}
		}
		if (!noIndustry) {
			mCurrentStr = "金融";
			return 2;
		}
		return index;
	}


}