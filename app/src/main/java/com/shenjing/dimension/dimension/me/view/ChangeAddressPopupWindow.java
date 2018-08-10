package com.shenjing.dimension.dimension.me.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.shenjing.dimension.R;
import com.shenjing.dimension.dimension.base.wheelview.OnWheelChangedListener;
import com.shenjing.dimension.dimension.base.wheelview.OnWheelScrollListener;
import com.shenjing.dimension.dimension.base.wheelview.WheelView;
import com.shenjing.dimension.dimension.main.LPApplicationLike;
import com.shenjing.dimension.dimension.me.adapter.WheelViewTextAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangeAddressPopupWindow extends PopupWindow implements View.OnClickListener {

	private WheelView wvProvince;
	private WheelView wvCitys;
	private WheelView wvArea;
	private View lyChangeAddress;
	private View lyChangeAddressChild;
	private TextView btnSure;
	private TextView btnCancel;

	private Context context;
	private JSONObject mJsonObj;
	private String mJson;
	/**
	 * 所有省
	 */
	private String[] mProvinceDatas;
	/**
	 * key - 省 value - 市s
	 */
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区s
	 */
	private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();


	/**
	 * 省的名称和id
	 * key--name values--ID
	 */
	private Map<String,String> mProvinceName_Id = new HashMap<>();
	/**
	 * 市的名称和id
	 * key--name values--ID
	 */
	private Map<String,String> mCityName_Id = new HashMap<>();
	/**
	 * 区的名称和id
	 * key--name values--ID
	 */
	private Map<String,String> mAreaName_Id = new HashMap<>();

	private ArrayList<String> arrProvinces = new ArrayList<String>();
	private ArrayList<String> arrCitys = new ArrayList<String>();
	private ArrayList<String> arrAreas = new ArrayList<String>();
	private WheelViewTextAdapter provinceAdapter;
	private WheelViewTextAdapter cityAdapter;
	private WheelViewTextAdapter areaAdapter;

	private String strProvince = "广东";
	private String strCity = "深圳";
	private String strArea = "福田区";
	private OnAddressCListener onAddressCListener;

	private int maxsize = 19;
	private int minsize = 14;

	public ChangeAddressPopupWindow(final Context context) {
		super(context);
		this.context = context;


	}

	public void setView() {
		View view= View.inflate(context, R.layout.edit_three_pop_layout,null);

		wvProvince = (WheelView) view.findViewById(R.id.wv_select_left);
		wvCitys = (WheelView) view.findViewById(R.id.wv_select_middle);
		wvArea = (WheelView)view. findViewById(R.id.wv_select_right);
		lyChangeAddress = view.findViewById(R.id.ly_myinfo_changeaddress);
		lyChangeAddressChild = view.findViewById(R.id.view_chose_address);
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

		lyChangeAddressChild.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

//		initJsonData();
		getSelect();


		initProvinces();
		provinceAdapter = new WheelViewTextAdapter(context, arrProvinces, getProvinceItem(strProvince), maxsize, minsize);
		wvProvince.setVisibleItems(5);
		wvProvince.setViewAdapter(provinceAdapter);
		wvProvince.setCurrentItem(getProvinceItem(strProvince));

		initCitys(mCitisDatasMap.get(strProvince));
		cityAdapter = new WheelViewTextAdapter(context, arrCitys, getCityItem(strCity), maxsize, minsize);
		wvCitys.setVisibleItems(5);
		wvCitys.setViewAdapter(cityAdapter);
		wvCitys.setCurrentItem(getCityItem(strCity));

		initAreas(mAreaDatasMap.get(strCity));
		areaAdapter = new WheelViewTextAdapter(context, arrAreas, getAreaItem(strArea), maxsize, minsize);
		wvArea.setVisibleItems(5);
		wvArea.setViewAdapter(areaAdapter);
		wvArea.setCurrentItem(getAreaItem(strArea));

		wvProvince.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
				strProvince = currentText;
				setTextviewSize(currentText, provinceAdapter);

				String[] citys = mCitisDatasMap.get(currentText);
				initCitys(citys);
				cityAdapter = new WheelViewTextAdapter(context, arrCitys, 0, maxsize, minsize);
				wvCitys.setVisibleItems(5);
				wvCitys.setViewAdapter(cityAdapter);
				wvCitys.setCurrentItem(0);
				setTextviewSize("0", cityAdapter);

				//根据市，地区联动
				String[] areas = mAreaDatasMap.get(citys[0]);
				initAreas(areas);
				areaAdapter = new WheelViewTextAdapter(context, arrAreas, 0, maxsize, minsize);
				wvArea.setVisibleItems(5);
				wvArea.setViewAdapter(areaAdapter);
				wvArea.setCurrentItem(0);
				setTextviewSize("0", areaAdapter);
			}
		});

		wvProvince.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, provinceAdapter);
			}
		});

		wvCitys.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
				strCity = currentText;
				setTextviewSize(currentText, cityAdapter);

				//根据市，地区联动
				String[] areas = mAreaDatasMap.get(currentText);
				initAreas(areas);
				areaAdapter = new WheelViewTextAdapter(context, arrAreas, 0, maxsize, minsize);
				wvArea.setVisibleItems(5);
				wvArea.setViewAdapter(areaAdapter);
				wvArea.setCurrentItem(0);
				setTextviewSize("0", areaAdapter);


			}
		});

		wvCitys.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, cityAdapter);
			}
		});

		wvArea.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
				strArea = currentText;
				setTextviewSize(currentText, cityAdapter);
			}
		});

		wvArea.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, areaAdapter);
			}
		});
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

	public void setAddresskListener(OnAddressCListener onAddressCListener) {
		this.onAddressCListener = onAddressCListener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSure) {
			if (onAddressCListener != null) {
				onAddressCListener.onClick(strProvince, strCity,strArea);
			}
		} else if (v == btnCancel) {

		} else if (v == lyChangeAddressChild) {
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
	public interface OnAddressCListener {
		public void onClick(String province, String city, String area);
	}
	//PopupWindow界面的显示效果
	public void getSelect(){
		String json = initJsonData();
		parseJson(json);


	}


	//读取本地json生成json字符串
	private String initJsonData(){
		try {
			InputStream is = LPApplicationLike.getContext().getResources().getAssets().open("地区json.json");
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			mJson = new String(buffer,"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mJson;
	}
	//解析json，并把解析出来的数据存放到不同的集合和Map中
	private void parseJson(String str){
		try {
			JSONArray jsonArray = new JSONArray(str);
			mProvinceDatas = new String[jsonArray.length()];
			for(int i = 0;i<jsonArray.length();i++){
				JSONObject jsonp = jsonArray.getJSONObject(i);
				String provinceName = jsonp.getString("region_name");
				String provinceId = jsonp.getString("region_id");
				mProvinceName_Id.put(provinceName,provinceId);
				mProvinceDatas[i] = provinceName;
				JSONArray jsonc = jsonp.getJSONArray("sub");
				String [] citiesData = new String[jsonc.length()];
				for(int j = 0;j<jsonc.length();j++){
					JSONObject jsoncity = jsonc.getJSONObject(j);
					String cityName = jsoncity.getString("region_name");
					String cityId = jsoncity.getString("region_id");
					mCityName_Id.put(cityName,cityId);
					citiesData[j] = cityName;
					JSONArray jsona = jsoncity.getJSONArray("sub");
					String [] areaData = new String[jsona.length()];
					for(int k = 0;k<jsona.length();k++){
						String areaName = jsona.getJSONObject(k).getString("region_name");
						String areaId = jsona.getJSONObject(k).getString("region_id");
						mAreaName_Id.put(areaName,areaId);
						areaData[k] = areaName;
					}
					mAreaDatasMap.put(cityName,areaData);
				}
				mCitisDatasMap.put(provinceName,citiesData);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 初始化省会
	 */
	public void initProvinces() {
		int length = mProvinceDatas.length;
		for (int i = 0; i < length; i++) {
			arrProvinces.add(mProvinceDatas[i]);
		}
	}

	/**
	 * 根据省会，生成该省会的所有城市
	 * 
	 * @param citys
	 */
	public void initCitys(String[] citys) {
		if (citys != null) {
			arrCitys.clear();
			int length = citys.length;
			for (int i = 0; i < length; i++) {
				arrCitys.add(citys[i]);
			}
		} else {
			String[] city = mCitisDatasMap.get("广东");
			arrCitys.clear();
			int length = city.length;
			for (int i = 0; i < length; i++) {
				arrCitys.add(city[i]);
			}
		}
		if (arrCitys != null && arrCitys.size() > 0
				&& !arrCitys.contains(strCity)) {
			strCity = arrCitys.get(0);
		}
	}

	/**
	 * 根据城市，生成该城市的所有地区
	 *
	 * @param areas
	 */
	public void initAreas(String[] areas) {
		if (areas != null) {
			arrAreas.clear();
			int length = areas.length;
			for (int i = 0; i < length; i++) {
				arrAreas.add(areas[i]);
			}
		} else {
			String[] area = mAreaDatasMap.get("深圳");
			arrAreas.clear();
			int length = area.length;
			for (int i = 0; i < length; i++) {
				arrAreas.add(area[i]);
			}
		}
		if (arrAreas != null && arrAreas.size() > 0
				&& !arrAreas.contains(strArea)) {
			strArea = arrAreas.get(0);
		}
	}

	/**
	 * 初始化地点
	 * 
	 * @param province
	 * @param city
	 */
	public void setAddress(String province, String city, String area) {
		if (province != null && province.length() > 0) {
			this.strProvince = province;
		}
		if (city != null && city.length() > 0) {
			this.strCity = city;
		}

		if (area != null && area.length() > 0) {
			this.strArea = area;
		}
	}

	/**
	 * 返回省会索引，没有就返回默认“广东”
	 * 
	 * @param province
	 * @return
	 */
	public int getProvinceItem(String province) {
		int size = arrProvinces.size();
		int provinceIndex = 0;
		boolean noprovince = true;
		for (int i = 0; i < size; i++) {
			if (province.equals(arrProvinces.get(i))) {
				noprovince = false;
				return provinceIndex;
			} else {
				provinceIndex++;
			}
		}
		if (noprovince) {
			strProvince = "广东";
			return 18;
		}
		return provinceIndex;
	}

	/**
	 * 得到城市索引，没有返回默认“深圳”
	 * 
	 * @param city
	 * @return
	 */
	public int getCityItem(String city) {
		int size = arrCitys.size();
		int cityIndex = 0;
		boolean nocity = true;
		for (int i = 0; i < size; i++) {
			System.out.println(arrCitys.get(i));
			if (city.equals(arrCitys.get(i))) {
				nocity = false;
				return cityIndex;
			} else {
				cityIndex++;
			}
		}
		if (nocity) {
			strCity = "深圳";
			return 2;
		}
		return cityIndex;
	}

	/**
	 * 得到地区索引，没有返回默认“福田区”
	 *
	 * @param area
	 * @return
	 */
	public int getAreaItem(String area) {
		int size = arrAreas.size();
		int areaIndex = 0;
		boolean noarea = true;
		for (int i = 0; i < size; i++) {
			System.out.println(arrAreas.get(i));
			if (area.equals(arrAreas.get(i))) {
				noarea = false;
				return areaIndex;
			} else {
				areaIndex++;
			}
		}
		if (noarea) {
			strArea = "福田区";
			return 1;
		}
		return areaIndex;
	}


}